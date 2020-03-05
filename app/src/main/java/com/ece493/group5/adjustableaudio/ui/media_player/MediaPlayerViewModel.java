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
    private MutableLiveData<ArrayList<Song>> queue;

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

    public void setCurrentlySelected(int position)
    {
        if (currentlySelected.getValue() == null || !currentlySelected.equals(position))
            currentlySelected.setValue(position);
    }

    public LiveData<Integer> getCurrentlySelected()
    {
        return currentlySelected;
    }

    public void setQueue(ArrayList<Song> queue)
    {
        this.queue.setValue(queue);
    }

    public LiveData<ArrayList<Song>> getQueue()
    {
        return queue;
    }

    public Song getCurrentSong()
    {
        return getSong(currentlySelected.getValue());
    }

    public Song getSong(int position)
    {
        if (position < 0 || position >= queue.getValue().size())
            return null;

        return queue.getValue().get(position);
    }

    public void selectNext()
    {
        if (!queue.getValue().isEmpty() && currentlySelected.getValue() < queue.getValue().size())
            currentlySelected.setValue(currentlySelected.getValue() + 1);
    }

    public void selectPrevious()
    {
        if (!queue.getValue().isEmpty() && currentlySelected.getValue() > 0)
            currentlySelected.setValue(currentlySelected.getValue() - 1);
    }

    public void dequeue(int index)
    {
        if (currentlySelected.getValue() == index)
            currentlySelected.setValue(index - 1);

        queue.getValue().remove(index);
        queue.setValue(queue.getValue());
    }
}