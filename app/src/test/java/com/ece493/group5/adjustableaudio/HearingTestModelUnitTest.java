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
    public void hearingTestTest()
    {
        hearingTestModel = new HearingTestModel(testContext, testAudioManager);

        hearingTestModel.setAudioFocusChecker(testAudioFocusChecker);

        hearingTestModel.setDisplayDialogs(false);

        hearingTestModel.runTest();

        assertTrue(hearingTestModel.getTestRunning());

        assertEquals("L", hearingTestModel.getCurrentEar());

        for (int i = 0; i < 16; i ++)
        {

            int progress = hearingTestModel.getProgress();
            int currentSound = hearingTestModel.getCurrentSound();

            assertEquals(i + 1, progress);
            assertEquals(i, currentSound);

            hearingTestModel.onSoundAck(false);

            assertEquals(i + 1, progress);
            assertEquals(i, currentSound);

            hearingTestModel.onSoundAck(true);

            progress = hearingTestModel.getProgress();
            currentSound = hearingTestModel.getCurrentSound();

            assertNotEquals(i, currentSound);
            assertNotEquals(i + 1, progress);
        }

        assertEquals("R", hearingTestModel.getCurrentEar());

        for (int i = 0; i < 15; i ++)
        {

            int progress = hearingTestModel.getProgress();
            int currentSound = hearingTestModel.getCurrentSound();

            assertEquals(i + 1, progress);
            assertEquals(i, currentSound);

            hearingTestModel.onSoundAck(false);

            assertEquals(i + 1, progress);
            assertEquals(i, currentSound);

            hearingTestModel.onSoundAck(true);

            progress = hearingTestModel.getProgress();
            currentSound = hearingTestModel.getCurrentSound();


            assertNotEquals(i + 1, progress);
            assertNotEquals(i, currentSound);

        }

        hearingTestModel.onSoundAck(true);

        assertEquals(16, hearingTestModel.getProgress());
        assertNotEquals(15, hearingTestModel.getCurrentSound());

        assertFalse(hearingTestModel.getTestRunning());
    }


}
