package com.ece493.group5.adjustableaudio.storage;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Saver {

    public static void saveResult(HearingTestResult result)
    {
        HearingTestResultListController.add(result);
        String jsonList = Jsonizer.toJson(HearingTestResultListController.getResultList());
        String encryptedList = Encryptor.encrypt(jsonList);
        // save string to storage
    }

    public static ArrayList<HearingTestResult> loadResults()
    {
        String encryptedList = ""; // TODO load from storage
        String jsonList = Encryptor.decrypt(encryptedList);
        return Jsonizer.fromJson(jsonList);
    }

    public static void savePreset(EqualizerPreset preset)
    {
        EqualizerPresetListController.add(preset);
        String jsonList = Jsonizer.toJson(EqualizerPresetListController.getPresetList());
        String encryptedList = Encryptor.encrypt((jsonList));
        // save string to storage
    }

    public static ArrayList<EqualizerPreset> loadPresets()
    {
        String encryptedList = ""; // TODO load from storage
        String jsonList = Encryptor.decrypt(encryptedList);
        return Jsonizer.fromJson(jsonList);
    }
}
