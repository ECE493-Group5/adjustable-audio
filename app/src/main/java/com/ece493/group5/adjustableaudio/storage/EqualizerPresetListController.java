package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
        getPresetList(context).add(preset);
    }

    public static void remove(Context context, EqualizerPreset preset)
    {
        getPresetList(context).remove(preset);
    }

}
