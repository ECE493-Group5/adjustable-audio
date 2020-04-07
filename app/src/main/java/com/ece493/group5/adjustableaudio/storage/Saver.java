package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The Saver class helps implement the following requirements:
 *
 * #SRS: Manually Controlling the Volumes through an Equalizer
 * #SRS: Viewable Hearing Test Result
 *
 * In particular, the Saver saves the presets and hearing test results to local storage.
 */

public class Saver
{

    private static final String SHARED_PREFS_FILE = "DATATYPE_STORAGE";
    private static final String HEARING_TEST_RESULT_IDENTIFIER= "HEARING_TEST";
    private static final String EQUALIZER_PRESET_IDENTIFIER = "EQUALIZER";

    public static void saveResult(Context context, String encryptedList)
    {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();

        editor.putString(HEARING_TEST_RESULT_IDENTIFIER, encryptedList);
        editor.apply();
    }

    public static String loadResults(Context context)
    {
        String encryptedList = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(HEARING_TEST_RESULT_IDENTIFIER, null);

        return encryptedList;
    }

    public static void savePreset(Context context, String encryptedList)
    {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();

        editor.putString(EQUALIZER_PRESET_IDENTIFIER, encryptedList);
        editor.apply();
    }

    public static String loadPresets(Context context)
    {
        String encryptedList = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(EQUALIZER_PRESET_IDENTIFIER, null);

        return encryptedList;
    }
}
