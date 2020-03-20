package com.ece493.group5.adjustableaudio.microphone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;
import com.ece493.group5.adjustableaudio.models.AudioData;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;

public class MicrophonePlayer
    implements IAudioDevice
{
    private static final String TAG = MicrophonePlayer.class.getSimpleName();
    private static final int SAMPLE_RATE_IN_HZ = 44100;

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;

    private final AudioData audioData = new AudioData();
    private final MicrophoneData microphoneData = new MicrophoneData();

    private byte[] buffer;
    private int bufferSize;

    private Thread worker;

    public MicrophonePlayer()
    {
        reset();
    }

    public void startRecording()
    {
        if (isRecording())
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

                while (isRecording())
                {
                    int read = audioRecord.read(buffer, 0, bufferSize);
                    if (read > 0)
                    {
                        for (int i = 0; i < read; i += 2)
                        {
                            buffer[i] = computeLeftBalance(buffer[i]);
                            buffer[i + 1] = computeRightBalance(buffer[i + 1]);
                        }

                        audioTrack.write(buffer, 0, read);
                    }
                }

                buffer = null;
            }
        };

        Log.d(TAG, "Started recording.");
        isRecording(true);
        audioRecord.startRecording();
        audioTrack.play();
        worker.start();
    }

    public void stopRecording()
    {
        if (!isRecording())
            return;

        Log.d(TAG, "Stopped recording.");
        isRecording(false);

        audioTrack.stop();
        audioRecord.stop();

        worker = null;
    }

    public void toggleRecording()
    {
        if (isRecording())
            stopRecording();
        else
            startRecording();
    }

    public MicrophoneData getMicrophoneData()
    {
        return microphoneData;
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

    public synchronized void isRecording(boolean recording)
    {
        microphoneData.setIsRecording(recording);

        if (microphoneData.isRecordingChanged()) {
            microphoneData.notifyObservers();
            microphoneData.clearAllChanges();
        }
    }

    public synchronized boolean isRecording()
    {
        return microphoneData.getIsRecording();
    }

    private byte computeLeftBalance(byte data)
    {
        synchronized (audioData) {
            data *= audioData.getLeftVolume();
        }

        return data;
    }

    private byte computeRightBalance(byte data)
    {
        synchronized (audioData) {
            data *= audioData.getRightVolume();
        }

        return data;
    }

    private AudioRecord findAudioRecord()
    {
        AudioRecord recorder = null;

        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_8BIT);

        if (bufferSize != AudioRecord.ERROR_BAD_VALUE)
        {
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_8BIT, bufferSize);

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
                AudioFormat.ENCODING_PCM_8BIT);

        if (bufferSize != AudioTrack.ERROR_BAD_VALUE)
        {
            track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_8BIT, bufferSize,
                    AudioTrack.PERFORMANCE_MODE_LOW_LATENCY);

            if (track.getState() == AudioTrack.STATE_UNINITIALIZED)
                track = null;
        }

        return track;
    }

    @Override
    public void setLeftVolume(double percent)
    {
        synchronized (audioData) {
            audioData.setLeftVolume(percent);
        }
    }

    @Override
    public void setRightVolume(double percent)
    {
        synchronized (audioData) {
            audioData.setRightVolume(percent);
        }
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        synchronized (audioData) {
            audioData.setEqualizerBand(band, level);
        }
    }

    @Override
    public void enableEqualizer()
    {

    }

    @Override
    public void disableEqualizer()
    {

    }
}
