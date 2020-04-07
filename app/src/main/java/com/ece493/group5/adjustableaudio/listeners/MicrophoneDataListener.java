package com.ece493.group5.adjustableaudio.listeners;

import com.ece493.group5.adjustableaudio.models.MicrophoneData;

import java.util.Observable;
import java.util.Observer;

/**
 * THe MicrophoneDataListener class helps implement the following requirement:
 *
 * #SRS: Makeshift Hearing Aid
 *
 * In particular, the MicrophoneDataListener class provides methods for when the MicrophoneData
 * object has updated or changed.
 */

public abstract class MicrophoneDataListener
    implements Observer
{

    @Override
    public void update(Observable o, Object arg)
    {
        MicrophoneData data = (MicrophoneData) o;

        if (data.isRecordingChanged())
            onIsRecordingChanged(data.getIsRecording());

        if (data.modeChanged())
            onModeChanged(data.getMode());
    }

    public abstract void onIsRecordingChanged(boolean isRecording);
    public abstract void onModeChanged(int mode);
}
