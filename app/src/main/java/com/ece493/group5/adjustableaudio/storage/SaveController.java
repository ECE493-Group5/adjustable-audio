package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.util.ArrayList;

/**
 * The SaveController class helps implement the following requirements:
 *
 * #SRS: Manually Controlling the Volumes through an Equalizer
 * #SRS: Viewable Hearing Test Result
 *
 * In particular, the SaveController helps save presets and hearing test results.
 */

public class SaveController
{

    static public void savePreset(Context context, EqualizerPreset preset)
    {
        EqualizerPresetListController.add(context, preset);

        savePresets(context);
    }

    static public void savePresets(Context context)
    {
        String jsonList = Jsonizer.toJson(EqualizerPresetListController.getPresetList(context));

        String encryptedList = Encrypter.encrypt(context, jsonList);

        Saver.savePreset(context, encryptedList);
    }

    static public void updatePreset(Context context, int presetPosition, EqualizerPreset updatedPreset)
    {
        EqualizerPresetListController.update(context, presetPosition, updatedPreset);

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

        saveResults(context);
    }

    static public void saveResults(Context context)
    {
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

    static public void deleteResult(Context context, int position)
    {
        HearingTestResultListController.removeAtPosition(context, position);
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
