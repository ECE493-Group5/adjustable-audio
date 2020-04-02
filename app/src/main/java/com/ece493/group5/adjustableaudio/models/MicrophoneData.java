package com.ece493.group5.adjustableaudio.models;

public class MicrophoneData extends ChangableData<MicrophoneData.Type>
{
    public static final int MODE_NORMAL = 1;
    public static final int MODE_NOISE_SUPPRESSION = 2;
    public static final int MODE_SPEECH_FOCUS = 3;

    public enum Type
    {
        IS_RECORDING, MODE
    }

    private boolean isRecording;
    private int mode;

    public MicrophoneData()
    {
        super(Type.class);

        isRecording = false;
        mode = MODE_NORMAL;
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

    public int getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        if (this.mode != mode) {
            this.mode = mode;
            setChanged(Type.MODE, true);
        }
    }

    public boolean modeChanged()
    {
        return getChanged(Type.MODE);
    }
}
