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
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.new_hearing_test_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Got it!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());
    }

    @After
    public void tearDown()
    {
        if (SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null)
        {
            SaveController.deleteResult(InstrumentationRegistry.getInstrumentation().getTargetContext(), 0);
        }
    }

    @Test
    public void testInitialHearingTestUI()
    {
        ViewInteraction textView3 = onView(allOf(withId(R.id.hearing_test_title_textview),
                withText("Hearing Test"), childAtPosition(allOf(withId(R.id.hearing_test_layout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 0), isDisplayed()));
        textView3.check(matches(withText("Hearing Test")));

        ViewInteraction textView5 = onView(allOf(withId(R.id.hearing_test_progress),
                withText("Progress"), childAtPosition(allOf(withId(R.id.progress_layout),
                        childAtPosition(withId(R.id.hearing_test_layout), 1)), 0),
                        isDisplayed()));
        textView5.check(matches(withText("Progress")));

        ViewInteraction textView6 = onView(allOf(withId(R.id.hearing_test_ear_textview),
                withText("Left"), childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 1), isDisplayed()));
        textView6.check(matches(withText("Left")));

        ViewInteraction textView7 = onView(allOf(withId(R.id.hearing_test_progress_textview),
                withText("0/16"), childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 2), isDisplayed()));
        textView7.check(matches(withText("0/16")));

        ViewInteraction seekBar = onView(allOf(withId(R.id.hearing_test_progress_bar),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 3), isDisplayed()));
        seekBar.check(matches(isDisplayed()));

        ViewInteraction textView8 = onView(allOf(withId(R.id.countdown_info_textview),
                withText("Test Starting In:"), childAtPosition(allOf(withId(R.id.beep_ack_layout),
                        childAtPosition(withId(R.id.hearing_test_layout), 2)), 0),
                        isDisplayed()));
        textView8.check(matches(withText("Test Starting In:")));

        ViewInteraction textView9 = onView(allOf(withId(R.id.hearing_test_countdown_integer_textview),
                withText("2"), childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 0), isDisplayed()));
        textView9.check(matches(withText("2")));

        ViewInteraction textView10 = onView(allOf(withId(R.id.hearing_test_countdown_separator_textview),
                withText("."), childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 1), isDisplayed()));
        textView10.check(matches(withText(".")));

        ViewInteraction textView11 = onView(allOf(withId(R.id.hearing_test_countdown_decimal_textview),
                withText("0"), childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 2), isDisplayed()));
        textView11.check(matches(withText("0")));

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
    public void hearingTestPerformTest()
    {
        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.hearing_test_start_test_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                3),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        try {
            Thread.sleep(2000);
        } catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));

        ViewInteraction textView14 = onView(allOf(withId(R.id.hearing_test_progress_textview),
                        childAtPosition(
                                allOf(withId(R.id.progress_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                1)),
                                2),
                        isDisplayed()));

        for (int i = 1; i <= 16; i++)
        {
            textView14.check(matches(withText(i + "/16")));
            floatingActionButton.perform(click());
        }


        ViewInteraction textView13 = onView(
                allOf(withId(R.id.hearing_test_ear_textview), withText("Right"),
                        childAtPosition(
                                allOf(withId(R.id.progress_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                1)),
                                1),
                        isDisplayed()));
        textView13.check(matches(withText("Right")));

        for (int i = 1; i <= 16; i++)
        {
            textView14.check(matches(withText(i + "/16")));
            floatingActionButton.perform(click());
        }

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.custom),
                                childAtPosition(
                                        withId(R.id.customPanel),
                                        0)),
                        0),
                        isDisplayed()));
        editText.perform(replaceText("Test"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.main_parent),
                        childAtPosition(
                                allOf(withId(R.id.hearing_test_result_recyclerview),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                4)),
                                0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction textView15 = onView(
                allOf(withId(R.id.testResultName), withText("Test"),
                        childAtPosition(
                                allOf(withId(R.id.main_parent),
                                        childAtPosition(
                                                withId(R.id.hearing_test_result_recyclerview),
                                                0)),
                                0),
                        isDisplayed()));
        textView15.check(matches(withText("Test")));
    }

    @Test
    public void hearingTestPerformWithNoNameTest()
    {
        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.hearing_test_start_test_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                3),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        try {
            Thread.sleep(2000);
        } catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));

        for (int i = 1; i <= 16; i++)
        {
            floatingActionButton.perform(click());
        }

        for (int i = 1; i <= 16; i++)
        {
            floatingActionButton.perform(click());
        }

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button2), withText("CANCEL"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                2)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.testResultName), withText("Hearing Test"),
                        childAtPosition(
                                allOf(withId(R.id.main_parent),
                                        childAtPosition(
                                                withId(R.id.hearing_test_result_recyclerview),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Hearing Test")));
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
