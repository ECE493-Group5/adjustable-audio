package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

public class EqualizerPreset
{
    private HashMap<Integer, Integer> equalizerSettings;
    private Float leftVolume;
    private Float rightVolume;
    private Float globalVolume;
    private String equalizerName;

    public EqualizerPreset(HashMap<Integer, Integer> equalizerSliders,
                           Float leftVolumeSet, Float rightVolumeSet, Float globalVolumeSet)
    {
        this.equalizerSettings = equalizerSliders;
        this.leftVolume = leftVolumeSet;
        this.rightVolume = rightVolumeSet;
        this.globalVolume = globalVolumeSet;
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

    public void setGlobalVolume(Float newGlobalVolume)
    {
        this.globalVolume = newGlobalVolume;
    }

    public Float getGlobalVolume()
    {
        return this.globalVolume;
    }

    public Float[] getLeftAndRightVolume()
    {
        Float[] volumes = {this.leftVolume, this.rightVolume};
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
