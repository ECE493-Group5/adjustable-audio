package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.os.Build;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.storage.EqualizerPresetListController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class EqualizerPresetListControllerUnitTest
{

    private static final int BAND0 = 0;
    private static final int BAND1 = 1;
    private static final int BAND2 = 2;
    private static final int BAND3 = 3;
    private static final int BAND4 = 4;
    private static final int BANDVAL0 = -1500;
    private static final int BANDVAL1 = -1000;
    private static final int BANDVAL2 = -500;
    private static final int BANDVAL3 = 0;
    private static final int BANDVAL4 = 500;
    private static final double RATIO_POINT5 = 0.50;
    private static final String TEST_EQUALIZER_NAME = "Test Equalizer Name";
    private static final int MB_100 = 100;
    private static final double RATIO_POINT6 = 0.60;
    private static final String NEW_EQUALIZER_NAME = "New Equalizer Name";

    private Context testContext;
    private ArrayList<EqualizerPreset> testPresetList;
    private EqualizerPreset testEqualizerPreset;

    @Before
    public void preSetListControllerTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        HashMap<Integer, Integer> testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put(BAND0, BANDVAL0);
        testEqualizerSettings.put(BAND1, BANDVAL1);
        testEqualizerSettings.put(BAND2, BANDVAL2);
        testEqualizerSettings.put(BAND3, BANDVAL3);
        testEqualizerSettings.put(BAND4, BANDVAL4);

        Double testLeftRightRatio = RATIO_POINT5;
        String testEqualizerName = TEST_EQUALIZER_NAME;
        testEqualizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);
    }

    @Test
    public void testAdd()
    {

        assertFalse(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(testEqualizerPreset));

        EqualizerPresetListController.add(testContext, testEqualizerPreset);

        assertTrue(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(testEqualizerPreset));

        int position = EqualizerPresetListController
                .getPresetList(testContext)
                .indexOf(testEqualizerPreset);

        EqualizerPresetListController.remove(testContext, position);
    }

    @Test
    public void testRemove()
    {

        EqualizerPresetListController.add(testContext, testEqualizerPreset);

        assertTrue(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(testEqualizerPreset));

        int position = EqualizerPresetListController
                .getPresetList(testContext)
                .indexOf(testEqualizerPreset);

        EqualizerPresetListController.remove(testContext, position);

        assertFalse(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(testEqualizerPreset));
    }

    @Test
    public void testUpdate()
    {
        EqualizerPresetListController.add(testContext, testEqualizerPreset);

        assertTrue(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(testEqualizerPreset));

        HashMap<Integer, Integer> newEqualizerSettings = new HashMap<>();
        newEqualizerSettings.put(BAND0, BANDVAL0 - MB_100);
        newEqualizerSettings.put(BAND1, BANDVAL1 + MB_100);
        newEqualizerSettings.put(BAND2, BANDVAL2 - MB_100);
        newEqualizerSettings.put(BAND3, BANDVAL3 + MB_100);
        newEqualizerSettings.put(BAND4, BANDVAL4 - MB_100);

        Double newLeftRightRatio = RATIO_POINT6;
        String newEqualizerName = NEW_EQUALIZER_NAME;
        EqualizerPreset newEqualizerPreset = new EqualizerPreset(newEqualizerSettings,
                newLeftRightRatio, newEqualizerName);

        int position = EqualizerPresetListController
                .getPresetList(testContext)
                .indexOf(testEqualizerPreset);

        assertNotEquals(EqualizerPresetListController
                .getPresetList(testContext)
                .get(position)
                , newEqualizerPreset);

        EqualizerPresetListController.update(testContext, position, newEqualizerPreset);

        assertFalse(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(testEqualizerPreset));

        assertTrue(EqualizerPresetListController
                .getPresetList(testContext)
                .contains(newEqualizerPreset));

        assertEquals(EqualizerPresetListController
                        .getPresetList(testContext)
                        .get(position)
                        , newEqualizerPreset);

        position = EqualizerPresetListController
                .getPresetList(testContext)
                .indexOf(newEqualizerPreset);

        EqualizerPresetListController.remove(testContext, position);
    }

}

