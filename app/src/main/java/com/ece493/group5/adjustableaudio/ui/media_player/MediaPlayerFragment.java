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
import android.os.SystemClock;
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
import com.ece493.group5.adjustableaudio.listeners.MediaQueueItemSwipeListener;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class MediaPlayerFragment extends Fragment
{
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();
    private static final String ZERO = "0";
    private static final int DEFAULT_SEEK_BAR_VALUE = 0;
    private static final int MILLISECONDS_CONVERSION = 1000;
    private static final int MINUTES_CONVERSION = 60;
    private static final int REQUEST_CODE_AUDIO_FILE = 0;
    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final int SECONDS_CONVERSION = 60;
    private static final int TEN = 10;
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private MediaPlayerViewModel mediaPlayerViewModel;
    private MediaQueueAdapter mediaQueueAdapter;
    private MediaBrowser mediaBrowser;
    private MediaController mediaController;
    private PlaybackState lastPlaybackState;

    private ImageButton skipPreviousButton;
    private ImageButton playPauseButton;
    private ImageButton skipNextButton;
    private TextView songTitleLabel;
    private TextView songArtistLabel;
    private TextView mediaTimeLabel;
    private RecyclerView recyclerView;
    private ImageButton addMediaButton;
    private SeekBar songSeekBar;

    private ScheduledFuture<?> scheduledFuture;
    private int songSeekBarPosition;

    private final MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback() {
            @Override
            public void onConnected()
            {
                MediaSession.Token token = mediaBrowser.getSessionToken();
                mediaController = new MediaController(getContext(), token);
                enableMediaControls();
                mediaController.registerCallback(controllerCallback);
                mediaController.getTransportControls()
                        .sendCustomAction(MusicService.ACTION_TRIGGER_UPDATE_PLAYBACK_STATE,
                                null);
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
            lastPlaybackState = state;
            mediaPlayerViewModel.setState(state);

            if (lastPlaybackState.getState() == PlaybackState.STATE_SKIPPING_TO_NEXT)
            {
                mediaController.getTransportControls().play();
            }
        }
    };

    private final Runnable updateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgressBar();
        }
    };

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);

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
                extras.putInt(MusicService.BUNDLE_QUEUE_INDEX, position);
                mediaController.getTransportControls()
                        .sendCustomAction(MusicService.ACTION_DEQUEUE, extras);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mediaQueueAdapter = new MediaQueueAdapter();
        mediaQueueAdapter.setOnSelectedListener(new MediaQueueAdapter.OnSelectedListener() {
            @Override
            public void onSelected(int position) {
                mediaPlayerViewModel.setCurrentlySelected(position);
            }
        });
        recyclerView.setAdapter(mediaQueueAdapter);

        mediaPlayerViewModel.getQueue().observe(this, new Observer<ArrayList<Song>>()
        {
            @Override
            public void onChanged(@Nullable ArrayList<Song> queue)
            {
                mediaQueueAdapter.setQueue(queue);
            }
        });

        mediaPlayerViewModel.getState().observe(this, new Observer<PlaybackState>()
        {
            @Override
            public void onChanged(@Nullable PlaybackState state)
            {
                if (state == null)
                    return;

                Bundle extras = state.getExtras();
                if (extras != null)
                {
                    extras.setClassLoader(MusicService.class.getClassLoader());

                    ArrayList<Song> queue = extras.getParcelableArrayList(MusicService.BUNDLE_QUEUE);
                    mediaPlayerViewModel.setQueue(queue);

                    int index = extras.getInt(MusicService.BUNDLE_QUEUE_INDEX, -1);
                    mediaPlayerViewModel.setCurrentlySelected(index);
                }

                switch (state.getState())
                {
                    case PlaybackState.STATE_PLAYING:
                        showPauseButton();
                        break;
                    case PlaybackState.STATE_PAUSED:
                    case PlaybackState.STATE_STOPPED:
                        showPlayButton();
                        break;
                    default:
                        break;
                }
            }
        });

        mediaPlayerViewModel.getCurrentlySelected().observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(@Nullable Integer position)
            {

                if (position != mediaQueueAdapter.getSelectedPosition())
                {
                    //Song has changed
                    songSeekBarPosition = DEFAULT_SEEK_BAR_VALUE;
                }

                mediaQueueAdapter.setSelectedPosition(position);

                Bundle extras = new Bundle();
                extras.putInt(MusicService.BUNDLE_QUEUE_INDEX, position);
                mediaController.getTransportControls()
                        .sendCustomAction(MusicService.ACTION_SONG_SELECTED, extras);

                Song song = mediaPlayerViewModel.getSong(position);

                if (song == null)
                {
                    songTitleLabel.setText("");
                    songArtistLabel.setText("");
                    songSeekBar.setProgress(DEFAULT_SEEK_BAR_VALUE);
                    songSeekBar.setMax(DEFAULT_SEEK_BAR_VALUE);
                }
                else
                {
                    songTitleLabel.setText(song.getTitle());
                    songArtistLabel.setText(song.getArtist());
                    songSeekBar.setMax((int)song.getDuration());
                    songSeekBar.setProgress(songSeekBarPosition);
                }
            }
        });

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

        mediaBrowser = new MediaBrowser(getContext(), new ComponentName(getContext(),
                MusicService.class), connectionCallback, null);

        mediaBrowser.connect();

        return root;
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
                scheduleProgressBarUpdate();
                mediaController.getTransportControls().skipToPrevious();
            }
        });


        playPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PlaybackState state = mediaController.getPlaybackState();

                if (state == null || mediaPlayerViewModel.getQueue().getValue().isEmpty())
                    return;

                if (state.getState() == PlaybackState.STATE_PLAYING)
                {
                    stopProgressBarUpdate();
                    mediaController.getTransportControls().pause();
                }
                else
                {
                    scheduleProgressBarUpdate();
                    mediaController.getTransportControls().play();
                }
            }
        });


        skipNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                scheduleProgressBarUpdate();
                mediaController.getTransportControls().skipToNext();
            }
        });

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                String finalTimerString;
                String durationSecondsString;
                String actualSecondsString;

                int durationMinutes = (songSeekBar.getMax()
                        % (MILLISECONDS_CONVERSION * SECONDS_CONVERSION * MINUTES_CONVERSION))
                        / (MILLISECONDS_CONVERSION * SECONDS_CONVERSION);
                int durationSeconds = ((songSeekBar.getMax()
                        % (MILLISECONDS_CONVERSION * SECONDS_CONVERSION * MINUTES_CONVERSION))
                        % (MILLISECONDS_CONVERSION * SECONDS_CONVERSION) / MILLISECONDS_CONVERSION);

                int actualMinutes = (i
                        % (MILLISECONDS_CONVERSION * SECONDS_CONVERSION * MINUTES_CONVERSION))
                        / (MILLISECONDS_CONVERSION * SECONDS_CONVERSION);
                int actualSeconds = ((i % (MILLISECONDS_CONVERSION * SECONDS_CONVERSION
                        * MINUTES_CONVERSION)) % (MILLISECONDS_CONVERSION * SECONDS_CONVERSION)
                        / MILLISECONDS_CONVERSION);

                if (durationSeconds < TEN)
                {
                    durationSecondsString = ZERO + durationSeconds;
                }
                else
                {
                    durationSecondsString = Integer.toString(durationSeconds);
                }

                if (actualSeconds < TEN)
                {
                    actualSecondsString = ZERO + actualSeconds;
                }
                else
                {
                    actualSecondsString = Integer.toString(actualSeconds);
                }

                finalTimerString = actualMinutes + ":" + actualSecondsString + "/"
                        + durationMinutes + ":" + durationSecondsString;

                mediaTimeLabel.setText(finalTimerString);
                songSeekBarPosition = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                stopProgressBarUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mediaController.getTransportControls().seekTo(seekBar.getProgress());
                scheduleProgressBarUpdate();
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

        mediaController.getTransportControls().sendCustomAction(MusicService.ACTION_ENQUEUE,
                song.toBundle());
    }


    private void stopProgressBarUpdate()
    {
        if(scheduledFuture != null)
        {
            scheduledFuture.cancel(true);
        }
    }


    private void scheduleProgressBarUpdate()
    {
        stopProgressBarUpdate();

        if (!scheduledExecutorService.isShutdown())
        {
            scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    handler.post(updateProgressTask);
                }
            }, PROGRESS_UPDATE_INITIAL_INTERVAL, PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }


    private void updateProgressBar()
    {
        lastPlaybackState = mediaController.getPlaybackState();

        if (lastPlaybackState == null)
        {
            return;
        }

        long currentPosition = lastPlaybackState.getPosition();

        if (lastPlaybackState.getState() != PlaybackState.STATE_PAUSED)
        {
            long timeDelta = SystemClock.elapsedRealtime() -
                    lastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * lastPlaybackState.getPlaybackSpeed();
        }

        songSeekBarPosition = (int) currentPosition;
        songSeekBar.setProgress((int)currentPosition);
    }
}