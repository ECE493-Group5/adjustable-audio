package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

public class EqualizerPreset
{
    private HashMap<Integer, Integer> equalizerSettings;
    private Double leftRightVolumeRatio;
    private String equalizerName;

    public EqualizerPreset()
    {
        leftRightVolumeRatio = 1.0;
    }

    public EqualizerPreset(HashMap<Integer, Integer> equalizerSliders,
                           double ratio, String equalizerNameSet)
    {
        this.equalizerSettings = equalizerSliders;
        this.leftRightVolumeRatio = ratio;
        this.equalizerName = equalizerNameSet;
    }

    public EqualizerPreset(EqualizerPreset original)
    {
        this.setEqualizerSettings(original.equalizerSettings);
        this.leftRightVolumeRatio = original.leftRightVolumeRatio;
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

    public void setLeftRightVolumeRatio(double ratio)
    {
        this.leftRightVolumeRatio = ratio;
    }

    public double getLeftRightVolumeRatio()
    {
        return this.leftRightVolumeRatio;
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
