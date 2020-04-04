package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.os.Build;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.EqualizerPresetListController;
import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class SaveControllerUnitTest
{

    public static final int BAND1 = 1;
    public static final int BAND2 = 2;
    public static final int BAND3 = 3;
    public static final int BAND4 = 4;
    public static final int BANDVAL1 = -1000;
    public static final int BANDVAL2 = -500;
    public static final int BANDVAL3 = 0;
    public static final int BANDVAL4 = 500;
    public static final double RATIO_POINT5 = 0.50;
    public static final String TEST_EQUALIZER_NAME = "Test Equalizer Name";
    public static final int NUM_FREQUENCIES = 16;
    public static final int BASE_FREQUENCY = 100;
    public static final int BASE_DBHL = 5;
    public static final int MAX_DB = 100;
    public static final int DB_80 = 80;
    public static final String TEST_NAME = "Test Name";
    public static final double RATIO_POINT2 = 0.20;
    public static final String NEW_EQUALIZER_NAME = "New Equalizer Name";
    private Context testContext;
    private EqualizerPreset testEqualizerPreset;
    private HearingTestResult testHearingTestResult;

    @Before
    public void saveControllerTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        HashMap<Integer, Integer> testEqualizerSettings = new HashMap<>();

        testEqualizerSettings.put(BAND1, BANDVAL1);
        testEqualizerSettings.put(BAND2, BANDVAL2);
        testEqualizerSettings.put(BAND3, BANDVAL3);
        testEqualizerSettings.put(BAND4, BANDVAL4);

        Double testLeftRightRatio = RATIO_POINT5;
        String testEqualizerName = TEST_EQUALIZER_NAME;
        testEqualizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        ArrayList<ToneData> toneDataList = new ArrayList<ToneData>();
        for (int i = 0; i < NUM_FREQUENCIES; i ++)
        {
            int frequency = BASE_FREQUENCY * i;
            double dbHL = BASE_DBHL * i;
            double lHeardAtDB = MAX_DB - (BASE_DBHL * i);
            double rHeardAtDB = DB_80 - (BASE_DBHL * i);
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            toneDataList.add(toneData);
        }

        String testTestName = TEST_NAME;

        testHearingTestResult = new HearingTestResult(testTestName, toneDataList);

    }

    @Test
    public void savePresetTest()
    {
        ArrayList<EqualizerPreset> testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNull(testEqualizerPresets);

        SaveController.savePreset(testContext, testEqualizerPreset);

        testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNotNull(testEqualizerPresets);

        SaveController.deletePreset(testContext, testEqualizerPresets.indexOf(testEqualizerPreset));
    }

    @Test
    public void loadPresetTest()
    {
        SaveController.savePreset(testContext, testEqualizerPreset);

        ArrayList<EqualizerPreset> testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNotNull(testEqualizerPresets);

        assertTrue(testEqualizerPresets.contains(testEqualizerPreset));

        SaveController.deletePreset(testContext, testEqualizerPresets.indexOf(testEqualizerPreset));
    }

    @Test
    public void updatePresetTest()
    {
        SaveController.savePreset(testContext, testEqualizerPreset);

        ArrayList<EqualizerPreset> testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNotNull(testEqualizerPresets);

        assertTrue(testEqualizerPresets.contains(testEqualizerPreset));

        HashMap<Integer, Integer> newEqualizerSettings = new HashMap<>();

        newEqualizerSettings.put(BAND1, -BANDVAL1);
        newEqualizerSettings.put(BAND2, -BANDVAL2);
        newEqualizerSettings.put(BAND3, -BANDVAL3);
        newEqualizerSettings.put(BAND4, -BANDVAL4);

        Double testLeftRightRatio = RATIO_POINT2;
        String testEqualizerName = NEW_EQUALIZER_NAME;
        EqualizerPreset newEqualizerPreset = new EqualizerPreset(newEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        int position = EqualizerPresetListController
                .getPresetList(testContext)
                .indexOf(testEqualizerPreset);

        SaveController.updatePreset(testContext, position, newEqualizerPreset);

        testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNotNull(testEqualizerPresets);

        assertFalse(testEqualizerPresets.contains(testEqualizerPreset));

        assertTrue(testEqualizerPresets.contains(newEqualizerPreset));

        assertEquals(newEqualizerPreset, testEqualizerPresets.get(position));

        SaveController.deletePreset(testContext, testEqualizerPresets.indexOf(newEqualizerPreset));
    }

    @Test
    public void deletePresetTest()
    {
        SaveController.savePreset(testContext, testEqualizerPreset);

        ArrayList<EqualizerPreset> testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNotNull(testEqualizerPresets);

        assertTrue(testEqualizerPresets.contains(testEqualizerPreset));

        int position = EqualizerPresetListController
                .getPresetList(testContext)
                .indexOf(testEqualizerPreset);

        SaveController.deletePreset(testContext, position);

        testEqualizerPresets = SaveController.loadPresets(testContext);

        assertNotNull(testEqualizerPresets);



        assertFalse(testEqualizerPresets.contains(testEqualizerPreset));
    }

    @Test
    public void saveResultTest()
    {
        ArrayList<HearingTestResult> testResults = SaveController.loadResults(testContext);

        assertNull(testResults);

        SaveController.saveResult(testContext, testHearingTestResult);

        testResults = SaveController.loadResults(testContext);

        assertNotNull(testResults);

        SaveController.deleteResult(testContext, testHearingTestResult);
    }

    @Test
    public void loadResultTest()
    {
        SaveController.saveResult(testContext, testHearingTestResult);

        ArrayList<HearingTestResult> testResults = SaveController.loadResults(testContext);

        assertNotNull(testResults);

        assertTrue(testResults.contains(testHearingTestResult));

        SaveController.deleteResult(testContext, testHearingTestResult);
    }

    @Test
    public void deleteResultTest()
    {
        SaveController.saveResult(testContext, testHearingTestResult);

        ArrayList<HearingTestResult> testResults = SaveController.loadResults(testContext);

        assertNotNull(testResults);

        assertTrue(testResults.contains(testHearingTestResult));

        SaveController.deleteResult(testContext, testHearingTestResult);

        testResults = SaveController.loadResults(testContext);

        assertNotNull(testResults);

        assertFalse(testResults.contains(testHearingTestResult));
    }
}
