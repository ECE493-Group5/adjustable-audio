package com.ece493.group5.adjustableaudio.models;

public class MicrophoneData extends ChangableData<MicrophoneData.Type>
{
    public enum Type
    {
        IS_ENABLED
    }

    private boolean isEnabled;

    public MicrophoneData()
    {
        super(Type.class);

        isEnabled = false;
    }

    public boolean getIsRecording()
    {
        return isEnabled;
    }

    public void setIsRecording(boolean isEnabled)
    {
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;
            setChanged(Type.IS_ENABLED, true);
        }
    }

    public boolean isRecordingChanged()
    {
        return getChanged(Type.IS_ENABLED);
    }
}
