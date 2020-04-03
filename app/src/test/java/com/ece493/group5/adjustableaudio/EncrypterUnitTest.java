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
    Context testContext;
    String testEncryptString;
    String testEncryptedString;

    @Before
    public void encrypterUnitTestSetup()
    {
        testEncryptString = "String To Encrypt!";
        testContext = ApplicationProvider.getApplicationContext();

        testEncryptedString = Encrypter.encrypt(
                testContext, testEncryptString);
    }


    @Test
    public void encryptTest()
    {
        String encryptedString = Encrypter.encrypt(testContext, testEncryptString);

        assertNotNull(encryptedString);
        assertNotEquals(testEncryptString, encryptedString);
        assertEquals(testEncryptedString, encryptedString);
    }

    @Test
    public void decryptTest()
    {
        String decryptedString = Encrypter.decrypt(testContext, testEncryptedString);

        assertNotNull(decryptedString);
        assertNotEquals(testEncryptedString, decryptedString);
        assertEquals(testEncryptString, decryptedString);
    }

    @Test
    public void testEncryptDecrypt()
    {
        String decryptedString = Encrypter.decrypt(testContext,
                Encrypter.encrypt(testContext, testEncryptString));

        assertNotNull(decryptedString);
        assertNotEquals(testEncryptedString, decryptedString);
        assertEquals(testEncryptString, decryptedString);
    }

}
