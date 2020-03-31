package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.matcher.ViewMatchers;
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
public class BasicSettingsFragmentTest {

    @Rule
    public ActivityTestRule<DisclaimerActivity> mActivityTestRule = new ActivityTestRule<>(DisclaimerActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void basicSettingsFragmentTest() {
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
                allOf(withId(R.id.navigation_settings), withContentDescription("Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.presetTitle), withText("Preset"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Preset")));

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("Default"),
                        childAtPosition(
                                allOf(withId(R.id.presetSpinner),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Default")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.equalizerTitle), withText("Equalizer"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Equalizer")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.equalizerBandTitle1), withText("60 Hz"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("60 Hz")));

        ViewInteraction seekBar = onView(
                allOf(withId(R.id.equalizerBandSeekbar1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        0),
                                1),
                        isDisplayed()));
        seekBar.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.equalizerBandValue1), withText("3dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        0),
                                2),
                        isDisplayed()));
        textView5.check(matches(withText("3dB")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.equalizerBandTitle2), withText("230 Hz"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        1),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("230 Hz")));

        ViewInteraction seekBar2 = onView(
                allOf(withId(R.id.equalizerBandSeekbar2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        1),
                                1),
                        isDisplayed()));
        seekBar2.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.equalizerBandValue2), withText("0dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        1),
                                2),
                        isDisplayed()));
        textView7.check(matches(withText("0dB")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.equalizerBandTitle3), withText("910 Hz"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        2),
                                0),
                        isDisplayed()));
        textView8.check(matches(withText("910 Hz")));

        ViewInteraction seekBar3 = onView(
                allOf(withId(R.id.equalizerBandSeekbar3),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        2),
                                1),
                        isDisplayed()));
        seekBar3.check(matches(isDisplayed()));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        2),
                                2),
                        isDisplayed()));
        textView9.check(matches(withText("0dB")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.equalizerBandTitle4), withText("3600 Hz"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        3),
                                0),
                        isDisplayed()));
        textView10.check(matches(withText("3600 Hz")));

        ViewInteraction seekBar4 = onView(
                allOf(withId(R.id.equalizerBandSeekbar4),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        3),
                                1),
                        isDisplayed()));
        seekBar4.check(matches(isDisplayed()));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.equalizerBandValue4), withText("0dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        3),
                                2),
                        isDisplayed()));
        textView11.check(matches(withText("0dB")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.equalizerBandTitle5), withText("14000 Hz"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        4),
                                0),
                        isDisplayed()));
        textView12.check(matches(withText("14000 Hz")));

        ViewInteraction seekBar5 = onView(
                allOf(withId(R.id.equalizerBandSeekbar5),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        4),
                                1),
                        isDisplayed()));
        seekBar5.check(matches(isDisplayed()));

        ViewInteraction textView13 = onView(
                allOf(withId(R.id.equalizerBandValue5), withText("3dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        4),
                                2),
                        isDisplayed()));
        textView13.check(matches(withText("3dB")));

        ViewInteraction textView14 = onView(
                allOf(withText("Global"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        0),
                                0),
                        isDisplayed()));
        textView14.check(matches(withText("Global")));

        ViewInteraction seekBar6 = onView(
                allOf(withId(R.id.settingsGlobalVolumeSeekbar),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        0),
                                1),
                        isDisplayed()));
        seekBar6.check(matches(isDisplayed()));

        ViewInteraction textView15 = onView(
                allOf(withText("Left"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        1),
                                0),
                        isDisplayed()));
        textView15.check(matches(withText("Left")));

        ViewInteraction seekBar7 = onView(
                allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        1),
                                1),
                        isDisplayed()));
        seekBar7.check(matches(isDisplayed()));

        ViewInteraction textView16 = onView(
                allOf(withText("Right"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        1),
                                2),
                        isDisplayed()));
        textView16.check(matches(withText("Right")));

        onView(allOf(withId(R.id.equalizerBandSeekbar1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        0),
                                1))).perform(setProgress(2800));

        ViewInteraction textView17 = onView(
                allOf(withId(R.id.equalizerBandValue1), withText("13dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        0),
                                2),
                        isDisplayed()));
        textView17.check(matches(withText("13dB")));

        onView(allOf(withId(R.id.equalizerBandSeekbar2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        1),
                                1))).perform(setProgress(300));

        ViewInteraction textView18 = onView(
                allOf(withId(R.id.equalizerBandValue2), withText("-12dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        1),
                                2),
                        isDisplayed()));
        textView18.check(matches(withText("-12dB")));

        onView(allOf(withId(R.id.equalizerBandSeekbar3),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        2),
                                1))).perform(setProgress(2200));

        ViewInteraction textView19 = onView(
                allOf(withId(R.id.equalizerBandValue3), withText("7dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        2),
                                2),
                        isDisplayed()));
        textView19.check(matches(withText("7dB")));

        onView(allOf(withId(R.id.equalizerBandSeekbar4),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        3),
                                1))).perform(setProgress(700));

        ViewInteraction textView20 = onView(
                allOf(withId(R.id.equalizerBandValue4), withText("-8dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        3),
                                2),
                        isDisplayed()));
        textView20.check(matches(withText("-8dB")));

        onView(allOf(withId(R.id.equalizerBandSeekbar5),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        4),
                                1))).perform(setProgress(2400));

        ViewInteraction textView21 = onView(
                allOf(withId(R.id.equalizerBandValue5), withText("9dB"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.equalizerTableLayout),
                                        4),
                                2),
                        isDisplayed()));
        textView21.check(matches(withText("9dB")));

        onView(allOf(withId(R.id.settingsGlobalVolumeSeekbar),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        0),
                                1))).perform(setProgress(77));

        ViewInteraction textView22 = onView(
                allOf(withId(R.id.settingsGlobalVolumeValue), withText("77%"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        0),
                                2),
                        isDisplayed()));
        textView22.check(matches(withText("77%")));

        onView(allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.volumeTableLayout),
                                        1),
                                1))).perform(setProgress(75));
    }

    private static ViewAction setProgress(final int progress)
    {
        return new ViewAction()
        {
            @Override
            public void perform(UiController uiController, View view)
            {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription()
            {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints()
            {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
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
