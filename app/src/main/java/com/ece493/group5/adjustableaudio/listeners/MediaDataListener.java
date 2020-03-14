package com.ece493.group5.adjustableaudio.listeners;

import android.os.Bundle;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.models.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;

import java.util.ArrayList;
import java.util.List;

public abstract class MediaDataListener
{
    public static final String EXTRA_STATE =  "BUNDLE_STATE";
    public static final String EXTRA_QUEUE =  "BUNDLE_QUEUE";
    public static final String EXTRA_QUEUE_INDEX =  "BUNDLE_QUEUE_INDEX";
    public static final String EXTRA_SONG =  "BUNDLE_SONG";
    public static final String EXTRA_TOTAL_DURATION = "BUNDLE_TOTAL_DURATION";
    public static final String EXTRA_ELAPSED_DURATION = "BUNDLE_ELAPSED_DURATION";

    public void handleChange(Bundle extras)
    {
        MediaData mediaData = MediaData.extract(extras);

        if (mediaData.queueChanged())
        {
            onQueueChanged(mediaData.getQueue());
        }

        if (mediaData.queueIndexChanged())
        {
            onQueueIndexChanged(mediaData.getQueueIndex(), mediaData.getCurrentSong());
        }

        if (mediaData.stateChanged())
        {
            onStateChanged(mediaData.getState());
        }

        if (mediaData.durationChanged())
        {
            onDurationChanged((int) mediaData.getElapsedDuration(), (int) mediaData.getTotalDuration());
        }
    }

    public void onQueueChanged(List<Song> queue) {}
    public void onQueueIndexChanged(int index, Song song) {}
    public void onStateChanged(int state) {}
    public void onDurationChanged(int elapsed, int total) {}
}
