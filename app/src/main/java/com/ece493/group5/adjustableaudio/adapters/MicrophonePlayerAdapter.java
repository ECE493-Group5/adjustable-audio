package com.ece493.group5.adjustableaudio.adapters;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.Equalizer;
import android.media.audiofx.NoiseSuppressor;
import android.os.CountDownTimer;
import android.util.Log;

import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;
import com.ece493.group5.adjustableaudio.models.AudioData;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;

import java.util.Map;

public class MicrophonePlayerAdapter
    implements IAudioDevice
{
    private static final String TAG = MicrophonePlayerAdapter.class.getSimpleName();

    private static final int SAMPLE_RATE = 44100;
    private static final int RECORD_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    private static final int TRACK_CHANNEL = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int ZERO_CROSSING_UPPER_THRESHOLD = 300;
    private static final int ZERO_CROSSING_LOWER_THRESHOLD = 200;
    private static final int SPEECH_ACTIVE_TIMEOUT = 1000;

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;

    private Context context;
    private Equalizer equalizer;
    private NoiseSuppressor noiseSuppressor;
    private AcousticEchoCanceler echoCanceler;
    private AutomaticGainControl automaticGainControl;

    private final AudioData audioData = new AudioData();
    private final MicrophoneData microphoneData = new MicrophoneData();
    private boolean speechActive;

    private short[] buffer;
    private int recordBufferSize;
    private int trackBufferSize;
    private int previousMode;

    private Thread worker;

    public MicrophonePlayerAdapter(Context c)
    {
        context = c;

        reset();

        Log.w(TAG, "NoiseSuppressor support: " + NoiseSuppressor.isAvailable());
        Log.w(TAG, "AcousticEchoCanceler support: " + AcousticEchoCanceler.isAvailable());
        Log.w(TAG, "AutomaticGainControl support: " + AutomaticGainControl.isAvailable());
    }

    public synchronized void startRecording()
    {
        if (isRecording())
            return;

        if (!isReady()) {
            Log.e(TAG, "MicrophonePlayer is not ready!");
            reset();
            startRecording(); // try to start recording again.
            return;
        }

        worker = new Thread() {
            @Override
            public void run()
            {
                buffer = new short[Math.min(trackBufferSize, recordBufferSize)];
                speechActive = false;
                int zeroCrossingRate = (ZERO_CROSSING_UPPER_THRESHOLD + ZERO_CROSSING_LOWER_THRESHOLD) / 2;
                long speechActiveTime = 0;

                while (isRecording())
                {
                    int read = audioRecord.read(buffer, 0, buffer.length);

                    if (isSpeechFocusEnabled())
                     zeroCrossingRate = calculateZeroCrossings(buffer);

                    for (int i = 0; i < read; i += 2)
                    {
                        buffer[i] = computeLeftBalance(buffer[i]);
                        buffer[i + 1] = computeRightBalance(buffer[i + 1]);
                    }

                    if (!isSpeechFocusEnabled())
                    {
                        audioTrack.write(buffer, 0, read);
                    }
                    else if (speechActive || isValidZeroCrossing(zeroCrossingRate))
                    {
                        if (!speechActive)
                        {
                            speechActive = true;
                            speechActiveTime = System.currentTimeMillis();
                        }

                        audioTrack.write(buffer, 0, read);

                        if (!isValidZeroCrossing(zeroCrossingRate) && System.currentTimeMillis() - speechActiveTime > SPEECH_ACTIVE_TIMEOUT)
                        {
                            speechActive = false;
                        }
                        else if (isValidZeroCrossing(zeroCrossingRate))
                        {
                            speechActive = true;
                            speechActiveTime = System.currentTimeMillis();
                        }
                    }
                }

                buffer = null;
            }
        };

        isRecording(true);

        previousMode = getAudioManager().getMode();
        getAudioManager().setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioRecord.startRecording();
        audioTrack.play();
        worker.start();
    }

    public synchronized void stopRecording()
    {
        if (!isRecording())
            return;

        isRecording(false);

        audioTrack.stop();
        audioRecord.stop();

        worker = null;

        disableEqualizer();
        disableNoiseFilter();
        getAudioManager().setMode(previousMode);
    }

    private int calculateZeroCrossings(short[] buffer)
    {
        int count = 0;
        for (int i = 0; i < buffer.length - 2; i += 2)
        {
            if (buffer[i] * buffer[i + 2] < 0)
                count++;
        }
        return count;
    }

    public synchronized void toggleRecording()
    {
        if (isRecording())
            stopRecording();
        else
            startRecording();
    }

    private static boolean isValidZeroCrossing(int zeroCrossingRate)
    {
        return !(ZERO_CROSSING_LOWER_THRESHOLD <= zeroCrossingRate && zeroCrossingRate <= ZERO_CROSSING_UPPER_THRESHOLD);
    }

    private AudioManager getAudioManager()
    {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public MicrophoneData getMicrophoneData()
    {
        return microphoneData;
    }

    private boolean isReady()
    {
        return audioRecord != null && audioTrack !=null;
    }

    private void reset()
    {
        if (audioRecord != null)
            audioRecord.release();

        if (audioTrack != null)
            audioTrack.release();

        audioRecord = findAudioRecord();
        if (audioRecord == null)
            return;

        audioTrack = findAudioTrack();
        if (audioTrack == null)
            return;

        enableEqualizer();
    }

    public void release()
    {
        audioTrack.release();
        audioRecord.release();

        audioTrack = null;
        audioRecord = null;
    }

    private synchronized void isRecording(boolean recording)
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

    private short computeLeftBalance(short data)
    {
        synchronized (audioData) {
            data *= audioData.getLeftVolume();
        }

        return data;
    }

    private short computeRightBalance(short data)
    {
        synchronized (audioData) {
            data *= audioData.getRightVolume();
        }

        return data;
    }

    private AudioRecord findAudioRecord()
    {
        recordBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, RECORD_CHANNEL, ENCODING);

        if (recordBufferSize != AudioRecord.ERROR_BAD_VALUE)
        {
            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                    SAMPLE_RATE, RECORD_CHANNEL, ENCODING, recordBufferSize);

            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                return recorder;
        }

        return null;
    }

    private AudioTrack findAudioTrack()
    {
        trackBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, TRACK_CHANNEL, ENCODING);

        if (trackBufferSize != AudioTrack.ERROR_BAD_VALUE) {
            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                    TRACK_CHANNEL, ENCODING, trackBufferSize,
                    AudioTrack.MODE_STREAM, audioRecord.getAudioSessionId());

            if (track.getState() == AudioRecord.STATE_INITIALIZED)
                return track;
        }

        return null;
    }

    @Override
    public void setLeftRightVolumeRatio(double ratio)
    {
        synchronized (audioData) {
            audioData.setLeftRightVolumeRatio(ratio);
        }
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        synchronized (audioData) {
            audioData.setEqualizerBand(band, level);

            if (audioData.equalizerBandChanged()) {
                equalizer.setBandLevel(band, level);
                audioData.clearAllChanges();
            }
        }
    }

    @Override
    public void enableEqualizer()
    {
        try {
            equalizer = new Equalizer(0, audioTrack.getAudioSessionId());
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize the Equalizer.");
            return;
        }

        //Initialized to normal equalizer preset
        equalizer.usePreset((short)0);
        equalizer.setEnabled(true);

        for (Map.Entry<Short, Short> entry: audioData.getEqualizerSettings())
            equalizer.setBandLevel(entry.getKey(), entry.getValue());
    }

    @Override
    public void disableEqualizer()
    {
        if (equalizer != null) {
            equalizer.release();
            equalizer = null;
        }
    }

    public void setMode(int mode)
    {
        // disable currently mode
        switch (microphoneData.getMode())
        {
            case MicrophoneData.MODE_NORMAL:
                break;
            case MicrophoneData.MODE_NOISE_SUPPRESSION:
                disableNoiseFilter();
                break;
            case MicrophoneData.MODE_SPEECH_FOCUS:
                break;
        }

        microphoneData.setMode(mode);

        // enable new mode
        switch (microphoneData.getMode())
        {
            case MicrophoneData.MODE_NORMAL:
                break;
            case MicrophoneData.MODE_NOISE_SUPPRESSION:
                enableNoiseFilter();
                break;
            case MicrophoneData.MODE_SPEECH_FOCUS:
                break;
        }

        if (microphoneData.modeChanged())
        {
            microphoneData.notifyObservers();
            microphoneData.clearAllChanges();
        }
    }

    private void enableNoiseFilter()
    {
        if (!isRecording())
            return;

        noiseSuppressor = NoiseSuppressor.create(audioRecord.getAudioSessionId());
        if (noiseSuppressor != null)
            noiseSuppressor.setEnabled(true);

        echoCanceler = AcousticEchoCanceler.create(audioRecord.getAudioSessionId());
        if (echoCanceler != null)
            echoCanceler.setEnabled(true);

        automaticGainControl = AutomaticGainControl.create(audioRecord.getAudioSessionId());
        if (automaticGainControl != null)
            automaticGainControl.setEnabled(true);
    }

    private void disableNoiseFilter()
    {
        if (noiseSuppressor != null) {
            noiseSuppressor.release();
            noiseSuppressor = null;
        }

        if (echoCanceler != null) {
            echoCanceler.release();
            echoCanceler = null;
        }

        if (automaticGainControl != null) {
            automaticGainControl.release();
            automaticGainControl = null;
        }
    }

    private boolean isSpeechFocusEnabled()
    {
        synchronized (microphoneData) {
            return microphoneData.getMode() == MicrophoneData.MODE_SPEECH_FOCUS;
        }
    }
}
