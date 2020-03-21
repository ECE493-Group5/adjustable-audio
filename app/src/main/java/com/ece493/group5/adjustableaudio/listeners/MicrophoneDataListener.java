package com.ece493.group5.adjustableaudio.listeners;

import com.ece493.group5.adjustableaudio.models.MicrophoneData;

import java.util.Observable;
import java.util.Observer;

public abstract class MicrophoneDataListener
    implements Observer
{

    @Override
    public void update(Observable o, Object arg)
    {
        MicrophoneData data = (MicrophoneData) o;

        if (data.isRecordingChanged())
            onIsRecordingChanged(data.getIsRecording());

        if (data.isNoiseFilterEnabledChanged())
            onIsNoiseFilterEnabledChanged(data.getIsNoiseFilterEnabled());
    }

    public abstract void onIsRecordingChanged(boolean isRecording);
    public abstract void onIsNoiseFilterEnabledChanged(boolean isEnabled);
}
