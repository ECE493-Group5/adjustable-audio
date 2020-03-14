package com.ece493.group5.adjustableaudio.listeners;

import android.os.Bundle;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
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
        extras.setClassLoader(MediaPlayerAdapter.class.getClassLoader());
        if (extras.containsKey(EXTRA_QUEUE))
        {
            ArrayList<Song> queue = extras.getParcelableArrayList(EXTRA_QUEUE);
            onQueueChanged(queue);
        }

        if (extras.containsKey(EXTRA_QUEUE_INDEX))
        {
            int index = extras.getInt(EXTRA_QUEUE_INDEX, -1);
            Song song = extras.getParcelable(EXTRA_SONG);
            onQueueIndexChanged(index, song);
        }

        if (extras.containsKey(EXTRA_STATE))
        {
            int state = extras.getInt(EXTRA_STATE,-1);
            onStateChanged(state);
        }

        if (extras.containsKey(EXTRA_ELAPSED_DURATION) && extras.containsKey(EXTRA_TOTAL_DURATION))
        {
            long elapsed = extras.getLong(EXTRA_ELAPSED_DURATION,0);
            long total = extras.getLong(EXTRA_TOTAL_DURATION,0);
            onDurationChanged((int) elapsed, (int) total);
        }
    }

    public abstract void onQueueChanged(List<Song> queue);
    public abstract void onQueueIndexChanged(int index, Song song);
    public abstract void onStateChanged(int state);
    public abstract void onDurationChanged(int elapsed, int total);
}
