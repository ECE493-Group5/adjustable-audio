package com.ece493.group5.adjustableaudio.models;

import android.content.Context;
import android.media.SoundPool;

import com.ece493.group5.adjustableaudio.views.HearingTestView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class HearingTestModel extends Observable {

    private Context mContext;
    private HearingTestView mView;

    private HashMap<String, Boolean> testResult;
    private String progress;
    private String soundData;
    private Boolean testRunning;
    private SoundPool soundPool;


    public HearingTestModel(Context mContext)
    {
        this.mContext = mContext;
        initTest();


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
        // TODO implement
    }

    private void initSoundPool()
    {
        //TODO implement
    }

    private void loadSounds()
    {
        //TODO implement
    }

    private void setVolume()
    {
        //TODO implement
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
