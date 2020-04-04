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

    private static final int NUM_FREQUENCIES = 16;
    private static final int BASE_FREQUENCY = 100;
    private static final int BASE_DBHL = 5;
    private static final int MAX_DB = 100;
    private static final int DB_80 = 80;
    private static final String TEST_NAME = "Test Name";
    private static final String NEW_NAME = "New Name";
    private static final int YEAR_1980 = 1980;
    private static final int MONTH_1 = 1;
    private static final int DAY_1 = 1;

    private ArrayList<ToneData> testTestResults;
    private String testTestName;
    private Date testTestDate;

    @Before
    public void hearingTestResultTestSetup()
    {
        testTestResults = new ArrayList<ToneData>();
        for (int i = 0; i < NUM_FREQUENCIES; i ++)
        {
            int frequency = BASE_FREQUENCY * i;
            double dbHL = BASE_DBHL * i;
            double lHeardAtDB = MAX_DB - (BASE_DBHL * i);
            double rHeardAtDB = DB_80 - (BASE_DBHL * i);
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            testTestResults.add(toneData);
        }

        testTestName = TEST_NAME;

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

        for (int i = 0; i < NUM_FREQUENCIES; i++)
        {
            int frequency = BASE_FREQUENCY * i + 1;
            double dbHL = BASE_DBHL * i + 1;
            double lHeardAtDB = MAX_DB - (BASE_DBHL * i) + 1;
            double rHeardAtDB = DB_80 - (BASE_DBHL * i) + 1;
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

        String newTestName = NEW_NAME;

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
        calendar.set(YEAR_1980, MONTH_1, DAY_1);
        Date newDate = calendar.getTime();

        testHearingTestResult.setTestDate(newDate);

        assertNotNull(testHearingTestResult.getTestDate());
        assertNotEquals(date, testHearingTestResult.getTestDate());
        assertEquals(newDate, testHearingTestResult.getTestDate());
    }

}
