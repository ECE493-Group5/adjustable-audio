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
import com.ece493.group5.adjustableaudio.models.Song;

import java.util.ArrayList;
import java.util.List;


public class MusicService extends MediaBrowserService
{
    private static final String TAG = MusicService.class.getSimpleName();

    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";

    public static final String BUNDLE_QUEUE =  "BUNDLE_QUEUE";
    public static final String BUNDLE_QUEUE_INDEX =  "BUNDLE_QUEUE_INDEX";

    public static final String LOCAL_MEDIA_ROOT_ID = "media_root_id";
    public static final String EMPTY_MEDIA_ROOT_ID = "empty_root_id";

    public static final String SELECTION = MediaStore.Audio.Media.IS_MUSIC + " != 0";

    public static final String ACTION_SONG_SELECTED = "ACTION_SELECT_SONG";
    public static final String ACTION_ENQUEUE = "ACTION_ENQUEUE";
    public static final String ACTION_DEQUEUE = "ACTION_DEQUEUE";
    public static final String ACTION_TRIGGER_UPDATE_PLAYBACK_STATE = "ACTION_TRIGGER_UPDATE_PLAYBACK_STATE";
    public static final String ACTION_LEFT_VOLUME_CHANGED = "ACTION_CHANGE_LEFT_VOLUME";
    public static final String ACTION_RIGHT_VOLUME_CHANGED = "ACTION_CHANGE_RIGHT_VOLUME";

    private MediaSession mediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private MediaPlayerAdapter mediaPlayerAdapter;

    private int queueIndex;
    private ArrayList<Song> queue;

    @Override
    public void onCreate() {
        super.onCreate();

        queueIndex = -1;
        queue = new ArrayList<>();

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

    public Song getSong(Integer index)
    {
        if (index == null || index < 0 || index >= queue.size())
            return null;

        return queue.get(index);
    }

    public List<Song> getQueue()
    {
        return queue;
    }

    public void setQueue(ArrayList<Song> queue)
    {
        this.queue = queue;
    }

    public int getQueueIndex()
    {
        return queueIndex;
    }

    public void setQueueIndex(int index)
    {
        this.queueIndex = index;
    }

    public void updatePlaybackState()
    {
        Bundle extras = new Bundle();
        extras.putInt(BUNDLE_QUEUE_INDEX, queueIndex);
        extras.putParcelableArrayList(BUNDLE_QUEUE, queue);
        PlaybackState.Builder builder = new PlaybackState
                .Builder()
                .setExtras(extras);

        if (mediaPlayerAdapter != null)
            builder.setState(
                    mediaPlayerAdapter.getState(),
                    mediaPlayerAdapter.getPosition(),
                    mediaPlayerAdapter.getPlaybackSpeed());

        mediaSession.setPlaybackState(builder.build());
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
        MediaSessionCallback()
        {
            updatePlaybackState();
        }

        @Override
        public void onPlay()
        {
            super.onPlay();

            Song song = getSong(queueIndex);
            if (song == null)
                return;

            mediaSession.setActive(true);
            mediaPlayerAdapter.playFile(song.getFilename());
        }

        @Override
        public void onPause()
        {
            super.onPause();
            mediaPlayerAdapter.pauseMedia();
        }

        @Override
        public void onSkipToNext()
        {
            super.onSkipToNext();

            Song nextSong = getSong(queueIndex + 1);
            if (nextSong == null)
                return;

            setQueueIndex(queueIndex + 1);
            mediaPlayerAdapter.playFile(nextSong.getFilename());

            // Propagate the new QueueIndex to client
            updatePlaybackState();
        }

        @Override
        public void onSkipToPrevious()
        {
            super.onSkipToNext();

            Song previousSong = getSong(queueIndex - 1);
            if (previousSong == null)
                return;

            setQueueIndex(queueIndex - 1);
            mediaPlayerAdapter.playFile(previousSong.getFilename());

            // Propagate the new QueueIndex to the client
            updatePlaybackState();
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

            switch (action)
            {
                case ACTION_SONG_SELECTED:
                    onSongSelected(extras);
                    break;
                case ACTION_ENQUEUE:
                    onEnqueue(extras);
                    break;
                case ACTION_DEQUEUE:
                    onDequeue(extras);
                case ACTION_TRIGGER_UPDATE_PLAYBACK_STATE:
                    updatePlaybackState();
                    break;
                case ACTION_LEFT_VOLUME_CHANGED:
                    break;
                case ACTION_RIGHT_VOLUME_CHANGED:
                    break;
            }
        }

        public void onEnqueue(@Nullable Bundle extras)
        {
            if (extras == null)
                return;

            getQueue().add(Song.fromBundle(extras));
            if (getQueue().size() == 1)
                setQueueIndex(0);

            updatePlaybackState();
        }

        public void onDequeue(@Nullable Bundle extras)
        {
            if (extras == null)
                return;

            int index = extras.getInt(BUNDLE_QUEUE_INDEX, -1);
            if (getQueueIndex() == index)
                setQueueIndex(index - 1);
            getQueue().remove(index);

            updatePlaybackState();
        }

        public void onSongSelected(@Nullable Bundle extras)
        {
            if (extras == null)
                return;

            setQueueIndex(extras.getInt(BUNDLE_QUEUE_INDEX, -1));
        }
    }


    class MediaPlayerListener implements PlaybackListener
    {
        MediaPlayerListener()
        {

        }

        public void onPlaybackStateChange(PlaybackState state)
        {
//            if (state.getState() == PlaybackState.STATE_SKIPPING_TO_NEXT)
//            {
//                songIndex = (songIndex + 1) % songQueue.size();
//                mediaPlayerAdapter.playFile(songQueue.get(songIndex));
//            }

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