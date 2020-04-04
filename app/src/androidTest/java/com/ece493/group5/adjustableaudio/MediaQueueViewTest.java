package com.ece493.group5.adjustableaudio;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
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
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Before
    public void setUp()
    {
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
    public void testChangingSongViaSelect()
    {
        ViewInteraction changeSong = onView(withId(R.id.mediaQueueRecyclerView));
        changeSong.perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
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