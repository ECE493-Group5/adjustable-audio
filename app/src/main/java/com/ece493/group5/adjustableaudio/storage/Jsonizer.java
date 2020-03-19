package com.ece493.group5.adjustableaudio.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public static <T> ArrayList<T> fromJson(String src)
    {
        Type collectionType = new TypeToken<ArrayList<T>>(){}.getType();
        ArrayList<T> result = getGson().fromJson(src, collectionType);
        return result;
    }
}
