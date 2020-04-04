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
    String jsonizedHearingTestResult;
    String jsonizedEqualizerPreset;
    String encryptedEqualizerPreset;
    String encryptedHearingTestResult;

    @Before
    public void encrypterUnitTestSetup()
    {
        testContext = ApplicationProvider.getApplicationContext();

        testEncryptString = "String To Encrypt!";

        testEncryptedString = Encrypter.encrypt(
                testContext, testEncryptString);

        jsonizedEqualizerPreset = "[{\"equalizerSettings\":{\"0\":-1500,\"1\":-1000,\"2\":-500,\"3\":0,\"4\":500}," +
                "\"leftRightVolumeRatio\":0.5,\"equalizerName\":\"Test Equalizer Name\"}]";

        encryptedEqualizerPreset = Encrypter.encrypt(
                testContext, jsonizedEqualizerPreset);

        jsonizedHearingTestResult = "[{\"TAG\":\"HearingTestResult\",\"testResults\":[{\"DELTA\":1.0E-4,\"frequency\":0,\"dBHL\":0.0,\"lHeardAtDB\":100.0,\"rHeardAtDB\":80.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":100,\"dBHL\":5.0,\"lHeardAtDB\":95.0,\"rHeardAtDB\":75.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":200,\"dBHL\":10.0,\"lHeardAtDB\":90.0,\"rHeardAtDB\":70.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":300,\"dBHL\":15.0,\"lHeardAtDB\":85.0,\"rHeardAtDB\":65.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":400,\"dBHL\":20.0,\"lHeardAtDB\":80.0,\"rHeardAtDB\":60.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":500,\"dBHL\":25.0,\"lHeardAtDB\":75.0,\"rHeardAtDB\":55.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":600,\"dBHL\":30.0,\"lHeardAtDB\":70.0,\"rHeardAtDB\":50.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":700,\"dBHL\":35.0,\"lHeardAtDB\":65.0,\"rHeardAtDB\":45.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":800,\"dBHL\":40.0,\"lHeardAtDB\":60.0,\"rHeardAtDB\":40.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":900,\"dBHL\":45.0,\"lHeardAtDB\":55.0,\"rHeardAtDB\":35.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":1000,\"dBHL\":50.0,\"lHeardAtDB\":50.0,\"rHeardAtDB\":30.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":1100,\"dBHL\":55.0,\"lHeardAtDB\":45.0,\"rHeardAtDB\":25.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":1200,\"dBHL\":60.0,\"lHeardAtDB\":40.0,\"rHeardAtDB\":20.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":1300,\"dBHL\":65.0,\"lHeardAtDB\":35.0,\"rHeardAtDB\":15.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":1400,\"dBHL\":70.0,\"lHeardAtDB\":30.0,\"rHeardAtDB\":10.0}," +
                "{\"DELTA\":1.0E-4,\"frequency\":1500,\"dBHL\":75.0,\"lHeardAtDB\":25.0,\"rHeardAtDB\":5.0}]," +
                "\"testName\":\"Test Name\",\"testDate\":\"Jan 1, 1980 4:0:00 PM\"}]";

        encryptedHearingTestResult = Encrypter.encrypt(
                testContext, jsonizedHearingTestResult);
    }


    @Test
    public void encryptTest()
    {
        String encryptedString = Encrypter.encrypt(testContext, testEncryptString);

        assertNotNull(encryptedString);
        assertNotEquals(testEncryptString, encryptedString);
        assertEquals(testEncryptedString, encryptedString);
    }

    public void encryptHearingTestResultTest()
    {
        String encryptedJsonTestResult = Encrypter.encrypt(testContext, jsonizedHearingTestResult);

        assertNotNull(encryptedJsonTestResult);
        assertNotEquals(jsonizedHearingTestResult, encryptedJsonTestResult);
        assertEquals(encryptedHearingTestResult, encryptedJsonTestResult);
    }

    public void encryptEqualizerPresetTest()
    {
        String encryptedJsonPreset = Encrypter.encrypt(testContext, jsonizedEqualizerPreset);

        assertNotNull(encryptedJsonPreset);
        assertNotEquals(jsonizedEqualizerPreset, encryptedJsonPreset);
        assertEquals(encryptedEqualizerPreset, encryptedJsonPreset);
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
