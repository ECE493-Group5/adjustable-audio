package com.ece493.group5.adjustableaudio.listeners;

import android.media.session.MediaSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;

public class MediaSessionListener extends MediaSession.Callback
{
    public static final String ACTION_SONG_SELECTED = "ACTION_SELECT_SONG";
    public static final String ACTION_TOGGLE = "ACTION_TOGGLE";
    public static final String ACTION_ENQUEUE = "ACTION_ENQUEUE";
    public static final String ACTION_DEQUEUE = "ACTION_DEQUEUE";
    public static final String ACTION_REQUEST_ALL_CHANGES = "ACTION_TRIGGER_UPDATE_PLAYBACK_STATE";
    public static final String ACTION_LEFT_RIGHT_VOLUME_RATIO_CHANGED = "ACTION_CHANGE_LEFT_RIGHT_VOLUME_RATIO";
    public static final String ACTION_EQUALIZER_BAND_CHANGED = "ACTION_EQUALIZER_BAND_CHANGED";
    public static final String ACTION_EQUALIZER_STATE = "ACTION_EQUALIZER_STATE";

    public static final String EXTRA_QUEUE_INDEX =  "BUNDLE_QUEUE_INDEX";
    public static final String EXTRA_VOLUME =  "BUNDLE_VOLUME";
    public static final String EXTRA_DECIBEL_LEVEL = "DECIBEL_LEVEL";
    public static final String EXTRA_EQUALIZER_BAND = "EQUALIZER_BAND";
    public static final String EXTRA_EQUALIZER_ENABLED = "EQUALIZER_ENABLED";

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

        if (adapter.hasCurrentSong())
            adapter.play();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (adapter.hasCurrentSong())
            adapter.pause();
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
            case ACTION_LEFT_RIGHT_VOLUME_RATIO_CHANGED:
                adapter.setLeftRightVolumeRatio(extras.getDouble(EXTRA_VOLUME, 0));
                break;
            case ACTION_EQUALIZER_BAND_CHANGED:
                adapter.setEqualizerBand(extras);
                break;
            case ACTION_EQUALIZER_STATE:
                boolean enabled = extras.getBoolean(EXTRA_EQUALIZER_ENABLED, false);
                if (enabled)
                    adapter.enableEqualizer();
                else
                    adapter.disableEqualizer();
        }
    }
}
