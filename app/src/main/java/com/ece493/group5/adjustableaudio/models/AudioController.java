package com.ece493.group5.adjustableaudio.models;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AudioController
        implements AudioDevice
{
    Context context;
    List<AudioDevice> devices;

    public AudioController(Context c)
    {
        context = c;
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

    @Override
    public void enableEqualizer()
    {
        for (AudioDevice device: devices)
            device.enableEqualizer();
    }

    @Override
    public void disableEqualizer()
    {
        for (AudioDevice device: devices)
            device.disableEqualizer();
    }

    public void setGlobalVolume(double percent)
    {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume*percent), 0);
    }
}
