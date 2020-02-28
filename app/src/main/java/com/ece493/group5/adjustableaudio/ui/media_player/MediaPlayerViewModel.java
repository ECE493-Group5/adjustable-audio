package com.ece493.group5.adjustableaudio.ui.media_player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MediaPlayerViewModel extends ViewModel {

    private MutableLiveData<Boolean> songPlaying;

    public MediaPlayerViewModel() {
        songPlaying = new MutableLiveData<>();
        songPlaying.setValue(false);
    }

    public void toggleIsSongPlaying() {
        songPlaying.setValue(!songPlaying.getValue());
    }

    public LiveData<Boolean> isSongPlaying() {
        return songPlaying;
    }
}