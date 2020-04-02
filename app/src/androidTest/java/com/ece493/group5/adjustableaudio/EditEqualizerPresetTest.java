package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ece493.group5.adjustableaudio.models.EqualizerPreset;
import com.ece493.group5.adjustableaudio.storage.SaveController;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
public class EditEqualizerPresetTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

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
                "Test Preset");

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
                withContentDescription("Settings"), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.perform(click());

        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Test Preset"))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString("Test Preset"))));
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

        ViewInteraction eqBand1 = onView(allOf(withId(R.id.equalizerBandValue1), withText("15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        eqBand1.check(matches(withText("15dB")));

        onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 1), 1)))
                .perform(SeekBarAction.setProgress(3000));

        ViewInteraction eqBand2 = onView(allOf(withId(R.id.equalizerBandValue2), withText("15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                                2), isDisplayed()));
        eqBand2.check(matches(withText("15dB")));

        ViewInteraction revertButton = onView(allOf(withId(R.id.revertButton), withText("REVERT"),
                childAtPosition(childAtPosition(
                        withClassName(is("android.widget.LinearLayout")), 4), 1),
                        isDisplayed()));
        revertButton.perform(click());

        ViewInteraction eqBand1Rev = onView(allOf(withId(R.id.equalizerBandValue1), withText("-15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0), 2),
                        isDisplayed()));
        eqBand1Rev.check(matches(withText("-15dB")));

        ViewInteraction eqBand2Rev = onView(allOf(withId(R.id.equalizerBandValue2), withText("-8dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1), 2),
                        isDisplayed()));
        eqBand2Rev.check(matches(withText("-8dB")));
    }

    @Test
    public void testRenameEqualizerPreset()
    {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction renameTextView = onView(allOf(withId(R.id.title), withText("Rename"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        renameTextView.perform(click());

        ViewInteraction cancelButton = onView(allOf(withId(android.R.id.button2), withText("CANCEL"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 2)));
        cancelButton.perform(scrollTo(), click());

        ViewInteraction overflowMenuButton2 = onView(allOf(
                withContentDescription("More options"), childAtPosition(childAtPosition(
                        withId(R.id.action_bar), 1), 0), isDisplayed()));
        overflowMenuButton2.perform(click());

        ViewInteraction renameTextView2 = onView(allOf(withId(R.id.title), withText("Rename"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        renameTextView2.perform(click());

        ViewInteraction newNameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0), isDisplayed()));
        newNameEditText.perform(replaceText("New Test Name"), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1),
                withText("Save"), childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0),
                        3)));
        saveButton.perform(scrollTo(), click());
    }

    @Test
    public void testApplyButton()
    {
        ViewInteraction applyButton = onView(allOf(withId(R.id.applyButton), withText("APPLY"),
                childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")),
                        4), 0),
                        isDisplayed()));
        applyButton.perform(click());
    }

    @Test
    public void testSwitchingPresets()
    {
        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Default"))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString("Default"))));

        ViewInteraction eqBand1Val = onView(allOf(withId(R.id.equalizerBandValue1), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        eqBand1Val.check(matches(withText("3dB")));

        ViewInteraction eqBand2Val = onView(allOf(withId(R.id.equalizerBandValue2), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        eqBand2Val.check(matches(withText("0dB")));

        ViewInteraction eqBand3Val = onView(allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        eqBand3Val.check(matches(withText("0dB")));

        ViewInteraction eqBand4Val = onView(allOf(withId(R.id.equalizerBandValue4), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        eqBand4Val.check(matches(withText("0dB")));

        ViewInteraction eqBand5Val = onView(allOf(withId(R.id.equalizerBandValue5), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        eqBand5Val.check(matches(withText("3dB")));

        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Test Preset"))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString("Test Preset"))));

        ViewInteraction eqBand1ValNew = onView(allOf(withId(R.id.equalizerBandValue1), withText("-15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        eqBand1ValNew.check(matches(withText("-15dB")));

        ViewInteraction eqBand2ValNew = onView(allOf(withId(R.id.equalizerBandValue2), withText("-8dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        eqBand2ValNew.check(matches(withText("-8dB")));

        ViewInteraction eqBand3ValNew = onView(allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        eqBand3ValNew.check(matches(withText("0dB")));

        ViewInteraction eqBand4ValNew = onView(allOf(withId(R.id.equalizerBandValue4), withText("8dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        eqBand4ValNew.check(matches(withText("8dB")));

        ViewInteraction eqBand5ValNew = onView(allOf(withId(R.id.equalizerBandValue5), withText("15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        eqBand5ValNew.check(matches(withText("15dB")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position)
    {

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
