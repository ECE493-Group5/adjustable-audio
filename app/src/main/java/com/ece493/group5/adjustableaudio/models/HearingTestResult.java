package com.ece493.group5.adjustableaudio.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HearingTestResult {

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

    public void calculateAudioBalance()
    {
        //TODO implement
    }

    public void calculateEqualizerSettings()
    {
        //TODO implement
    }

}
