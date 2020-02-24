package com.ece493.group5.adjustableaudio.ui.microphone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MicrophoneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MicrophoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is microphone fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}