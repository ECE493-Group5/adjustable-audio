package com.ece493.group5.adjustableaudio.ui.media_player;

import android.media.session.PlaybackState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ece493.group5.adjustableaudio.models.Song;

import java.util.ArrayList;

public class MediaPlayerViewModel extends ViewModel
{
    private MutableLiveData<PlaybackState> state;

    public MediaPlayerViewModel()
    {
        state = new MutableLiveData<>();
    }

    public void setState(PlaybackState playbackState)
    {
        this.state.setValue(playbackState);
    }

    public LiveData<PlaybackState> getState()
    {
        return state;
    }
}