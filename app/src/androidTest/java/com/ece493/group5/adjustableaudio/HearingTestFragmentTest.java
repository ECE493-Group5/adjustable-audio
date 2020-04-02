package com.ece493.group5.adjustableaudio;


import android.view.View;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class HearingTestFragmentTest extends BaseInstrumentedTest
{
    private static final String EMPTY_VIEW_MESSAGE = "Recycler view should be empty";
    private static final String HEARING_TEST = "Hearing Test";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Before
    public void setup()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_hearing_test),
                withContentDescription(HEARING_TEST), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 0), isDisplayed()));

        bottomNavigationItemView.check(matches(isClickable()));
        bottomNavigationItemView.perform(click());
    }

    @Test
    public void testHearingTestFragmentUI() {

        ViewInteraction hearingTestTitle = onView(allOf(withText(HEARING_TEST), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        hearingTestTitle.check(matches(withText(HEARING_TEST)));

        ViewInteraction audioTitle = onView(allOf(withId(R.id.virtual_audiologist_title),
                withText(R.string.title_virtual_audiologist), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 0), isDisplayed()));
        audioTitle.check(matches(withText(R.string.title_virtual_audiologist)));

        ViewInteraction instructionTitle = onView(allOf(withId(R.id.take_hearing_test_caption),
                withText(R.string.caption_take_hearing_test), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 1), isDisplayed()));
        instructionTitle.check(matches(withText(R.string.caption_take_hearing_test)));

        ViewInteraction newHearingTestButton = onView(allOf(withId(R.id.new_hearing_test_button),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                        2), isDisplayed()));
        newHearingTestButton.check(matches(isDisplayed()));

        newHearingTestButton.check(matches(isClickable()));

        ViewInteraction resultListTitle = onView(allOf(withId(R.id.hearing_test_result_caption),
                withText(R.string.caption_your_hearing_tests), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                        3), isDisplayed()));
        resultListTitle.check(matches(withText(R.string.caption_your_hearing_tests)));

        ViewInteraction resultList = onView(allOf(withId(R.id.hearing_test_result_recyclerview),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 4), isDisplayed()));
        resultList.check(matches(isDisplayed()));

        RecyclerView recyclerView = (RecyclerView) mActivityTestRule.getActivity().findViewById(R.id.hearing_test_result_recyclerview);
        assertTrue(EMPTY_VIEW_MESSAGE, recyclerView.getAdapter().getItemCount() == 0);

    }

    @Test
    public void testHearingTestButton()
    {
        ViewInteraction newHearingTestButton = onView(allOf(withId(R.id.new_hearing_test_button),
                childAtPosition(childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0), 2), isDisplayed()));
        newHearingTestButton.perform(click());
    }
}
