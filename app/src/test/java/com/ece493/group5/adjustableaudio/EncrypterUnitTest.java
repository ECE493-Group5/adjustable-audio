package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.os.Build;

import com.ece493.group5.adjustableaudio.storage.Encrypter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class EncrypterUnitTest
{
    Context testApplicationContext;
    String testEncryptString;
    String testEncryptedString;

    @Before
    public void encrypterUnitTestSetup()
    {
        testEncryptString = "String To Encrypt!";
        testApplicationContext = ApplicationProvider.getApplicationContext();

        testEncryptedString = Encrypter.encrypt(
                testApplicationContext, testEncryptedString);
    }


    @Test
    public void encryptTest()
    {
        String encryptedString = Encrypter.encrypt(testApplicationContext, testEncryptString);

        assertNotNull(encryptedString);
        assertNotEquals(testEncryptString, encryptedString);
        assertEquals(testEncryptedString, encryptedString);
    }

    @Test
    public void decryptTest()
    {
        String decryptedString = Encrypter.decrypt(testApplicationContext, testEncryptedString);

        assertNotNull(decryptedString);
        assertNotEquals(testEncryptedString, decryptedString);
        assertEquals(testEncryptString, decryptedString);
    }

    @Test
    public void testEncryptDecrypt()
    {
        String decryptedString = Encrypter.decrypt(testApplicationContext,
                Encrypter.encrypt(testApplicationContext, testEncryptString));

        assertNotNull(decryptedString);
        assertNotEquals(testEncryptedString, decryptedString);
        assertEquals(testEncryptString, decryptedString);
    }

}
