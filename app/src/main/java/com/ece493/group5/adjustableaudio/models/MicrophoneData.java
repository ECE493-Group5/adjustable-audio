package com.ece493.group5.adjustableaudio.models;

public class MicrophoneData extends ChangableData<MicrophoneData.Type>
{
    public enum Type
    {
        IS_RECORDING, IS_NOISE_FILTER_ENABLED
    }

    private boolean isRecording;
    private boolean isNoiseFilterEnabled;

    public MicrophoneData()
    {
        super(Type.class);

        isRecording = false;
        isNoiseFilterEnabled = false;
    }

    public boolean getIsRecording()
    {
        return isRecording;
    }

    public void setIsRecording(boolean isEnabled)
    {
        if (this.isRecording != isEnabled) {
            this.isRecording = isEnabled;
            setChanged(Type.IS_RECORDING, true);
        }
    }

    public boolean isRecordingChanged()
    {
        return getChanged(Type.IS_RECORDING);
    }

    public boolean getIsNoiseFilterEnabled()
    {
        return isNoiseFilterEnabled;
    }

    public void setIsNoiseFilterEnabled(boolean isEnabled)
    {
        if (this.isNoiseFilterEnabled != isEnabled) {
            this.isNoiseFilterEnabled = isEnabled;
            setChanged(Type.IS_NOISE_FILTER_ENABLED, true);
        }
    }

    public boolean isNoiseFilterEnabledChanged()
    {
        return getChanged(Type.IS_NOISE_FILTER_ENABLED);
    }
}
