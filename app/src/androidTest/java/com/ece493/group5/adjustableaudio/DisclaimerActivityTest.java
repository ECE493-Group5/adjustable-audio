package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DisclaimerActivityTest {

    @Rule
    public ActivityTestRule<DisclaimerActivity> mActivityTestRule = new ActivityTestRule<>(DisclaimerActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void disclaimerActivityTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.disclaimerTitle), withText("Disclaimer"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Disclaimer")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.disclaimerText), withText("This software is provided on as-is basis without any warranty, responsibility, or liability. While the application is a tool which conducts amateur hearing tests, it should not be used as a medical standard in any situation. Moreover, the software is not, in any circumstance, intended for use in the diagnosis of disease or other conditions, or in the cure, mitigation, treatment, or prevention of disease. If the user is concerned about potential hearing loss, he or she should contact a professional audiologist or seek emergency medical attention if necessary. This software provides methods to export sensitive medical data to different applications by request of the user. The user should only do so if he or she is comfortable sharing their personal medical data."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("This software is provided on as-is basis without any warranty, responsibility, or liability. While the application is a tool which conducts amateur hearing tests, it should not be used as a medical standard in any situation. Moreover, the software is not, in any circumstance, intended for use in the diagnosis of disease or other conditions, or in the cure, mitigation, treatment, or prevention of disease. If the user is concerned about potential hearing loss, he or she should contact a professional audiologist or seek emergency medical attention if necessary. This software provides methods to export sensitive medical data to different applications by request of the user. The user should only do so if he or she is comfortable sharing their personal medical data.")));

        ViewInteraction checkBox = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        checkBox.check(matches(isDisplayed()));

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.checkBox), withText("I have read and understand the above disclaimer."),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialCheckBox.perform(scrollTo(), click());

        ViewInteraction button = onView(
                allOf(withId(R.id.continueButton),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                2),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.continueButton), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        materialButton.perform(scrollTo(), click());
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
