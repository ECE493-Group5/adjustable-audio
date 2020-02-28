package com.ece493.group5.adjustableaudio.ui.media_player;

import android.media.session.PlaybackState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MediaPlayerViewModel extends ViewModel {

    private MutableLiveData<Integer> playbackState;

    public MediaPlayerViewModel() {
        playbackState = new MutableLiveData<>();
        playbackState.setValue(PlaybackState.STATE_PAUSED);
    }

    public void setPlaybackState(int playbackState) {
        this.playbackState.setValue(playbackState);
    }

    public LiveData<Integer> getPlaybackState() {
        return playbackState;
    }
}