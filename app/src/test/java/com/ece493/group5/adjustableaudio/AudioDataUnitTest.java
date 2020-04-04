package com.ece493.group5.adjustableaudio;

import android.os.Parcel;

import com.ece493.group5.adjustableaudio.models.AudioData;

import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;
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

        int percent = 50;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentOutsideUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(101.0);

        int percent = 0;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(99.9);

        int percent = 1;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0);

        int percent = 100;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentOutsideLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(-1.0);

        int percent = 100;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void percentToVolumeRatioTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);

        int percent = 50;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0);

        int percent = 100;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(99.0);

        int percent = 1;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioOutsideUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0);

        int percent = 101;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioOutsideLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(100);

        int percent = -1;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void getLeftVolumeTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);

        assertEquals(testLeftRightVolumeRatio, audioData.getLeftVolume(), DELTA);
    }

    @Test
    public void getLeftVolumeTestInside()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0.5);

        assertEquals(0.5, audioData.getLeftVolume(), DELTA);
    }

    @Test
    public void getLeftVolumeTestOutside()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(2);

        assertEquals(1, audioData.getLeftVolume(), DELTA);
    }

    @Test
    public void getRightVolumeTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);

        assertEquals(testLeftRightVolumeRatio, audioData.getRightVolume(), DELTA);
    }

    @Test
    public void getRightVolumeTestInside()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(2);

        assertEquals(0.5, audioData.getRightVolume(), DELTA);
    }

    @Test
    public void getRightVolumeTestOutside()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0.5);

        assertEquals(1, audioData.getRightVolume(), DELTA);
    }

    @Test
    public void getEqualizerBandTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(0.5);
        audioData.setEqualizerBand((short)0, (short)-1500);
        audioData.setEqualizerBand((short)1, (short)-1000);
        audioData.setEqualizerBand((short)2, (short)-500);
        audioData.setEqualizerBand((short)3, (short)0);
        audioData.setEqualizerBand((short)4, (short)500);

        short setting = audioData.getEqualizerBand((short) 0);

        assertEquals((short) -1500, setting);
    }

}
