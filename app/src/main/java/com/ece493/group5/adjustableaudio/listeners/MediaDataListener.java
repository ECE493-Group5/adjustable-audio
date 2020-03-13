package com.ece493.group5.adjustableaudio.listeners;

import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.Bundle;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.enums.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;

import java.util.ArrayList;
import java.util.List;

public abstract class MediaDataListener
{
    public void handleChange(PlaybackState playbackState)
    {
        Bundle extras = playbackState.getExtras();
        extras.setClassLoader(MediaPlayerAdapter.class.getClassLoader());
        MediaData dataChanged = (MediaData) extras
                .getSerializable(MediaData.EXTRA_DATA_CHANGED);

        switch (dataChanged)
        {
            case QUEUE:
                ArrayList<Song> queue = extras.getParcelableArrayList(MediaData.EXTRA_QUEUE);
                onQueueChanged(queue);
                break;
            case QUEUE_INDEX:
                int index = extras.getInt(MediaData.EXTRA_QUEUE_INDEX, -1);
                Song song = extras.getParcelable(MediaData.EXTRA_SONG);
                onQueueIndexChanged(index, song);
                break;
            case STATE:
                onStateChanged(playbackState.getState());
                break;
            case DURATION:
                long elapsed = playbackState.getPosition();
                long total = extras.getLong(MediaData.EXTRA_DURATION,0);
                onDurationChanged((int) elapsed, (int) total);
        }
    }

    public abstract void onQueueChanged(List<Song> queue);
    public abstract void onQueueIndexChanged(int index, Song song);
    public abstract void onStateChanged(int state);
    public abstract void onDurationChanged(int elapsed, int total);
}
