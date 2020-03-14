package com.ece493.group5.adjustableaudio.listeners;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.services.MusicService;

public class MediaSessionListener extends MediaSession.Callback
{
    private static final String TAG = MusicService.class.getSimpleName();

    public static final String ACTION_SONG_SELECTED = "ACTION_SELECT_SONG";
    public static final String ACTION_ENQUEUE = "ACTION_ENQUEUE";
    public static final String ACTION_DEQUEUE = "ACTION_DEQUEUE";
    public static final String ACTION_REQUEST_ALL_CHANGES = "ACTION_TRIGGER_UPDATE_PLAYBACK_STATE";
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
        if (adapter.hasCurrentSong())
            adapter.play();

        Log.d(TAG, "onPlay");
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (adapter.hasCurrentSong())
            adapter.pause();

        Log.d(TAG, "onPause");
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
