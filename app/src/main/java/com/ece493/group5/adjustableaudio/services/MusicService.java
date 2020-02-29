package com.ece493.group5.adjustableaudio.services;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MusicService extends MediaBrowserService
{
    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";

    private static final String MEDIA_ROOT_ID = "media_root_id";
    private static final String EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    private static final String SELECTION = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    private static final String[] PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
    };

    private MediaSession mediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private MediaPlayerAdapter mediaPlayerAdapter;

    private List<MediaSession.QueueItem> mediaQueue = new ArrayList<>();
    private int nextToPlay = -1;

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
        Log.d("MusicService", clientPackageName + " " + clientUid);

        if (allowBrowsing(clientPackageName, clientUid))
            return new BrowserRoot(MEDIA_ROOT_ID, null);
        else
            return new BrowserRoot(EMPTY_MEDIA_ROOT_ID, null);
    }


    @Override
    public void onLoadChildren(String parentId, final Result<List<MediaBrowser.MediaItem>> result)
    {
        //  Browsing not allowed
        if (TextUtils.equals(EMPTY_MEDIA_ROOT_ID, parentId))
        {
            result.sendResult(null);
            return;
        }

        List<MediaBrowser.MediaItem> mediaItems = new ArrayList<>();
        if (MEDIA_ROOT_ID.equals(parentId))
        {
            Cursor cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    SELECTION,
                    null,
                    null);

            while(cursor.moveToNext())
            {
                MediaDescription description = new MediaDescription.Builder()
                        .setMediaId(cursor.getString(0))
                        .setTitle(cursor.getString(1))
                        .setSubtitle(cursor.getString(2))
                        .build();

                MediaBrowser.MediaItem item = new MediaBrowser.MediaItem(
                        description, MediaBrowser.MediaItem.FLAG_BROWSABLE);

                mediaItems.add(item);
            }
        }

        result.sendResult(mediaItems);
    }

    protected boolean allowBrowsing(String clientPackageName, int clientUid)
    {
        int uid = 0;

        try
        {
            uid = getPackageManager()
                    .getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA)
                    .uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return clientPackageName.equals(PACKAGE_NAME) && clientUid == uid;
    }

    class MediaSessionCallback extends MediaSession.Callback
    {
        @Override
        public void onPrepare()
        {
            if (mediaQueue.isEmpty() && nextToPlay < 0)
            {
                //Nothing to play
                return;
            }

            String mediaID = mediaQueue.get(nextToPlay).getDescription().getMediaId();
            //TODO: Translate mediaID to actual media

            if(!mediaSession.isActive())
            {
                mediaSession.setActive(true);
            }
        }


        @Override
        public void onPlay()
        {
            if(mediaQueue.isEmpty())
            {
                //Nothing to play
                return;
            }

            onPrepare();
            mediaPlayerAdapter.playMediaFile();
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

        @Override
        public void onCustomAction(@NonNull String action, @Nullable Bundle extras)
        {
            super.onCustomAction(action, extras);
        }
    }
}