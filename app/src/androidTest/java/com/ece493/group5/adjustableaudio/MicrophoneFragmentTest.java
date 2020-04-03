package com.ece493.group5.adjustableaudio;


import android.view.View;

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
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MicrophoneFragmentTest extends BaseInstrumentedTest
{
    private static final String CONSTRAINT_LAYOUT = "androidx.constraintlayout.widget.ConstraintLayout";
    private static final String MICROPHONE = "Microphone";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String READ_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(READ_STORAGE,
                    MICROPHONE_PERMISSION,
                    WRITE_STORAGE);

    @Before
    public void setup()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_microphone),
                withContentDescription(MICROPHONE), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 1), isDisplayed()));
        bottomNavigationItemView.perform(click());
    }

    @Test
    public void testMicrophoneUISetup()
    {
        ViewInteraction textView = onView(allOf(withText(MICROPHONE), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        textView.check(matches(withText(MICROPHONE)));

        ViewInteraction microphoneButton = onView(allOf(withId(R.id.microphoneEnableButton), childAtPosition(
                childAtPosition(withId(R.id.nav_host_fragment), 0), 0),
                isDisplayed()));
        microphoneButton.check(matches(isDisplayed()));
        microphoneButton.check(matches(isClickable()));

        ViewInteraction speechButton = onView(allOf(withId(R.id.speechFocusToggleButton),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        1), 0), isDisplayed()));
        speechButton.check(matches(isDisplayed()));
        speechButton.check(matches(isClickable()));

        ViewInteraction normalButton = onView(allOf(withId(R.id.normalToggleButton),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        1), 1), isDisplayed()));
        normalButton.check(matches(isDisplayed()));
        normalButton.check(matches(isClickable()));

        ViewInteraction noiseFilterButton = onView(allOf(withId(R.id.noiseFilterToggleButton),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        1), 2), isDisplayed()));
        noiseFilterButton.check(matches(isDisplayed()));
        noiseFilterButton.check(matches(isClickable()));
    }

    @Test
    public void testMicrophoneButtonToggle()
    {
        ViewInteraction microphoneToggleButton = onView(allOf(withId(R.id.microphoneEnableButton),
                childAtPosition(childAtPosition(withId(R.id.nav_host_fragment), 0),
                        0), isDisplayed()));
        microphoneToggleButton.perform(click());

        microphoneToggleButton.check(matches(isChecked()));

        microphoneToggleButton.perform(click());

        microphoneToggleButton.check(matches(isNotChecked()));
    }

    @Test
    public void testSpeechFocusButtonToggle()
    {
        ViewInteraction speechFocusButton = onView(allOf(withId(R.id.speechFocusToggleButton),
                withText(R.string.speech_focus), childAtPosition(childAtPosition(
                        withClassName(is(CONSTRAINT_LAYOUT)),
                        1), 0), isDisplayed()));

        speechFocusButton.check(matches(isNotChecked()));

        speechFocusButton.perform(click());

        speechFocusButton.check(matches(isChecked()));
    }

    @Test
    public void testNoiseFilterButtonToggle()
    {
        ViewInteraction noiseFilterButton = onView(
                allOf(withId(R.id.noiseFilterToggleButton), withText(R.string.noise_filter),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(CONSTRAINT_LAYOUT)),
                                        1),
                                2),
                        isDisplayed()));

        noiseFilterButton.check(matches(isNotChecked()));
        noiseFilterButton.perform(click());
    }

    @Test
    public void testNormalFocusButtonToggle()
    {
        onView(allOf(withId(R.id.speechFocusToggleButton), withText(R.string.speech_focus),
                childAtPosition(childAtPosition(withClassName(
                        is(CONSTRAINT_LAYOUT)),
                        1), 0), isDisplayed())).perform(click());

        ViewInteraction normalButton = onView(allOf(withId(R.id.normalToggleButton),
                withText(R.string.normal), childAtPosition(childAtPosition(
                        withClassName(is(CONSTRAINT_LAYOUT)),
                        1), 1), isDisplayed()));

        normalButton.check(matches(isNotChecked()));

        normalButton.perform(click());

        normalButton.check(matches(isChecked()));
    }
}
