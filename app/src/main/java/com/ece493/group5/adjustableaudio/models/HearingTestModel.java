package com.ece493.group5.adjustableaudio.models;

import android.content.Context;
import android.media.SoundPool;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.views.HearingTestView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class HearingTestModel extends Observable
{
    private static final long BEEP_DURATION = 3000;
    private static final String NUMBER_FREQUENCIES = "5";
    private static final int[] tones = {30, 90, 233, 250, 347,
                                        500, 907, 1000, 1353, 2000,
                                        3533, 4000, 5267, 8000, 11333, 15667 };
    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";

    private Context mContext;
    private HearingTestView mView;

    private HashMap<String, Boolean> testResult;
    private String progress;
    private String soundData;
    private Boolean testRunning;
    private SoundPool soundPool;
    private int currentSound;
    private float LVolume;
    private float RVolume;
    private ArrayList<Integer> soundPoolSounds;


    public HearingTestModel(Context mContext)
    {
        this.mContext = mContext;
        initTest();
        initSoundPool();

    }

    public String getProgress()
    {
        return this.progress;
    }

    private void setProgress(String progress)
    {
        this.progress = progress;
    }

    private void initResult()
    {
        this.testResult = new HashMap<String, Boolean>();
    }

    private void updateResult(Boolean heard)
    {
        this.testResult.put(this.soundData, heard);
    }

    private void initTest()
    {
        this.initProgress();
        this.initResult();
        this.initVolume();
        this.initNextSound();
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

        //TODO implement
    }

    private void loadSounds()
    {
        for (int i = 0; i < tones.length; i++)
        {
            String resourceName = "tone_" + Integer.toString(tones[i]) + "hz_3s";
            int resID = mContext.getResources().getIdentifier(resourceName,
                    "raw", PACKAGE_NAME);
            int soundID = soundPool.load(mContext, resID, 1);
            soundPoolSounds.add(soundID);
        }
//        int sound1 = soundPool.load(mContext, R.raw.tone_30hz_3s, 1);
//        int sound2 = soundPool.load(mContext, R.raw.tone_90hz_3s, 1);
//        int sound3 = soundPool.load(mContext, R.raw.tone_233hz_3s, 1);
//        int sound4 = soundPool.load(mContext, R.raw.tone_250hz_3s, 1);
//        int sound5 = soundPool.load(mContext, R.raw.tone_347hz_3s, 1);
//        int sound6 = soundPool.load(mContext, R.raw.tone_500hz_3s, 1);
//        int sound7 = soundPool.load(mContext, R.raw.tone_907hz_3s, 1);
//        int sound8 = soundPool.load(mContext, R.raw.tone_1000hz_3s, 1);
//        int sound9 = soundPool.load(mContext, R.raw.tone_1353hz_3s, 1);
//        int sound10 = soundPool.load(mContext, R.raw.tone_2000hz_3s, 1);
//        int sound11 = soundPool.load(mContext, R.raw.tone_3533hz_3s, 1);
//        int sound12 = soundPool.load(mContext, R.raw.tone_4000hz_3s, 1);
//        int sound13 = soundPool.load(mContext, R.raw.tone_5267hz_3s, 1);
//        int sound14 = soundPool.load(mContext, R.raw.tone_8000hz_3s, 1);
//        int sound15 = soundPool.load(mContext, R.raw.tone_11333hz_3s, 1);
//        int sound16 = soundPool.load(mContext, R.raw.tone_15667hz_3s, 1);
    }

    private void setVolume(float lVolume, float rVolume)
    {
        this.soundPool.setVolume(currentSound, lVolume, rVolume);
    }

    private void initVolume(){
        //TODO implement
    }

    private void initNextSound()
    {
        //TODO implement
    }

    private void requestAudioFocus()
    {
        //TODO implement
    }

    private void playNextSound()
    {
        //TODO implement
    }

    private void updateProgress(Boolean heard)
    {
        //TODO implement
    }

    private void setNextSound(Boolean heard)
    {
        //TODO implement
    }


    public void runTest()
    {
        testRunning = true;
        this.playNextSound();
    }

    public void onSoundAck(Boolean heard)
    {
        if (testRunning)
        {
            updateProgress(heard);
            updateResult(heard);
            setNextSound(heard);
            notifyObservers(this.progress);
            playNextSound();
        }
    }

}
