package com.ece493.group5.adjustableaudio.models;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.storage.Saver;
import com.ece493.group5.adjustableaudio.views.HearingTestView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class HearingTestModel extends Observable
{
    private static final long BEEP_DURATION = 3000;
    private static final String NUMBER_FREQUENCIES = "16";
    private static final int MAX_DB = 100;
    private static final int[] TONES = {30, 90, 233, 250, 347,
                                        500, 907, 1000, 1353, 2000,
                                        3533, 4000, 5267, 8000, 11333, 15667};
    private static final double[] REFERENCE_FREQUENCY_DBHL_VALUES = {
            60.0, 37.0, 19.0, 18.0,
            14.6, 11.0, 6.0, 5.5,
            5.5, 4.5, 6.5, 9.5,
            14.8, 17.5, 23.0, 52.5};
    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";

    private Context mContext;
    private HearingTestView mView;

    private ArrayList<ToneData> toneDataArrayList;
    private String progress;
    private String soundData;
    private String currentEar;
    private Boolean testRunning;
    private Boolean testFinished;
    private Boolean isPaused;
    private Boolean maxVolumeReached;
    private SoundPool soundPool;
    private AudioManager audioManager;
    private int currentSound;
    private double dbHLLevel;
    private float LVolume;
    private float RVolume;
    private ArrayList<Integer> soundPoolSounds;
    private AudioManager.OnAudioFocusChangeListener onFocusChangeListener;


    public HearingTestModel(Context mContext)
    {
        this.mContext = mContext;
        this.currentEar = "L";
        this.testRunning = false;
        initTest();
        initToneDataArrayList();
        initSoundPool();
        this.audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        this.onFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
            @Override
            public void onAudioFocusChange(int focusChange)
            {
                if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
                {
                    // Your app has been granted audio focus again
                    // Raise volume to normal, restart playback if necessary
                    if (isPaused)
                    {
                        unPauseTest();
                    }

                }
                else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
                {
                    // Permanent loss of audio focus
                    // Pause hearing test
                    audioManager.abandonAudioFocus(this);
                    if (!isPaused)
                    {
                        pauseTest();
                    }

                }
                else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
                {
                    // Pause playback
                    if (!isPaused)
                    {
                        pauseTest();
                    }
                }
                else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                {
                    // Volume is important to the results of the hearing test, so still pause
                    if (!isPaused)
                    {
                        pauseTest();
                    }
                }
            }

            private Boolean requestAudioFocus()
            {
                int request = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
                return request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
            }

            private void abandonAudioFocus()
            {
                audioManager.abandonAudioFocus(this);
            }
        };

    }

    private void pauseTest()
    {
        //TODO implement
    }

    private void unPauseTest()
    {
        //TODO implement
    }

    public String getProgress()
    {
        return this.progress;
    }

    public boolean getTestState()
    {
        return testRunning;
    }

    private void setProgress(String progress)
    {
        this.progress = progress;
    }

    private void initToneDataArrayList()
    {
        this.toneDataArrayList = new ArrayList<ToneData>();
    }

    private void updateResult()
    {
        if (currentEar.equals("L"))
        {
            toneDataArrayList.get(currentSound).setLHeardAtDB(dbHLLevel +
                    REFERENCE_FREQUENCY_DBHL_VALUES[currentSound]);
        }
        else
        {
            toneDataArrayList.get(currentSound).setRHeardAtDB(dbHLLevel +
                    REFERENCE_FREQUENCY_DBHL_VALUES[currentSound]);
        }

    }

    private void initTest()
    {
        this.initProgress();
        this.initNextSound();
        this.initVolume();
    }

    private void initProgress()
    {
        this.progress = "1/" + NUMBER_FREQUENCIES;
    }

    private void initSoundPool()
    {
        SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setMaxStreams(1);
        this.soundPool = soundPoolBuilder.build();
        this.soundPoolSounds = new ArrayList<Integer>();
        loadSounds();
    }

    private void loadSounds()
    {
        for (int i = 0; i < TONES.length; i++)
        {
            String resourceName = "tone_" + Integer.toString(TONES[i]) + "hz_3s";
            int resID = mContext.getResources().getIdentifier(resourceName,
                    "raw", PACKAGE_NAME);
            int soundID = soundPool.load(mContext, resID, 1);
            soundPoolSounds.add(soundID);
            this.toneDataArrayList.add(new ToneData(TONES[i], REFERENCE_FREQUENCY_DBHL_VALUES[i]));

        }
    }

    private void setVolume()
    {
        double newVolume = dbHLLevel + REFERENCE_FREQUENCY_DBHL_VALUES[currentSound];
        if (newVolume >= 100)
        {
            newVolume = 100;
            maxVolumeReached = true;
        }
        if (currentEar.equals("L"))
        {
            LVolume = dBToGain(newVolume);
            RVolume = 0.0f;
        }
        else
        {
            LVolume = 0.0f;
            RVolume = dBToGain(newVolume);
        }
    }

    private void initVolume()
    {
        dbHLLevel = -5;
        maxVolumeReached = false;
        if (currentEar.equals("L"))
        {
            LVolume = dBToGain(dbHLLevel + REFERENCE_FREQUENCY_DBHL_VALUES[0] );
            RVolume = 0.0f;
        }
        else
        {
            LVolume = 0.0f;
            RVolume = dBToGain(dbHLLevel + REFERENCE_FREQUENCY_DBHL_VALUES[0]);
        }
    }

    private void initNextSound()
    {
        // TODO convert to initTestState() function, combine with init_progress
        currentSound = 0;
    }


    private void playNextSound()
    {
        Log.d("HearingTestModel", "Sound Played");
        soundPool.play(soundPoolSounds.get(currentSound), LVolume, RVolume, 1, 0,1);
    }

    private void updateTestState(Boolean heard)
    {
        if (heard || maxVolumeReached)
        {
            if (currentSound < TONES.length-1)
            {
                currentSound += 1;
                setProgress(Integer.toString(currentSound+1) + "/" + NUMBER_FREQUENCIES);
                updateResult();
                resetVolume();
                maxVolumeReached = false;
            }
            else if (currentEar == "L")
            {
                updateResult();
                currentEar = "R";
                initTest();
                maxVolumeReached = false;
            }
            else
            {
                onTestFinish();
                ((Activity)(mContext)).finish();
            }
        }
        else {
            increaseVolume();
        }
    }

    private void increaseVolume()
    {
        this.dbHLLevel += 5;
        setVolume();
    }

    private void resetVolume()
    {
        this.dbHLLevel = -5;
        setVolume();
    }

    public void runTest()
    {
        testRunning = true;
        setChanged();
        this.playNextSound();
        notifyObservers(this.progress);
    }

    private void saveResult(HearingTestResult result)
    {
        Saver.saveResult(result);
    }

    public void onSoundAck(Boolean heard)
    {
        if (testRunning)
        {
            updateTestState(heard);
            setChanged();
            notifyObservers(this.progress);
            playNextSound();
        }
    }

    private void onTestFinish()
    {
        HearingTestResult result = new HearingTestResult(toneDataArrayList);
    }

    private float dBToGain(double dBSPL)
    {
        return (float) Math.pow(10, (dBSPL-MAX_DB)*.05);
    }

}
