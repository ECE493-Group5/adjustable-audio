package com.ece493.group5.adjustableaudio;


import android.view.View;

import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
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
public class HearingTestActivityTest extends BaseInstrumentedTest
{
    private static final String CONSTRAINT_LAYOUT = "androidx.constraintlayout.widget.ConstraintLayout";
    private static final String GOT_IT = "Got it!";
    private static final String HEARING_TEST = "Hearing Test";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String NUM_FREQUENCIES = "/16";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String RECYCLER_VIEW_MESSAGE = "Recycler view should have one hearing test result";
    private static final String TEST = "Test";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Before
    public void setup()
    {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_hearing_test), withContentDescription(HEARING_TEST),
                        childAtPosition(childAtPosition(withId(R.id.nav_view), 0), 0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction takeHearingTestButton = onView(allOf(withId(R.id.new_hearing_test_button),
                childAtPosition(childAtPosition(
                        withClassName(is(CONSTRAINT_LAYOUT)),
                        0), 2), isDisplayed()));
        takeHearingTestButton.perform(click());

        ViewInteraction confirmButton = onView(
                allOf(withId(android.R.id.button1), withText(GOT_IT), childAtPosition(
                        childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        confirmButton.check(matches(isClickable()));

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
                withText(R.string.title_hearing_test), childAtPosition(allOf(withId(R.id.hearing_test_layout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 0), isDisplayed()));
        hearingTestTitle.check(matches(withText(R.string.title_hearing_test)));

        ViewInteraction progressTitle = onView(allOf(withId(R.id.hearing_test_progress),
                withText(R.string.caption_hearing_test_progress), childAtPosition(allOf(withId(R.id.progress_layout),
                        childAtPosition(withId(R.id.hearing_test_layout), 1)), 0),
                        isDisplayed()));
        progressTitle.check(matches(withText(R.string.caption_hearing_test_progress)));

        ViewInteraction earText = onView(allOf(withId(R.id.hearing_test_ear_textview),
                withText(R.string.label_hearing_test_left_ear),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 1), isDisplayed()));
        earText.check(matches(withText(R.string.label_hearing_test_left_ear)));

        ViewInteraction progressText = onView(allOf(withId(R.id.hearing_test_progress_textview),
                withText(R.string.hearing_test_progress_placeholder),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 2), isDisplayed()));
        progressText.check(matches(withText(R.string.hearing_test_progress_placeholder)));

        ViewInteraction seekBar = onView(allOf(withId(R.id.hearing_test_progress_bar),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 3), isDisplayed()));
        seekBar.check(matches(isDisplayed()));

        ViewInteraction testText = onView(allOf(withId(R.id.countdown_info_textview),
                withText(R.string.caption_countdown_info),
                childAtPosition(allOf(withId(R.id.beep_ack_layout),
                        childAtPosition(withId(R.id.hearing_test_layout), 2)), 0),
                        isDisplayed()));
        testText.check(matches(withText(R.string.caption_countdown_info)));

        ViewInteraction firstCount = onView(allOf(withId(R.id.hearing_test_countdown_integer_textview),
                withText(R.string.hearing_test_beep_countdown_integer_placeholder),
                childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 0), isDisplayed()));
        firstCount.check(matches(withText(R.string.hearing_test_beep_countdown_integer_placeholder)));

        ViewInteraction decimalCount = onView(allOf(withId(R.id.hearing_test_countdown_separator_textview),
                withText(R.string.hearing_test_beep_countdown_decimal_point),
                childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 1), isDisplayed()));
        decimalCount.check(matches(withText(R.string.hearing_test_beep_countdown_decimal_point)));

        ViewInteraction secondCount = onView(allOf(withId(R.id.hearing_test_countdown_decimal_textview),
                withText(R.string.hearing_test_beep_countdown_decimal_placeholder),
                childAtPosition(allOf(withId(R.id.countdown_layout), childAtPosition(
                        withId(R.id.beep_ack_layout), 1)), 2), isDisplayed()));
        secondCount.check(matches(withText(R.string.hearing_test_beep_countdown_decimal_placeholder)));

        ViewInteraction imageButton = onView(allOf(withId(R.id.hearing_test_start_test_button),
                childAtPosition(allOf(withId(R.id.beep_ack_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 2)),
                                3), isDisplayed()));
        imageButton.check(matches(isDisplayed()));
        imageButton.check(matches(isClickable()));
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
        ackButton.check(matches(isClickable()));

        ViewInteraction progressText = onView(allOf(withId(R.id.hearing_test_progress_textview),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 2), isDisplayed()));

        for (int i = 1; i <= 16; i++)
        {
            progressText.check(matches(withText(i + NUM_FREQUENCIES)));
            ackButton.perform(click());
        }

        ViewInteraction earText = onView(allOf(withId(R.id.hearing_test_ear_textview),
                withText(R.string.label_hearing_test_right_ear),
                childAtPosition(allOf(withId(R.id.progress_layout), childAtPosition(
                        withId(R.id.hearing_test_layout), 1)), 1), isDisplayed()));
        earText.check(matches(withText(R.string.label_hearing_test_right_ear)));

        for (int i = 1; i <= 16; i++)
        {
            progressText.check(matches(withText(i + NUM_FREQUENCIES)));
            ackButton.perform(click());
        }

        ViewInteraction nameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0),
                isDisplayed()));
        nameEditText.perform(replaceText(TEST), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1),
                withText(R.string.save_button),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        saveButton.check(matches(isClickable()));
        saveButton.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(allOf(withId(R.id.main_parent), childAtPosition(
                allOf(withId(R.id.hearing_test_result_recyclerview), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        4)), 0), isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        RecyclerView recyclerView = (RecyclerView) mActivityTestRule.getActivity().findViewById(R.id.hearing_test_result_recyclerview);
        assertTrue(RECYCLER_VIEW_MESSAGE, recyclerView.getAdapter().getItemCount() == 1);

        ViewInteraction testNameText = onView(allOf(withId(R.id.testResultName), withText(TEST),
                childAtPosition(allOf(withId(R.id.main_parent), childAtPosition(
                        withId(R.id.hearing_test_result_recyclerview), 0)), 0),
                        isDisplayed()));
        testNameText.check(matches(withText(TEST)));
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

        ViewInteraction cancelButton = onView(allOf(withId(android.R.id.button2),
                withText(R.string.negative_button_dialog),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 2)));
        cancelButton.check(matches(isClickable()));
        cancelButton.perform(scrollTo(), click());

        RecyclerView recyclerView = (RecyclerView) mActivityTestRule.getActivity().findViewById(R.id.hearing_test_result_recyclerview);
        assertTrue(RECYCLER_VIEW_MESSAGE, recyclerView.getAdapter().getItemCount() == 1);

        ViewInteraction defaultTitle = onView(allOf(withId(R.id.testResultName), withText(HEARING_TEST),
                childAtPosition(allOf(withId(R.id.main_parent), childAtPosition(
                        withId(R.id.hearing_test_result_recyclerview), 0)), 0),
                        isDisplayed()));
        defaultTitle.check(matches(withText(HEARING_TEST)));
    }
}
