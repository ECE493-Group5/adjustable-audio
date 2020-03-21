package com.ece493.group5.adjustableaudio.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HearingTestResult {

    private ArrayList<ToneData> testResults;
    private String testName;
    private Date testDate;

    public HearingTestResult(ArrayList<ToneData> testResults)
    {
        this.testResults = testResults;
        this.testName = new String();
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

    public String toJSON()
    {
        //TODO implement
        return "";
    }

    public void Update(String soundInfo, Boolean heard)
    {
        // delete function, no longer used
    }


}
