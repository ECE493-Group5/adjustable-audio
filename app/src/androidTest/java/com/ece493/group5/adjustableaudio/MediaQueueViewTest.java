package com.ece493.group5.adjustableaudio;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;

import com.ece493.group5.adjustableaudio.services.MusicService;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.rule.ServiceTestRule;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MediaQueueViewTest extends BaseInstrumentedTest
{
    private static final String EMPTY_VIEW_MESSAGE = "Recycler view should not be empty";
    private static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";


    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Rule
    public ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Before
    public void setUp()
    {
        Intent service = new Intent(ApplicationProvider.getApplicationContext(), MusicService.class);

        try {
            serviceTestRule.startService(service);
        } catch (TimeoutException e)
        {
            e.printStackTrace();
        }

        Intent resultData = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(not(isInternal())).respondWith(result);

        //Add Two Songs
        addSongToMediaQueue();
        addSongToMediaQueue();
    }

    private void addSongToMediaQueue()
    {
        ViewInteraction appCompatImageButton = onView(allOf(withId(R.id.addMediaButton),
                childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 1), 0),
                isDisplayed()));
        appCompatImageButton.perform(click());
    }

    @Test
    public void testAddMedia()
    {
        ViewInteraction testAddMediaButton = onView(allOf(withId(R.id.addMediaButton),
                childAtPosition(childAtPosition(withClassName(is(LINEAR_LAYOUT)),
                        1), 0), isDisplayed()));
        testAddMediaButton.perform(click());

        RecyclerView mediaQueueRecyclerView = (RecyclerView) intentsTestRule.getActivity()
                .findViewById(R.id.mediaQueueRecyclerView);
        RecyclerView.Adapter mediaQueueAdapter = mediaQueueRecyclerView.getAdapter();

        assertNotNull(mediaQueueAdapter);
        assertTrue(EMPTY_VIEW_MESSAGE, mediaQueueAdapter.getItemCount()>=3);
    }

    @Test
    public void testPlayMediaWithPausingBetweenSongs()
    {
        onView(withId(R.id.mediaQueueRecyclerView)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));

        ViewInteraction testPlayButton = onView(allOf(withId(R.id.playButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 1), isDisplayed()));
        testPlayButton.perform(click());

        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        testPlayButton.perform(click());

        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        ViewInteraction testSkipForwardButton = onView(allOf(withId(R.id.skipForwardButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 2), isDisplayed()));
        testSkipForwardButton.perform(click());

        assertEquals(1, MusicService.getInstance().getMediaPlayerAdapter().getQueueIndex());
        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        ViewInteraction testSkipPreviousButton = onView(allOf(withId(R.id.skipPrevButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 0), isDisplayed()));
        testSkipPreviousButton.perform(click());

        assertEquals(0, MusicService.getInstance().getMediaPlayerAdapter().getQueueIndex());
        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
    }

    @Test
    public void testPlayMedia()
    {
        onView(withId(R.id.mediaQueueRecyclerView)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));

        ViewInteraction testPlayButton = onView(allOf(withId(R.id.playButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 1), isDisplayed()));
        testPlayButton.perform(click());

        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        ViewInteraction testSkipForwardButton = onView(allOf(withId(R.id.skipForwardButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 2), isDisplayed()));
        testSkipForwardButton.perform(click());

        assertEquals(1, MusicService.getInstance().getMediaPlayerAdapter().getQueueIndex());
        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        ViewInteraction testSkipPreviousButton = onView(allOf(withId(R.id.skipPrevButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 0), isDisplayed()));
        testSkipPreviousButton.perform(click());

        assertEquals(0, MusicService.getInstance().getMediaPlayerAdapter().getQueueIndex());
        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
    }

    @Test
    public void testSeekDuration()
    {
        onView(withId(R.id.mediaQueueRecyclerView)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));

        try {
            onView(allOf(withId(R.id.progressTrack), childAtPosition(
                    childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                            1), 3), isDisplayed()))
                    .perform(SeekBarAction.setProgress(-1));
        } catch (Exception e)
        {
            assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
        }

        ViewInteraction testPlayButton = onView(allOf(withId(R.id.playButton),
                childAtPosition(allOf(withId(R.id.playButtonToolBar), childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 5)), 1), isDisplayed()));
        testPlayButton.perform(click());

        SeekBar seekBar =  intentsTestRule.getActivity().findViewById(R.id.progressTrack);

        try {
            onView(allOf(withId(R.id.progressTrack), childAtPosition(
                    childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                            1), 3), isDisplayed()))
                    .perform(SeekBarAction.setProgress(seekBar.getMax() + 1));
        } catch (Exception e)
        {
            assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
        }

        onView(allOf(withId(R.id.progressTrack), childAtPosition(
                childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1), 3), isDisplayed()))
                .perform(SeekBarAction.setProgress(seekBar.getMax()/2));

        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        testPlayButton.perform(click());

        onView(allOf(withId(R.id.progressTrack), childAtPosition(
                childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1), 3), isDisplayed()))
                .perform(SeekBarAction.setProgress(seekBar.getMax()/3));

        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
    }

    @Test
    public void testChangingSongViaSelect()
    {
        ViewInteraction changeSong = onView(withId(R.id.mediaQueueRecyclerView));
        changeSong.perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        ViewInteraction textView3 = onView(allOf(withId(R.id.labelSongTitle)));
        textView3.check((matches(not(withText("")))));

        ViewInteraction textView4 = onView(allOf(withId(R.id.labelArtist)));
        textView4.check((matches(not(withText("")))));
    }

    @Test
    public void testDeleteSongFromQueue()
    {
        RecyclerView mediaQueueRecyclerView = (RecyclerView) intentsTestRule.getActivity()
                .findViewById(R.id.mediaQueueRecyclerView);
        int originalSize = mediaQueueRecyclerView.getAdapter().getItemCount();

        onView(withId(R.id.mediaQueueRecyclerView)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, new GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.BOTTOM_RIGHT, GeneralLocation.BOTTOM_LEFT,
                Press.FINGER)));

        int currentSize = mediaQueueRecyclerView.getAdapter().getItemCount();

        assertEquals(originalSize-1, currentSize);
    }
}
