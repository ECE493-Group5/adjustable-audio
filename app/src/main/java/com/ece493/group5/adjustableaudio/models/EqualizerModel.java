package com.ece493.group5.adjustableaudio.models;

import android.util.Log;

import com.ece493.group5.adjustableaudio.ui.settings.SettingsFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EqualizerModel
{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    List<EqualizerPreset> equalizerPresets;

    public EqualizerModel()
    {
        equalizerPresets = new ArrayList<>();
        loadPresets();
    }

    private void loadPresets()
    {
        //TODO: Should change to loading from the file system

        //Default Preset
        HashMap<Integer, Integer> defaultEqualizerValues = new HashMap<>();
        defaultEqualizerValues.put(0, 300);
        defaultEqualizerValues.put(1, 0);
        defaultEqualizerValues.put(2, 0);
        defaultEqualizerValues.put(3, 0);
        defaultEqualizerValues.put(4, 300);
        EqualizerPreset defaultPreset = new EqualizerPreset(defaultEqualizerValues, 1, 1, 1);
        defaultPreset.setEqualizerName("Default");
        equalizerPresets.add(defaultPreset);

        //Restaurant Preset
        //TODO: Determine actual restaurant preset values
        HashMap<Integer, Integer> restaurantEqualizerValues = new HashMap<>();
        restaurantEqualizerValues.put(0, -100);
        restaurantEqualizerValues.put(1, -50);
        restaurantEqualizerValues.put(2, 0);
        restaurantEqualizerValues.put(3, 50);
        restaurantEqualizerValues.put(4, 100);
        EqualizerPreset restaurantPreset = new EqualizerPreset(restaurantEqualizerValues, 1, 1, 1);
        restaurantPreset.setEqualizerName("Restaurant");
        equalizerPresets.add(restaurantPreset);

        //MonoPreset
        //TODO: Confirm actual mono preset values
        HashMap<Integer, Integer> monoEqualizerValues = new HashMap<>();
        monoEqualizerValues.put(0, 0);
        monoEqualizerValues.put(1, 0);
        monoEqualizerValues.put(2, 0);
        monoEqualizerValues.put(3, 0);
        monoEqualizerValues.put(4, 0);
        EqualizerPreset monoPreset = new EqualizerPreset(monoEqualizerValues, 1, 1, 1);
        monoPreset.setEqualizerName("Mono");
        equalizerPresets.add(monoPreset);
    }

    public List<String> getEqualizerPresetNames()
    {
        List<String> presetNames = new ArrayList<>();
        for (EqualizerPreset equalizerPreset : equalizerPresets)
        {
            presetNames.add(equalizerPreset.getEqualizerName());
        }
        return presetNames;
    }

    public EqualizerPreset getEqualizerPreset(int position)
    {
        return equalizerPresets.get(position);
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
