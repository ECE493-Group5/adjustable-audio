package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.ToneData;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class ToneDataUnitTest
{
    private static final double DELTA = 0.0001;
    private static final int FREQUENCY_1000 = 1000;
    private static final double DBHL_5POINT5 = 5.5;
    private static final double DB_10POINT5 = 10.5;
    private static final double DB_15POINT5 = 15.5;
    private static final double DBHL_30POINT5 = 30.5;
    private static final int FREQUENCY_500 = 500;
    private static final double DB_40 = 40.0;
    private static final double DB_45 = 45.0;

    private int testFrequency;
    private double testDBHL;
    private double testLHeardAtDB;
    private double testRHeardAtDB;

    @Before
    public void ToneDataSetup()
    {
        testFrequency = FREQUENCY_1000;
        testDBHL = DBHL_5POINT5;
        testLHeardAtDB = DB_10POINT5;
        testRHeardAtDB = DB_15POINT5;
    }

    @Test
    public void testToneDataConstructor()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        assertEquals(testFrequency, toneData.getFrequency());
        assertEquals(testDBHL, toneData.getdBHL(), DELTA);

        assertEquals(0.0, toneData.getLHeardAtDB(), DELTA);
        assertEquals(0.0, toneData.getRHeardAtDB(), DELTA);
    }

    @Test
    public void dBHLTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        double newDBHL = DBHL_30POINT5;

        toneData.setdBHL(newDBHL);

        assertNotEquals(testDBHL, toneData.getdBHL());
        assertEquals(newDBHL, toneData.getdBHL(), DELTA);
    }

    @Test
    public void frequencyTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        int newFrequency = FREQUENCY_500;

        toneData.setFrequency(newFrequency);

        assertNotEquals(testFrequency, toneData.getFrequency());
        assertEquals(newFrequency, toneData.getFrequency());
    }

    @Test
    public void lHeardAtDBTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        Double newLHeardAtDB = DB_40;

        toneData.setLHeardAtDB(newLHeardAtDB);

        assertNotNull(toneData.getLHeardAtDB());
        assertEquals(newLHeardAtDB, toneData.getLHeardAtDB());
    }

    @Test
    public void rHeardAtDBTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        Double newRHeardAtDB = DB_45;

        toneData.setRHeardAtDB(newRHeardAtDB);

        assertNotNull(toneData.getRHeardAtDB());
        assertEquals(newRHeardAtDB, toneData.getRHeardAtDB());
    }
}
