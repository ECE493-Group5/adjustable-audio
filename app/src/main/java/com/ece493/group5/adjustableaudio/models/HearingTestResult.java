package com.ece493.group5.adjustableaudio.models;

import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.audiofx.Equalizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HearingTestResult
{
    private final String TAG = HearingTestResult.class.getSimpleName();
    private ArrayList<ToneData> testResults;
    private String testName;
    private Date testDate;

    public HearingTestResult(String name, ArrayList<ToneData> testResults)
    {
        this.testResults = testResults;
        this.testName = name;
        this.testDate = new Date();
    }

    public String getTestName()
    {
        return this.testName;
    }

    public void setTestName(String newName)
    {
        this.testName = newName;
    }

    public ArrayList<ToneData> getTestResults()
    {
        return this.testResults;
    }

    public Date getTestDate()
    {
     return this.testDate;
    }

    public int calculateLeftVolume()
    {
        MediaPlayer dummyMediaPlayer = new MediaPlayer();
        Equalizer dummyEqualizer = new Equalizer(0, dummyMediaPlayer.getAudioSessionId());

        for (int i = 0; i < dummyEqualizer.getNumberOfBands(); i++)
            Log.d(TAG, "band: "+ i + ", center freq: " + dummyEqualizer.getCenterFreq((short)i)/1000);

        dummyEqualizer.release();
        dummyMediaPlayer.release();

        for (ToneData toneData: testResults) {
            Log.d(TAG, "dbHL: " + toneData.getdBHL() + ", Freq: " + toneData.getFrequency() + ", lDb: " + toneData.getLHeardAtDB() + ", rDb: " + toneData.getRHeardAtDB());
        }

        return 0;
    }

    public int calculateRightVolume()
    {
        return 0;
    }

    public HashMap<Integer, Integer> calculateEqualizerSettings()
    {
        return null;
    }

}
