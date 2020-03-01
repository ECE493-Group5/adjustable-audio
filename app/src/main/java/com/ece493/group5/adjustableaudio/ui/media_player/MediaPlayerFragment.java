package com.ece493.group5.adjustableaudio.ui.media_player;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaPlayerFragment extends Fragment
{
    private static final String TAG = MediaPlayerFragment.class.getSimpleName();

    private MediaPlayerViewModel mediaPlayerViewModel;
    private MediaBrowser mediaBrowser;
    private MediaController mediaController;

    private TextView songTitle;
    private TextView artistTitle;
//    private ImageView albumArt;
    private ImageButton skipPreviousButton;
    private ImageButton rewindButton;
    private ImageButton playPauseButton;
    private ImageButton fastForwardButton;
    private ImageButton skipNextButton;
    private TextView songTitleLabel;
    private TextView songArtistLabel;
    private TextView mediaTimeLabel;

    private Integer audioIndex;
    private Bundle songBundle;
    ArrayList<Song> audioList;


    private final MediaController.Callback controllerCallback = new MediaController.Callback()
    {
        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state)
        {
            super.onPlaybackStateChanged(state);
            mediaPlayerViewModel.setState(state);
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadata metadata)
        {
            super.onMetadataChanged(metadata);
            mediaPlayerViewModel.setMetadata(metadata);
        }
    };


    private final MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback()
    {
        @Override
        public void onConnected()
        {
            Log.d(TAG, "onConnected");
            MediaSession.Token token = mediaBrowser.getSessionToken();
            mediaController = new MediaController(getContext(), token);

            enableMediaControls(mediaController);

            mediaPlayerViewModel.setState(mediaController.getPlaybackState());
            mediaPlayerViewModel.setMetadata(mediaController.getMetadata());

            mediaController.registerCallback(controllerCallback);
        }

        @Override
        public void onConnectionSuspended()
        {
            // The Service has crashed.
            // Disable transport controls until it automatically reconnects.
            disableMediaControls();
        }

        @Override
        public void onConnectionFailed()
        {
            // The Service has refused our connection.
            Log.d("MediaBrowser", "Failed to connect to MediaBrowserService.");
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);

        mediaBrowser =
                new MediaBrowser(
                        getContext(), new ComponentName(getContext(), MusicService.class),
                        connectionCallback, null);

//        albumArt = root.findViewById(R.id.albumArt);
        skipPreviousButton = root.findViewById(R.id.skipPrevButton);
        rewindButton = root.findViewById(R.id.fastRewindButton);
        playPauseButton = root.findViewById(R.id.playButton);
        fastForwardButton = root.findViewById(R.id.fastForwardButton);
        skipNextButton = root.findViewById(R.id.skipForwardButton);
        songTitleLabel = root.findViewById(R.id.labelSongTitle);
        songArtistLabel = root.findViewById(R.id.labelArtist);
        mediaTimeLabel = root.findViewById(R.id.mediaTime);

        mediaPlayerViewModel.getState().observe(this, new Observer<PlaybackState>()
        {
            @Override
            public void onChanged(@Nullable PlaybackState state)
            {
                Log.d("MediaPlayerFragment", "PlaybackState changed.");
                Log.d(TAG,  Integer.toString(state.getState()));

                if (state != null && state.getState() == PlaybackState.STATE_PLAYING)
                    showPauseButton();
                else
                    showPlayButton();
            }
        });

        mediaPlayerViewModel.getMetadata().observe(this, new Observer<MediaMetadata>()
        {
            @Override
            public void onChanged(@Nullable MediaMetadata metadata)
            {
                if (metadata == null)
                    return;

                MediaDescription mediaDescription = metadata.getDescription();
//                albumArt.setImageBitmap(mediaDescription.getIconBitmap());
                songArtistLabel.setText(mediaDescription.getTitle());
                songTitleLabel.setText(mediaDescription.getSubtitle());

                Log.d("MediaPlayer", "" + mediaDescription.getSubtitle());
                Log.d("MediaPlayer", "" + mediaDescription.getDescription());
            }
        });

        boolean perm = checkAndRequestPermissions();
        if (perm)
        {
            Log.d("MediaPlayerFragment", "Requested Permission.");
            this.loadAudio();
        }
        else
        {
            Log.d("MediaPlayerFragment", "No Permissions.");
        }

        songBundle = new Bundle();
        audioIndex = 0;
        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mediaBrowser.disconnect();
    }


    private boolean checkAndRequestPermissions()
    {
        int readStoragePermission = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStoragePermission = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> permissionsToRequest = new ArrayList<>();

        if (readStoragePermission != PackageManager.PERMISSION_GRANTED || writeStoragePermission != PackageManager.PERMISSION_GRANTED)
        {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    1);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == 1)
        {
            this.loadAudio();
        }
    }

    private void loadAudio()
    {
        ContentResolver contentResolver = getActivity().getContentResolver();

        Uri uri =  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        Cursor cursor = contentResolver.query(uri, null, selection, null,
                null);
        if (cursor!= null && cursor.getCount() > 0)
        {
            audioList = new ArrayList<Song>();
            while(cursor.moveToNext())
            {
                String filename = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                String mediaId = title.replace(' ', '_');
                Song song = new Song(title, artist, album, filename, mediaId);
                audioList.add(song);
            }
        }
        audioIndex = 0;
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

    public void enableMediaControls(final MediaController mediaController)
    {
        skipPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "SkipPrevButton is pressed");
                mediaController.getTransportControls().skipToPrevious();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "RewindButton is pressed");
                mediaController.getTransportControls().rewind();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "PlayButton is pressed");
                PlaybackState state = mediaController.getPlaybackState();

                if (state == null)
                    return;

                Log.d(TAG, Integer.toString(state.getState()));

                if (state.getState() == PlaybackState.STATE_PLAYING)
                {
                    Log.d(TAG, "About to pause");
                    mediaController.getTransportControls().pause();
                }
                else {
                    Log.d(TAG, "About to send song");
                    songBundle.putParcelable("SONG_TO_PLAY", audioList.get(audioIndex));
                    mediaController.getTransportControls().playFromMediaId(audioList.get(audioIndex).getMediaId(), songBundle);
                }
            }
        });

        fastForwardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "FastForwardButton is pressed");
                mediaController.getTransportControls().fastForward();
            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("MediaPlayerFragment", "Skip Next Button is pressed");
                mediaController.getTransportControls().skipToNext();
            }
        });
    }

    public void disableMediaControls()
    {
        skipPreviousButton.setOnClickListener(null);
        rewindButton.setOnClickListener(null);
        playPauseButton.setOnClickListener(null);
        fastForwardButton.setOnClickListener(null);
        skipNextButton.setOnClickListener(null);
    }
}