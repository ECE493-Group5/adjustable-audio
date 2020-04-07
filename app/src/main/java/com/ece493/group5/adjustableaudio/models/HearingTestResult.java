package com.ece493.group5.adjustableaudio.models;


import java.util.ArrayList;
import java.util.Date;

/**
 * The HearingTestResult class helps implement the following requirement:
 *
 * #SRS: Viewable Hearing Test Result
 */

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

    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }

        if (this.getClass() != other.getClass())
        {
            return false;
        }

        if (!this.testResults.equals(((HearingTestResult) other).testResults))
        {
            return false;
        }

        if (!this.testName.equals(((HearingTestResult) other).testName))
        {
            return false;
        }
        
        return true;
    }
}
