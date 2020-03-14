package com.ece493.group5.adjustableaudio.listeners;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.notifications.MusicNotificationManager;

public class MediaSessionListener extends MediaSession.Callback
{
    public static final String ACTION_SONG_SELECTED = "ACTION_SELECT_SONG";
    public static final String ACTION_TOGGLE = "ACTION_TOGGLE";
    public static final String ACTION_ENQUEUE = "ACTION_ENQUEUE";
    public static final String ACTION_DEQUEUE = "ACTION_DEQUEUE";
    public static final String ACTION_REQUEST_ALL_CHANGES = "ACTION_TRIGGER_UPDATE_PLAYBACK_STATE";
    public static final String ACTION_LEFT_VOLUME_CHANGED = "ACTION_CHANGE_LEFT_VOLUME";
    public static final String ACTION_RIGHT_VOLUME_CHANGED = "ACTION_CHANGE_RIGHT_VOLUME";

    private MediaPlayerAdapter adapter;
    private MusicNotificationManager notificationManager;

    public MediaSessionListener(MediaPlayerAdapter adapter, MusicNotificationManager notificationManager)
    {
        super();

        this.adapter = adapter;
        this.notificationManager = notificationManager;
    }

    @Override
    public void onPlay()
    {
        super.onPlay();

        if (adapter.hasCurrentSong())
            adapter.play();

        notificationManager.updateSong(adapter.getCurrentSong());
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (adapter.hasCurrentSong())
            adapter.pause();

        notificationManager.updateSong(adapter.getCurrentSong());
    }

    @Override
    public void onSkipToNext()
    {
        super.onSkipToNext();

        if (adapter.hasNextSong()) {
            boolean wasPlaying = adapter.isPlaying();

            adapter.skipToNextSong();

            if (wasPlaying)
                adapter.play();

            notificationManager.updateSong(adapter.getCurrentSong());
        }
    }

    @Override
    public void onSkipToPrevious()
    {
        super.onSkipToNext();

        if (adapter.hasPreviousSong()) {
            boolean wasPlaying = adapter.isPlaying();

            adapter.skipToPreviousSong();

            if (wasPlaying)
                adapter.play();

            notificationManager.updateSong(adapter.getCurrentSong());
        }
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
        adapter.seekTo(position);
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
            case ACTION_TOGGLE:
                if (adapter.isPlaying())
                    onPause();
                else
                    onPlay();
                break;
            case ACTION_ENQUEUE:
                adapter.enqueue(extras);
                break;
            case ACTION_DEQUEUE:
                adapter.dequeue(extras);
            case ACTION_REQUEST_ALL_CHANGES:
                adapter.notifyAllChanged();
                break;
            case ACTION_LEFT_VOLUME_CHANGED:
                break;
            case ACTION_RIGHT_VOLUME_CHANGED:
                break;
        }
    }
}
