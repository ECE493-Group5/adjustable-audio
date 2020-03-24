package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

public class EqualizerPreset
{
    private HashMap<Integer, Integer> equalizerSettings;
    private int leftVolume;
    private int rightVolume;
    private String equalizerName;

    public EqualizerPreset()
    {

    }

    public EqualizerPreset(HashMap<Integer, Integer> equalizerSliders,
                           int leftVolumeSet, int rightVolumeSet, String equalizerNameSet)
    {
        this.equalizerSettings = equalizerSliders;
        this.leftVolume = leftVolumeSet;
        this.rightVolume = rightVolumeSet;
        this.equalizerName = equalizerNameSet;
    }

    public EqualizerPreset(EqualizerPreset original)
    {
        this.setEqualizerSettings(original.equalizerSettings);
        this.leftVolume = original.leftVolume;
        this.rightVolume = original.rightVolume;
        this.equalizerName = original.equalizerName;
    }

    public void setEqualizerSettings(HashMap<Integer, Integer> newEqualizerSettings)
    {
        if (newEqualizerSettings == null)
        {
            return;
        }

        this.equalizerSettings = new HashMap<>();

        for (Integer key : newEqualizerSettings.keySet())
        {
            this.equalizerSettings.put(key, newEqualizerSettings.get(key));
        }
    }

    public HashMap<Integer, Integer> getEqualizerSettings()
    {
        return this.equalizerSettings;
    }

    public void setLeftVolume(int newLeftVolume)
    {
        this.leftVolume = newLeftVolume;
    }

    public int getLeftVolume()
    {
        return this.leftVolume;
    }

    public void setRightVolume(int newRightVolume)
    {
        this.rightVolume = newRightVolume;
    }

    public int getRightVolume()
    {
        return this.rightVolume;
    }

    public void setEqualizerName(String newName)
    {
        this.equalizerName = newName;
    }

    public String getEqualizerName()
    {
        return this.equalizerName;
    }

    public void setFrequencyBand(int band, int millibelLevel)
    {
        equalizerSettings.put(band, millibelLevel);
    }

}
