package com.ece493.group5.adjustableaudio.ui.media_player;

import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ece493.group5.adjustableaudio.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MediaPlayerViewModel extends ViewModel
{
    private MutableLiveData<PlaybackState> state;
    private MutableLiveData<Integer> currentlySelected;
    private MutableLiveData<List<MediaSession.QueueItem>> queue;

    public MediaPlayerViewModel()
    {
        state = new MutableLiveData<>();
        currentlySelected = new MutableLiveData<>();
        queue = new MutableLiveData<>();
    }

    public void setState(PlaybackState playbackState)
    {
        this.state.setValue(playbackState);
    }

    public LiveData<PlaybackState> getState()
    {
        return state;
    }

    public void setCurrentlySelected(Integer position)
    {
        if (!position.equals(currentlySelected.getValue()))
            currentlySelected.setValue(position);
    }

    public LiveData<Integer> getCurrentlySelected()
    {
        return currentlySelected;
    }

    public void setQueue(List<MediaSession.QueueItem> queue)
    {
        this.queue.setValue(queue);
    }

    public LiveData<List<MediaSession.QueueItem>> getQueue()
    {
        return queue;
    }

    public Song getCurrentSong()
    {
        return getSong(currentlySelected.getValue());
    }

    public Song getSong(int position)
    {
        return Song.fromQueueItem(queue.getValue().get(position));
    }

    public void selectNext()
    {
        if (currentlySelected.getValue() < queue.getValue().size())
            currentlySelected.setValue(currentlySelected.getValue() + 1);
    }

    public void selectPrevious()
    {
        if (!queue.getValue().isEmpty() && currentlySelected.getValue() > 0)
            currentlySelected.setValue(currentlySelected.getValue() - 1);
    }

    public void enqueue(Song song)
    {
        queue.getValue().add(song.toQueueItem());
        queue.setValue(queue.getValue());

        if (queue.getValue().size() == 1)
            currentlySelected.setValue(0);
    }

    public void dequeue(int index)
    {
        if (queue.getValue().size() == 1)
            currentlySelected.setValue(null);
        else if (currentlySelected.getValue() == index)
            currentlySelected.setValue(index - 1);

        queue.getValue().remove(index);
        queue.setValue(queue.getValue());
    }
}