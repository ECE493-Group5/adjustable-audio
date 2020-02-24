package com.ece493.group5.adjustableaudio.ui.hearing_test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HearingTestViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HearingTestViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is hearing test fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}