package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class HearingTestResultListController {

    private static ArrayList<HearingTestResult> resultList = null;

    static public ArrayList<HearingTestResult> getResultList(Context context) {
        if (resultList == null) {
            resultList = loadResults(context);
            if (resultList == null)
            {
                resultList = new ArrayList<HearingTestResult>();
            }
        }
        return resultList ;
    }

    public static void add(Context context, HearingTestResult preset)
    {
        getResultList(context).add(preset);
    }

    static private ArrayList<HearingTestResult> loadResults(Context context)
    {
        String encryptedList = Saver.loadResults(context);
        try{
            String jsonList = Encryptor.decrypt(context, encryptedList);
            return Jsonizer.fromJson(jsonList);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e){
            e.printStackTrace();
            return null;
        }
    }
}
