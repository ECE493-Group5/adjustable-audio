package com.ece493.group5.adjustableaudio;


import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.HashMap;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditEqualizerPresetTest extends BaseInstrumentedTest
{

    private static final String DEFAULT = "Default";
    private static final String EIGHT_DB = "8dB";
    private static final String FIFTEEN_DB = "15dB";
    private static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String MORE_OPTIONS = "More options";
    private static final String NEGATIVE_EIGHT_DB = "-8dB";
    private static final String NEGATIVE_FIFTEEN_DB = "-15dB";
    private static final String NEW_TEST_NAME = "New Test Name";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String RENAME = "Rename";
    private static final String SETTINGS = "Settings";
    private static final String TEST_PRESET = "Test Preset";
    private static final String THREE_DB = "3dB";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String ZERO_DB = "0dB";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(READ_STORAGE_PERMISSION,
                    MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

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

    private void createPreset()
    {
        HashMap<Integer, Integer> dummyEqualizerValues = new HashMap<>();
        dummyEqualizerValues.put(0, -1500);
        dummyEqualizerValues.put(1, -800);
        dummyEqualizerValues.put(2, 0);
        dummyEqualizerValues.put(3, 800);
        dummyEqualizerValues.put(4, 1500);

        EqualizerPreset dummyEqualizerPreset = new EqualizerPreset(dummyEqualizerValues, 0.5,
                TEST_PRESET);

        mActivityTestRule.getActivity().getEqualizerModel()
                .addEqualizerSetting(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                dummyEqualizerPreset);
    }

    @Before
    public void setUpTest()
    {
        clearEqualizerPresets();
        createPreset();

        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription(SETTINGS), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.perform(click());

        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TEST_PRESET))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString(TEST_PRESET))));
    }

    @After
    public void removePreset()
    {
        if(SaveController.loadPresets(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null)
        {
            SaveController.deletePreset(InstrumentationRegistry.getInstrumentation().getTargetContext(), 1);
        }
    }

    @Test
    public void testRevertButton()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(3000));

        ViewInteraction eqBand1 = onView(allOf(withId(R.id.equalizerBandValue1), withText(FIFTEEN_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        eqBand1.check(matches(withText(FIFTEEN_DB)));

        onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 1), 1)))
                .perform(SeekBarAction.setProgress(3000));

        ViewInteraction eqBand2 = onView(allOf(withId(R.id.equalizerBandValue2), withText(FIFTEEN_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                                2), isDisplayed()));
        eqBand2.check(matches(withText(FIFTEEN_DB)));

        ViewInteraction revertButton = onView(allOf(withId(R.id.revertButton), withText(R.string.button_revert),
                childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 4), 1),
                        isDisplayed()));
        revertButton.check(matches(isDisplayed()));
        revertButton.check(matches(isClickable()));
        revertButton.perform(click());

        ViewInteraction eqBand1Rev = onView(allOf(withId(R.id.equalizerBandValue1), withText(NEGATIVE_FIFTEEN_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0), 2),
                        isDisplayed()));
        eqBand1Rev.check(matches(withText(NEGATIVE_FIFTEEN_DB)));

        ViewInteraction eqBand2Rev = onView(allOf(withId(R.id.equalizerBandValue2), withText(NEGATIVE_EIGHT_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1), 2),
                        isDisplayed()));
        eqBand2Rev.check(matches(withText(NEGATIVE_EIGHT_DB)));
    }

    @Test
    public void testRenameEqualizerPreset()
    {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription(MORE_OPTIONS),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction renameTextView = onView(allOf(withId(R.id.title), withText(RENAME),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        renameTextView.check(matches(isDisplayed()));
        renameTextView.check(matches(isEnabled()));
        renameTextView.perform(click());

        ViewInteraction cancelButton = onView(allOf(withId(android.R.id.button2),
                withText(R.string.negative_button_dialog),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 2)));
        cancelButton.check(matches(isDisplayed()));
        cancelButton.check(matches(isClickable()));
        cancelButton.perform(scrollTo(), click());

        ViewInteraction overflowMenuButton2 = onView(allOf(
                withContentDescription(MORE_OPTIONS), childAtPosition(childAtPosition(
                        withId(R.id.action_bar), 1), 0), isDisplayed()));
        overflowMenuButton2.perform(click());

        ViewInteraction renameTextView2 = onView(allOf(withId(R.id.title), withText(RENAME),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        renameTextView2.perform(click());

        ViewInteraction newNameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0), isDisplayed()));
        newNameEditText.perform(replaceText(NEW_TEST_NAME), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1),
                withText(R.string.save_button), childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0),
                        3)));
        saveButton.check(matches(isDisplayed()));
        saveButton.check(matches(isClickable()));
        saveButton.perform(scrollTo(), click());
    }

    @Test
    public void testApplyButton()
    {
        ViewInteraction applyButton = onView(allOf(withId(R.id.applyButton), withText(R.string.button_apply),
                childAtPosition(childAtPosition(withClassName(is(LINEAR_LAYOUT)),
                        4), 0),
                        isDisplayed()));
        applyButton.check(matches(isDisplayed()));
        applyButton.check(matches(isClickable()));
        applyButton.perform(click());
    }

    @Test
    public void testSwitchingPresets()
    {
        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(DEFAULT))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString(DEFAULT))));

        ViewInteraction eqBand1Val = onView(allOf(withId(R.id.equalizerBandValue1), withText(THREE_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        eqBand1Val.check(matches(withText(THREE_DB)));

        ViewInteraction eqBand2Val = onView(allOf(withId(R.id.equalizerBandValue2), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        eqBand2Val.check(matches(withText(ZERO_DB)));

        ViewInteraction eqBand3Val = onView(allOf(withId(R.id.equalizerBandValue3), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        eqBand3Val.check(matches(withText(ZERO_DB)));

        ViewInteraction eqBand4Val = onView(allOf(withId(R.id.equalizerBandValue4), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        eqBand4Val.check(matches(withText(ZERO_DB)));

        ViewInteraction eqBand5Val = onView(allOf(withId(R.id.equalizerBandValue5), withText(THREE_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        eqBand5Val.check(matches(withText(THREE_DB)));

        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TEST_PRESET))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString(TEST_PRESET))));

        ViewInteraction eqBand1ValNew = onView(allOf(withId(R.id.equalizerBandValue1), withText(NEGATIVE_FIFTEEN_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        eqBand1ValNew.check(matches(withText(NEGATIVE_FIFTEEN_DB)));

        ViewInteraction eqBand2ValNew = onView(allOf(withId(R.id.equalizerBandValue2), withText(NEGATIVE_EIGHT_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        eqBand2ValNew.check(matches(withText(NEGATIVE_EIGHT_DB)));

        ViewInteraction eqBand3ValNew = onView(allOf(withId(R.id.equalizerBandValue3), withText(ZERO_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        eqBand3ValNew.check(matches(withText(ZERO_DB)));

        ViewInteraction eqBand4ValNew = onView(allOf(withId(R.id.equalizerBandValue4), withText(EIGHT_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        eqBand4ValNew.check(matches(withText(EIGHT_DB)));

        ViewInteraction eqBand5ValNew = onView(allOf(withId(R.id.equalizerBandValue5), withText(FIFTEEN_DB),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        eqBand5ValNew.check(matches(withText(FIFTEEN_DB)));
    }
}
