package com.ece493.group5.adjustableaudio.models;

public interface AudioDevice
{
    void setLeftVolume(double percent);
    void setRightVolume(double percent);
    void setEqualizerBand(short band, short level);
    void enableEqualizer();
    void disableEqualizer();
}
