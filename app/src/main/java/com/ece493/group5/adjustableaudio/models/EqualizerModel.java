package com.ece493.group5.adjustableaudio.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EqualizerModel
{
    List<EqualizerPreset> equalizerPresets;

    public EqualizerModel()
    {
        //TODO: Should change to loading from the file system
        equalizerPresets = new ArrayList<>();
    }


    public void saveEqualizerSetting(EqualizerPreset equalizerPreset)
    {
        equalizerPresets.add(equalizerPreset);

        //TODO: Encrypt Equalizer Setting
        //TODO: Save Equalizer Setting
    }


    public void deleteEqualizerSetting(String equalizerSettingToBeDeleted)
    {
        for (EqualizerPreset preset : equalizerPresets)
        {
            if (preset.getEqualizerName() == equalizerSettingToBeDeleted)
            {
                equalizerPresets.remove(preset);
                break;
            }
        }
    }


    public void renameEqualizerSetting(String newName, String oldName)
    {

    }


    public void switchEqualizerSetting()
    {

    }


    public void setVolume(Float leftVolume, Float rightVolume)
    {

    }


    public void setFrequencyBand(Integer band, Float decibelLevel)
    {

    }


}
