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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class HearingTestModelUnitTest
{

    Context testContext;
    HearingTestModel hearingTestModel;
    AudioManager testAudioManager;

    @Before
    public void HearingTestModelTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        testAudioManager = mock(AudioManager.class);

        when(testAudioManager.generateAudioSessionId()).thenReturn(0);

        hearingTestModel = new HearingTestModel(testContext, testAudioManager);

    }

    @Test
    public void hearingTestTest()
    {
        hearingTestModel.runTest();

        assertTrue(hearingTestModel.getTestRunning());

        for (int i = 0; i < 20; i ++)
        {
            hearingTestModel.onSoundAck(true);
        }

        assertEquals("R", hearingTestModel.getCurrentEar());
    }


}
