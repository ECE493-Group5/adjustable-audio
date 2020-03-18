package com.ece493.group5.adjustableaudio.storage;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import java.util.ArrayList;

public class EqualizerPresetListController {

    private static ArrayList<EqualizerPreset> presetList = null;

    static public ArrayList<EqualizerPreset> getEmotionList() {
        if (presetList == null) {
            presetList = Saver.loadPresets();
        }
        return presetList;
    }
}
