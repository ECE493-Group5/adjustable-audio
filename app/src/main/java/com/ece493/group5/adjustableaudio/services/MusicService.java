package com.ece493.group5.adjustableaudio.services;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.listeners.PlaybackListener;

import java.util.ArrayList;
import java.util.List;


public class MusicService extends MediaBrowserService
{
    private static final String TAG = MusicService.class.getSimpleName();

    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";

    public static final String LOCAL_MEDIA_ROOT_ID = "media_root_id";
    public static final String EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    public static final String SELECTION = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    public static final String[] PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA
    };

    private MediaSession mediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private MediaPlayerAdapter mediaPlayerAdapter;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MediaPlayerFragment", "MusicService onCreate");
        // Create the MediaSession
        mediaSession = new MediaSession(this, "Music Service");
        mediaSessionCallback = new MediaSessionCallback();
        mediaSession.setCallback(mediaSessionCallback);
        setSessionToken(mediaSession.getSessionToken());

        mediaPlayerAdapter = new MediaPlayerAdapter(this, new MediaPlayerListener());
    }


    @Override
    public void onDestroy() {
        this.mediaPlayerAdapter.stopMedia();
        this.mediaSession.release();
    }


    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        Log.d(TAG, clientPackageName + " " + clientUid);

        if (allowBrowsing(clientPackageName, clientUid))
            return new BrowserRoot(LOCAL_MEDIA_ROOT_ID, null);
        else
            return new BrowserRoot(EMPTY_MEDIA_ROOT_ID, null);
    }


    @Override
    public void onLoadChildren(String parentId, final Result<List<MediaBrowser.MediaItem>> result) {
        if (TextUtils.equals(EMPTY_MEDIA_ROOT_ID, parentId))
        {
            result.sendResult(null);
            return;
        }

        List<MediaBrowser.MediaItem> mediaItems = new ArrayList<>();
        if (LOCAL_MEDIA_ROOT_ID.equals(parentId))
            mediaItems.addAll(queryLocalMediaItems());

        result.sendResult(mediaItems);
    }


    protected List<MediaBrowser.MediaItem> queryLocalMediaItems()
    {
        List<MediaBrowser.MediaItem> mediaItems = new ArrayList<>();
        Uri uri =  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, SELECTION, null, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                MediaDescription mediaDescription = new MediaDescription.Builder()
                        .setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
                        .setSubtitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)))
                        .setDescription(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)))
                        .setMediaId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)).replace(' ', '_'))
                        .build();

                mediaItems.add(new MediaBrowser.MediaItem(mediaDescription, MediaBrowser.MediaItem.FLAG_PLAYABLE));
            }
        }

        return mediaItems;
    }


    protected boolean allowBrowsing(String clientPackageName, int clientUid) {
        int uid = 0;

        try
        {
            uid = getPackageManager()
                    .getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA)
                    .uid;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return clientPackageName.equals(PACKAGE_NAME) && clientUid == uid;
    }


    class MediaSessionCallback extends MediaSession.Callback
    {
        @Override
        public void onPlay()
        {
            super.onPlay();
            mediaSession.setActive(true);
            mediaPlayerAdapter.playMedia();
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras)
        {
            super.onPlayFromMediaId(mediaId, extras);
            mediaSession.setActive(true);
            mediaPlayerAdapter.playFile(mediaId);
            Log.d(TAG, "onPlayFromMediaId: " + mediaId);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            Log.d(TAG, "onPause: will pause media");
            mediaPlayerAdapter.pauseMedia();
        }

        @Override
        public void onSkipToNext()
        {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious()
        {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop()
        {
            super.onStop();
            mediaPlayerAdapter.stopMedia();
            mediaSession.setActive(false);
        }

        @Override
        public void onCustomAction(@NonNull String action, @Nullable Bundle extras)
        {
            super.onCustomAction(action, extras);
        }
    }


    class MediaPlayerListener implements PlaybackListener
    {
        MediaPlayerListener()
        {

        }

        public void onPlaybackStateChange(PlaybackState state)
        {
            //Tell media session the state
            mediaSession.setPlaybackState(state);

//            if (state.getState() == PlaybackState.STATE_PLAYING)
//            {
//
//            }
//            else if (state.getState() == PlaybackState.STATE_PAUSED)
//            {
//
//            }
//            else if (state.getState() == PlaybackState.STATE_STOPPED)
//            {
//
//            }
        }
    }
}