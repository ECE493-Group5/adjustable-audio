package com.ece493.group5.adjustableaudio;


import android.view.View;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteHearingTestResultTest extends BaseInstrumentedTest
{
    private static final String EMPTY_VIEW_MESSAGE = "Recycler view should be empty";
    private static final String HEARING_TEST = "Hearing Test";
    private static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String TEST = "Test";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private static final int[] TONES = {30, 90, 233, 250, 347, 500, 907, 1000, 1353, 2000,
            3533, 4000, 5267, 8000, 11333, 15667};

    private static final double[] REFERENCE_FREQUENCY_DBHL_VALUES = {60.0, 37.0, 19.0, 18.0,
            14.6, 11.0, 6.0, 5.5, 5.5, 4.5, 6.5, 9.5, 14.8, 17.5, 23.0, 52.5};

    private static final double DECIBEL_LEVEL = 35.5;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    private void clearHearingTestResult()
    {
        int size;

        //Clear any hearing tests
        if
        (SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null
                && (size = SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()).size())>0)
        {
            for(int i = 0; i < size; i++)
            {
                SaveController.deleteResult(InstrumentationRegistry.getInstrumentation().getTargetContext(), i);
            }
        }
    }

    private void createDummyHearingTestResult()
    {
        ArrayList<ToneData> dummyToneDataList = new ArrayList<>();

        for (int i = 0; i < TONES.length; i++)
        {
            ToneData dummyToneData =  new ToneData(TONES[i], REFERENCE_FREQUENCY_DBHL_VALUES[i]);
            dummyToneData.setLHeardAtDB(DECIBEL_LEVEL);
            dummyToneData.setRHeardAtDB(DECIBEL_LEVEL);
            dummyToneDataList.add(i, dummyToneData);
        }

        HearingTestResult dummyHearingTestResult = new HearingTestResult(TEST, dummyToneDataList);
        SaveController.saveResult(InstrumentationRegistry.getInstrumentation().getTargetContext(), dummyHearingTestResult);
    }

    private void navigateToHearingTestResultView()
    {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_hearing_test), withContentDescription(HEARING_TEST),
                        childAtPosition(childAtPosition(withId(R.id.nav_view), 0), 0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction recyclerView = onView(allOf(withId(R.id.hearing_test_result_recyclerview),
                childAtPosition(withClassName(is(LINEAR_LAYOUT)), 4)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
    }

    @Before
    public void setup()
    {
        clearHearingTestResult();
        createDummyHearingTestResult();
        navigateToHearingTestResultView();
    }

    @Test
    public void testDeleteHearingTestResult()
    {
        ViewInteraction deleteButton = onView(allOf(withId(R.id.hearing_test_result_delete_button),
                withText(R.string.delete_button), childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 2),
                        1), isDisplayed()));
        deleteButton.perform(click());

        ViewInteraction cancelButton = onView(allOf(withId(android.R.id.button2),
                withText(R.string.negative_button_dialog), childAtPosition(childAtPosition(withId(R.id.buttonPanel),
                        0), 2)));
        cancelButton.check(matches(isDisplayed()));
        cancelButton.check(matches(isClickable()));
        cancelButton.perform(scrollTo(), click());

        ViewInteraction deleteButton1 = onView(allOf(withId(R.id.hearing_test_result_delete_button),
                withText(R.string.delete_button), childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 2), 1),
                        isDisplayed()));
        deleteButton1.perform(click());

        ViewInteraction deleteConfirmationButton = onView(allOf(withId(android.R.id.button1),
                withText(R.string.delete_button), childAtPosition(childAtPosition(withId(R.id.buttonPanel),
                        0), 3)));
        deleteConfirmationButton.check(matches(isDisplayed()));
        deleteConfirmationButton.check(matches(isClickable()));
        deleteConfirmationButton.perform(scrollTo(), click());

        ViewInteraction resultList = onView(allOf(withId(R.id.hearing_test_result_recyclerview),
                        childAtPosition(childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                                4), isDisplayed()));
        resultList.check(matches(isDisplayed()));

        RecyclerView recyclerView = (RecyclerView) mActivityTestRule.getActivity().findViewById(R.id.hearing_test_result_recyclerview);
        assertTrue(EMPTY_VIEW_MESSAGE, recyclerView.getAdapter().getItemCount() == 0);
    }
}
