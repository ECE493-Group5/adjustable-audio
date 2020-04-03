package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HearingTestResultUnitTest
{

    private ArrayList<ToneData> testTestResults;
    private String testTestName;
    private Date testTestDate;

    @Before
    public void hearingTestResultTestSetup()
    {
        testTestResults = new ArrayList<ToneData>();
        for (int i = 0; i < 16; i ++)
        {
            int frequency = 100 * i;
            double dbHL = 5 * i;
            double lHeardAtDB = 100 - (5 * i);
            double rHeardAtDB = 80 - (5 * i);
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            testTestResults.add(toneData);
        }

        testTestName = "Test Name";

        testTestDate = new Date();
    }

    @Test
    public void testHearingTestResultConstructor()
    {
        HearingTestResult testHearingTestResult = new HearingTestResult(
                testTestName, testTestResults);

        ArrayList<ToneData> HearingTestResults = testHearingTestResult.getTestResults();

        for (ToneData toneData : HearingTestResults)
        {
            assertNotNull(toneData);
            assertTrue(testHearingTestResult.getTestResults().contains(toneData));
        }

        assertEquals(testTestName, testHearingTestResult.getTestName());
        assertEquals(testTestDate, testHearingTestResult.getTestDate());
    }

    @Test
    public void testTestResults()
    {
        HearingTestResult testHearingTestResult = new HearingTestResult(
                testTestName, testTestResults);

        ArrayList<ToneData> newHearingTestResults = new ArrayList<ToneData>();

        for (int i = 0; i < 16; i++)
        {
            int frequency = 100 * i + 1;
            double dbHL = 5 * i + 1;
            double lHeardAtDB = 100 - (5 * i) + 1;
            double rHeardAtDB = 80 - (5 * i) + 1;
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            newHearingTestResults.add(toneData);
        }

        testHearingTestResult.setTestResults(newHearingTestResults);

        for (int i = 0; i < newHearingTestResults.size(); i++)
        {
            assertNotNull(testHearingTestResult.getTestResults().get(i));
            assertNotEquals(testTestResults.get(i), testHearingTestResult.getTestResults().get(i));
            assertEquals(newHearingTestResults.get(i), testHearingTestResult.getTestResults().get(i));
        }

    }

    @Test
    public void testNameTest()
    {
        HearingTestResult testHearingTestResult = new HearingTestResult(
                testTestName, testTestResults);

        String newTestName = "New Name";

        testHearingTestResult.setTestName(newTestName);

        assertNotNull(testHearingTestResult.getTestName());
        assertNotEquals(testTestName, testHearingTestResult.getTestName());
        assertEquals(newTestName, testHearingTestResult.getTestName());
    }

    @Test
    public void testDateTest()
    {
        HearingTestResult testHearingTestResult = new HearingTestResult(
                testTestName, testTestResults);

        Date date = testHearingTestResult.getTestDate();

        Calendar calendar = new GregorianCalendar();
        calendar.set(1980,1,1);
        Date newDate = calendar.getTime();

        testHearingTestResult.setTestDate(newDate);

        assertNotNull(testHearingTestResult.getTestDate());
        assertNotEquals(date, testHearingTestResult.getTestDate());
        assertEquals(newDate, testHearingTestResult.getTestDate());
    }

}
