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

    public void setTestResults(ArrayList<ToneData> newResults)
    {
        testResults = newResults;
    }

    public Date getTestDate()
    {
        return this.testDate;
    }

    public void setTestDate(Date newDate)
    {
        testDate = newDate;
    }
}
