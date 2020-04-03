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

    private int testFrequency;
    private double testDBHL;
    private double testLHeardAtDB;
    private double testRHeardAtDB;

    @Before
    public void ToneDataSetup()
    {
        testFrequency = 1000;
        testDBHL = 5.5;
        testLHeardAtDB = 10.5;
        testRHeardAtDB = 15.5;
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

        double newDBHL = 30.5;

        toneData.setdBHL(newDBHL);

        assertNotEquals(testDBHL, toneData.getdBHL());
        assertEquals(newDBHL, toneData.getdBHL(), DELTA);
    }

    @Test
    public void frequencyTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        int newFrequency = 500;

        toneData.setFrequency(newFrequency);

        assertNotEquals(testFrequency, toneData.getFrequency());
        assertEquals(newFrequency, toneData.getFrequency());
    }

    @Test
    public void lHeardAtDBTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        Double newLHeardAtDB = 40.0;

        toneData.setLHeardAtDB(newLHeardAtDB);

        assertNotNull(toneData.getLHeardAtDB());
        assertEquals(newLHeardAtDB, toneData.getLHeardAtDB());
    }

    @Test
    public void rHeardAtDBTest()
    {
        ToneData toneData = new ToneData(testFrequency, testDBHL);

        Double newRHeardAtDB = 45.0;

        toneData.setRHeardAtDB(newRHeardAtDB);

        assertNotNull(toneData.getRHeardAtDB());
        assertEquals(newRHeardAtDB, toneData.getRHeardAtDB());
    }
}
