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

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ToneGeneratorUnitTest
{

    private static final int SAMPLE_RATE = 44100;

    Context testContext;
    ToneGenerator toneGenerator;

    @Before
    public void toneGeneratorTestSetup()
    {

        testContext = ApplicationProvider.getApplicationContext();

        AudioManager audioManager =
                (AudioManager) testContext.getSystemService(Context.AUDIO_SERVICE);

        toneGenerator = new ToneGenerator(audioManager);
    }

//    @Test
//    public void generateTrackTest()
//    {
//        int testDurationMs = 3000;
//
//        AudioTrack testTrack = toneGenerator.generateTrack(testDurationMs);
//
//        int sampleRate = testTrack.getSampleRate();
//
//        assertEquals(SAMPLE_RATE, sampleRate);
//
//
//    }

    @Test
    public void generateToneTest()
    {
        short[] samples = toneGenerator.generateTone(500, 3000, 0.025f, "L");
        int samplesLength = SAMPLE_RATE * 2 * (3000/1000);
        assertEquals(samplesLength, samples.length);
    }
}
