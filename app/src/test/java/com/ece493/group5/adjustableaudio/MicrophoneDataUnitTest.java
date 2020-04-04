package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.MicrophoneData;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class MicrophoneDataUnitTest
{

    public static final int MODE_NORMAL = 1;
    public static final int MODE_NOISE_SUPPRESSION = 2;
    public static final int MODE_SPEECH_FOCUS = 3;

    @Test
    public void microphoneDataConstructorTest()
    {
        MicrophoneData microphoneData = new MicrophoneData();

        assertFalse(microphoneData.getIsRecording());

        assertEquals(MODE_NORMAL, microphoneData.getMode());
    }

    @Test
    public void setModeTest()
    {
        MicrophoneData microphoneData = new MicrophoneData();

        assertNotEquals(MODE_NOISE_SUPPRESSION, microphoneData.getMode());

        microphoneData.setMode(MODE_NOISE_SUPPRESSION);

        assertEquals(MODE_NOISE_SUPPRESSION, microphoneData.getMode());
    }

    @Test
    public void setIsRecordingTest()
    {
        MicrophoneData microphoneData = new MicrophoneData();

        assertFalse(microphoneData.getIsRecording());

        microphoneData.setIsRecording(true);

        assertTrue(microphoneData.getIsRecording());
    }

    @Test
    public void isRecordingChangedTest()
    {
        MicrophoneData microphoneData = new MicrophoneData();

        assertFalse(microphoneData.isRecordingChanged());

        microphoneData.setIsRecording(true);

        assertTrue(microphoneData.isRecordingChanged());
    }

    @Test
    public void modeChangedTest()
    {
        MicrophoneData microphoneData = new MicrophoneData();

        assertFalse(microphoneData.modeChanged());

        microphoneData.setMode(MODE_SPEECH_FOCUS);

        assertTrue(microphoneData.modeChanged());
    }

}
