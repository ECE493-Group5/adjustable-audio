package com.ece493.group5.adjustableaudio.models;

import java.util.HashMap;

/**
 * The ApplicationEqualizerPresets class helps implement the following requirements:
 *
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes through an Equalizer
 *
 * In particular, the ApplicationEqualizerPresets creates the "default" preset for the application.
 */

class ApplicationEqualizerPresets
{
    private static final String DEFAULT_NAME = "Default";

    private static final double defaultLeftRightVolumeRatioSetting = 1;

    private static final Integer firstFrequencyBand = 0;
    private static final Integer secondFrequencyBand = 1;
    private static final Integer thirdFrequencyBand = 2;
    private static final Integer fourthFrequencyBand = 3;
    private static final Integer fifthFrequencyBand = 4;

    private static final Integer lowestMillibelLevel = 0;
    private static final Integer normalMillibelLevel = 300;

    static EqualizerPreset getDefaultPreset()
    {
        HashMap<Integer, Integer> defaultEqualizerValues  = new HashMap<>();
        defaultEqualizerValues.put(firstFrequencyBand, normalMillibelLevel);
        defaultEqualizerValues.put(secondFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(thirdFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(fourthFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(fifthFrequencyBand, normalMillibelLevel);
        return new EqualizerPreset(defaultEqualizerValues,
                defaultLeftRightVolumeRatioSetting, DEFAULT_NAME);
    }
}
