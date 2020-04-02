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
    public void testSettingsFragmentUISetup()
    {
        ViewInteraction settingsTitle = onView(allOf(withText("Settings"), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        settingsTitle.check(matches(withText("Settings")));

        ViewInteraction presetTitle = onView(allOf(withId(R.id.presetTitle), withText("Preset"),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        0), 0), isDisplayed()));
        presetTitle.check(matches(withText("Preset")));

        ViewInteraction currentPreset = onView(allOf(withId(android.R.id.text1), withText("Default"),
                childAtPosition(allOf(withId(R.id.presetSpinner), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1)),
                        0), isDisplayed()));
        currentPreset.check(matches(withText("Default")));

        ViewInteraction spinner = onView(allOf(withId(R.id.presetSpinner), childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 0), 1),
                isDisplayed()));
        spinner.check(matches(isDisplayed()));

        ViewInteraction equalizerTitle = onView(allOf(withId(R.id.equalizerTitle), withText("Equalizer"),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 2), isDisplayed()));
        equalizerTitle.check(matches(withText("Equalizer")));

        ViewInteraction firstEqRange = onView(allOf(withId(R.id.equalizerBandTitle1), withText("60 Hz"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        0), isDisplayed()));
        firstEqRange.check(matches(withText("60 Hz")));

        ViewInteraction firstEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 0), 1),
                isDisplayed()));
        firstEqBand.check(matches(isDisplayed()));

        ViewInteraction firstEqValue = onView(allOf(withId(R.id.equalizerBandValue1), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        firstEqValue.check(matches(withText("3dB")));

        ViewInteraction secondEqRange = onView(allOf(withId(R.id.equalizerBandTitle2), withText("230 Hz"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        0), isDisplayed()));
        secondEqRange.check(matches(withText("230 Hz")));

        ViewInteraction secondEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 1), 1),
                isDisplayed()));
        secondEqBand.check(matches(isDisplayed()));

        ViewInteraction secondEqBandValue = onView(allOf(withId(R.id.equalizerBandValue2), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        1), 2), isDisplayed()));
        secondEqBandValue.check(matches(withText("0dB")));

        ViewInteraction thirdEqRange = onView(allOf(withId(R.id.equalizerBandTitle3), withText("910 Hz"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        0), isDisplayed()));
        thirdEqRange.check(matches(withText("910 Hz")));

        ViewInteraction thirdEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar3),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        1), isDisplayed()));
        thirdEqBand.check(matches(isDisplayed()));

        ViewInteraction thirdEqBandValue = onView(allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        thirdEqBandValue.check(matches(withText("0dB")));

        ViewInteraction fourthEqRange = onView(allOf(withId(R.id.equalizerBandTitle4), withText("3600 Hz"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        0), isDisplayed()));
        fourthEqRange.check(matches(withText("3600 Hz")));

        ViewInteraction fourthEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar4),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        1), isDisplayed()));
        fourthEqBand.check(matches(isDisplayed()));

        ViewInteraction fourthEqValue = onView(allOf(withId(R.id.equalizerBandValue4), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        fourthEqValue.check(matches(withText("0dB")));

        ViewInteraction fifthEqRange = onView(allOf(withId(R.id.equalizerBandTitle5), withText("14000 Hz"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        0), isDisplayed()));
        fifthEqRange.check(matches(withText("14000 Hz")));

        ViewInteraction fifthEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar5),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        1), isDisplayed()));
        fifthEqBand.check(matches(isDisplayed()));

        ViewInteraction fifthEqValue = onView(allOf(withId(R.id.equalizerBandValue5), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        fifthEqValue.check(matches(withText("3dB")));

        ViewInteraction volumeTitle = onView(allOf(withId(R.id.volumeTitle), withText("Volume"),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                        5), isDisplayed()));
        volumeTitle.check(matches(withText("Volume")));

        ViewInteraction globalVolumeTitle = onView(allOf(withText("Global"), childAtPosition(
                childAtPosition(withId(R.id.volumeTableLayout), 0), 0),
                isDisplayed()));
        globalVolumeTitle.check(matches(withText("Global")));

        ViewInteraction globalVolume = onView(allOf(withId(R.id.settingsGlobalVolumeSeekbar),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 0),
                        1), isDisplayed()));
        globalVolume.check(matches(isDisplayed()));

        ViewInteraction leftVolumeLabel = onView(allOf(withId(R.id.leftVolumeLabel),
                withText("Left\n(50%)"), childAtPosition(childAtPosition(
                        withId(R.id.volumeTableLayout), 1), 0), isDisplayed()));
        leftVolumeLabel.check(matches(withText("Left\n(50%)")));

        ViewInteraction audioBalance = onView(allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 1),
                        1), isDisplayed()));
        audioBalance.check(matches(isDisplayed()));

        ViewInteraction rightVolumeLabel = onView(allOf(withId(R.id.rightVolumeLabel),
                withText("Right\n(50%)"), childAtPosition(childAtPosition(withId(R.id.volumeTableLayout),
                        1), 2), isDisplayed()));
        rightVolumeLabel.check(matches(withText("Right\n(50%)")));
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
                withText("15 / 15"), childAtPosition(childAtPosition(withId(R.id.volumeTableLayout),
                        0), 2), isDisplayed()));
        globalVolTextView.check(matches(withText("15 / 15")));
    }

    @Test
    public void testLeftRightVolumeSeekbar()
    {
        onView(allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar), childAtPosition(
                childAtPosition(withId(R.id.volumeTableLayout), 1),
                1))).perform(SeekBarAction.setProgress(75));

        ViewInteraction leftLabel = onView(allOf(withId(R.id.leftVolumeLabel), withText("Left\n(25%)"),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 1), 0),
                isDisplayed()));
        leftLabel.check(matches(withText("Left\n(25%)")));

        ViewInteraction rightLabel = onView(allOf(withId(R.id.rightVolumeLabel), withText("Right\n(75%)"),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 1), 2),
                isDisplayed()));
        rightLabel.check(matches(withText("Right\n(75%)")));
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
