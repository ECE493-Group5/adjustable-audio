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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.listeners.MediaSessionListener;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.notifications.MusicNotificationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class MusicService extends MediaBrowserService implements Observer
{
    private static final String TAG = MusicService.class.getSimpleName();

    public static final String EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    public static final String LOCAL_MEDIA_ROOT_ID = "media_root_id";
    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";
    public static final String SELECTION = MediaStore.Audio.Media.IS_MUSIC + " != 0";

    private MediaSession mediaSession;
    private MediaPlayerAdapter mediaPlayerAdapter;
    private MusicNotificationManager musicNotificationManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Create the MediaSession
        mediaSession = new MediaSession(this, "Music Service");
        mediaPlayerAdapter = new MediaPlayerAdapter(this);
//        musicNotificationManager = new MusicNotificationManager(this);
        mediaSession.setCallback(new MediaSessionListener(mediaPlayerAdapter));
        setSessionToken(mediaSession.getSessionToken());

        mediaPlayerAdapter.addObserver(this);
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
        if (allowBrowsing(clientPackageName, clientUid))
            return new BrowserRoot(LOCAL_MEDIA_ROOT_ID, null);
        else
            return new BrowserRoot(EMPTY_MEDIA_ROOT_ID, null);
    }

    @Override
    public void onLoadChildren(String parentId, final Result<List<MediaBrowser.MediaItem>> result)
    {
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
        Cursor cursor = getContentResolver().query(uri, null, SELECTION, null,
                null);

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

                mediaItems.add(new MediaBrowser.MediaItem(mediaDescription,
                        MediaBrowser.MediaItem.FLAG_PLAYABLE));
            }
        }
        return mediaItems;
    }

    protected boolean allowBrowsing(String clientPackageName, int clientUid)
    {
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

    @Override
    public void update(Observable o, Object playbackStateObject)
    {
        PlaybackState state = (PlaybackState) playbackStateObject;

        switch (state.getState())
        {
            case PlaybackState.STATE_PLAYING:
                mediaSession.setActive(true);
                break;
            case PlaybackState.STATE_STOPPED:
                mediaSession.setActive(false);
                break;
        }

        mediaSession.setPlaybackState(state);
    }
}