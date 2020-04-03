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
    private HashMap<Integer, Integer> testEqualizerSettings;
    private Double testLeftRightRatio;
    private String testEqualizerName;

    @Before
    public void equalizerPresetTestSetup()
    {
        testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put(0, -1500);
        testEqualizerSettings.put(1, -1000);
        testEqualizerSettings.put(2, -500);
        testEqualizerSettings.put(3, 0);
        testEqualizerSettings.put(4, 500);

        testLeftRightRatio = 0.50;
        testEqualizerName = "Test Equalizer Name";
    }

    @Test
    public void testFirstEqualizerPresetConstructor()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset();

        assertEquals(1.0, equalizerPreset.getLeftRightVolumeRatio());
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
        newEqualizerSettings.put(0, 1500);
        newEqualizerSettings.put(1, 1000);
        newEqualizerSettings.put(2, 500);
        newEqualizerSettings.put(3, -500);
        newEqualizerSettings.put(4, -1000);

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

        Double newTestLeftRightRatio = 0.75;
        equalizerPreset.setLeftRightVolumeRatio(newTestLeftRightRatio);

        assertNotEquals(testLeftRightRatio, equalizerPreset.getLeftRightVolumeRatio());
        assertEquals(newTestLeftRightRatio, equalizerPreset.getLeftRightVolumeRatio());
    }

    @Test
    public void testEqualizerName()
    {
        EqualizerPreset equalizerPreset = new EqualizerPreset(testEqualizerSettings,
                testLeftRightRatio, testEqualizerName);

        String newEqualizerName = "New Equalizer Name";
        equalizerPreset.setEqualizerName(newEqualizerName);

        assertNotEquals(testEqualizerName, equalizerPreset.getEqualizerName());
        assertEquals(newEqualizerName, equalizerPreset.getEqualizerName());
    }

}
