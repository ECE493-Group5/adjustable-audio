package com.ece493.group5.adjustableaudio.ui.media_player;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ece493.group5.adjustableaudio.enums.MediaData;
import com.ece493.group5.adjustableaudio.listeners.MediaDataListener;
import com.ece493.group5.adjustableaudio.listeners.MediaQueueItemSwipeListener;
import com.ece493.group5.adjustableaudio.listeners.MediaSessionListener;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;
import com.ece493.group5.adjustableaudio.utils.TimeUtils;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MediaPlayerFragment extends Fragment
{
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();
    private static final int REQUEST_CODE_AUDIO_FILE = 0;
    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private MediaPlayerViewModel mediaPlayerViewModel;
    private MediaQueueAdapter mediaQueueAdapter;
    private MediaBrowser mediaBrowser;
    private MediaController mediaController;

    private ImageButton skipPreviousButton;
    private ImageButton playPauseButton;
    private ImageButton skipNextButton;
    private TextView songTitleLabel;
    private TextView songArtistLabel;
    private TextView mediaTimeLabel;
    private RecyclerView recyclerView;
    private ImageButton addMediaButton;
    private SeekBar songSeekBar;

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
            synchronized (isTracking) {
                if (!isTracking) {
                    songSeekBar.setProgress(elapsed);
                    songSeekBar.setMax(total);
                }
            }
        }
    };

    private final MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback() {
            @Override
            public void onConnected()
            {
                MediaSession.Token token = mediaBrowser.getSessionToken();

                mediaController = new MediaController(getContext(), token);
                mediaController.registerCallback(controllerCallback);
                mediaController.getTransportControls()
                        .sendCustomAction(MediaSessionListener.ACTION_REQUEST_ALL_CHANGES,
                                null);

                enableMediaControls();
            }

            @Override
            public void onConnectionSuspended()
            {
                super.onConnectionSuspended();
                disableMediaControls();
            }

            @Override
            public void onConnectionFailed()
            {
                // The Service has refused our connection.
                Log.d(TAG, "Failed to connect to MediaBrowserService.");
                disableMediaControls();
            }
    };

    private final MediaController.Callback controllerCallback = new MediaController.Callback()
    {
        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state)
        {
            super.onPlaybackStateChanged(state);

            /** NOTE: Its important that the view model sets the state here.
             * Otherwise there is a race condition during the start up of the
             * fragment (which can potentially lead to a crash). */
            mediaPlayerViewModel.setState(state);
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
        addMediaButton = root.findViewById(R.id.addMediaButton);
        songSeekBar = root.findViewById(R.id.progressTrack);

        recyclerView = root.findViewById(R.id.mediaQueueRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MediaQueueItemSwipeListener() {
            @Override
            public void onSwiped(int position) {
                Bundle extras = new Bundle();
                extras.putInt(MediaData.EXTRA_QUEUE_INDEX, position);
                mediaController.getTransportControls()
                        .sendCustomAction(MediaSessionListener.ACTION_DEQUEUE, extras);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mediaQueueAdapter = new MediaQueueAdapter();
        mediaQueueAdapter.setOnSelectedListener(new MediaQueueAdapter.OnSelectedListener() {
            @Override
            public void onSelected(int position) {
                Bundle extras = new Bundle();
                extras.putInt(MediaData.EXTRA_QUEUE_INDEX, position);
                mediaController.getTransportControls()
                        .sendCustomAction(MediaSessionListener.ACTION_SONG_SELECTED, extras);
            }
        });
        recyclerView.setAdapter(mediaQueueAdapter);

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

        checkAndRequestPermissions();

        mediaPlayerViewModel.getState().observe(this, new Observer<PlaybackState>() {
            @Override
            public void onChanged(@Nullable PlaybackState state) {
                if (state != null)
                    mediaDataListener.handleChange(state);
            }
        });

        mediaBrowser = new MediaBrowser(getContext(), new ComponentName(getContext(),
                MusicService.class), connectionCallback, null);

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (!mediaBrowser.isConnected())
            mediaBrowser.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (mediaBrowser.isConnected()) {
            mediaBrowser.disconnect();
        }
    }

    private boolean checkAndRequestPermissions()
    {
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                || !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            String[] permissionsToRequest = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            requestPermissions(permissionsToRequest, REQUEST_CODE_PERMISSIONS);

            return false;
        }

        return true;
    }

    protected boolean hasPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(this.getContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        /* TODO: Handle when request is not granted */
    }

    public void showPauseButton()
    {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_pause_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }

    public void showPlayButton()
    {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_play_arrow_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }

    public void enableMediaControls()
    {
        skipPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mediaController.getTransportControls().skipToPrevious();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PlaybackState state = mediaController.getPlaybackState();

                if (state.getState() == PlaybackState.STATE_PLAYING)
                    mediaController.getTransportControls().pause();
                else
                    mediaController.getTransportControls().play();
            }
        });


        skipNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mediaController.getTransportControls().skipToNext();
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
                synchronized (isTracking) { isTracking = true; }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mediaController.getTransportControls().seekTo(seekBar.getProgress());
                synchronized (isTracking) { isTracking = false; }
            }
        });
    }

    public void disableMediaControls()
    {
        skipPreviousButton.setOnClickListener(null);
        playPauseButton.setOnClickListener(null);
        skipNextButton.setOnClickListener(null);
        songSeekBar.setOnSeekBarChangeListener(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQUEST_CODE_AUDIO_FILE:
                onAudioFileResult(resultCode, data);
                break;
        }
    }

    protected void onAudioFileResult(int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        Uri uri = data.getData();

        Cursor cursor = getActivity()
                .getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();

        Song song = new Song();
        song.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        song.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        song.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
        song.setFilename(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        song.setMediaId(song.getFilename().replace(' ', '_'));

        cursor.close();

        mediaController.getTransportControls().sendCustomAction(MediaSessionListener.ACTION_ENQUEUE,
                song.toBundle());
    }
}