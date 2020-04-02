package com.ece493.group5.adjustableaudio;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteHearingTestResultTest {

    private static final int[] TONES = {30, 90, 233, 250, 347,
            500, 907, 1000, 1353, 2000,
            3533, 4000, 5267, 8000, 11333, 15667};

    private static final double[] REFERENCE_FREQUENCY_DBHL_VALUES = {
            60.0, 37.0, 19.0, 18.0,
            14.6, 11.0, 6.0, 5.5,
            5.5, 4.5, 6.5, 9.5,
            14.8, 17.5, 23.0, 52.5};

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    private void clearHearingTestResult()
    {
        //Clear any hearing tests
        if(SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null
                && SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()).size()>0)
        {
            int size = SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()).size();
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
            dummyToneData.setLHeardAtDB(35.5);
            dummyToneData.setRHeardAtDB(35.5);
            dummyToneDataList.add(i, dummyToneData);
        }

        HearingTestResult dummyHearingTestResult = new HearingTestResult("Test", dummyToneDataList);
        SaveController.saveResult(InstrumentationRegistry.getInstrumentation().getTargetContext(), dummyHearingTestResult);
    }

    private void navigateToHearingTestResultView()
    {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_hearing_test), withContentDescription("Hearing Test"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.hearing_test_result_recyclerview),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                4)));
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
    public void deleteHearingTestResultTest()
    {
        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.hearing_test_result_delete_button), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button2), withText("CANCEL"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                2)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.hearing_test_result_delete_button), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("DELETE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.hearing_test_result_recyclerview),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                4),
                        isDisplayed()));
        recyclerView2.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
