package com.ece493.group5.adjustableaudio;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.ece493.group5.adjustableaudio.services.MusicService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.rule.ServiceTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class MusicNotificationTest extends BaseInstrumentedTest
{
    private static final String BASIC_SAMPLE_PACKAGE = "com.ece493.group5.adjustableaudio";
    private static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice mDevice;


    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Rule
    public ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    @Before
    public void startMainActivityFromHomeScreen()
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

        //Add Song
        addSongToMediaQueue();

        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    private void addSongToMediaQueue()
    {
        ViewInteraction appCompatImageButton = onView(allOf(withId(R.id.addMediaButton),
                childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 1), 0),
                isDisplayed()));
        appCompatImageButton.perform(click());
    }

    @After
    public void tearDown()
    {
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        ApplicationProvider.getApplicationContext().sendBroadcast(it);

        RecyclerView mediaQueueRecyclerView = (RecyclerView) intentsTestRule.getActivity()
                .findViewById(R.id.mediaQueueRecyclerView);
        int originalSize;

        while(true)
        {
            onView(withId(R.id.mediaQueueRecyclerView)).perform(RecyclerViewActions
                    .actionOnItemAtPosition(0, new GeneralSwipeAction(
                            Swipe.SLOW, GeneralLocation.BOTTOM_RIGHT, GeneralLocation.BOTTOM_LEFT,
                            Press.FINGER)));

            originalSize = mediaQueueRecyclerView.getAdapter().getItemCount();

            if(originalSize == 0)
            {
                break;
            }
        }

        mDevice.pressHome();
    }

    @Test
    public void testNotificationWithSingleSong()
    {
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text("AdjustableAudio")), LAUNCH_TIMEOUT);
        UiObject2 title = mDevice.findObject(By.text("AdjustableAudio"));
        assertEquals("AdjustableAudio", title.getText());

        title.click();
        UiObject2 nextButton = mDevice.findObject(By.desc("Next"));
        assertNull(nextButton);

        UiObject2 previousButton = mDevice.findObject(By.desc("Previous"));
        assertNull(previousButton);

        UiObject2 playButton = mDevice.findObject(By.desc("Play"));
        assertNotNull(playButton);

        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        playButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);

        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
        UiObject2 pauseButton = mDevice.findObject(By.desc("Pause"));
        assertNotNull(pauseButton);

        pauseButton = mDevice.findObject(By.desc("Pause"));
        pauseButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);
        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
    }

    @Test
    public void testNotificationWithThreeSongsWhilePlaying()
    {
        addSongToMediaQueue();
        addSongToMediaQueue();

        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text("AdjustableAudio")), LAUNCH_TIMEOUT);
        UiObject2 title = mDevice.findObject(By.text("AdjustableAudio"));
        assertEquals("AdjustableAudio", title.getText());

        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        title.click();
        UiObject2 nextButton = mDevice.findObject(By.desc("Next"));
        assertNotNull(nextButton);

        UiObject2 playButton = mDevice.findObject(By.desc("Play"));
        assertNotNull(playButton);

        playButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);

        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
        UiObject2 pauseButton = mDevice.findObject(By.desc("Pause"));
        assertNotNull(pauseButton);

        nextButton = mDevice.findObject(By.desc("Next"));
        nextButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);
        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        UiObject2 previousButton = mDevice.findObject(By.desc("Reverse"));
        assertNotNull(previousButton);
        previousButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);
        assertTrue(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
    }

    @Test
    public void testNotificationWithThreeSongsWhilePaused()
    {
        addSongToMediaQueue();
        addSongToMediaQueue();

        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text("AdjustableAudio")), LAUNCH_TIMEOUT);
        UiObject2 title = mDevice.findObject(By.text("AdjustableAudio"));
        assertEquals("AdjustableAudio", title.getText());

        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        title.click();
        UiObject2 nextButton = mDevice.findObject(By.desc("Next"));
        assertNotNull(nextButton);

        UiObject2 playButton = mDevice.findObject(By.desc("Play"));
        assertNotNull(playButton);

        nextButton = mDevice.findObject(By.desc("Next"));
        nextButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);
        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());

        UiObject2 previousButton = mDevice.findObject(By.desc("Reverse"));
        assertNotNull(previousButton);
        previousButton.click();

        mDevice.waitForWindowUpdate(null, LAUNCH_TIMEOUT);
        assertFalse(MusicService.getInstance().getMediaPlayerAdapter().isPlaying());
    }
}
