package com.ece493.group5.adjustableaudio;

import android.util.Log;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.Jsonizer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JsonizerUnitTest
{
    ArrayList<String> testStringList;
    ArrayList<Integer> testIntegerList;
    ArrayList<ToneData> testToneDataList;
    ArrayList<HearingTestResult> testResultList;
    ArrayList<EqualizerPreset> testPresetList;
    String testJsonStringList;
    String testJsonIntegerList;
    String testJsonToneDataList;
    String testJsonResultList;
    String testJsonPresetList;

    @Before
    public void JsonizerTestSetup() {
        testStringList = new ArrayList<String>();
        testStringList.add("first");
        testStringList.add("second");
        testStringList.add("third");
        testJsonStringList = "[\"first\",\"second\",\"third\"]";

        testIntegerList = new ArrayList<Integer>();
        testIntegerList.add(1);
        testIntegerList.add(2);
        testIntegerList.add(3);
        testJsonIntegerList = "[1,2,3]";

        testToneDataList = new ArrayList<ToneData>();
        testToneDataList.add(new ToneData(250, 19.0));
        testToneDataList.add(new ToneData(500, 11.0));
        testToneDataList.add(new ToneData(1000, 5.0));
        testJsonToneDataList = "[{\"DELTA\":1.0E-4,\"frequency\":250,\"dBHL\":19.0,\"lHeardAtDB\":0.0,\"rHeardAtDB\":0.0},{\"DELTA\":1.0E-4,\"frequency\":500,\"dBHL\":11.0,\"lHeardAtDB\":0.0,\"rHeardAtDB\":0.0},{\"DELTA\":1.0E-4,\"frequency\":1000,\"dBHL\":5.0,\"lHeardAtDB\":0.0,\"rHeardAtDB\":0.0}]";

        testResultList = new ArrayList<HearingTestResult>();
        ArrayList<ToneData> toneDataList = new ArrayList<ToneData>();
        for (int i = 0; i < 16; i ++)
        {
            int frequency = 100 * i;
            double dbHL = 5 * i;
            double lHeardAtDB = 100 - (5 * i);
            double rHeardAtDB = 80 - (5 * i);
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            toneDataList.add(toneData);
        }
        String testTestName = "Test Name";
        HearingTestResult testHearingTestResult = new HearingTestResult(testTestName, toneDataList);
        testResultList.add(testHearingTestResult);
        testJsonResultList = "";

        testPresetList = new ArrayList<EqualizerPreset>();
        HashMap<Integer, Integer> testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put(0, -1500);
        testEqualizerSettings.put(1, -1000);
        testEqualizerSettings.put(2, -500);
        testEqualizerSettings.put(3, 0);
        testEqualizerSettings.put(4, 500);
        Double testLeftRightRatio = 0.50;
        String testEqualizerName = "Test Equalizer Name";
        EqualizerPreset testEqualizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);
        testPresetList.add(testEqualizerPreset);
        testJsonResultList = "";


    }

    @Test
    public void toJsonSimpleObjectTest()
    {
        String jsonStringList = Jsonizer.toJson(testStringList);

        assertNotNull(jsonStringList);
        assertEquals(testJsonStringList, jsonStringList);

        String jsonIntegerList = Jsonizer.toJson(testIntegerList);

        assertNotNull(jsonIntegerList);
        assertEquals(testJsonIntegerList, jsonIntegerList);
    }

    @Test
    public void toJsonComplexObjectTest()
    {
        String jsonToneDataList = Jsonizer.toJson(testToneDataList);

        assertNotNull(jsonToneDataList);
        assertEquals(testJsonToneDataList, jsonToneDataList);
    }

    @Test
    public void toJsonHearingTestResult()
    {

    }

    @Test
    public void toJsonEqualizerPreset()
    {

    }

    @Test
    public void fromJsonSimpleObjectTest()
    {
        ArrayList<String> stringList = Jsonizer.fromJson(testJsonStringList, String[].class);

        assertNotNull(stringList);
        assertEquals(testStringList, stringList);

        ArrayList<Integer> integerList = Jsonizer.fromJson(testJsonIntegerList, Integer[].class);

        assertNotNull(integerList);
        assertEquals(testIntegerList, integerList);
    }

    @Test
    public void fromJsonComplexObjectTest()
    {
        ArrayList<ToneData> toneDataList = Jsonizer.fromJson(testJsonToneDataList, ToneData[].class);

        assertNotNull(toneDataList);
        assertEquals(testToneDataList, toneDataList);
    }

    @Test
    public void fromJsonHearingTestResult()
    {

    }

    @Test
    public void fromJsonEqualizerPreset()
    {

    }

}
