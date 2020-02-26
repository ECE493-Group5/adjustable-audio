package com.ece493.group5.adjustableaudio.ui.media_player;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MediaPlayerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MediaPlayerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Media Player");
    }

    public LiveData<String> getText() {
        return mText;
    }
}