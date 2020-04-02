package com.ece493.group5.adjustableaudio;


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
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends BaseInstrumentedTest
{
    private static final String HEARING_TEST = "Hearing Test";
    private static final String MEDIA_PLAYER = "Media Player";
    private static final String MICROPHONE = "Microphone";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String SETTINGS = "Settings";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Test
    public void testMainActivityUISetup()
    {
        ViewInteraction hearingTestNav = onView(allOf(withId(R.id.navigation_hearing_test),
                withContentDescription(HEARING_TEST), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 0), isDisplayed()));
        hearingTestNav.check(matches(isDisplayed()));
        hearingTestNav.check(matches(isClickable()));

        ViewInteraction microphoneNav = onView(allOf(withId(R.id.navigation_microphone),
                withContentDescription(MICROPHONE), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 1), isDisplayed()));
        microphoneNav.check(matches(isDisplayed()));
        microphoneNav.check(matches(isClickable()));

        ViewInteraction mediaPlayerNav = onView(allOf(withId(R.id.navigation_media_player),
                withContentDescription(MEDIA_PLAYER), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 2), isDisplayed()));
        mediaPlayerNav.check(matches(isDisplayed()));
        mediaPlayerNav.check(matches(isSelected()));

        ViewInteraction settingsNav = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription(SETTINGS), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        settingsNav.check(matches(isDisplayed()));
        settingsNav.check(matches(isClickable()));
    }

    @Test
    public void testHearingTestNav()
    {
        ViewInteraction hearingTestNav = onView(allOf(withId(R.id.navigation_hearing_test),
                withContentDescription(HEARING_TEST), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 0), isDisplayed()));

        hearingTestNav.perform(click());
        hearingTestNav.check(matches(isSelected()));
    }

    @Test
    public void testMicrophoneNav()
    {
        ViewInteraction microphoneNav = onView(allOf(withId(R.id.navigation_microphone),
                withContentDescription(MICROPHONE), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 1), isDisplayed()));

        microphoneNav.perform(click());
        microphoneNav.check(matches(isSelected()));
    }

    @Test
    public void testMediaPlayerNav()
    {
        ViewInteraction mediaPlayerNav = onView(allOf(withId(R.id.navigation_media_player),
                withContentDescription(MEDIA_PLAYER), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 2), isDisplayed()));
        mediaPlayerNav.perform(click());
        mediaPlayerNav.check(matches(isSelected()));
    }

    @Test
    public void testSettingsNav()
    {
        ViewInteraction settingsNav = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription(SETTINGS), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        settingsNav.perform(click());
        settingsNav.check(matches(isSelected()));
    }
}
