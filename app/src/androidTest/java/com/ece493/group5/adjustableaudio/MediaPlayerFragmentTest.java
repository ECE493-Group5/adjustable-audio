package com.ece493.group5.adjustableaudio;


import android.view.View;

import org.hamcrest.core.IsInstanceOf;
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
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MediaPlayerFragmentTest extends BaseInstrumentedTest
{
    private static final String LEFT_VOLUME_LABEL = "Left\n(50%)";
    private static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    private static final String MEDIA_PLAYER = "Media Player";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String NEW_LEFT_VOLUME_LABEL = "Left\n(25%)";
    private static final String NEW_RIGHT_VOLUME_LABEL = "Right\n(75%)";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String RIGHT_VOLUME_LABEL = "Right\n(50%)";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Test
    public void testMediaPlayerFragmentUISetup()
    {
        ViewInteraction mediaPlayerTitle = onView(allOf(withText(MEDIA_PLAYER), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        mediaPlayerTitle.check(matches(withText(MEDIA_PLAYER)));

        ViewInteraction mediaQueueList = onView(allOf(withId(R.id.mediaQueueRecyclerView),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 0), isDisplayed()));
        mediaQueueList.check(matches(isDisplayed()));

        RecyclerView mediaQueueView = mainActivityTestRule.getActivity().findViewById(R.id.mediaQueueRecyclerView);
        assertNotNull(mediaQueueView.getAdapter());
        assertEquals(0, mediaQueueView.getAdapter().getItemCount());

        ViewInteraction addMediaButton = onView(allOf(withId(R.id.addMediaButton),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1), 0), isDisplayed()));
        addMediaButton.check(matches(isDisplayed()));
        addMediaButton.check(matches(isClickable()));

        ViewInteraction progressBar = onView(allOf(withId(R.id.progressTrack), childAtPosition(
                childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1), 3), isDisplayed()));
        progressBar.check(matches(isDisplayed()));
        progressBar.check(matches(isEnabled()));

        ViewInteraction mediaTime = onView(allOf(withId(R.id.mediaTime), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1),
                        4), isDisplayed()));
        mediaTime.check(matches(isDisplayed()));

        ViewInteraction playButton = onView(allOf(withId(R.id.playButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        5)), 1), isDisplayed()));
        playButton.check(matches(isDisplayed()));
        playButton.check(matches(isClickable()));

        ViewInteraction skipPrevButton = onView(allOf(withId(R.id.skipPrevButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 5)),
                        0), isDisplayed()));
        skipPrevButton.check(matches(isDisplayed()));
        skipPrevButton.check(matches(isClickable()));

        ViewInteraction skipForwardButton = onView(allOf(withId(R.id.skipForwardButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        5)), 2), isDisplayed()));
        skipForwardButton.check(matches(isDisplayed()));
        skipForwardButton.check(matches(isClickable()));

        ViewInteraction volumeSeekBar = onView(allOf(withId(R.id.leftRightVolumeRatioSeekBar),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 6),
                1), isDisplayed()));
        volumeSeekBar.check(matches(isDisplayed()));
        volumeSeekBar.check(matches(isEnabled()));

        ViewInteraction leftVolumeLabel = onView(allOf(withId(R.id.leftLabel), withText(LEFT_VOLUME_LABEL),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 0), isDisplayed()));
        leftVolumeLabel.check(matches(withText(LEFT_VOLUME_LABEL)));

        ViewInteraction rightVolumeLabel = onView(allOf(withId(R.id.rightLabel), withText(RIGHT_VOLUME_LABEL),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 2), isDisplayed()));
        rightVolumeLabel.check(matches(withText(RIGHT_VOLUME_LABEL)));
    }

    @Test
    public void testPlay()
    {
        ViewInteraction testPlayButton = onView(allOf(withId(R.id.playButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 1), isDisplayed()));
        testPlayButton.perform(click());
    }

    @Test
    public void testSkipForward()
    {
        ViewInteraction testSkipForwardButton = onView(allOf(withId(R.id.skipForwardButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 2), isDisplayed()));
        testSkipForwardButton.perform(click());
    }

    @Test
    public void testSkipPrevious()
    {
        ViewInteraction testSkipPreviousButton = onView(allOf(withId(R.id.skipPrevButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 0), isDisplayed()));
        testSkipPreviousButton.perform(click());
    }

    @Test
    public void testLeftRightRatioSeekBar()
    {
        onView(allOf(withId(R.id.leftRightVolumeRatioSeekBar), childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 6),
                1))).perform(SeekBarAction.setProgress(75));

        ViewInteraction leftVolumeLabel = onView(allOf(withId(R.id.leftLabel), withText(NEW_LEFT_VOLUME_LABEL),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 0), isDisplayed()));
        leftVolumeLabel.check(matches(withText(NEW_LEFT_VOLUME_LABEL)));

        ViewInteraction rightVolumeLabel = onView(allOf(withId(R.id.rightLabel), withText(NEW_RIGHT_VOLUME_LABEL),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 2), isDisplayed()));
        rightVolumeLabel.check(matches(withText(NEW_RIGHT_VOLUME_LABEL)));
    }
}
