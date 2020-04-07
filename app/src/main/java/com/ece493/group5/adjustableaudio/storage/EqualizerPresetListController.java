package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import java.util.ArrayList;

/**
 * The EqualizerPresetListController helps implement the following requirement:
 *
 * #SRS: Manually Controlling the Volumes through an Equalizer
 */

public class EqualizerPresetListController
{

    private static ArrayList<EqualizerPreset> presetList = null;

    static public ArrayList<EqualizerPreset> getPresetList(Context context)
    {
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

    public static void update(Context context, int presetPosition, EqualizerPreset equalizerPreset)
    {
        getPresetList(context).set(presetPosition, equalizerPreset);
    }

    public static void remove(Context context, int presetPosition)
    {
        getPresetList(context).remove(presetPosition);
    }

}
