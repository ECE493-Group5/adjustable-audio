package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

public class EqualizerPreset
{
    private HashMap<Integer, Integer> equalizerSettings;
    private int leftVolume;
    private int rightVolume;
    private String equalizerName;

    public EqualizerPreset(HashMap<Integer, Integer> equalizerSliders,
                           int leftVolumeSet, int rightVolumeSet)
    {
        this.equalizerSettings = equalizerSliders;
        this.leftVolume = leftVolumeSet;
        this.rightVolume = rightVolumeSet;
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

    public int[] getLeftAndRightVolume()
    {
        int[] volumes = {this.leftVolume, this.rightVolume};
        return volumes;
    }

    public void setEqualizerName(String newName)
    {
        this.equalizerName = newName;
    }

    public String getEqualizerName()
    {
        return this.equalizerName;
    }

    public void toJSON()
    {

    }

}
