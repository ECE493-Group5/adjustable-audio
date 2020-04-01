package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void navigateToSettingsFragmentTest()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription("Settings"), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.perform(click());
    }

    @Test
    public void testFirstEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(2800));

        ViewInteraction checkEqBand1 = onView(allOf(withId(R.id.equalizerBandValue1),
                withText("13dB"), childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        0), 2), isDisplayed()));
        checkEqBand1.check(matches(withText("13dB")));
    }

    @Test
    public void testSecondEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 1), 1)))
                .perform(SeekBarAction.setProgress(300));

        ViewInteraction checkEqBand2 = onView(allOf(withId(R.id.equalizerBandValue2),
                withText("-12dB"), childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        1), 2), isDisplayed()));
        checkEqBand2.check(matches(withText("-12dB")));
    }

    @Test
    public void testThirdEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar3), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 2), 1)))
                .perform(SeekBarAction.setProgress(2200));

        ViewInteraction checkEqBand3 = onView(allOf(withId(R.id.equalizerBandValue3), withText("7dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        2), 2), isDisplayed()));
        checkEqBand3.check(matches(withText("7dB")));
    }

    @Test
    public void testFourthEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar4), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 3), 1)))
                .perform(SeekBarAction.setProgress(700));

        ViewInteraction checkEqBand4 = onView(allOf(withId(R.id.equalizerBandValue4),
                withText("-8dB"), childAtPosition(childAtPosition(
                        withId(R.id.equalizerTableLayout), 3), 2),
                        isDisplayed()));
        checkEqBand4.check(matches(withText("-8dB")));
    }

    @Test
    public void testFifthEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar5), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 4), 1)))
                .perform(SeekBarAction.setProgress(2400));

        ViewInteraction checkEqBand5 = onView(allOf(withId(R.id.equalizerBandValue5), withText("9dB"),
                        childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                                2), isDisplayed()));
        checkEqBand5.check(matches(withText("9dB")));
    }

    @Test
    public void testGlobalVolumeSeekbar()
    {
        onView(allOf(withId(R.id.settingsGlobalVolumeSeekbar), childAtPosition(childAtPosition(
                withId(R.id.volumeTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(77));

        ViewInteraction globalVolTextView = onView(allOf(withId(R.id.settingsGlobalVolumeValue),
                withText("77%"), childAtPosition(childAtPosition(withId(R.id.volumeTableLayout),
                        0), 2), isDisplayed()));
        globalVolTextView.check(matches(withText("77%")));
    }

    @Test
    public void testLeftRightVolumeSeekbar()
    {
        onView(allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar), childAtPosition(
                childAtPosition(withId(R.id.volumeTableLayout), 1),
                1))).perform(SeekBarAction.setProgress(75));
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
