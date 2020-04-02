package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HearingTestActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setup()
    {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_hearing_test), withContentDescription("Hearing Test"),
                        childAtPosition(childAtPosition(withId(R.id.nav_view), 0), 0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction takeHearingTestButton = onView(allOf(withId(R.id.new_hearing_test_button),
                childAtPosition(childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0), 2), isDisplayed()));
        takeHearingTestButton.perform(click());

        ViewInteraction confirmButton = onView(
                allOf(withId(android.R.id.button1), withText("Got it!"), childAtPosition(
                        childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        confirmButton.perform(scrollTo(), click());
    }

    @After
    public void tearDown()
    {
        if (SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null
        && SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()).size() > 0)
        {
            SaveController.deleteResult(InstrumentationRegistry.getInstrumentation().getTargetContext(), 0);
        }
    }

    @Test
    public void testInitialHearingTestUI()
    {
        ViewInteraction hearingTestTitle = onView(allOf(withId(R.id.hearing_test_title_textview),
                withText("Hearing Test"), childAtPosition(allOf(withId(R.id.hearing_test_layout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 0), isDisplayed()));
        hearingTestTitle.check(matches(withText("Hearing Test")));

        ViewInteraction progressTitle = onView(allOf(withId(R.id.hearing_test_progress),
                withText("Progress"), childAtPosition(allOf(withId(R.id.progress_layout),
                        childAtPosition(withId(R.id.hearing_test_layout), 1)), 0),
                        isDisplayed()));
        progressTitle.check(matches(withText("Progress")));

        ViewInteraction earText = onView(allOf(withId(R.id.hearing_test_ear_textview),
                withText("Left"), childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 1), isDisplayed()));
        earText.check(matches(withText("Left")));

        ViewInteraction progressText = onView(allOf(withId(R.id.hearing_test_progress_textview),
                withText("0/16"), childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 2), isDisplayed()));
        progressText.check(matches(withText("0/16")));

        ViewInteraction seekBar = onView(allOf(withId(R.id.hearing_test_progress_bar),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 3), isDisplayed()));
        seekBar.check(matches(isDisplayed()));

        ViewInteraction testText = onView(allOf(withId(R.id.countdown_info_textview),
                withText("Test Starting In:"), childAtPosition(allOf(withId(R.id.beep_ack_layout),
                        childAtPosition(withId(R.id.hearing_test_layout), 2)), 0),
                        isDisplayed()));
        testText.check(matches(withText("Test Starting In:")));

        ViewInteraction firstCount = onView(allOf(withId(R.id.hearing_test_countdown_integer_textview),
                withText("2"), childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 0), isDisplayed()));
        firstCount.check(matches(withText("2")));

        ViewInteraction decimalCount = onView(allOf(withId(R.id.hearing_test_countdown_separator_textview),
                withText("."), childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 1), isDisplayed()));
        decimalCount.check(matches(withText(".")));

        ViewInteraction secondCount = onView(allOf(withId(R.id.hearing_test_countdown_decimal_textview),
                withText("0"), childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 2), isDisplayed()));
        secondCount.check(matches(withText("0")));

//        ViewInteraction imageButton = onView(
//                allOf(withId(R.id.hearing_test_start_test_button),
//                        childAtPosition(
//                                allOf(withId(R.id.beep_ack_layout),
//                                        childAtPosition(
//                                                withId(R.id.hearing_test_layout),
//                                                2)),
//                                2),
//                        isDisplayed()));
//        imageButton.check(matches(isDisplayed()));
    }

    @Test
    public void testTakingHearingTest()
    {
        ViewInteraction hearingTestStart = onView(allOf(withId(R.id.hearing_test_start_test_button),
                childAtPosition(allOf(withId(R.id.beep_ack_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 2)), 3), isDisplayed()));
        hearingTestStart.perform(click());

        //Wait the two seconds for the test to start
        try
        {
            Thread.sleep(2000);
        } catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        ViewInteraction ackButton = onView(allOf(withId(R.id.hearing_test_beep_ack_button),
                childAtPosition(allOf(withId(R.id.beep_ack_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 2)), 2), isDisplayed()));

        ViewInteraction progressText = onView(allOf(withId(R.id.hearing_test_progress_textview),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 2), isDisplayed()));

        for (int i = 1; i <= 16; i++)
        {
            progressText.check(matches(withText(i + "/16")));
            ackButton.perform(click());
        }

        ViewInteraction earText = onView(allOf(withId(R.id.hearing_test_ear_textview),
                withText("Right"), childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 1), isDisplayed()));
        earText.check(matches(withText("Right")));

        for (int i = 1; i <= 16; i++)
        {
            progressText.check(matches(withText(i + "/16")));
            ackButton.perform(click());
        }

        ViewInteraction nameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0),
                isDisplayed()));
        nameEditText.perform(replaceText("Test"), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1), withText("Save"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        saveButton.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(allOf(withId(R.id.main_parent), childAtPosition(
                allOf(withId(R.id.hearing_test_result_recyclerview), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        4)), 0), isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction testNameText = onView(allOf(withId(R.id.testResultName), withText("Test"),
                childAtPosition(allOf(withId(R.id.main_parent), childAtPosition(
                        withId(R.id.hearing_test_result_recyclerview), 0)), 0),
                        isDisplayed()));
        testNameText.check(matches(withText("Test")));
    }

    @Test
    public void testTakeHearingTestWithNoName()
    {
        ViewInteraction hearingTestStart = onView(allOf(withId(R.id.hearing_test_start_test_button),
                childAtPosition(allOf(withId(R.id.beep_ack_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 2)), 3), isDisplayed()));
        hearingTestStart.perform(click());

        //Wait the two seconds for the test to start
        try {
            Thread.sleep(2000);
        } catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        ViewInteraction ackButton = onView(allOf(withId(R.id.hearing_test_beep_ack_button),
                childAtPosition(allOf(withId(R.id.beep_ack_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 2)), 2), isDisplayed()));

        for (int i = 1; i <= 16; i++)
        {
            ackButton.perform(click());
        }

        for (int i = 1; i <= 16; i++)
        {
            ackButton.perform(click());
        }

        ViewInteraction cancelButton = onView(allOf(withId(android.R.id.button2), withText("CANCEL"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 2)));
        cancelButton.perform(scrollTo(), click());

        ViewInteraction defaultTitle = onView(allOf(withId(R.id.testResultName), withText("Hearing Test"),
                childAtPosition(allOf(withId(R.id.main_parent), childAtPosition(
                        withId(R.id.hearing_test_result_recyclerview), 0)), 0),
                        isDisplayed()));
        defaultTitle.check(matches(withText("Hearing Test")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view)
            {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
