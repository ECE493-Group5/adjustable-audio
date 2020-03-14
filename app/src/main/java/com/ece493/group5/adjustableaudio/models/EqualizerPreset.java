package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

public class EqualizerPreset
{
    private HashMap<Integer, Integer> equalizerSettings;
    private Float leftVolume;
    private Float rightVolume;
    private String equalizerName;

    public EqualizerPreset(Float leftVolumeSet, Float rightVolumeSet)
    {
        this.equalizerSettings = new HashMap<>();
        this.leftVolume = leftVolumeSet;
        this.rightVolume = rightVolumeSet;
    }


    public HashMap<Integer, Integer> getEqualizerSettings()
    {
        return this.equalizerSettings;
    }


    public void setLeftVolume(Float newLeftVolume)
    {
        this.leftVolume = newLeftVolume;
    }


    public Float getLeftVolume()
    {
        return this.leftVolume;
    }


    public void setRightVolume(Float newRightVolume)
    {
        this.rightVolume = newRightVolume;
    }


    public Float getRightVolume()
    {
        return this.rightVolume;
    }


    public Float[] getLeftAndRightVolume()
    {
        Float[] volumes = {this.leftVolume, this.rightVolume};
        return volumes;
    }


    public String getEqualizerName()
    {
        return this.equalizerName;
    }


    public void toJSON()
    {

    }

}
