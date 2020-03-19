package com.ece493.group5.adjustableaudio.microphone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class MicrophonePlayer
{
    private static final String TAG = MicrophonePlayer.class.getSimpleName();
    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private static final int BUFFER_SIZE = 320;

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private Boolean active;

    private byte[] buffer;

    private Thread worker = new Thread() {
        @Override
        public void run()
        {
            buffer = new byte[BUFFER_SIZE];
            while (isActive())
            {
                audioRecord.read(buffer, 0, BUFFER_SIZE);
                audioTrack.write(buffer, 0, BUFFER_SIZE);
            }
        }
    };

    public MicrophonePlayer()
    {
        active = false;
    }

    void start()
    {
        audioRecord = findAudioRecord();
        audioTrack = findAudioTrack();

        audioRecord.startRecording();
        audioTrack.play();

        worker.start();
    }

    void stop()
    {
        isActive(false);

        audioTrack.stop();
        audioRecord.stop();

        audioTrack = null;
        audioRecord = null;
    }

    protected void isActive(boolean active)
    {
        synchronized (this.active) {
            this.active = active;
        }
    }

    protected boolean isActive()
    {
        synchronized (this.active) {
            return active;
        }
    }

    private static AudioRecord findAudioRecord()
    {
        AudioRecord recorder = null;
        Log.d(TAG, "===== Initializing AudioRecord API =====");
        int buffer = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (buffer != AudioRecord.ERROR_BAD_VALUE)
        {
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, buffer);

            if (recorder.getState() == AudioRecord.STATE_UNINITIALIZED)
            {
                Log.e(TAG, "====== AudioRecord UnInitilaised ====== ");
                return null;
            }
        }

        return recorder;
    }

    private static AudioTrack findAudioTrack()
    {
        AudioTrack track = null;
        Log.d(TAG, "===== Initializing AudioTrack API ====");
        int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize != AudioTrack.ERROR_BAD_VALUE)
        {
            track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    AudioTrack.MODE_STREAM);

            if (track.getState() == AudioTrack.STATE_UNINITIALIZED)
            {
                Log.e(TAG, "===== AudioTrack Uninitialized =====");
                return null;
            }
        }

        return track;
    }
}
