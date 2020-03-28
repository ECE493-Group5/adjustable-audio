package com.ece493.group5.adjustableaudio.interfaces;

public interface IAudioDevice
{
    void setLeftRightVolumeRatio(double ratio);
    void setEqualizerBand(short band, short level);
    void enableEqualizer();
    void disableEqualizer();
}
