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

    private Context testContext;
    private EqualizerPreset testEqualizerPreset;
    private HearingTestResult testHearingTestResult;

    @Before
    public void saveControllerTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        HashMap<Integer, Integer> testEqualizerSettings = new HashMap<>();

        testEqualizerSettings.put(1, -1000);
        testEqualizerSettings.put(2, -500);
        testEqualizerSettings.put(3, 0);
        testEqualizerSettings.put(4, 500);

        Double testLeftRightRatio = 0.50;
        String testEqualizerName = "Test Equalizer Name";
        testEqualizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

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

        newEqualizerSettings.put(1, -1500);
        newEqualizerSettings.put(2, -700);
        newEqualizerSettings.put(3, 300);
        newEqualizerSettings.put(4, 500);

        Double testLeftRightRatio = 0.20;
        String testEqualizerName = "New Equalizer Name";
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
