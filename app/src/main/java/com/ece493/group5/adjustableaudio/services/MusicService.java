package com.ece493.group5.adjustableaudio.services;

import android.content.Intent;
import android.media.AudioManager;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.service.media.MediaBrowserService;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MusicService extends MediaBrowserService {

    private MediaSession mediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private MediaPlayerAdapter mediaPlayerAdapter;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Create the MediaSession
        mediaSession = new MediaSession(this, "Music Service");
        mediaSessionCallback = new MediaSessionCallback();
        mediaSession.setCallback(mediaSessionCallback);
        setSessionToken(mediaSession.getSessionToken());

        mediaPlayerAdapter = new MediaPlayerAdapter(this);
    }


    @Override
    public void onDestroy()
    {
        this.mediaPlayerAdapter.stop();
        this.mediaSession.release();
    }


    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints)
    {
        return null;
    }


    @Override
    public void onLoadChildren(String parentId, Result<List<MediaBrowser.MediaItem>> result)
    {

    }


    class MediaSessionCallback extends MediaSession.Callback
    {

        @Override
        public void onPlay()
        {

        }


        @Override
        public void onPause()
        {
            mediaPlayerAdapter.pauseMedia();
        }


        @Override
        public void onSkipToNext()
        {

        }


        @Override
        public void onSkipToPrevious()
        {

        }
    }

}