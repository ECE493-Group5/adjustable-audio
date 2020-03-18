package com.ece493.group5.adjustableaudio.listeners;

import android.os.Bundle;

import com.ece493.group5.adjustableaudio.models.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;

import java.util.List;

public abstract class MediaDataListener
{
    public void handleChanges(Bundle extras)
    {
        handleChanges(MediaData.extract(extras));
    }

    public void handleChanges(MediaData mediaData)
    {
        if (mediaData.queueChanged())
            onQueueChanged(mediaData.getQueue());

        if (mediaData.queueIndexChanged())
            onQueueIndexChanged(mediaData.getQueueIndex(), mediaData.getCurrentSong());

        if (mediaData.stateChanged())
            onStateChanged(mediaData.getState());

        if (mediaData.durationChanged())
            onDurationChanged((int) mediaData.getElapsedDuration(), (int) mediaData.getTotalDuration());
    }

    public void onQueueChanged(List<Song> queue) {}
    public void onQueueIndexChanged(int index, Song song) {}
    public void onStateChanged(int state) {}
    public void onDurationChanged(int elapsed, int total) {}
}
