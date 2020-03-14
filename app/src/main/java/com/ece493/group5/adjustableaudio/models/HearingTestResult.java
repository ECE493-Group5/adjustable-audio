package com.ece493.group5.adjustableaudio.models;

import java.util.Date;
import java.util.HashMap;

public class HearingTestResult {

    private HashMap<String, Boolean> testResults;
    private String testName;
    private Date testDate;

    public HearingTestResult()
    {
        this.testResults = new HashMap<String, Boolean>();
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

    public HashMap<String, Boolean> getTestResults()
    {
        return this.testResults;
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
        //TODO implement (possibly switch to only being creatable by a full completed test?)
    }


}
