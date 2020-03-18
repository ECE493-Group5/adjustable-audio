package com.ece493.group5.adjustableaudio.models;

import java.util.ArrayList;
import java.util.List;

public class AudioController
        implements AudioDevice
{
    List<AudioDevice> devices;

    public AudioController()
    {
        devices = new ArrayList<>();
    }

    public void registerDevice(AudioDevice device)
    {
        devices.add(device);
    }

    public void unregisterDevice(AudioDevice device)
    {
        devices.remove(device);
    }

    @Override
    public void setLeftVolume(double percent)
    {
        for (AudioDevice device: devices)
            device.setLeftVolume(percent);
    }

    @Override
    public void setRightVolume(double percent)
    {
        for (AudioDevice device: devices)
            device.setRightVolume(percent);
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        for (AudioDevice device: devices)
            device.setEqualizerBand(band, level);
    }
}
