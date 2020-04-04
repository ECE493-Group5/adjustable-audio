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
    Context testContext;
    private ArrayList<EqualizerPreset> testPresetList;
    private EqualizerPreset testEqualizerPreset;

    @Before
    public void preSetListControllerTestSetup()
    {
        testContext = ApplicationProvider.getApplicationContext();

        HashMap<Integer, Integer> testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put(0, -1500);
        testEqualizerSettings.put(1, -1000);
        testEqualizerSettings.put(2, -500);
        testEqualizerSettings.put(3, 0);
        testEqualizerSettings.put(4, 500);

        Double testLeftRightRatio = 0.50;
        String testEqualizerName = "Test Equalizer Name";
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
        newEqualizerSettings.put(0, -1600);
        newEqualizerSettings.put(1, -1500);
        newEqualizerSettings.put(2, -400);
        newEqualizerSettings.put(3, 100);
        newEqualizerSettings.put(4, 400);

        Double newLeftRightRatio = 0.60;
        String newEqualizerName = "New Equalizer Name";
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
