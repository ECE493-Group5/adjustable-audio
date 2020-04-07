package com.ece493.group5.adjustableaudio.ui.media_player;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The MediaPlayerViewModel helps develop the following requirement:
 *
 * #SRS: Media Player
 *
 * In particular, the MediaPlayerViewModel helps implement the media queue.
 */

import com.ece493.group5.adjustableaudio.models.MediaData;

public class MediaPlayerViewModel extends ViewModel
{
    private MutableLiveData<MediaData> mediaData;

    public MediaPlayerViewModel()
    {
        mediaData = new MutableLiveData<>();
    }

    public void setMediaData(MediaData extras)
    {
        this.mediaData.setValue(extras);
    }

    public LiveData<MediaData> getMediaData()
    {
        return mediaData;
    }
}