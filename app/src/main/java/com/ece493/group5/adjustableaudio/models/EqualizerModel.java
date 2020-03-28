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
    EqualizerPreset currentEqualizerPreset;

    public EqualizerModel(Context context)
    {
        equalizerPresets = new ArrayList<>();
        loadPresets(context);
        currentEqualizerPreset = new EqualizerPreset();
        switchEqualizerSetting(0);
    }

    public void loadPresets(Context context)
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

    public int getCurrentEqualizerSettingPosition()
    {
        return currentEqualizerSettingPosition;
    }

    public HashMap<Integer, Integer> getCurrentEqualizerBandValues()
    {
        return currentEqualizerPreset.getEqualizerSettings();
    }

    private HashMap<Integer, Integer> copyEqualizerBandValues(HashMap<Integer, Integer> originalBandValues)
    {
        HashMap<Integer, Integer> currentEqualizerBandValues = new HashMap<>();

        for (Integer key : originalBandValues.keySet())
        {
            currentEqualizerBandValues.put(key, originalBandValues.get(key));
        }

        return currentEqualizerBandValues;
    }

    public void setCurrentLeftVolume(int leftVolume)
    {
        currentEqualizerPreset.setLeftVolume(leftVolume);
    }

    public int getCurrentLeftVolume()
    {
        return currentEqualizerPreset.getLeftVolume();
    }

    public void setCurrentRightVolume(int rightVolume)
    {
        currentEqualizerPreset.setRightVolume(rightVolume);
    }

    public int getCurrentRightVolume()
    {
        return currentEqualizerPreset.getRightVolume();
    }

    public String getCurrentEqualizerName()
    {
        return currentEqualizerPreset.getEqualizerName();
    }

    public void setFrequencyBand(int frequencyBand, int millibelLevel)
    {
        currentEqualizerPreset.setFrequencyBand(frequencyBand, millibelLevel);
    }

    public void addEqualizerSetting(Context context, String equalizerPresetName)
    {
        HashMap<Integer, Integer> presetBandValues = copyEqualizerBandValues(currentEqualizerPreset.getEqualizerSettings());
        EqualizerPreset newEqualizerPreset = new EqualizerPreset(presetBandValues,
                currentEqualizerPreset.getLeftVolume(), currentEqualizerPreset.getRightVolume(),
                equalizerPresetName);

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
        HashMap<Integer, Integer> presetBandValues = copyEqualizerBandValues(currentEqualizerPreset.getEqualizerSettings());
        EqualizerPreset updatedEqualizerPreset = new EqualizerPreset(presetBandValues,
                currentEqualizerPreset.getLeftVolume(), currentEqualizerPreset.getRightVolume(),
                currentEqualizerPreset.getEqualizerName());

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

        currentEqualizerPreset.setEqualizerName(equalizerPresets.get(newEqualizerSettingPosition).getEqualizerName());
        currentEqualizerPreset.setLeftVolume(equalizerPresets.get(currentEqualizerSettingPosition).getLeftVolume());
        currentEqualizerPreset.setRightVolume(equalizerPresets.get(currentEqualizerSettingPosition).getRightVolume());

        HashMap<Integer, Integer> presetValues =
                copyEqualizerBandValues(equalizerPresets.get(currentEqualizerSettingPosition).getEqualizerSettings());
        currentEqualizerPreset.setEqualizerSettings(presetValues);
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
