package com.ece493.group5.adjustableaudio.models;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EqualizerModel
{
    private static final String TAG = EqualizerModel.class.getSimpleName();

    List<EqualizerPreset> equalizerPresets;
    int currentEqualizerSettingPosition;
    HashMap<Integer, Integer> currentEqualizerBandValues;
    int currentLeftVolume;
    int currentRightVolume;
    int currentGlobalVolume;

    public EqualizerModel()
    {
        equalizerPresets = new ArrayList<>();
        loadPresets();
        setInitialEqualizerState();
    }

    private void loadPresets()
    {
        //TODO: Should change to loading from the file system
        // SAVE equalizer's millidecibel value --> not sure about the volume settings yet
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

    private void setInitialEqualizerState()
    {
        currentEqualizerSettingPosition = 0;
        currentEqualizerBandValues = equalizerPresets.get(currentEqualizerSettingPosition)
                .getEqualizerSettings();
        currentLeftVolume = equalizerPresets.get(currentEqualizerSettingPosition).getLeftVolume();
        currentRightVolume = equalizerPresets.get(currentEqualizerSettingPosition).getRightVolume();
        currentGlobalVolume = equalizerPresets.get(currentEqualizerSettingPosition)
                .getGlobalVolume();
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

    public void addEqualizerSetting(String equalizerPresetName)
    {
        EqualizerPreset newEqualizerPreset = new EqualizerPreset(currentEqualizerBandValues,
                currentLeftVolume, currentRightVolume, currentGlobalVolume);
        newEqualizerPreset.setEqualizerName(equalizerPresetName);
        equalizerPresets.add(newEqualizerPreset);

        //TODO: Encrypt Equalizer Setting
        //TODO: Save Equalizer Setting
    }


    public void deleteEqualizerSetting(int equalizerSettingToBeDeleted)
    {
       equalizerPresets.remove(equalizerSettingToBeDeleted);
    }


    public void renameEqualizerSetting(int equalizerSettingToBeRenamed, String newName)
    {
        equalizerPresets.get(equalizerSettingToBeRenamed).setEqualizerName(newName);
    }


    public void switchEqualizerSetting(int newEqualizerSettingPosition)
    {
        currentEqualizerSettingPosition = newEqualizerSettingPosition;
        currentEqualizerBandValues = equalizerPresets.get(currentEqualizerSettingPosition)
                .getEqualizerSettings();
        currentLeftVolume = equalizerPresets.get(currentEqualizerSettingPosition).getLeftVolume();
        currentRightVolume = equalizerPresets.get(currentEqualizerSettingPosition).getRightVolume();
        currentGlobalVolume = equalizerPresets.get(currentEqualizerSettingPosition)
                .getGlobalVolume();
    }

    public void setCurrentLeftVolume(int leftVolume)
    {
        currentLeftVolume = leftVolume;
    }

    public void setCurrentRightVolume(int rightVolume)
    {
        currentRightVolume = rightVolume;
    }

    public void setCurrentGlobalVolume(int globalVolume)
    {
        currentGlobalVolume = globalVolume;
    }

    public void setFrequencyBand(int band, int millibelLevel)
    {
        currentEqualizerBandValues.put(band, millibelLevel);
    }
}
