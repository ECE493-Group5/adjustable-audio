package com.ece493.group5.adjustableaudio;


import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

import com.ece493.group5.adjustableaudio.models.ToneGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ToneGeneratorUnitTest
{

    private static final int SAMPLE_RATE = 44100;
    private static final int NEW_AUDIO_SESSION = 0;
    public static final int MS_3000 = 3000;
    public static final int FREQUENCY_500 = 500;
    public static final float VOLUME_GAIN_POINT_025 = 0.025f;
    public static final String LEFT_EAR = "L";
    public static final int VALUE_2 = 2;
    public static final int MS_PER_SECOND = 1000;

    Context testContext;
    ToneGenerator toneGenerator;
    AudioManager audioManager;

    @Before
    public void toneGeneratorTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        audioManager = mock(AudioManager.class);

        when(audioManager.generateAudioSessionId()).thenReturn(NEW_AUDIO_SESSION);

    }

    @Test
    public void generateTrackTest()
    {
        toneGenerator = new ToneGenerator(audioManager);

        int testDurationMs = MS_3000;

        AudioTrack testTrack = toneGenerator.generateTrack(testDurationMs);

        int sampleRate = testTrack.getSampleRate();

        assertEquals(SAMPLE_RATE, sampleRate);
    }

    @Test
    public void generateToneTest()
    {
        toneGenerator = new ToneGenerator(audioManager);

        short[] samples = toneGenerator.generateTone(
                FREQUENCY_500, MS_3000, VOLUME_GAIN_POINT_025, LEFT_EAR);

        int samplesLength = SAMPLE_RATE * VALUE_2 * (MS_3000/ MS_PER_SECOND);

        assertEquals(samplesLength, samples.length);
    }
}
