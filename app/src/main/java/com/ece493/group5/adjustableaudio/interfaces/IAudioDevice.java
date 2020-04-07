package com.ece493.group5.adjustableaudio.interfaces;

/**
 * The IAudioDevice interface helps implement the following requirements:
 *
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes through an Equalizer
 */

public interface IAudioDevice
{
    void setLeftRightVolumeRatio(double ratio);
    void setEqualizerBand(short band, short level);
    void enableEqualizer();
    void disableEqualizer();
}
