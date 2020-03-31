package com.ece493.group5.adjustableaudio.models;


import android.content.Context;

import com.ece493.group5.adjustableaudio.storage.SaveController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EqualizerModel
{
    private static final String TAG = EqualizerModel.class.getSimpleName();

    private List<EqualizerPreset> equalizerPresets;
    private int currentEqualizerSettingPosition;
    private EqualizerPreset currentEqualizerPreset;

    public EqualizerModel(Context context)
    {
        equalizerPresets = new ArrayList<>();
        loadPresets(context);
        currentEqualizerPreset = new EqualizerPreset();
        switchEqualizerSetting(0);
    }

    public void loadPresets(Context context)
    {
        equalizerPresets.clear();
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

    public List<EqualizerPreset> getEqualizerPresets()
    {
        return equalizerPresets;
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

    public void setCurrentLeftRightVolumeRatio(double ratio)
    {
        currentEqualizerPreset.setLeftRightVolumeRatio(ratio);
    }

    public double getCurrentLeftRightVolumeRatio()
    {
        return currentEqualizerPreset.getLeftRightVolumeRatio();
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
                currentEqualizerPreset.getLeftRightVolumeRatio(),
                equalizerPresetName);

        equalizerPresets.add(newEqualizerPreset);
        SaveController.savePreset(context, newEqualizerPreset);

        switchEqualizerSetting(equalizerPresets.size()-1);
    }


    public void deleteEqualizerSetting(Context context, int equalizerSettingToBeDeleted)
    {
        SaveController.deletePreset(context, equalizerSettingToBeDeleted);
        equalizerPresets.remove(equalizerSettingToBeDeleted);

        if (equalizerSettingToBeDeleted == currentEqualizerSettingPosition)
            currentEqualizerSettingPosition -= 1;
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
                currentEqualizerPreset.getLeftRightVolumeRatio(),
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
        currentEqualizerPreset.setLeftRightVolumeRatio(equalizerPresets.get(currentEqualizerSettingPosition).getLeftRightVolumeRatio());

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
