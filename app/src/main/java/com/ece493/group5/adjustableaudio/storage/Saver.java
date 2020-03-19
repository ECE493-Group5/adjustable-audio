package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Saver {

    private static final String SHARED_PREFS_FILE = "DATATYPE_STORAGE";
    private static final String HEARING_TEST_RESULT_IDENTIFIER= "HEARING_TEST";
    private static final String EQUALIZER_PRESET_IDENTIFIER = "EQUALIZER";

    public static void saveResult(Context context, HearingTestResult result)
    {
        HearingTestResultListController.add(context, result);
        String jsonList = Jsonizer.toJson(HearingTestResultListController.getResultList(context));
        String encryptedList = Encryptor.encrypt(jsonList);
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(HEARING_TEST_RESULT_IDENTIFIER, encryptedList);
        editor.apply();
    }

    public static ArrayList<HearingTestResult> loadResults(Context context)
    {
        String encryptedList = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(HEARING_TEST_RESULT_IDENTIFIER, null);
        String jsonList = Encryptor.decrypt(encryptedList);
        return Jsonizer.fromJson(jsonList);
    }

    public static void savePreset(Context context, EqualizerPreset preset)
    {
        EqualizerPresetListController.add(context, preset);
        String jsonList = Jsonizer.toJson(EqualizerPresetListController.getPresetList(context));
        String encryptedList = Encryptor.encrypt((jsonList));
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(EQUALIZER_PRESET_IDENTIFIER, encryptedList);
        editor.apply();
    }

    public static ArrayList<EqualizerPreset> loadPresets(Context context)
    {
        String encryptedList = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(EQUALIZER_PRESET_IDENTIFIER, null);
        String jsonList = Encryptor.decrypt(encryptedList);
        return Jsonizer.fromJson(jsonList);
    }
}
