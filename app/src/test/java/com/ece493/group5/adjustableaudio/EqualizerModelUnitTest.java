package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.os.Build;

import com.ece493.group5.adjustableaudio.models.EqualizerModel;
import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class EqualizerModelUnitTest
{
    private static final String NEW_PRESET_NAME = "new preset";
    private static final String NEW_NAME = "a new name";
    private static final int MB_2000 = 2000;
    private static final String DEFAULT_NAME = "Default";
    private static final double defaultLeftRightVolumeRatioSetting = 1;
    private static final Integer firstFrequencyBand = 0;
    private static final Integer secondFrequencyBand = 1;
    private static final Integer thirdFrequencyBand = 2;
    private static final Integer fourthFrequencyBand = 3;
    private static final Integer fifthFrequencyBand = 4;
    private static final Integer lowestMillibelLevel = 0;
    private static final Integer normalMillibelLevel = 300;

    private Context testContext;
    private EqualizerModel equalizerModel;
    private EqualizerPreset defaultPreset;

    @Before
    public void equalizerModelTestSetup()
    {
        testContext = ApplicationProvider.getApplicationContext();

        HashMap<Integer, Integer> defaultEqualizerValues  = new HashMap<>();
        defaultEqualizerValues.put(firstFrequencyBand, normalMillibelLevel);
        defaultEqualizerValues.put(secondFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(thirdFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(fourthFrequencyBand, lowestMillibelLevel);
        defaultEqualizerValues.put(fifthFrequencyBand, normalMillibelLevel);
        defaultPreset = new EqualizerPreset(defaultEqualizerValues,
                defaultLeftRightVolumeRatioSetting, DEFAULT_NAME);
    }

    @Test
    public void getEqualizerPresetsTest()
    {
        equalizerModel = new EqualizerModel(testContext);

        List<EqualizerPreset> equalizerPresets = equalizerModel.getEqualizerPresets();

        assertTrue(equalizerPresets.contains(defaultPreset));
    }

    @Test
    public void addEqualizerSettingTest()
    {
        equalizerModel = new EqualizerModel(testContext);

        int position = equalizerModel.getCurrentEqualizerSettingPosition();

        equalizerModel.addEqualizerSetting(testContext, NEW_PRESET_NAME);

        assertNotEquals(position, equalizerModel.getCurrentEqualizerSettingPosition());

        assertNotNull(equalizerModel.getCurrentEqualizerName());

        assertEquals(NEW_PRESET_NAME, equalizerModel.getCurrentEqualizerName());
    }

    @Test
    public void deleteEqualizerSettingTest()
    {
        equalizerModel = new EqualizerModel(testContext);

        int position = equalizerModel.getCurrentEqualizerSettingPosition();

        equalizerModel.addEqualizerSetting(testContext, NEW_PRESET_NAME);

        assertNotEquals(position, equalizerModel.getCurrentEqualizerSettingPosition());

        assertNotNull(equalizerModel.getCurrentEqualizerName());

        assertEquals(NEW_PRESET_NAME, equalizerModel.getCurrentEqualizerName());

        equalizerModel.deleteEqualizerSetting(testContext,
                equalizerModel.getCurrentEqualizerSettingPosition());

        assertEquals(position, equalizerModel.getCurrentEqualizerSettingPosition());

        assertEquals(DEFAULT_NAME, equalizerModel.getCurrentEqualizerName());
    }

    @Test
    public void renameEqualizerSettingTest()
    {
        equalizerModel = new EqualizerModel(testContext);

        int position = equalizerModel.getCurrentEqualizerSettingPosition();

        equalizerModel.addEqualizerSetting(testContext, NEW_PRESET_NAME);

        assertNotEquals(position, equalizerModel.getCurrentEqualizerSettingPosition());

        assertNotNull(equalizerModel.getCurrentEqualizerName());

        assertEquals(NEW_PRESET_NAME, equalizerModel.getCurrentEqualizerName());

        equalizerModel.renameEqualizerSetting(testContext, NEW_NAME);

        equalizerModel.switchEqualizerSetting(equalizerModel.getCurrentEqualizerSettingPosition());

        assertNotNull(equalizerModel.getCurrentEqualizerName());

        assertNotEquals(NEW_PRESET_NAME, equalizerModel.getCurrentEqualizerName());

        assertEquals(NEW_NAME, equalizerModel.getCurrentEqualizerName());
    }

    @Test
    public void revertEqualizerChanges()
    {
        equalizerModel = new EqualizerModel(testContext);

        equalizerModel.addEqualizerSetting(testContext, NEW_PRESET_NAME);

        equalizerModel.setFrequencyBand(firstFrequencyBand, MB_2000);

        equalizerModel.setFrequencyBand(secondFrequencyBand, MB_2000);

        assertEquals((Integer)MB_2000, equalizerModel.getCurrentEqualizerBandValues().get(firstFrequencyBand));

        assertEquals((Integer)MB_2000, equalizerModel.getCurrentEqualizerBandValues().get(firstFrequencyBand));

        equalizerModel.revertEqualizerChanges();

        assertNotEquals((Integer)MB_2000, equalizerModel.getCurrentEqualizerBandValues().get(firstFrequencyBand));

        assertNotEquals((Integer)MB_2000, equalizerModel.getCurrentEqualizerBandValues().get(firstFrequencyBand));
    }

}
