package com.ece493.group5.adjustableaudio.storage;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import java.util.HashMap;

public class ApplicationEqualizerPresets
{
    private static final String DEFAULT_NAME = "Default";
    private static final String NONE_NAME = "None";

    private static final int defaultLeftVolumeSetting = 0;
    private static final int defaultRightVolumeSetting = 0;

    private static final Integer firstFrequencyBand = 0;
    private static final Integer secondFrequencyBand = 1;
    private static final Integer thirdFrequencyBand = 2;
    private static final Integer fourthFrequencyBand = 3;
    private static final Integer fifthFrequencyBand = 4;

    private static final Integer lowestMillibelLevel = 0;
    private static final Integer normalMillibelLevel = 300;

    public static EqualizerPreset getDefaultPreset()
    {
        HashMap<Integer, Integer> defaultEqualizerValues  = new HashMap<>();
        defaultEqualizerValues.put(firstFrequencyBand, normalMillibelLevel);
        defaultEqualizerValues.put(secondFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(thirdFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(fourthFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(fifthFrequencyBand, normalMillibelLevel);
        EqualizerPreset defaultEqualizerPreset = new EqualizerPreset(defaultEqualizerValues,
                defaultLeftVolumeSetting, defaultRightVolumeSetting);
        defaultEqualizerPreset.setEqualizerName(DEFAULT_NAME);
        return defaultEqualizerPreset;
    }

    public static EqualizerPreset getNonePreset()
    {
        HashMap<Integer, Integer> noneEqualizerValues = null;
        EqualizerPreset noneEqualizerPreset = new EqualizerPreset(noneEqualizerValues,
                defaultLeftVolumeSetting, defaultRightVolumeSetting);
        noneEqualizerPreset.setEqualizerName(NONE_NAME);
        return noneEqualizerPreset;
    }
}
