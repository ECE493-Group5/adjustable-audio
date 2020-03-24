package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

public class ApplicationEqualizerPresets
{
    private static final String DEFAULT_NAME = "Default";

    private static final int defaultLeftVolumeSetting = 50;
    private static final int defaultRightVolumeSetting = 50;

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
                defaultLeftVolumeSetting, defaultRightVolumeSetting, DEFAULT_NAME);
        return defaultEqualizerPreset;
    }
}
