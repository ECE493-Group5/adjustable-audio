package com.ece493.group5.adjustableaudio;

import android.view.View;
import android.widget.Spinner;

import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddEqualizerPresetTest extends BaseInstrumentedTest
{
    private static final String ADD = "Add";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String MORE_OPTIONS = "More options";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String SETTINGS = "Settings";
    private static final String TEST_PRESET = "Test Preset";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    private void clearEqualizerPresets()
    {
        int size;

        if
        (SaveController.loadPresets(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null
                && ((size = SaveController.loadPresets(InstrumentationRegistry.getInstrumentation().getTargetContext()).size()) > 0))
        {
            for (int i = 1; i < size; i++)
            {
                SaveController.deletePreset(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                        i);
            }
        }
    }

    @Before
    public void setupTest()
    {
        clearEqualizerPresets();

        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription(SETTINGS), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.perform(click());

        setEqualizerBands();
    }

    @After
    public void tearDownTest()
    {
        if(SaveController.loadPresets(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null)
        {
            SaveController.deletePreset(InstrumentationRegistry.getInstrumentation().getTargetContext(), 1);
        }
    }

    public void setEqualizerBands()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(0));

        onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 1), 1)))
                .perform(SeekBarAction.setProgress(700));

        onView(allOf(withId(R.id.equalizerBandSeekbar3), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 2), 1)))
                .perform(SeekBarAction.setProgress(1500));

        onView(allOf(withId(R.id.equalizerBandSeekbar4), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 3), 1)))
                .perform(SeekBarAction.setProgress(2300));

        onView(allOf(withId(R.id.equalizerBandSeekbar5), childAtPosition(
                childAtPosition(withId(R.id.equalizerTableLayout), 4), 1)))
                .perform(SeekBarAction.setProgress(3000));

    }

    @Test
    public void testAddEqualizerPreset()
    {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription(MORE_OPTIONS),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction addTextView = onView(allOf(withId(R.id.title), withText(ADD),
                        childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        addTextView.check(matches(isDisplayed()));
        addTextView.check(matches(isEnabled()));
        addTextView.perform(click());

        ViewInteraction nameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0), isDisplayed()));
        nameEditText.perform(replaceText(TEST_PRESET), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1),
                withText(R.string.save_button), childAtPosition(childAtPosition(withId(R.id.buttonPanel),
                        0), 3)));
        saveButton.check(matches(isDisplayed()));
        saveButton.check(matches(isClickable()));
        saveButton.perform(scrollTo(), click());

        ViewInteraction verifyPresetName = onView(allOf(withId(android.R.id.text1),
                withText(TEST_PRESET), childAtPosition(allOf(withId(R.id.presetSpinner),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                1)), 0), isDisplayed()));
        verifyPresetName.check(matches(withText(TEST_PRESET)));

        ViewInteraction applyButton = onView(allOf(withId(R.id.applyButton),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        4), 0), isDisplayed()));
        applyButton.check(matches(isDisplayed()));
        applyButton.check(matches(isClickable()));

        ViewInteraction revertButton = onView(allOf(withId(R.id.revertButton),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        4), 1), isDisplayed()));
        revertButton.check(matches(isDisplayed()));
        revertButton.check(matches(isClickable()));

        Spinner spinner = mActivityTestRule.getActivity().findViewById(R.id.presetSpinner);
        assertEquals(2, spinner.getAdapter().getCount());
    }
}
