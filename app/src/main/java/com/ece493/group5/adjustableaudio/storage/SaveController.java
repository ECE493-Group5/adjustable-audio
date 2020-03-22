package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.storage.Encrypter;
import com.ece493.group5.adjustableaudio.storage.EqualizerPresetListController;
import com.ece493.group5.adjustableaudio.storage.HearingTestResultListController;
import com.ece493.group5.adjustableaudio.storage.Jsonizer;
import com.ece493.group5.adjustableaudio.storage.Saver;

import java.util.ArrayList;


public class SaveController {

    static public void savePreset(Context context, EqualizerPreset preset)
    {
        EqualizerPresetListController.add(context, preset);
        String jsonList = Jsonizer.toJson(EqualizerPresetListController.getPresetList(context));
        String encryptedList = Encrypter.encrypt(context, jsonList);
        Saver.savePreset(context, encryptedList);
    }

    static public void deletePreset(Context context, int presetPosition)
    {
        EqualizerPresetListController.remove(context, presetPosition);
        String jsonList = Jsonizer.toJson(EqualizerPresetListController.getPresetList(context));
        String encryptedList = Encrypter.encrypt(context, jsonList);
        Saver.savePreset(context, encryptedList);
    }

    static public ArrayList<EqualizerPreset> loadPresets(Context context)
    {
        String encryptedList = Saver.loadPresets(context);
        if (encryptedList == null)
        {
            return null;
        }
        String jsonList = Encrypter.decrypt(context, encryptedList);
        if (jsonList == null)
        {
            return null;
        }
        return Jsonizer.fromJson(jsonList, EqualizerPreset[].class);
    }

    static public void saveResult(Context context, HearingTestResult result)
    {
        HearingTestResultListController.add(context, result);
        String jsonList = Jsonizer.toJson(HearingTestResultListController.getResultList(context));
        String encryptedList = Encrypter.encrypt(context, jsonList);
        Saver.saveResult(context, encryptedList);
    }

    static public void deleteResult(Context context, HearingTestResult result)
    {
        HearingTestResultListController.remove(context, result);
        String jsonList = Jsonizer.toJson(HearingTestResultListController.getResultList(context));
        String encryptedList = Encrypter.encrypt(context, jsonList);
        Saver.saveResult(context, encryptedList);
    }

    static public ArrayList<HearingTestResult> loadResults(Context context)
    {
        String encryptedList = Saver.loadResults(context);
        if (encryptedList == null)
        {
            return null;
        }
        String jsonList = Encrypter.decrypt(context, encryptedList);
        if (jsonList == null)
        {
            return null;
        }
        return Jsonizer.fromJson(jsonList, HearingTestResult[].class);
    }
}
