package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.os.Build;

import com.ece493.group5.adjustableaudio.storage.Saver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class SaverUnitTest
{

    public static final String PRESETS_LIST_STRING = "List of presets";
    public static final String RESULTS_LIST_STRING = "List of Results";
    Context testContext;
    String testPresetListString;
    String testResultListString;

    @Before
    public void saverTestSetup()
    {
        testContext = ApplicationProvider.getApplicationContext();

        testPresetListString = PRESETS_LIST_STRING;
        testResultListString = RESULTS_LIST_STRING;
    }

    @Test
    public void savePresetsTest()
    {
        String loadedPresets = Saver.loadPresets(testContext);

        assertNull(loadedPresets);

        Saver.savePreset(testContext, testPresetListString);

        String newLoadedPresets = Saver.loadPresets(testContext);

        assertNotNull(newLoadedPresets);
    }

    @Test
    public void loadPresetsTest()
    {
        Saver.savePreset(testContext, testPresetListString);

        String newLoadedPresets = Saver.loadPresets(testContext);

        assertNotNull(newLoadedPresets);
        assertEquals(testPresetListString, newLoadedPresets);
    }

    @Test
    public void saveResultsTest()
    {
        String loadedResults = Saver.loadResults(testContext);

        assertNull(loadedResults);

        Saver.saveResult(testContext, testResultListString);

        String newLoadedResults = Saver.loadResults(testContext);

        assertNotNull(newLoadedResults);
    }

    @Test
    public void loadResultsTest()
    {
        Saver.saveResult(testContext, testResultListString);

        String newLoadedResults = Saver.loadResults(testContext);

        assertNotNull(newLoadedResults);
        assertEquals(testResultListString, newLoadedResults);
    }
}
