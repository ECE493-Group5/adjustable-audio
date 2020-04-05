package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotEquals;

public class EqualizerPresetUnitTest
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
    private static final double RATIO_1 = 1.0;
    private static final double RATIO_POINT75 = 0.75;
    private static final String NEW_EQUALIZER_NAME = "New Equalizer Name";

    private HashMap<Integer, Integer> testEqualizerSettings;
    private Double testLeftRightRatio;
    private String testEqualizerName;

    @Before
    public void equalizerPresetTestSetup()
    {
        testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put(BAND0, BANDVAL0);
        testEqualizerSettings.put(BAND1, BANDVAL1);
        testEqualizerSettings.put(BAND2, BANDVAL2);
        testEqualizerSettings.put(BAND3, BANDVAL3);
        testEqualizerSettings.put(BAND4, BANDVAL4);

        testLeftRightRatio = RATIO_POINT5;
        testEqualizerName = TEST_EQUALIZER_NAME;
    }

    @Test
    public void testFirstEqualizerPresetConstructor()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset();

        assertEquals(RATIO_1, equalizerPreset.getLeftRightVolumeRatio());
        assertNull(equalizerPreset.getEqualizerName());
        assertNull(equalizerPreset.getEqualizerSettings());
    }

    @Test
    public void testSecondEqualizerPresetConstructor()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        HashMap<Integer, Integer> equalizerPresetSettings = equalizerPreset.getEqualizerSettings();

        for (Integer key : equalizerPresetSettings.keySet())
        {
            assertNotNull(testEqualizerSettings.get(key));
            assertEquals(testEqualizerSettings.get(key), equalizerPresetSettings.get(key));
        }

        assertEquals(testLeftRightRatio, equalizerPreset.getLeftRightVolumeRatio());
        assertEquals(testEqualizerName, equalizerPreset.getEqualizerName());
    }

    @Test
    public void testThirdEqualizerPresetConstructor()
    {
        EqualizerPreset originalEqualizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        EqualizerPreset copyEqualizerPreset = new EqualizerPreset(originalEqualizerPreset);

        HashMap<Integer, Integer> copyEqualizerPresetSettings =
                copyEqualizerPreset.getEqualizerSettings();

        for (Integer key : copyEqualizerPresetSettings.keySet())
        {
            assertNotNull(testEqualizerSettings.get(key));
            assertEquals(testEqualizerSettings.get(key), copyEqualizerPresetSettings.get(key));
        }

        assertEquals(testLeftRightRatio, copyEqualizerPreset.getLeftRightVolumeRatio());
        assertEquals(testEqualizerName, copyEqualizerPreset.getEqualizerName());
    }

    @Test
    public void testEqualizerSettings()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        HashMap<Integer, Integer> newEqualizerSettings = new HashMap<>();
        newEqualizerSettings.put(BAND0, -1 * BANDVAL0 + 1);
        newEqualizerSettings.put(BAND1, -1 * BANDVAL1 + 1);
        newEqualizerSettings.put(BAND2, -1 * BANDVAL2 + 1);
        newEqualizerSettings.put(BAND3, -1 * BANDVAL3 + 1);
        newEqualizerSettings.put(BAND4, -1 * BANDVAL4 + 1);

        equalizerPreset.setEqualizerSettings(newEqualizerSettings);

        for (Integer key : equalizerPreset.getEqualizerSettings().keySet())
        {
            assertNotNull(newEqualizerSettings.get(key));
            assertNotEquals(testEqualizerSettings.get(key),
                    equalizerPreset.getEqualizerSettings().get(key));
            assertEquals( newEqualizerSettings.get(key),
                    equalizerPreset.getEqualizerSettings().get(key));
        }
    }

    @Test
    public void setTestLeftRightRatio()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        Double newTestLeftRightRatio = RATIO_POINT75;
        equalizerPreset.setLeftRightVolumeRatio(newTestLeftRightRatio);

        assertNotEquals(testLeftRightRatio, equalizerPreset.getLeftRightVolumeRatio());
        assertEquals(newTestLeftRightRatio, equalizerPreset.getLeftRightVolumeRatio());
    }

    @Test
    public void testEqualizerName()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        String newEqualizerName = NEW_EQUALIZER_NAME;
        equalizerPreset.setEqualizerName(newEqualizerName);

        assertNotEquals(testEqualizerName, equalizerPreset.getEqualizerName());
        assertEquals(newEqualizerName, equalizerPreset.getEqualizerName());
    }

}
