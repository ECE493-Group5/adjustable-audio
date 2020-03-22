package com.ece493.group5.adjustableaudio.storage;

import android.util.Log;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Jsonizer {

    private static Gson gson = null;

    private static Gson getGson() {
        if (gson == null) {
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
        Log.d("Jsonizer", "result list: " + array.toString());
        Log.d("Jsonizer", "result list class: " + array[0].getClass().toString());
        return new ArrayList<T>(Arrays.asList(array));
    }

}
