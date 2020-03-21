package com.ece493.group5.adjustableaudio.interfaces;

public interface IAudioDevice
{
    void setLeftVolume(double percent);
    void setRightVolume(double percent);
    void setEqualizerBand(short band, short level);
    void enableEqualizer();
    void disableEqualizer();
}
