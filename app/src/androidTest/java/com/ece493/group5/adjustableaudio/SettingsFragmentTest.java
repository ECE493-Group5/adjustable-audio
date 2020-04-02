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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest extends BaseInstrumentedTest
{
    private static final String DEFAULT = "Default";
    private static final String EQ_FREQ_BAND_1 = "60 Hz";
    private static final String EQ_FREQ_BAND_2 = "230 Hz";
    private static final String EQ_FREQ_BAND_3 = "910 Hz";
    private static final String EQ_FREQ_BAND_4 = "3600 Hz";
    private static final String EQ_FREQ_BAND_5 = "14000 Hz";
    private static final String LEFT_VOLUME_LABEL = "Left\n(50%)";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String NEGATIVE_EIGHT_DB = "-8dB";
    private static final String NEGATIVE_TWELVE_DB = "-12dB";
    private static final String NEW_LEFT_VOLUME_LABEL = "Left\n(25%)";
    private static final String NEW_RIGHT_VOLUME_LABEL = "Right\n(75%)";
    private static final String NINE_DB = "9dB";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String RIGHT_VOLUME_LABEL = "Right\n(50%)";
    private static final String SETTINGS = "Settings";
    private static final String SEVEN_DB = "7dB";
    private static final String THIRTEEN_DB = "13dB";
    private static final String THREE_DB = "3dB";
    private static final String TOTAL_VOLUME = "15 / 15";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String ZERO_DB = "0dB";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Before
    public void navigateToSettingsFragmentTest()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription(SETTINGS), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.perform(click());
    }

    @Test
    public void testSettingsFragmentUISetup()
    {
        ViewInteraction settingsTitle = onView(allOf(withText(SETTINGS), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        settingsTitle.check(matches(withText(SETTINGS)));

        ViewInteraction presetTitle = onView(allOf(withId(R.id.presetTitle), withText(R.string.title_preset),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        0), 0), isDisplayed()));
        presetTitle.check(matches(withText(R.string.title_preset)));

        ViewInteraction currentPreset = onView(allOf(withId(android.R.id.text1), withText(DEFAULT),
                childAtPosition(allOf(withId(R.id.presetSpinner), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1)),
                        0), isDisplayed()));
        currentPreset.check(matches(withText(DEFAULT)));

        ViewInteraction spinner = onView(allOf(withId(R.id.presetSpinner), childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 0), 1),
                isDisplayed()));
        spinner.check(matches(isDisplayed()));

        ViewInteraction equalizerTitle = onView(allOf(withId(R.id.equalizerTitle), withText(R.string.title_equalizer),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 2), isDisplayed()));
        equalizerTitle.check(matches(withText(R.string.title_equalizer)));

        ViewInteraction firstEqRange = onView(allOf(withId(R.id.equalizerBandTitle1), withText(EQ_FREQ_BAND_1),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        0), isDisplayed()));
        firstEqRange.check(matches(withText(EQ_FREQ_BAND_1)));

        ViewInteraction firstEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 0), 1),
                isDisplayed()));
        firstEqBand.check(matches(isDisplayed()));

        ViewInteraction firstEqValue = onView(allOf(withId(R.id.equalizerBandValue1), withText(THREE_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        firstEqValue.check(matches(withText(THREE_DB)));

        ViewInteraction secondEqRange = onView(allOf(withId(R.id.equalizerBandTitle2), withText(EQ_FREQ_BAND_2),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        0), isDisplayed()));
        secondEqRange.check(matches(withText(EQ_FREQ_BAND_2)));

        ViewInteraction secondEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 1), 1),
                isDisplayed()));
        secondEqBand.check(matches(isDisplayed()));

        ViewInteraction secondEqBandValue = onView(allOf(withId(R.id.equalizerBandValue2), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        1), 2), isDisplayed()));
        secondEqBandValue.check(matches(withText(ZERO_DB)));

        ViewInteraction thirdEqRange = onView(allOf(withId(R.id.equalizerBandTitle3), withText(EQ_FREQ_BAND_3),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        0), isDisplayed()));
        thirdEqRange.check(matches(withText(EQ_FREQ_BAND_3)));

        ViewInteraction thirdEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar3),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        1), isDisplayed()));
        thirdEqBand.check(matches(isDisplayed()));

        ViewInteraction thirdEqBandValue = onView(allOf(withId(R.id.equalizerBandValue3), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        thirdEqBandValue.check(matches(withText(ZERO_DB)));

        ViewInteraction fourthEqRange = onView(allOf(withId(R.id.equalizerBandTitle4), withText(EQ_FREQ_BAND_4),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        0), isDisplayed()));
        fourthEqRange.check(matches(withText(EQ_FREQ_BAND_4)));

        ViewInteraction fourthEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar4),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        1), isDisplayed()));
        fourthEqBand.check(matches(isDisplayed()));

        ViewInteraction fourthEqValue = onView(allOf(withId(R.id.equalizerBandValue4), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        fourthEqValue.check(matches(withText(ZERO_DB)));

        ViewInteraction fifthEqRange = onView(allOf(withId(R.id.equalizerBandTitle5), withText(EQ_FREQ_BAND_5),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        0), isDisplayed()));
        fifthEqRange.check(matches(withText(EQ_FREQ_BAND_5)));

        ViewInteraction fifthEqBand = onView(allOf(withId(R.id.equalizerBandSeekbar5),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        1), isDisplayed()));
        fifthEqBand.check(matches(isDisplayed()));

        ViewInteraction fifthEqValue = onView(allOf(withId(R.id.equalizerBandValue5), withText(THREE_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        fifthEqValue.check(matches(withText(THREE_DB)));

        ViewInteraction volumeTitle = onView(allOf(withId(R.id.volumeTitle), withText(R.string.title_volume),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                        5), isDisplayed()));
        volumeTitle.check(matches(withText(R.string.title_volume)));

        ViewInteraction globalVolumeTitle = onView(allOf(withText(R.string.title_global), childAtPosition(
                childAtPosition(withId(R.id.volumeTableLayout), 0), 0),
                isDisplayed()));
        globalVolumeTitle.check(matches(withText(R.string.title_global)));

        ViewInteraction globalVolume = onView(allOf(withId(R.id.settingsGlobalVolumeSeekbar),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 0),
                        1), isDisplayed()));
        globalVolume.check(matches(isDisplayed()));

        ViewInteraction leftVolumeLabel = onView(allOf(withId(R.id.leftVolumeLabel),
                withText(LEFT_VOLUME_LABEL), childAtPosition(childAtPosition(
                        withId(R.id.volumeTableLayout), 1), 0), isDisplayed()));
        leftVolumeLabel.check(matches(withText(LEFT_VOLUME_LABEL)));

        ViewInteraction audioBalance = onView(allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 1),
                        1), isDisplayed()));
        audioBalance.check(matches(isDisplayed()));

        ViewInteraction rightVolumeLabel = onView(allOf(withId(R.id.rightVolumeLabel),
                withText(RIGHT_VOLUME_LABEL), childAtPosition(childAtPosition(withId(R.id.volumeTableLayout),
                        1), 2), isDisplayed()));
        rightVolumeLabel.check(matches(withText(RIGHT_VOLUME_LABEL)));
    }

    @Test
    public void testFirstEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(2800));

        ViewInteraction checkEqBand1 = onView(allOf(withId(R.id.equalizerBandValue1),
                withText(THIRTEEN_DB), childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        0), 2), isDisplayed()));
        checkEqBand1.check(matches(withText(THIRTEEN_DB)));
    }

    @Test
    public void testSecondEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 1), 1)))
                .perform(SeekBarAction.setProgress(300));

        ViewInteraction checkEqBand2 = onView(allOf(withId(R.id.equalizerBandValue2),
                withText(NEGATIVE_TWELVE_DB), childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        1), 2), isDisplayed()));
        checkEqBand2.check(matches(withText(NEGATIVE_TWELVE_DB)));
    }

    @Test
    public void testThirdEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar3), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 2), 1)))
                .perform(SeekBarAction.setProgress(2200));

        ViewInteraction checkEqBand3 = onView(allOf(withId(R.id.equalizerBandValue3), withText(SEVEN_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        2), 2), isDisplayed()));
        checkEqBand3.check(matches(withText(SEVEN_DB)));
    }

    @Test
    public void testFourthEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar4), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 3), 1)))
                .perform(SeekBarAction.setProgress(700));

        ViewInteraction checkEqBand4 = onView(allOf(withId(R.id.equalizerBandValue4),
                withText(NEGATIVE_EIGHT_DB), childAtPosition(childAtPosition(
                        withId(R.id.equalizerTableLayout), 3), 2),
                        isDisplayed()));
        checkEqBand4.check(matches(withText(NEGATIVE_EIGHT_DB)));
    }

    @Test
    public void testFifthEqualizerBand()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar5), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 4), 1)))
                .perform(SeekBarAction.setProgress(2400));

        ViewInteraction checkEqBand5 = onView(allOf(withId(R.id.equalizerBandValue5), withText(NINE_DB),
                        childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                                2), isDisplayed()));
        checkEqBand5.check(matches(withText(NINE_DB)));
    }

    @Test
    public void testGlobalVolumeSeekbar()
    {
        onView(allOf(withId(R.id.settingsGlobalVolumeSeekbar), childAtPosition(childAtPosition(
                withId(R.id.volumeTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(77));

        ViewInteraction globalVolTextView = onView(allOf(withId(R.id.settingsGlobalVolumeValue),
                withText(TOTAL_VOLUME), childAtPosition(childAtPosition(withId(R.id.volumeTableLayout),
                        0), 2), isDisplayed()));
        globalVolTextView.check(matches(withText(TOTAL_VOLUME)));
    }

    @Test
    public void testLeftRightVolumeSeekbar()
    {
        onView(allOf(withId(R.id.settingsLeftRightVolumeRatioSeekbar), childAtPosition(
                childAtPosition(withId(R.id.volumeTableLayout), 1),
                1))).perform(SeekBarAction.setProgress(75));

        ViewInteraction leftLabel = onView(allOf(withId(R.id.leftVolumeLabel), withText(NEW_LEFT_VOLUME_LABEL),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 1), 0),
                isDisplayed()));
        leftLabel.check(matches(withText(NEW_LEFT_VOLUME_LABEL)));

        ViewInteraction rightLabel = onView(allOf(withId(R.id.rightVolumeLabel), withText(NEW_RIGHT_VOLUME_LABEL),
                childAtPosition(childAtPosition(withId(R.id.volumeTableLayout), 1), 2),
                isDisplayed()));
        rightLabel.check(matches(withText(NEW_RIGHT_VOLUME_LABEL)));
    }
}
