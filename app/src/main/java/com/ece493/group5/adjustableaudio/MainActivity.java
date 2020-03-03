package com.ece493.group5.adjustableaudio;

import android.content.ComponentName;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;

import com.ece493.group5.adjustableaudio.listeners.MediaFragmentListener;
import com.ece493.group5.adjustableaudio.services.MusicService;
import com.ece493.group5.adjustableaudio.ui.media_player.MediaPlayerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements MediaFragmentListener
{


    private static final String TAG = MainActivity.class.getSimpleName();

    private MediaBrowser mediaBrowser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_hearing_test, R.id.navigation_microphone, R.id.navigation_media_player, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mediaBrowser = new MediaBrowser(this,
                new ComponentName(this, MusicService.class), connectionCallback, null);
        this.mediaBrowser.connect();

        Log.d(TAG, "OnCreate Main Activity");
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "OnStart Main Activity");
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        if (getMediaController() != null)
        {
            getMediaController().unregisterCallback(controllerCallback);
        }
        this.mediaBrowser.disconnect();
    }


    private final MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback() {
        @Override
        public void onConnected() {
            Log.d(TAG, "Media Browser is onConnected");
            MediaSession.Token token = mediaBrowser.getSessionToken();

            if (token == null)
            {
                Log.d(TAG, "Token is null");
            }
            MediaController mediaController = new MediaController(getApplicationContext(), token);
            setMediaController(mediaController);

//            enableMediaControls();

//            mediaPlayerViewModel.setState(mediaController.getPlaybackState());
//            mediaPlayerViewModel.setMetadata(mediaController.getMetadata());

            mediaController.registerCallback(controllerCallback);
        }

        @Override
        public void onConnectionFailed()
        {
            // The Service has refused our connection.
            Log.d(TAG, "Failed to connect to MediaBrowserService.");
        }
    };

    private final MediaController.Callback controllerCallback = new MediaController.Callback()
    {
        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state)
        {
            super.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadata metadata)
        {
            if (metadata == null)
            {
                return;
            }

            super.onMetadataChanged(metadata);
        }
    };

    @Override
    public MediaBrowser getMediaBrowser()
    {
        return this.mediaBrowser;
    }


    public void onConnected()
    {

    }
}
