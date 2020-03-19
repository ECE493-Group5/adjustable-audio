package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

    private static final int KEY_SIZE = 256;
    private static final String SHARED_PREFS_FILE = "KEY_STORAGE";
    private static final String SECRET_KEY_IDENTIFIER = "S_KEY";


    private static SecretKey secretKey = null;

    private static SecretKey getSecretKey(Context context)
    {
        if (secretKey == null)
        {
            secretKey = loadSecretKey(context);
        }
        return secretKey;
    }
    public static String encrypt(Context context, String encryptString)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(context));
        byte[] encryptedBytes = cipher.doFinal(encryptString.getBytes(StandardCharsets.UTF_8));
        return new String(encryptedBytes, StandardCharsets.UTF_8);
    }

    public static String decrypt(Context context, String encryptedString)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException
    {
        byte[] encryptedBytes = encryptedString.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context));
        return new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
    }

    private static SecretKey createSecretKey(Context context)
            throws NoSuchAlgorithmException
    {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keyGen.init(KEY_SIZE, random);
        secretKey = keyGen.generateKey();
        saveSecretKey(context, secretKey);
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
                SecretKey secretKey = createSecretKey(context);
            } catch(NoSuchAlgorithmException e) {
                e.printStackTrace();;
            }
            return secretKey;
        }
        else {
            byte[] decodedKey = Base64.decode(sKeyString, android.util.Base64.DEFAULT);
            SecretKey secretKey =
                    new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            return secretKey;
        }
    }

    private static void saveSecretKey(Context context, SecretKey secretKey)
    {
        String base64EncodedKey = Base64.encodeToString(secretKey.getEncoded(),
                android.util.Base64.DEFAULT);
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(SECRET_KEY_IDENTIFIER, base64EncodedKey);
        editor.apply();
    }
}
