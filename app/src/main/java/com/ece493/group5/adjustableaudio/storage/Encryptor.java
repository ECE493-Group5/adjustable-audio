package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

    private static final int KEY_SIZE = 256;
    private static final String SHARED_PREFS_FILE = "KEY_STORAGE";
    private static final String SECRET_KEY_IDENTIFIER = "S_KEY";
    private static final String IV_IDENTIFIER = "IV";

    private static SecretKey secretKey = null;
    private static IvParameterSpec iv = null;

    private static SecretKey getSecretKey(Context context)
    {
        if (secretKey == null)
        {
            secretKey = loadSecretKey(context);
        }
        return secretKey;
    }

    private static IvParameterSpec getIV(Context context)
    {
        if (iv == null)
        {
            iv = loadIV(context);
        }
        return iv;
    }
    public static String encrypt(Context context, String encryptString)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(context), getIV(context));
        byte[] encryptedBytes = cipher.doFinal(encryptString.getBytes(StandardCharsets.UTF_8));
        return  Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT);
    }

    public static String decrypt(Context context, String encryptedString)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException
    {
        byte[] encryptedBytes = Base64.decode(encryptedString, android.util.Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context), getIV(context));
        String result = new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
        return result;
    }

    private static SecretKey generateSecretKey(Context context)
            throws NoSuchAlgorithmException
    {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keyGen.init(KEY_SIZE, random);
        secretKey = keyGen.generateKey();
        saveSecretKey(context);
        return secretKey;
    }

    private static SecretKey loadSecretKey(Context context)
    {
        String sKeyString = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(SECRET_KEY_IDENTIFIER, null);
        if (sKeyString == null)
        {
            try {
                SecretKey newSecretKey = generateSecretKey(context);
                return newSecretKey;
            } catch(NoSuchAlgorithmException e) {
                e.printStackTrace();;
                return null;
            }
        }
        else {
            byte[] decodedKey = Base64.decode(sKeyString, android.util.Base64.DEFAULT);
            SecretKey newSecretKey =
                    new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            return newSecretKey;
        }
    }

    private static void saveSecretKey(Context context)
    {
        String base64EncodedKey = Base64.encodeToString(secretKey.getEncoded(),
                android.util.Base64.DEFAULT);
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(SECRET_KEY_IDENTIFIER, base64EncodedKey);
        editor.apply();
    }

    private static IvParameterSpec generateIV(Context context)
    {
        SecureRandom secureRandom = new SecureRandom();
        byte[] byteIV = new byte[16];
        secureRandom.nextBytes(byteIV);
        iv = new IvParameterSpec(byteIV);
        saveIV(context);
        return iv;
    }

    private static IvParameterSpec loadIV(Context context){
        String ivString = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(IV_IDENTIFIER, null);
        if (ivString == null)
        {
            IvParameterSpec newIV = generateIV(context);
            return newIV;
        }
        else {
            byte[] decodedIV = Base64.decode(ivString, android.util.Base64.DEFAULT);
            IvParameterSpec newIV = new IvParameterSpec(decodedIV);
            return newIV;
        }
    }

    private static void saveIV(Context context)
    {
        String base64EncodedIV = Base64.encodeToString(iv.getIV(),
                android.util.Base64.DEFAULT);
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(IV_IDENTIFIER, base64EncodedIV);
        editor.apply();
    }
}
