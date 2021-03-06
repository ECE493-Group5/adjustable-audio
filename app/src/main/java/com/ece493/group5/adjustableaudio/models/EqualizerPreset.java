package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

/**
 * The EqualizerPreset class helps implement the following requirements:
 *
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes through an Equalizer
 *
 * In particular, the Equalizer preset class implements the presets. A preset is an audio setting
 * containing millibel levels for the equalizer and a ratio for the left/right audio balance.
 */

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

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }

        if (this.getClass() != other.getClass())
        {
            return false;
        }

        if (!this.equalizerSettings.equals(((EqualizerPreset) other).equalizerSettings))
        {
            return false;
        }

        if (!this.leftRightVolumeRatio.equals(((EqualizerPreset) other).leftRightVolumeRatio))
        {
            return false;
        }

        if (!this.equalizerName.equals(((EqualizerPreset) other).equalizerName))
        {
            return false;
        }

        return true;
    }

}
