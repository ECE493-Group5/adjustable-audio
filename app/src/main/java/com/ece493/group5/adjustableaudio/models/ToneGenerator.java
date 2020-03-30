package com.ece493.group5.adjustableaudio.models;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class ToneGenerator
{

    private static final int SAMPLE_RATE = 44100;
    private static final int VALUE_2 = 2;
    private static final double MS_PER_SECOND = 1000.0;
    private static final int MAX_AMPLITUDE = 0x7FFF;
    private static final int MUTE = 0;
    private static final int BITS_PER_BYTE= 8;

    private static final String LEFT_EAR = "L";
    private static final String RIGHT_EAR = "R";

    private AudioFormat audioFormat;
    private AudioAttributes audioAttributes;
    private AudioManager audioManager;

    public ToneGenerator(AudioManager audioManager)
    {
        this.audioManager = audioManager;
        audioFormat = new AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                .setSampleRate(SAMPLE_RATE)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build();

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

    }

    public AudioTrack generateTone(double freq, int durationMs, float volumeGain, String ear)
    {
        int sessionID = audioManager.generateAudioSessionId();
        short[] samples = generateSamples(freq, durationMs, volumeGain, ear);
        int bufferSize = samples.length;
        AudioTrack audioTrack = new AudioTrack(audioAttributes, audioFormat,
                samples.length * (Short.SIZE/BITS_PER_BYTE),
                AudioTrack.MODE_STREAM, sessionID);
        return audioTrack;
    }

    private short[] generateSamples(double freq, int durationMs, float volumeGain, String ear)
    {
        int count = (int)(SAMPLE_RATE * (double)VALUE_2 * (durationMs / MS_PER_SECOND)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += VALUE_2)
        {
            short sample = (short)(Math.sin(VALUE_2 * Math.PI * i / (VALUE_2*SAMPLE_RATE / freq)) *
                    (MAX_AMPLITUDE * volumeGain));
            if (ear.equals(LEFT_EAR))
            {
                samples[i] = sample;
                samples[i + 1] = MUTE;
            }
            else if (ear.equals(RIGHT_EAR))
            {
                samples[i] = MUTE;
                samples[i + 1] = sample;
            }
        }
        return samples;
    }

}
