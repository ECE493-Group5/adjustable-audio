package com.ece493.group5.adjustableaudio.controllers;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
        if (!devices.contains(device))
            devices.add(device);
    }

    public void unregisterDevice(IAudioDevice device)
    {
        if (devices.contains(device))
            devices.remove(device);
    }

    @Override
    public void setLeftRightVolumeRatio(double ratio)
    {
        for (IAudioDevice device: devices)
            device.setLeftRightVolumeRatio(ratio);
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

    public void restartEqualizer()
    {
        disableEqualizer();
        enableEqualizer();
    }

    public void setGlobalVolume(int value)
    {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
    }
}
