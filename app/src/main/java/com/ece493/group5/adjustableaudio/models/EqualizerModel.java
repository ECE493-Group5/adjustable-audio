package com.ece493.group5.adjustableaudio.models;


import android.content.Context;

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
        equalizerPresets = new ArrayList<>();
        loadPresets(context);
        setInitialEqualizerState();
    }

    private void loadPresets(Context context)
    {
       List<EqualizerPreset> loadedPresets = SaveController.loadPresets(context);

        if (loadedPresets == null)
        {
            EqualizerPreset defaultPreset = ApplicationEqualizerPresets.getDefaultPreset();
            equalizerPresets.add(defaultPreset);
            SaveController.savePreset(context, defaultPreset);
        }
        else
        {
            for (EqualizerPreset preset : loadedPresets)
            {
                equalizerPresets.add(new EqualizerPreset(preset));
            }
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

    public int getCurrentEqualizerSettingPosition()
    {
        return currentEqualizerSettingPosition;
    }

    public HashMap<Integer, Integer> getCurrentEqualizerBandValues()
    {
        return currentEqualizerBandValues;
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

    public String getCurrentEqualizerName()
    {
        return currentEqualizerName;
    }

    public void setFrequencyBand(int frequencyBand, int millibelLevel)
    {
        currentEqualizerBandValues.put(frequencyBand, millibelLevel);
    }

    public void addEqualizerSetting(Context context, String equalizerPresetName)
    {
        EqualizerPreset newEqualizerPreset = new EqualizerPreset(currentEqualizerBandValues,
                currentLeftVolume, currentRightVolume);
        newEqualizerPreset.setEqualizerName(equalizerPresetName);
        equalizerPresets.add(newEqualizerPreset);
        SaveController.savePreset(context, newEqualizerPreset);

        switchEqualizerSetting(equalizerPresets.size()-1);
    }


    public void deleteEqualizerSetting(Context context, int equalizerSettingToBeDeleted)
    {
        SaveController.deletePreset(context, equalizerSettingToBeDeleted);
        equalizerPresets.remove(equalizerSettingToBeDeleted);
    }


    public void renameEqualizerSetting(Context context, String newName)
    {
        equalizerPresets.get(currentEqualizerSettingPosition).setEqualizerName(newName);
        SaveController.updatePreset(context, currentEqualizerSettingPosition,
                equalizerPresets.get(currentEqualizerSettingPosition));
    }

    public void updateEqualizerPreset(Context context)
    {
        EqualizerPreset updatedEqualizerPreset = new EqualizerPreset(currentEqualizerBandValues,
                currentLeftVolume, currentRightVolume);
        updatedEqualizerPreset.setEqualizerName(currentEqualizerName);

        equalizerPresets.set(currentEqualizerSettingPosition, updatedEqualizerPreset);
        SaveController.updatePreset(context, currentEqualizerSettingPosition, updatedEqualizerPreset);
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

    public List<String> getEqualizerPresetNames()
    {
        List<String> presetNames = new ArrayList<>();
        for (EqualizerPreset equalizerPreset : equalizerPresets)
        {
            presetNames.add(equalizerPreset.getEqualizerName());
        }
        return presetNames;
    }
}
