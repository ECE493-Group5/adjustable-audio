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

    private static final int NUM_FREQUENCIES = 16;
    private static final int BASE_FREQUENCY = 100;
    private static final int BASE_DBHL = 5;
    private static final int MAX_DB = 100;
    private static final int DB_80 = 80;
    private static final String TEST_NAME = "Test Name";

    private Context testContext;
    private ArrayList<HearingTestResult> testResultList;
    private HearingTestResult testHearingTestResult;

    @Before
    public void resultListControllerTestSetup()
    {
        testContext = ApplicationProvider.getApplicationContext();

        ArrayList<ToneData> toneDataList = new ArrayList<ToneData>();
        for (int i = 0; i < NUM_FREQUENCIES; i ++)
        {
            int frequency = BASE_FREQUENCY * i;
            double dbHL = BASE_DBHL * i;
            double lHeardAtDB = MAX_DB - (BASE_DBHL * i);
            double rHeardAtDB = DB_80 - (BASE_DBHL * i);
            ToneData toneData = new ToneData(frequency, dbHL);
            toneData.setLHeardAtDB(lHeardAtDB);
            toneData.setRHeardAtDB(rHeardAtDB);
            toneDataList.add(toneData);
        }

        String testTestName = TEST_NAME;

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
