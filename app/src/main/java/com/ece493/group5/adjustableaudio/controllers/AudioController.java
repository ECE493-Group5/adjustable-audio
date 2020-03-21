package com.ece493.group5.adjustableaudio.controllers;

import android.content.Context;
import android.media.AudioManager;

import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;

import java.util.ArrayList;
import java.util.List;

public class AudioController
        implements IAudioDevice
{
    Context context;
    List<IAudioDevice> devices;

    public AudioController(Context c)
    {
        context = c;
        devices = new ArrayList<>();
    }

    public void registerDevice(IAudioDevice device)
    {
        devices.add(device);
    }

    public void unregisterDevice(IAudioDevice device)
    {
        devices.remove(device);
    }

    @Override
    public void setLeftVolume(double percent)
    {
        for (IAudioDevice device: devices)
            device.setLeftVolume(percent);
    }

    @Override
    public void setRightVolume(double percent)
    {
        for (IAudioDevice device: devices)
            device.setRightVolume(percent);
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        for (IAudioDevice device: devices)
            device.setEqualizerBand(band, level);
    }

    @Override
    public void enableEqualizer()
    {
        for (IAudioDevice device: devices)
            device.enableEqualizer();
    }

    @Override
    public void disableEqualizer()
    {
        for (IAudioDevice device: devices)
            device.disableEqualizer();
    }

    public void setGlobalVolume(double percent)
    {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume*percent), 0);
    }
}
