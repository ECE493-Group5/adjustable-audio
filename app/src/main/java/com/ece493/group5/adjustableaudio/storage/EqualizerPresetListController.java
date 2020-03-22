package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;
import android.util.Log;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import java.util.ArrayList;

public class EqualizerPresetListController {

    private static ArrayList<EqualizerPreset> presetList = null;

    static public ArrayList<EqualizerPreset> getPresetList(Context context) {
        if (presetList == null)
        {
            presetList = SaveController.loadPresets(context);
            if (presetList == null)
            {
                presetList = new ArrayList<EqualizerPreset>();
            }
        }
        return presetList;
    }

    public static void add(Context context, EqualizerPreset preset)
    {
        Log.d("PresetController", "Before Add - " + Integer.toString(getPresetList(context).size()));
        getPresetList(context).add(preset);
        Log.d("PresetController", "After Add - " + Integer.toString(getPresetList(context).size()));
    }

    public static void remove(Context context, int presetPosition)
    {
        getPresetList(context).remove(presetPosition);
    }

}
