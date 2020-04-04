package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.AudioData;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AudioDataUnitTest
{

    private static final double DELTA = 0.0001;

    private double testLeftRightVolumeRatio;
    private HashMap<Short, Short> testEqualizerSettings;

    @Before
    public void audioDataTestSetup()
    {
        testLeftRightVolumeRatio = 1.0;

        testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put((short)0, (short)-1500);
        testEqualizerSettings.put((short)1, (short)-1000);
        testEqualizerSettings.put((short)2, (short)-500);
        testEqualizerSettings.put((short)3, (short)0);
        testEqualizerSettings.put((short)4, (short)500);
    }

    @Test
    public void volumeRatioToPercentTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 50;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentOutsideUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(101.0);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 0;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(99.9);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 1;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 100;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentOutsideLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(-1.0);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 100;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void percentToVolumeRatioTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 50;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 100;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(99.0);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 1;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioOutsideUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = 101;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioOutsideLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(100);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        int percent = -1;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }




}
