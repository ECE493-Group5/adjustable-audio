package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.ece493.group5.adjustableaudio.models.HearingTestModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class HearingTestModelUnitTest
{

    private static final String LEFT_EAR = "L";
    private static final String RIGHT_EAR = "R";
    private static final int NUM_FREQUENCIES = 16;
    private static final double DBHL_MIN = -5;
    private static final double DBHL_INCREMENT = 5;
    private static final double MAX_DB = 100;
    private static final double DELTA = 0.0001;
    private static final double[] REFERENCE_FREQUENCY_DBHL_VALUES = {
            60.0, 37.0, 19.0, 18.0,
            14.6, 11.0, 6.0, 5.5,
            5.5, 4.5, 6.5, 9.5,
            14.8, 17.5, 23.0, 52.5};

    Context testContext;
    HearingTestModel hearingTestModel;
    AudioManager testAudioManager;
    HearingTestModel.AudioFocusChecker testAudioFocusChecker;

    @Before
    public void HearingTestModelTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        testAudioManager = mock(AudioManager.class);

        when(testAudioManager.generateAudioSessionId()).thenReturn(0);

        testAudioFocusChecker = mock(HearingTestModel.AudioFocusChecker.class);

        when(testAudioFocusChecker.requestAudioFocus()).thenReturn(true);

    }

    @Test
    public void hearingTestStateTest()
    {
        hearingTestModel = new HearingTestModel(testContext, testAudioManager);

        hearingTestModel.setAudioFocusChecker(testAudioFocusChecker);

        hearingTestModel.setDisplayDialogs(false);

        hearingTestModel.runTest();

        assertTrue(hearingTestModel.getTestRunning());

        assertEquals(LEFT_EAR, hearingTestModel.getCurrentEar());

        for (int i = 0; i < NUM_FREQUENCIES; i ++)
        {

            assertEquals(i + 1, hearingTestModel.getProgress());
            assertEquals(i, hearingTestModel.getCurrentSound());

            hearingTestModel.onSoundAck(false);

            assertEquals(i + 1, hearingTestModel.getProgress());
            assertEquals(i, hearingTestModel.getCurrentSound());

            hearingTestModel.onSoundAck(true);

            assertNotEquals(i, hearingTestModel.getCurrentSound());
            assertNotEquals(i + 1, hearingTestModel.getProgress());
        }

        assertEquals(RIGHT_EAR, hearingTestModel.getCurrentEar());

        for (int i = 0; i < NUM_FREQUENCIES - 1; i ++)
        {

            assertEquals(i + 1, hearingTestModel.getProgress());
            assertEquals(i, hearingTestModel.getCurrentSound());

            hearingTestModel.onSoundAck(false);

            assertEquals(i + 1, hearingTestModel.getProgress());
            assertEquals(i, hearingTestModel.getCurrentSound());

            hearingTestModel.onSoundAck(true);

            assertNotEquals(i + 1, hearingTestModel.getProgress());
            assertNotEquals(i, hearingTestModel.getCurrentSound());

        }

        hearingTestModel.onSoundAck(true);

        assertEquals(NUM_FREQUENCIES, hearingTestModel.getProgress());
        assertEquals(NUM_FREQUENCIES - 1, hearingTestModel.getCurrentSound());

        assertFalse(hearingTestModel.getTestRunning());
    }


    @Test
    public void hearingTestVolumeMaxTest()
    {
        hearingTestModel = new HearingTestModel(testContext, testAudioManager);

        hearingTestModel.setAudioFocusChecker(testAudioFocusChecker);

        hearingTestModel.setDisplayDialogs(false);

        hearingTestModel.runTest();

        assertTrue(hearingTestModel.getTestRunning());

        assertEquals(LEFT_EAR, hearingTestModel.getCurrentEar());

        assertEquals(DBHL_MIN, hearingTestModel.getDbHLLevel(), DELTA);

        int i = 0;
        double dBSPL = 0;
        double dBHL = 0;
        while (dBSPL < MAX_DB)
        {

            dBHL = DBHL_MIN + (i * DBHL_INCREMENT);

            dBSPL = DBHL_MIN + (i * DBHL_INCREMENT) + REFERENCE_FREQUENCY_DBHL_VALUES[0];

            assertEquals(dBSPL, hearingTestModel.getEffectiveDbLevel(), DELTA);

            assertEquals(dBHL, hearingTestModel.getDbHLLevel(), DELTA);

            hearingTestModel.onSoundAck(false);

            i+=1;

        }

        dBHL = DBHL_MIN;

        dBSPL = DBHL_MIN + REFERENCE_FREQUENCY_DBHL_VALUES[1];

        assertEquals(dBSPL, hearingTestModel.getEffectiveDbLevel(), DELTA);

        assertEquals(dBHL, hearingTestModel.getDbHLLevel(), DELTA);
    }

    @Test
    public void hearingTestVolumeSetTest()
    {
        hearingTestModel = new HearingTestModel(testContext, testAudioManager);

        hearingTestModel.setAudioFocusChecker(testAudioFocusChecker);

        hearingTestModel.setDisplayDialogs(false);

        hearingTestModel.runTest();

        assertTrue(hearingTestModel.getTestRunning());

        assertEquals(LEFT_EAR, hearingTestModel.getCurrentEar());

        assertEquals(DBHL_MIN, hearingTestModel.getDbHLLevel(), DELTA);

        double dBSPL = 0;
        double dBHL = 0;

        for (int i = 0; i < NUM_FREQUENCIES -1; i++)
        {
            for (int j = 0; j < 5; j ++)
            {
                dBHL = DBHL_MIN + (j * DBHL_INCREMENT);

                dBSPL = DBHL_MIN + (j * DBHL_INCREMENT) + REFERENCE_FREQUENCY_DBHL_VALUES[i];

                assertEquals(dBSPL, hearingTestModel.getEffectiveDbLevel(), DELTA);

                assertEquals(dBHL, hearingTestModel.getDbHLLevel(), DELTA);

                hearingTestModel.onSoundAck(false);
            }

            hearingTestModel.onSoundAck(true);

            dBHL = DBHL_MIN;

            dBSPL = DBHL_MIN + REFERENCE_FREQUENCY_DBHL_VALUES[i + 1];

            assertEquals(dBSPL, hearingTestModel.getEffectiveDbLevel(), DELTA);

            assertEquals(dBHL, hearingTestModel.getDbHLLevel(), DELTA);
        }

        hearingTestModel.onSoundAck(true);

        dBSPL = 0;
        dBHL = 0;

        for (int i = 0; i < NUM_FREQUENCIES -1; i++)
        {
            for (int j = 0; j < 5; j ++)
            {
                dBHL = DBHL_MIN + (j * DBHL_INCREMENT);

                dBSPL = DBHL_MIN + (j * DBHL_INCREMENT) + REFERENCE_FREQUENCY_DBHL_VALUES[i];

                assertEquals(dBSPL, hearingTestModel.getEffectiveDbLevel(), DELTA);

                assertEquals(dBHL, hearingTestModel.getDbHLLevel(), DELTA);

                hearingTestModel.onSoundAck(false);
            }

            hearingTestModel.onSoundAck(true);

            dBHL = DBHL_MIN;

            dBSPL = DBHL_MIN + REFERENCE_FREQUENCY_DBHL_VALUES[i + 1];

            assertEquals(dBSPL, hearingTestModel.getEffectiveDbLevel(), DELTA);

            assertEquals(dBHL, hearingTestModel.getDbHLLevel(), DELTA);
        }

    }

}
