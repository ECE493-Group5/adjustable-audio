package com.ece493.group5.adjustableaudio.ui.media_player;

import android.graphics.drawable.Drawable;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;

public class MediaPlayerFragment extends Fragment {

    private MediaPlayerViewModel mediaPlayerViewModel;
    private MediaBrowser mediaBrowser;

    private ImageView albumArt;
    private ImageButton skipPreviousButton;
    private ImageButton rewindButton;
    private ImageButton playPauseButton;
    private ImageButton fastForwardButton;
    private ImageButton skipNextButton;

    private final MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback() {
        @Override
        public void onConnected() {
            MediaSession.Token token = mediaBrowser.getSessionToken();
            final MediaController mediaController = new MediaController(getActivity(), token);
            enableMediaControls(mediaController);
        }

        @Override
        public void onConnectionSuspended() {
            // The Service has crashed.
            // Disable transport controls until it automatically reconnects.
            disableMediaControls();
        }

        @Override
        public void onConnectionFailed() {
            // The Service has refused our connection.
            Log.d("MediaBrowser", "Failed to connect to MediaBrowserService.");
        }

    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);

        /** TODO: mediaBrowser = new MediaBrowser(); **/

        albumArt = root.findViewById(R.id.albumArt);
        skipPreviousButton = root.findViewById(R.id.skipPrevButton);
        rewindButton = root.findViewById(R.id.fastRewindButton);
        playPauseButton = root.findViewById(R.id.playButton);
        fastForwardButton = root.findViewById(R.id.fastForwardButton);
        skipNextButton = root.findViewById(R.id.skipForwardButton);

        mediaPlayerViewModel.isSongPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSongPlaying) {
                if (isSongPlaying)
                    showPauseButton();
                else
                    showPlayButton();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mediaBrowser.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mediaBrowser.disconnect();
    }

    public void showPauseButton() {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_pause_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }

    public void showPlayButton() {
        Drawable drawable
                = getResources().getDrawable(R.drawable.ic_play_arrow_grey_24dp, null);

        playPauseButton.setImageDrawable(drawable);
    }

    public void enableMediaControls(final MediaController mediaController) {
        skipPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MediaPlayerFragment", "SkipPrevButton is pressed");
                mediaController.getTransportControls().skipToPrevious();
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MediaPlayerFragment", "RewindButton is pressed");
                mediaController.getTransportControls().rewind();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MediaPlayerFragment", "PlayButton is pressed");
                mediaPlayerViewModel.toggleIsSongPlaying();
                boolean isSongPlaying = mediaPlayerViewModel.isSongPlaying().getValue();

                if (isSongPlaying) {
                    mediaController.getTransportControls().play();
                } else {
                    mediaController.getTransportControls().pause();
                }
            }
        });

        fastForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MediaPlayerFragment", "FastForwardButton is pressed");
                mediaController.getTransportControls().fastForward();
            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MediaPlayerFragment", "Skip Next Button is pressed");
                mediaController.getTransportControls().skipToNext();
            }
        });
    }

    public void disableMediaControls() {
        skipPreviousButton.setOnClickListener(null);
        rewindButton.setOnClickListener(null);
        playPauseButton.setOnClickListener(null);
        fastForwardButton.setOnClickListener(null);
        skipNextButton.setOnClickListener(null);
    }
}