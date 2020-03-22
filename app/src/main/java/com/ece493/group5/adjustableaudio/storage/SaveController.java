package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.util.ArrayList;

public class SaveController {
    
    private static ArrayList<EqualizerPreset> loadPresets(Context context)
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

    static private ArrayList<HearingTestResult> loadResults(Context context)
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
