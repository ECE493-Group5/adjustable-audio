package com.ece493.group5.adjustableaudio.models;


import android.content.Context;
import android.util.Log;

import com.ece493.group5.adjustableaudio.storage.SaveController;

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
    String currentEqualizerName;

    public EqualizerModel(Context context)
    {
        loadPresets(context);
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

    private void loadPresets(Context context)
    {
        equalizerPresets = SaveController.loadPresets(context);

        if (equalizerPresets == null)
        {
            equalizerPresets = new ArrayList<>();

            //Default Preset
            HashMap<Integer, Integer> defaultEqualizerValues  = new HashMap<>();
            defaultEqualizerValues.put(0, 300);
            defaultEqualizerValues.put(1, 0);
            defaultEqualizerValues.put(2, 0);
            defaultEqualizerValues.put(3, 0);
            defaultEqualizerValues.put(4, 300);
            EqualizerPreset defaultPreset = new EqualizerPreset(defaultEqualizerValues, 0, 0);
            defaultPreset.setEqualizerName("Default");
            equalizerPresets.add(defaultPreset);
            SaveController.savePreset(context, defaultPreset);
        }
    }

    private void setInitialEqualizerState()
    {
        currentEqualizerSettingPosition = 0;
        currentLeftVolume = equalizerPresets.get(currentEqualizerSettingPosition).getLeftVolume();
        currentRightVolume = equalizerPresets.get(currentEqualizerSettingPosition).getRightVolume();
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

    public void addEqualizerSetting(Context context, String equalizerPresetName)
    {
        EqualizerPreset newEqualizerPreset = new EqualizerPreset(currentEqualizerBandValues,
                currentLeftVolume, currentRightVolume);
        newEqualizerPreset.setEqualizerName(equalizerPresetName);
        equalizerPresets.add(newEqualizerPreset);
        SaveController.savePreset(context, newEqualizerPreset);
    }


    public void deleteEqualizerSetting(Context context, int equalizerSettingToBeDeleted)
    {
        SaveController.deletePreset(context, equalizerSettingToBeDeleted);
        equalizerPresets.remove(equalizerSettingToBeDeleted);
    }


    public void renameEqualizerSetting(int equalizerSettingToBeRenamed, String newName)
    {
        equalizerPresets.get(equalizerSettingToBeRenamed).setEqualizerName(newName);
    }

    public void updateEqualizerPreset()
    {
        EqualizerPreset updatedEqualizerPreset = new EqualizerPreset(currentEqualizerBandValues,
                currentLeftVolume, currentRightVolume);
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
