package com.ece493.group5.adjustableaudio.storage;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class EqualizerPresetListController {

    private static ArrayList<EqualizerPreset> presetList = null;

    static public ArrayList<EqualizerPreset> getPresetList() {
        if (presetList == null) {
            presetList = Saver.loadPresets();
        }
        return presetList;
    }

    public static void add(EqualizerPreset preset)
    {
        getPresetList().add(preset);
    }


}
