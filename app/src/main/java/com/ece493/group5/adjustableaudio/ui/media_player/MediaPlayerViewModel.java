package com.ece493.group5.adjustableaudio.ui.media_player;

import android.media.MediaMetadata;
import android.media.session.PlaybackState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MediaPlayerViewModel extends ViewModel {

    private MutableLiveData<PlaybackState> state;
    private MutableLiveData<MediaMetadata> metadata;

    public MediaPlayerViewModel() {
        state = new MutableLiveData<>();
        metadata = new MutableLiveData<>();
    }

    public void setState(PlaybackState playbackState) {
        this.state.setValue(playbackState);
    }

    public LiveData<PlaybackState> getState() {
        return state;
    }

    public void setMetadata(MediaMetadata metadata) {
        this.metadata.setValue(metadata);
    }

    public LiveData<MediaMetadata> getMetadata() {
        return metadata;
    }
}