package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
public class test {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void test() {
        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.checkBox), withText("I have read and understand the above disclaimer."),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialCheckBox.perform(scrollTo(), click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.continueButton), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton.perform(scrollTo(), click());

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

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction floatingActionButton5 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton5.perform(click());

        ViewInteraction floatingActionButton6 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton6.perform(click());

        ViewInteraction floatingActionButton7 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton7.perform(click());

        ViewInteraction floatingActionButton8 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton8.perform(click());

        ViewInteraction floatingActionButton9 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton9.perform(click());

        ViewInteraction floatingActionButton10 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton10.perform(click());

        ViewInteraction floatingActionButton11 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton11.perform(click());

        ViewInteraction floatingActionButton12 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton12.perform(click());

        ViewInteraction floatingActionButton13 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton13.perform(click());

        ViewInteraction floatingActionButton14 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton14.perform(click());

        ViewInteraction floatingActionButton15 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton15.perform(click());

        ViewInteraction floatingActionButton16 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton16.perform(click());

        ViewInteraction floatingActionButton17 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton17.perform(click());

        ViewInteraction floatingActionButton18 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton18.perform(click());

        ViewInteraction floatingActionButton19 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton19.perform(click());

        ViewInteraction floatingActionButton20 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton20.perform(click());

        ViewInteraction floatingActionButton21 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton21.perform(click());

        ViewInteraction floatingActionButton22 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton22.perform(click());

        ViewInteraction floatingActionButton23 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton23.perform(click());

        ViewInteraction floatingActionButton24 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton24.perform(click());

        ViewInteraction floatingActionButton25 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton25.perform(click());

        ViewInteraction floatingActionButton26 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton26.perform(click());

        ViewInteraction floatingActionButton27 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton27.perform(click());

        ViewInteraction floatingActionButton28 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton28.perform(click());

        ViewInteraction floatingActionButton29 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton29.perform(click());

        ViewInteraction floatingActionButton30 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton30.perform(click());

        ViewInteraction floatingActionButton31 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton31.perform(click());

        ViewInteraction floatingActionButton32 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton32.perform(click());

        ViewInteraction floatingActionButton33 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton33.perform(click());

        ViewInteraction floatingActionButton34 = onView(
                allOf(withId(R.id.hearing_test_beep_ack_button),
                        childAtPosition(
                                allOf(withId(R.id.beep_ack_layout),
                                        childAtPosition(
                                                withId(R.id.hearing_test_layout),
                                                2)),
                                2),
                        isDisplayed()));
        floatingActionButton34.perform(click());

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
