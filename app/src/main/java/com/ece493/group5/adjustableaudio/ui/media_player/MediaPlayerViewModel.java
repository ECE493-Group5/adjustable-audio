package com.ece493.group5.adjustableaudio.ui.media_player;

import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ece493.group5.adjustableaudio.models.Song;

import java.util.ArrayList;

public class MediaPlayerViewModel extends ViewModel
{
    private MutableLiveData<Bundle> extras;

    public MediaPlayerViewModel()
    {
        extras = new MutableLiveData<>();
    }

    public void setExtras(Bundle extras)
    {
        this.extras.setValue(extras);
    }

    public LiveData<Bundle> getExtras()
    {
        return extras;
    }
}