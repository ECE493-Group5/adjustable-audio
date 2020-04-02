package com.ece493.group5.adjustableaudio;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Spinner;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HearingTestResultFragmentTest
{
    private static final String CHILD_AT = "Child at position";
    private static final String CONSTRAINT_LAYOUT = "androidx.constraintlayout.widget.ConstraintLayout";
    private static final String EMPTY_VIEW_MESSAGE = "Recycler view should not be empty";
    private static final String EQUALIZER_PRESET_MESSAGE = "There should be two equalizer presets: Default and Test";
    private static final String HEARING_TEST = "Hearing Test";
    private static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String NAVIGATE_UP = "Navigate up";
    private static final String NEW_TEST_NAME = "New Test Name";
    private static final String PARENT = " in parent ";
    private static final String POPUP_LAYOUT = "android.widget.PopupWindow$PopupBackgroundView";
    private static final String READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String SETTINGS = "Settings";
    private static final String TEST = "Test";
    private static final String WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private static final int[] TONES = {30, 90, 233, 250, 347, 500, 907, 1000, 1353, 2000,
            3533, 4000, 5267, 8000, 11333, 15667};

    private static final double[] REFERENCE_FREQUENCY_DBHL_VALUES = {60.0, 37.0, 19.0, 18.0,
            14.6, 11.0, 6.0, 5.5, 5.5, 4.5, 6.5, 9.5, 14.8, 17.5, 23.0, 52.5};

    private static final double DECIBEL_LEVEL = 35.5;

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
            READ_STORAGE_PERMISSION, MICROPHONE_PERMISSION, WRITE_STORAGE_PERMISSION);

    private void clearHearingTestResult()
    {
        //Clear any hearing tests
        if(SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()) != null
        && SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()).size()>0)
        {
            int size = SaveController.loadResults(InstrumentationRegistry.getInstrumentation().getTargetContext()).size();
            for(int i = 0; i < size; i++)
            {
                SaveController.deleteResult(InstrumentationRegistry.getInstrumentation().getTargetContext(), i);
            }
        }
    }

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

    private void createDummyHearingTestResult()
    {
        ArrayList<ToneData> dummyToneDataList = new ArrayList<>();

        for (int i = 0; i < TONES.length; i++)
        {
           ToneData dummyToneData =  new ToneData(TONES[i], REFERENCE_FREQUENCY_DBHL_VALUES[i]);
           dummyToneData.setLHeardAtDB(DECIBEL_LEVEL);
           dummyToneData.setRHeardAtDB(DECIBEL_LEVEL);
           dummyToneDataList.add(i, dummyToneData);
        }

        HearingTestResult dummyHearingTestResult = new HearingTestResult(TEST, dummyToneDataList);
        SaveController.saveResult(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                dummyHearingTestResult);
    }

    private void navigateToHearingTestResultView()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_hearing_test),
                withContentDescription(HEARING_TEST), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 0), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction hearingTestResultList = onView(allOf(withId(R.id.hearing_test_result_recyclerview),
                childAtPosition(withClassName(is(LINEAR_LAYOUT)), 4)));
        hearingTestResultList.perform(actionOnItemAtPosition(0, click()));
    }

    @Before
    public void setup()
    {
        Intent resultData = new Intent(Intent.ACTION_SEND);

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(not(isInternal())).respondWith(result);

        clearHearingTestResult();
        clearEqualizerPresets();
        createDummyHearingTestResult();
        navigateToHearingTestResultView();
    }

    @After
    public void tearDown()
    {
        clearHearingTestResult();
        clearEqualizerPresets();
    }

    @Test
    public void testHearingTestResultUI()
    {
        ViewInteraction audiogramPlot = onView(allOf(withId(R.id.AudiogramPlot), childAtPosition(
                childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                1), isDisplayed()));
        audiogramPlot.check(matches(isDisplayed()));

        ViewInteraction renameButton = onView(allOf(withId(R.id.hearing_test_result_rename_button),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        2), 0), isDisplayed()));
        renameButton.check(matches(isDisplayed()));
        renameButton.check(matches(isClickable()));

        ViewInteraction deleteButton = onView(allOf(withId(R.id.hearing_test_result_delete_button),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 2),
                        1), isDisplayed()));
        deleteButton.check(matches(isDisplayed()));
        deleteButton.check(matches(isClickable()));

        ViewInteraction shareButton = onView(allOf(withId(R.id.hearing_test_result_share_button),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        2), 2), isDisplayed()));
        shareButton.check(matches(isDisplayed()));
        shareButton.check(matches(isClickable()));

        ViewInteraction presetButton = onView(allOf(withId(R.id.hearing_test_result_eq_preset_button),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                        0), 3), isDisplayed()));
        presetButton.check(matches(isDisplayed()));
        presetButton.check(matches(isClickable()));
    }

    @Test
    public void testRenamingHearingTestResult()
    {
        ViewInteraction renameButton = onView(allOf(withId(R.id.hearing_test_result_rename_button),
                withText(R.string.button_change_name), childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 2), 0),
                        isDisplayed()));
        renameButton.perform(click());

        ViewInteraction cancelButton = onView(allOf(withId(android.R.id.button2),
                withText(R.string.negative_button_dialog), childAtPosition(childAtPosition(
                        withId(R.id.buttonPanel), 0), 2)));
        cancelButton.check(matches(isDisplayed()));
        cancelButton.check(matches(isClickable()));
        cancelButton.perform(scrollTo(), click());

        ViewInteraction renameButton1 = onView(allOf(withId(R.id.hearing_test_result_rename_button),
                withText(R.string.button_change_name), childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 2),
                        0), isDisplayed()));
        renameButton1.perform(click());

        ViewInteraction newNameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0), isDisplayed()));
        newNameEditText.perform(replaceText(NEW_TEST_NAME), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1),
                withText(R.string.save_button), childAtPosition(childAtPosition(
                        withId(R.id.buttonPanel), 0), 3)));
        saveButton.check(matches(isDisplayed()));
        saveButton.check(matches(isClickable()));
        saveButton.perform(scrollTo(), click());

        ViewInteraction newTestNameTitle = onView(allOf(withId(R.id.hearing_test_result_textview),
                withText(NEW_TEST_NAME), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0),
                        0), isDisplayed()));
        newTestNameTitle.check(matches(withText(NEW_TEST_NAME)));

        ViewInteraction backButton = onView(allOf(withContentDescription(NAVIGATE_UP),
                childAtPosition(allOf(withId(R.id.action_bar), childAtPosition(
                        withId(R.id.action_bar_container), 0)), 2), isDisplayed()));
        backButton.check(matches(isClickable()));
        backButton.perform(click());

        ViewInteraction newTestName = onView(allOf(withId(R.id.testResultName), withText(NEW_TEST_NAME),
                childAtPosition(allOf(withId(R.id.main_parent), childAtPosition(
                        withId(R.id.hearing_test_result_recyclerview), 0)), 0),
                        isDisplayed()));
        newTestName.check(matches(withText(NEW_TEST_NAME)));

        RecyclerView recyclerView = (RecyclerView) intentsTestRule.getActivity().findViewById(R.id.hearing_test_result_recyclerview);
        int numResults = recyclerView.getAdapter().getItemCount();

        assertTrue(EMPTY_VIEW_MESSAGE, numResults == 1);
    }

    @Test
    public void testExportAudioSetting()
    {
        ViewInteraction exportButton = onView(allOf(withId(R.id.hearing_test_result_eq_preset_button),
                withText(R.string.button_export_to_audio_setting), childAtPosition(childAtPosition(
                        withClassName(is(CONSTRAINT_LAYOUT)),
                        0), 3), isDisplayed()));
        exportButton.perform(click());

        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription(SETTINGS), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.check(matches(isClickable()));
        bottomNavigationItemView.perform(click());

        Spinner spinner = intentsTestRule.getActivity().findViewById(R.id.presetSpinner);
        assertTrue(EQUALIZER_PRESET_MESSAGE, spinner.getAdapter().getCount() == 2);

        ViewInteraction presetSpinner = onView(allOf(withId(R.id.presetSpinner),
                childAtPosition(childAtPosition(withClassName(is(LINEAR_LAYOUT)),
                        0), 1), isDisplayed()));
        presetSpinner.check(matches(isClickable()));
        presetSpinner.perform(click());

        DataInteraction dropDownMenu = onData(anything()).inAdapterView(childAtPosition(
                withClassName(is(POPUP_LAYOUT)), 0))
                .atPosition(1);
        dropDownMenu.check(matches(isEnabled()));
        dropDownMenu.perform(click());

        ViewInteraction testName = onView(allOf(withId(android.R.id.text1), withText(TEST),
                childAtPosition(allOf(withId(R.id.presetSpinner), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1)), 0), isDisplayed()));
        testName.check(matches(withText(TEST)));
    }

    @Test
    public void testShareHearingTest()
    {
        ViewInteraction shareButton = onView(allOf(withId(R.id.hearing_test_result_share_button),
                withText(R.string.button_share), childAtPosition(childAtPosition(
                        withClassName(is(LINEAR_LAYOUT)), 2),
                        2), isDisplayed()));

        shareButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText(CHILD_AT + position + PARENT);
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
