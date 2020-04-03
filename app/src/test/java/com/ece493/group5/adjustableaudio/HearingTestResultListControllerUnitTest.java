package com.ece493.group5.adjustableaudio;

import android.content.Context;
import android.os.Build;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.HearingTestResultListController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import androidx.test.core.app.ApplicationProvider;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class HearingTestResultListControllerUnitTest
{
    Context testContext;
    private ArrayList<HearingTestResult> testResultList;
    private HearingTestResult testHearingTestResult;

    @Before
    public void resultListControllerTestSetup()
    {
        testContext = ApplicationProvider.getApplicationContext();

        ArrayList<ToneData> toneDataList = new ArrayList<ToneData>();
        for (int i = 0; i < 16; i ++)
        {
            int frequency = 100 * i;
            double dbHL = 5 * i;
            double lHeardAtDB = 100 - (5 * i);
            double rHeardAtDB = 80 - (5 * i);
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            toneDataList.add(toneData);
        }

        String testTestName = "Test Name";

        testHearingTestResult = new HearingTestResult(testTestName, toneDataList);
    }

    @Test
    public void testAdd()
    {
        assertFalse(HearingTestResultListController
                .getResultList(testContext)
                .contains(testHearingTestResult));

        HearingTestResultListController.add(testContext, testHearingTestResult);

        assertTrue(HearingTestResultListController
                .getResultList(testContext)
                .contains(testHearingTestResult));

        HearingTestResultListController.remove(testContext, testHearingTestResult);
    }

    @Test
    public void testRemove()
    {
        HearingTestResultListController.add(testContext, testHearingTestResult);

        assertTrue(HearingTestResultListController
                .getResultList(testContext)
                .contains(testHearingTestResult));

        HearingTestResultListController.remove(testContext, testHearingTestResult);

        assertFalse(HearingTestResultListController
                .getResultList(testContext)
                .contains(testHearingTestResult));
    }
}
