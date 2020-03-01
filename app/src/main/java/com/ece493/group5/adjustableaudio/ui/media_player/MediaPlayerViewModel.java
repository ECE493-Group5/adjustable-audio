package com.ece493.group5.adjustableaudio.ui.media_player;

import android.media.MediaMetadata;
import android.media.session.PlaybackState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MediaPlayerViewModel extends ViewModel
{
    private MutableLiveData<PlaybackState> state;
    private MutableLiveData<MediaMetadata> metadata;
    private MutableLiveData<List<MediaMetadata>> queue;

    public MediaPlayerViewModel()
    {
        state = new MutableLiveData<>();
        metadata = new MutableLiveData<>();
        queue = new MutableLiveData<>();

        queue.setValue(new ArrayList<MediaMetadata>());
    }

    public void setState(PlaybackState playbackState)
    {
        this.state.setValue(playbackState);
    }

    public LiveData<PlaybackState> getState()
    {
        return state;
    }

    public void setMetadata(MediaMetadata metadata)
    {
        this.metadata.setValue(metadata);
    }

    public LiveData<MediaMetadata> getMetadata()
    {
        return metadata;
    }

    public LiveData<List<MediaMetadata>> getQueue()
    {
        return queue;
    }

    public void enqueue(MediaMetadata metadata)
    {
        if (queue.getValue().isEmpty())
            setMetadata(metadata);

        queue.getValue().add(metadata);
        queue.notifyAll();
    }

    public void dequeue(int index)
    {
        queue.getValue().remove(index);
        queue.notifyAll();

        if (index == 0 && !queue.getValue().isEmpty())
            setMetadata(queue.getValue().get(0));
    }
}