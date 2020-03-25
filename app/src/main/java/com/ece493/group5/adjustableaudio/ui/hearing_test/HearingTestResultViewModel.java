package com.ece493.group5.adjustableaudio.ui.hearing_test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HearingTestResultViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HearingTestResultViewModel()
    {

    }

    public LiveData<String> getText()
    {
        return mText;
    }
}