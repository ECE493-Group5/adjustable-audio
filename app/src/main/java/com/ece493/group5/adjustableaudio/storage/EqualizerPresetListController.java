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
            presetList = loadPresets(context);
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

    private static ArrayList<EqualizerPreset> loadPresets(Context context)
    {
        String encryptedList = Saver.loadPresets(context);
        try{
            String jsonList = Encrypter.decrypt(context, encryptedList);
            return Jsonizer.fromJson(jsonList, EqualizerPreset[].class);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException |
                 InvalidAlgorithmParameterException e){
            e.printStackTrace();
            return null;
        }
    }
}
