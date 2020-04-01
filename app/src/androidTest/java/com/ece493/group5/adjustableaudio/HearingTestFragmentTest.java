package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
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
public class HearingTestFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setup()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_hearing_test),
                withContentDescription("Hearing Test"), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 0), isDisplayed()));

        bottomNavigationItemView.perform(click());
    }

    @Test
    public void hearingTestFragmentUITest() {

        ViewInteraction textView = onView(allOf(withText("Hearing Test"), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        textView.check(matches(withText("Hearing Test")));

        ViewInteraction textView2 = onView(allOf(withId(R.id.virtual_audiologist_title),
                withText("Virtual Audiologist"), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 0), isDisplayed()));
        textView2.check(matches(withText("Virtual Audiologist")));

        ViewInteraction textView3 = onView(allOf(withId(R.id.take_hearing_test_caption),
                withText("Take a new hearing test!"), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 1), isDisplayed()));
        textView3.check(matches(withText("Take a new hearing test!")));

        ViewInteraction imageButton = onView(allOf(withId(R.id.new_hearing_test_button), childAtPosition(
                childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                2), isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(allOf(withId(R.id.hearing_test_result_caption),
                withText("Your hearing test results:"), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                        3), isDisplayed()));
        textView4.check(matches(withText("Your hearing test results:")));

        ViewInteraction recyclerView = onView(allOf(withId(R.id.hearing_test_result_recyclerview),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 4), isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }

    @Test
    public void testHearingTestButton()
    {
        ViewInteraction floatingActionButton = onView(allOf(withId(R.id.new_hearing_test_button),
                childAtPosition(childAtPosition(
                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                        0), 2), isDisplayed()));
        floatingActionButton.perform(click());
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
