package com.ece493.group5.adjustableaudio.models;


import android.util.Log;

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
    String currentEqualizerName;

    public EqualizerModel()
    {
        equalizerPresets = new ArrayList<>();
        loadPresets();
        setInitialEqualizerState();
    }

    public int getCurrentEqualizerSettingPosition()
    {
        return currentEqualizerSettingPosition;
    }

    public HashMap<Integer, Integer> getCurrentEqualizerBandValues()
    {
        return currentEqualizerBandValues;
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
        EqualizerPreset defaultPreset = new EqualizerPreset(defaultEqualizerValues, 50, 50, 50);
        defaultPreset.setEqualizerName("Default");
        equalizerPresets.add(defaultPreset);
    }

    private void setInitialEqualizerState()
    {
        currentEqualizerSettingPosition = 0;
        currentLeftVolume = equalizerPresets.get(currentEqualizerSettingPosition).getLeftVolume();
        currentRightVolume = equalizerPresets.get(currentEqualizerSettingPosition).getRightVolume();
        currentGlobalVolume = equalizerPresets.get(currentEqualizerSettingPosition)
                .getGlobalVolume();
        currentEqualizerName = equalizerPresets.get(currentEqualizerSettingPosition).getEqualizerName();

        setEqualizerBandValues();
    }

    private void setEqualizerBandValues()
    {
        currentEqualizerBandValues = new HashMap<>();
        HashMap <Integer, Integer> presetValues = equalizerPresets.get(currentEqualizerSettingPosition)
                .getEqualizerSettings();

        for (Integer key : presetValues.keySet())
        {
            currentEqualizerBandValues.put(key, presetValues.get(key));
        }
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

    public void updateEqualizerPreset()
    {
        EqualizerPreset updatedEqualizerPreset = new EqualizerPreset(currentEqualizerBandValues,
                currentLeftVolume, currentRightVolume, currentGlobalVolume);
        updatedEqualizerPreset.setEqualizerName(currentEqualizerName);
        equalizerPresets.set(currentEqualizerSettingPosition, updatedEqualizerPreset);
    }

    public void revertEqualizerChanges()
    {
        switchEqualizerSetting(currentEqualizerSettingPosition);
    }

    public void switchEqualizerSetting(int newEqualizerSettingPosition)
    {
        if (currentEqualizerSettingPosition != newEqualizerSettingPosition)
        {
            currentEqualizerSettingPosition = newEqualizerSettingPosition;
        }

        currentEqualizerName = equalizerPresets.get(newEqualizerSettingPosition).getEqualizerName();
        currentLeftVolume = equalizerPresets.get(currentEqualizerSettingPosition).getLeftVolume();
        currentRightVolume = equalizerPresets.get(currentEqualizerSettingPosition).getRightVolume();
        currentGlobalVolume = equalizerPresets.get(currentEqualizerSettingPosition)
                .getGlobalVolume();
        setEqualizerBandValues();
    }

    public void setCurrentLeftVolume(int leftVolume)
    {
        currentLeftVolume = leftVolume;
    }

    public int getCurrentLeftVolume()
    {
        return currentLeftVolume;
    }

    public void setCurrentRightVolume(int rightVolume)
    {
        currentRightVolume = rightVolume;
    }

    public int getCurrentRightVolume()
    {
        return currentRightVolume;
    }

    public void setCurrentGlobalVolume(int globalVolume)
    {
        currentGlobalVolume = globalVolume;
    }

    public int getCurrentGlobalVolume()
    {
        return currentGlobalVolume;
    }

    public void setCurrentEqualizerName(String newCurrentEqualizerName)
    {
        this.currentEqualizerName = newCurrentEqualizerName;
    }

    public String getCurrentEqualizerName()
    {
        return currentEqualizerName;
    }

    public void setFrequencyBand(int frequencyBand, int millibelLevel)
    {
        currentEqualizerBandValues.put(frequencyBand, millibelLevel);
    }

}
