package com.ece493.group5.adjustableaudio.storage;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Jsonizer class helps implement the following requirements:
 *
 * #SRS: Manually Controlling the Volumes through an Equalizer
 * #SRS: Viewable Hearing Test Result
 *
 * In particular, the Jsonizer is used to turn presets and hearing test results into JSON strings.
 */

public class Jsonizer
{

    private static Gson gson = null;

    private static Gson getGson()
    {
        if (gson == null)
        {
            gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();
        }
        return gson;
    }

    public static String toJson(Object object)
    {
        return getGson().toJson(object);
    }

    public static <T> ArrayList<T> fromJson(String src, Class<T[]> clazz )
    {
        T[] array = new Gson().fromJson(src, clazz);
        return new ArrayList<T>(Arrays.asList(array));
    }

}
