package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.utils.TimeUtils;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TimeUtilsUnitTest
{
    private static final int TEST_TIME_IN_SECONDS = 30;
    private static final String TEST_TIME_AS_STRING = "30";
    private static final int MS_30000 = 30000;

    private long testTimeInSeconds;
    private String testTimeAsString;

    @Before
    public void timeUtilsTestSetup()
    {
        testTimeInSeconds = TEST_TIME_IN_SECONDS;

        testTimeAsString = TEST_TIME_AS_STRING;
    }

    @Test
    public void millisecondsToSecondsTest()
    {
        long ms = MS_30000;

        long timeInSeconds = TimeUtils.millisecondsToSeconds(ms);

        assertNotEquals(0, timeInSeconds);

        assertEquals(testTimeInSeconds, timeInSeconds);
    }

    /*
     * This test is included in the test plan in order to achieve statement coverage, but is not
     * testable due to errors in the android framework related to DateUtils that should be addressed
     *  in a coming update
     */
//    @Test
//    public void durationAsStringTest()
//    {
//        long ms = 30000;
//
//        String timeAsString = TimeUtils.durationAsString(ms);
//
//        assertNotNull(timeAsString);
//
//        assertEquals(testTimeAsString, timeAsString);
//    }

}
