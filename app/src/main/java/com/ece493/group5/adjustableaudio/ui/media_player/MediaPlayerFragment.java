package com.ece493.group5.adjustableaudio.ui.media_player;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.adapters.MediaQueueAdapter;
import com.ece493.group5.adjustableaudio.controllers.AudioController;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;
import com.ece493.group5.adjustableaudio.controllers.MusicServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.EqualizerModelListener;
import com.ece493.group5.adjustableaudio.listeners.MediaDataListener;
import com.ece493.group5.adjustableaudio.listeners.MediaQueueItemSwipeListener;
import com.ece493.group5.adjustableaudio.models.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.utils.TimeUtils;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MediaPlayerFragment extends Fragment
{
    private static final int REQUEST_CODE_AUDIO_FILE = 0;

    private MediaPlayerViewModel mediaPlayerViewModel;
    private MediaQueueAdapter mediaQueueAdapter;
    private MusicServiceInteractor musicServiceInteractor;
    private MicrophoneServiceInteractor microphoneServiceInteractor;
    private AudioController audioController;
    private EqualizerModelListener equalizerModelListener;

    private ImageButton addMediaButton;
    private ImageButton skipPreviousButton;
    private ImageButton playPauseButton;
    private ImageButton skipNextButton;
    private TextView songTitleLabel;
    private TextView songArtistLabel;
    private TextView mediaTimeLabel;
    private RecyclerView recyclerView;
    private SeekBar songSeekBar;
    private SeekBar leftVolumeSeekbar;
    private SeekBar rightVolumeSeekbar;

    private Boolean isTracking;

    private final MediaDataListener mediaDataListener = new MediaDataListener() {
        @Override
        public void onQueueChanged(List<Song> queue)
        {
            mediaQueueAdapter.setQueue(queue);
        }

        @Override
        public void onQueueIndexChanged(int index, Song song)
        {
            mediaQueueAdapter.setSelectedPosition(index);

            if (song == null)
            {
                songTitleLabel.setText("");
                songArtistLabel.setText("");
            }
            else
            {
                songTitleLabel.setText(song.getTitle());
                songArtistLabel.setText(song.getArtist());

                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(index);
            }
        }

        @Override
        public void onStateChanged(int state)
        {
            switch (state)
            {
                case PlaybackState.STATE_PLAYING:
                    showPauseButton();
                    break;
                case PlaybackState.STATE_PAUSED:
                case PlaybackState.STATE_STOPPED:
                case PlaybackState.STATE_BUFFERING:
                case PlaybackState.STATE_CONNECTING:
                case PlaybackState.STATE_NONE:
                case PlaybackState.STATE_ERROR:
                    showPlayButton();
                    break;
            }
        }

        @Override
        public void onDurationChanged(int elapsed, int total)
        {
            if (!isTracking) {
                songSeekBar.setProgress(elapsed);
                songSeekBar.setMax(total);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);
        isTracking = false;

        skipPreviousButton = root.findViewById(R.id.skipPrevButton);
        playPauseButton = root.findViewById(R.id.playButton);
        skipNextButton = root.findViewById(R.id.skipForwardButton);
        songTitleLabel = root.findViewById(R.id.labelSongTitle);
        songArtistLabel = root.findViewById(R.id.labelArtist);
        mediaTimeLabel = root.findViewById(R.id.mediaTime);
        songSeekBar = root.findViewById(R.id.progressTrack);
        leftVolumeSeekbar = root.findViewById(R.id.leftVolumeSeekBar);
        rightVolumeSeekbar = root.findViewById(R.id.rightVolumeSeekBar);

        recyclerView = root.findViewById(R.id.mediaQueueRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MediaQueueItemSwipeListener() {
            @Override
            public void onSwiped(int position) {
                musicServiceInteractor.dequeue(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mediaQueueAdapter = new MediaQueueAdapter();
        mediaQueueAdapter.setOnSelectedListener(new MediaQueueAdapter.OnSelectedListener() {
            @Override
            public void onSelected(int position) {
                musicServiceInteractor.selectSong(position);
            }
        });
        recyclerView.setAdapter(mediaQueueAdapter);

        addMediaButton = root.findViewById(R.id.addMediaButton);
        addMediaButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent requestAudioIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(requestAudioIntent, REQUEST_CODE_AUDIO_FILE);
            }
        });

        mediaPlayerViewModel.getMediaData().observe(this, new Observer<MediaData>() {
            @Override
            public void onChanged(@Nullable MediaData mediaData) {
                if (mediaData != null)
                    mediaDataListener.handleChanges(mediaData);
            }
        });

        audioController = new AudioController(getContext());

        musicServiceInteractor = new MusicServiceInteractor(getContext()) {
            @Override
            public void onConnectionEstablished() {
                super.onConnectionEstablished();
                enableMediaControls();
                audioController.registerDevice(musicServiceInteractor);
            }

            @Override
            public void onConnectionLost() {
                super.onConnectionLost();
                disableMediaControls();
                audioController.unregisterDevice(musicServiceInteractor);
            }

            @Override
            public void onDataChanged(MediaData data)
            {
                super.onDataChanged(data);
                mediaPlayerViewModel.setMediaData(data);
            }
        };

        microphoneServiceInteractor = new MicrophoneServiceInteractor(getContext()) {
            @Override
            public void onConnectionEstablished() {
                audioController.registerDevice(microphoneServiceInteractor);
            }

            @Override
            public void onConnectionLost() {
                audioController.unregisterDevice(microphoneServiceInteractor);
            }
        };

        equalizerModelListener = (EqualizerModelListener) getContext();
        leftVolumeSeekbar.setProgress(equalizerModelListener.getEqualizerModel().getCurrentLeftVolume());
        rightVolumeSeekbar.setProgress(equalizerModelListener.getEqualizerModel().getCurrentRightVolume());
        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        checkStoragePermission();
        musicServiceInteractor.connect();
        microphoneServiceInteractor.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        musicServiceInteractor.disconnect();
        microphoneServiceInteractor.disconnect();
    }

    private void checkStoragePermission()
    {
        boolean hasReadPermission =
                ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        boolean hasWritePermission =  ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasReadPermission || !hasWritePermission)
        {
            disableMediaControls();
            addMediaButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showStoragePermissionsDialog();
                }
            });
        }
    }

    private void showStoragePermissionsDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.title_permissions_dialog);
        alertDialogBuilder.setMessage(R.string.dialog_msg_permission_storage);

        alertDialogBuilder.setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    private void showPauseButton()
    {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_pause_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }

    private void showPlayButton()
    {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_play_arrow_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }

    private void enableMediaControls()
    {
        skipPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicServiceInteractor.skipToPrevious();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicServiceInteractor.togglePlayPause();
            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicServiceInteractor.skipToNext();
            }
        });

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                String total = TimeUtils.durationAsString(seekBar.getMax());
                String elapsed = TimeUtils.durationAsString(progress);

                mediaTimeLabel.setText(elapsed + " / " + total);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                musicServiceInteractor.seekTo(seekBar.getProgress());
                isTracking = false;
            }
        });

        leftVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double volume = (1.0 - (Math.log(leftVolumeSeekbar.getMax() - leftVolumeSeekbar.getProgress()) / Math.log(leftVolumeSeekbar.getMax())));
                audioController.setLeftVolume(volume);
                equalizerModelListener.getEqualizerModel().setCurrentLeftVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                musicServiceInteractor.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicServiceInteractor.enableEqualizer();
            }
        });

        rightVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double volume = (1.0 - (Math.log(rightVolumeSeekbar.getMax() - rightVolumeSeekbar.getProgress()) / Math.log(rightVolumeSeekbar.getMax())));
                audioController.setRightVolume(volume);
                equalizerModelListener.getEqualizerModel().setCurrentRightVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                musicServiceInteractor.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicServiceInteractor.enableEqualizer();
            }
        });
    }

    private void disableMediaControls()
    {
        skipPreviousButton.setOnClickListener(null);
        playPauseButton.setOnClickListener(null);
        skipNextButton.setOnClickListener(null);
        songSeekBar.setOnSeekBarChangeListener(null);
        leftVolumeSeekbar.setOnSeekBarChangeListener(null);
        rightVolumeSeekbar.setOnSeekBarChangeListener(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUDIO_FILE) {
            onAudioFileResult(resultCode, data);
        }
    }

    private void onAudioFileResult(int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        Uri uri = data.getData();

        assert uri != null;
        Cursor cursor = Objects.requireNonNull(getActivity())
                .getContentResolver()
                .query(uri, null, null, null, null);

        assert cursor != null;
        cursor.moveToFirst();

        Song song = new Song();
        song.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        song.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        song.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
        song.setFilename(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        song.setMediaId(song.getFilename().replace(' ', '_'));

        cursor.close();

        musicServiceInteractor.enqueue(song);
    }
}