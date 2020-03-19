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

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private Boolean active;

    private byte[] buffer;
    private int bufferSize;

    private Thread worker;

    public MicrophonePlayer()
    {
        active = false;
        reset();
    }

    public void startRecording()
    {
        if (isActive())
            return;

        if (!isReady()) {
            Log.e(TAG, "MicrophonePlayer is not ready!");
            reset();
            return;
        }

        worker = new Thread() {
            @Override
            public void run()
            {
                buffer = new byte[bufferSize];

                while (isActive())
                {
                    audioRecord.read(buffer, 0, bufferSize);
                    for (byte b: buffer) {
                        if (b != 0)
                            Log.d(TAG, "" + b);
                    }
                    audioTrack.write(buffer, 0, bufferSize);
                }

                buffer = null;
            }
        };

        Log.d(TAG, "Started recording.");
        isActive(true);
        audioRecord.startRecording();
        audioTrack.play();
        worker.start();
    }

    public void stopRecording()
    {
        if (!isActive())
            return;

        Log.d(TAG, "Stopped recording.");
        isActive(false);

        audioTrack.stop();
        audioRecord.stop();

        worker = null;
    }

    public boolean isReady()
    {
        return audioRecord != null && audioTrack !=null;
    }

    public void reset()
    {
        if (audioRecord != null)
            audioRecord.release();

        if (audioTrack != null)
            audioTrack.release();

        audioRecord = findAudioRecord();
        audioTrack = findAudioTrack();
    }

    public void release()
    {
        audioTrack.release();
        audioRecord.release();

        audioTrack = null;
        audioRecord = null;
    }

    public void isActive(boolean active)
    {
        synchronized (this.active) {
            this.active = active;
        }
    }

    public boolean isActive()
    {
        synchronized (this.active) {
            return active;
        }
    }

    private AudioRecord findAudioRecord()
    {
        AudioRecord recorder = null;

        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize != AudioRecord.ERROR_BAD_VALUE)
        {
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize);

            if (recorder.getState() == AudioRecord.STATE_UNINITIALIZED)
                recorder = null;
        }

        return recorder;
    }

    private AudioTrack findAudioTrack()
    {
        AudioTrack track = null;

        bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (bufferSize != AudioTrack.ERROR_BAD_VALUE)
        {
            track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    AudioTrack.MODE_STREAM);

            if (track.getState() == AudioTrack.STATE_UNINITIALIZED)
                track = null;
        }

        return track;
    }
}
