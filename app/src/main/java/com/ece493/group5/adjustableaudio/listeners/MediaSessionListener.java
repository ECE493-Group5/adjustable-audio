package com.ece493.group5.adjustableaudio.listeners;

import android.media.session.MediaSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;

public class MediaSessionListener extends MediaSession.Callback
{
    public static final String ACTION_SONG_SELECTED = "ACTION_SELECT_SONG";
    public static final String ACTION_ENQUEUE = "ACTION_ENQUEUE";
    public static final String ACTION_DEQUEUE = "ACTION_DEQUEUE";
    public static final String ACTION_TRIGGER_UPDATE_PLAYBACK_STATE = "ACTION_TRIGGER_UPDATE_PLAYBACK_STATE";
    public static final String ACTION_LEFT_VOLUME_CHANGED = "ACTION_CHANGE_LEFT_VOLUME";
    public static final String ACTION_RIGHT_VOLUME_CHANGED = "ACTION_CHANGE_RIGHT_VOLUME";

    private MediaPlayerAdapter adapter;

    public MediaSessionListener(MediaPlayerAdapter adapter)
    {
        super();

        this.adapter = adapter;
    }

    @Override
    public void onPlay()
    {
        super.onPlay();
//            musicNotificationManager.updateSong(song);
        adapter.play();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        adapter.pause();
    }

    @Override
    public void onSkipToNext()
    {
        super.onSkipToNext();

        if (adapter.hasNextSong())
            adapter.skipToNextSong();
//            musicNotificationManager.updateSong(nextSong);
    }

    @Override
    public void onSkipToPrevious()
    {
        super.onSkipToNext();

        if (adapter.hasPreviousSong())
            adapter.skipToPreviousSong();
//            musicNotificationManager.updateSong(previousSong);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        adapter.stop();
    }

    @Override
    public void onSeekTo (long position)
    {
        super.onSeekTo(position);
        adapter.seekTo((int) position);
    }

    @Override
    public void onCustomAction(@NonNull String action, @Nullable Bundle extras)
    {
        super.onCustomAction(action, extras);

        switch (action)
        {
            case ACTION_SONG_SELECTED:
                adapter.onSongSelected(extras);
                break;
            case ACTION_ENQUEUE:
                adapter.enqueue(extras);
                break;
            case ACTION_DEQUEUE:
                adapter.dequeue(extras);
            case ACTION_TRIGGER_UPDATE_PLAYBACK_STATE:
                adapter.notifyPlaybackStateChanged();
                break;
            case ACTION_LEFT_VOLUME_CHANGED:
                break;
            case ACTION_RIGHT_VOLUME_CHANGED:
                break;
        }
    }
}
