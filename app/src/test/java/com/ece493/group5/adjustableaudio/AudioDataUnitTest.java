package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.AudioData;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AudioDataUnitTest
{

    private static final double DELTA = 0.0001;
    private static final double RATIO_101 = 101.0;
    private static final double RATIO_99_9 = 99.9;
    private static final double RATIO_99 = 99.0;
    private static final double RATIO_1 = 1.0;
    private static final double RATIO_0 = 0.0;
    private static final double RATIO_NEG1 = -1.0;
    private static final double RATIO_POINT5 = 0.5;
    private static final double RATIO_2 = 2.0;
    private static final int PERCENT_50 = 50;
    private static final int PERCENT_0 = 0;
    private static final int PERCENT_1 = 1;
    private static final int PERCENT_100 = 100;
    private static final int PERCENT_101 = 101;
    private static final int PERCENT_NEG1 = -1;
    private static final short BAND0 = (short) 0;
    private static final short BAND1 = (short) 1;
    private static final short BAND2 = (short) 2;
    private static final short BAND3 = (short) 3;
    private static final short BAND4 = (short) 4;
    private static final short BANDVAL0 = (short) -1500;
    private static final short BANDVAL1 = (short) -1000;
    private static final short BANDVAL2 = (short) -500;
    private static final short BANDVAL3 = (short) 0;
    private static final short BANDVAL4 = (short) 500;



    private double testLeftRightVolumeRatio;
    private HashMap<Short, Short> testEqualizerSettings;

    @Before
    public void audioDataTestSetup()
    {
        testLeftRightVolumeRatio = RATIO_1;

        testEqualizerSettings = new HashMap<>();
        testEqualizerSettings.put(BAND0, BANDVAL0);
        testEqualizerSettings.put(BAND1, BANDVAL1);
        testEqualizerSettings.put(BAND2, BANDVAL2);
        testEqualizerSettings.put(BAND3, BANDVAL3);
        testEqualizerSettings.put(BAND4, BANDVAL4);
    }

    @Test
    public void volumeRatioToPercentTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);

        int percent = PERCENT_50;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentOutsideUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_101);

        int percent = PERCENT_0;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_99_9);

        int percent = PERCENT_1;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_0);

        int percent = PERCENT_100;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void volumeRatioToPercentOutsideLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_NEG1);

        int percent = PERCENT_100;

        int testPercent = AudioData.volumeRatioToPercent(audioData.getLeftRightVolumeRatio());

        assertEquals(percent, testPercent);
    }

    @Test
    public void percentToVolumeRatioTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(testLeftRightVolumeRatio);

        int percent = PERCENT_50;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_0);

        int percent = PERCENT_100;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_99);

        int percent = PERCENT_1;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioOutsideUpperBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_0);

        int percent = PERCENT_101;

        double testRatio = AudioData.percentToVolumeRatio(percent);

        assertEquals(audioData.getLeftRightVolumeRatio(), testRatio, DELTA);
    }

    @Test
    public void percentToVolumeRatioOutsideLowerBoundaryTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(PERCENT_100);

        int percent = PERCENT_NEG1;

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
        audioData.setLeftRightVolumeRatio(RATIO_POINT5);

        assertEquals(RATIO_POINT5, audioData.getLeftVolume(), DELTA);
    }

    @Test
    public void getLeftVolumeTestOutside()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_2);

        assertEquals(RATIO_1, audioData.getLeftVolume(), DELTA);
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
        audioData.setLeftRightVolumeRatio(RATIO_2);

        assertEquals(RATIO_POINT5, audioData.getRightVolume(), DELTA);
    }

    @Test
    public void getRightVolumeTestOutside()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_POINT5);

        assertEquals(RATIO_1, audioData.getRightVolume(), DELTA);
    }

    @Test
    public void getEqualizerBandTest()
    {
        AudioData audioData = new AudioData();
        audioData.setLeftRightVolumeRatio(RATIO_POINT5);
        audioData.setEqualizerBand(BAND0, BANDVAL0);
        audioData.setEqualizerBand(BAND1, BANDVAL1);
        audioData.setEqualizerBand(BAND2, BANDVAL2);
        audioData.setEqualizerBand(BAND3, BANDVAL3);
        audioData.setEqualizerBand(BAND4, BANDVAL4);

        short setting = audioData.getEqualizerBand(BAND0);

        assertEquals(BANDVAL0, setting);
    }

}
