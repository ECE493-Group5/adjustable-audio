package com.ece493.group5.adjustableaudio.models;

public interface AudioDevice
{
    double getLeftVolume();
    double getRightVolume();

    void setLeftVolume(double percent);
    void setRightVolume(double percent);
}
