package com.ece493.group5.adjustableaudio;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MediaPlayerFragmentTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setUp()
    {
        Intent resultData = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(not(isInternal())).respondWith(result);
    }

    @Test
    public void testMediaPlayerFragmentUISetup()
    {
        ViewInteraction mediaPlayerTitle = onView(allOf(withText("Media Player"), childAtPosition(
                allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container),
                        0)), 0), isDisplayed()));
        mediaPlayerTitle.check(matches(withText("Media Player")));

        ViewInteraction mediaQueueList = onView(allOf(withId(R.id.mediaQueueRecyclerView),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 0), isDisplayed()));
        mediaQueueList.check(matches(isDisplayed()));

        ViewInteraction addMediaButton = onView(allOf(withId(R.id.addMediaButton),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1), 0), isDisplayed()));
        addMediaButton.check(matches(isDisplayed()));

        ViewInteraction progressBar = onView(allOf(withId(R.id.progressTrack), childAtPosition(
                childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1), 3), isDisplayed()));
        progressBar.check(matches(isDisplayed()));

        ViewInteraction mediaTime = onView(allOf(withId(R.id.mediaTime), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1),
                        4), isDisplayed()));
        mediaTime.check(matches(isDisplayed()));

        ViewInteraction playButton = onView(allOf(withId(R.id.playButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        5)), 1), isDisplayed()));
        playButton.check(matches(isDisplayed()));

        ViewInteraction skipPrevButton = onView(allOf(withId(R.id.skipPrevButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 5)),
                        0), isDisplayed()));
        skipPrevButton.check(matches(isDisplayed()));

        ViewInteraction skipForwardButton = onView(allOf(withId(R.id.skipForwardButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        5)), 2), isDisplayed()));
        skipForwardButton.check(matches(isDisplayed()));

        ViewInteraction volumeSeekBar = onView(allOf(withId(R.id.leftRightVolumeRatioSeekBar),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 6),
                1), isDisplayed()));
        volumeSeekBar.check(matches(isDisplayed()));

        ViewInteraction leftVolumeLabel = onView(allOf(withId(R.id.leftLabel), withText("Left\n(50%)"),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 0), isDisplayed()));
        leftVolumeLabel.check(matches(withText("Left\n(50%)")));

        ViewInteraction rightVolumeLabel = onView(allOf(withId(R.id.rightLabel), withText("Right\n(50%)"),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 2), isDisplayed()));
        rightVolumeLabel.check(matches(withText("Right\n(50%)")));
    }

    @Test
    public void testAddMedia()
    {
        ViewInteraction testAddMediaButton = onView(allOf(withId(R.id.addMediaButton),
                        childAtPosition(childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1), 0), isDisplayed()));
        testAddMediaButton.perform(click());
    }

    @Test
    public void testPlay()
    {
        ViewInteraction testPlayButton = onView(allOf(withId(R.id.playButton),
                        childAtPosition(allOf(withId(R.id.playButtonToolBar),
                                childAtPosition(withClassName(is("android.widget.LinearLayout")),
                                        5)),
                                1), isDisplayed()));
        testPlayButton.perform(click());
    }

    @Test
    public void testSkipForward()
    {
        ViewInteraction testSkipForwardButton = onView(allOf(withId(R.id.skipForwardButton),
                        childAtPosition(allOf(withId(R.id.playButtonToolBar),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5)), 2), isDisplayed()));
        testSkipForwardButton.perform(click());
    }

    @Test
    public void testSkipPrevious()
    {
        ViewInteraction testSkipPreviousButton = onView(allOf(withId(R.id.skipPrevButton),
                        childAtPosition(allOf(withId(R.id.playButtonToolBar),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5)), 0), isDisplayed()));
        testSkipPreviousButton.perform(click());
    }

    @Test
    public void testLeftRightRatioSeekBar()
    {
        onView(allOf(withId(R.id.leftRightVolumeRatioSeekBar), childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 6),
                1))).perform(setProgress(75));

        ViewInteraction leftVolumeLabel = onView(allOf(withId(R.id.leftLabel), withText("Left\n(25%)"),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 0), isDisplayed()));
        leftVolumeLabel.check(matches(withText("Left\n(25%)")));

        ViewInteraction rightVolumeLabel = onView(allOf(withId(R.id.rightLabel), withText("Right\n(75%)"),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        6), 2), isDisplayed()));
        rightVolumeLabel.check(matches(withText("Right\n(75%)")));
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
