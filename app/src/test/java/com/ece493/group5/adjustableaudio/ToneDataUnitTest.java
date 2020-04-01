package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.ToneData;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ToneDataUnitTest
{

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
        assertEquals(testDBHL, toneData.getdBHL());

        assertNull(toneData.getLHeardAtDB());
        assertNull(toneData.getRHeardAtDB());
    }
}
