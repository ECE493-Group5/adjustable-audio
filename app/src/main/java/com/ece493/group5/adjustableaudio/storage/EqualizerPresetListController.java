package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class EqualizerPresetListController {

    private static ArrayList<EqualizerPreset> presetList = null;

    static public ArrayList<EqualizerPreset> getPresetList(Context context) {
        if (presetList == null) {
            presetList = Saver.loadPresets(context);
        }
        return presetList;
    }

    public static void add(Context context, EqualizerPreset preset)
    {
        getPresetList(context).add(preset);
    }


}
