package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
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
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setUpTest()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_settings),
                withContentDescription("Settings"), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 3), isDisplayed()));
        bottomNavigationItemView.perform(click());

        createPreset();
    }

    public void createPreset()
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

        ViewInteraction overflowMenuButton = onView(
                allOf(withContentDescription("More options"), childAtPosition(
                        childAtPosition(withId(R.id.action_bar), 1), 0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction materialTextView = onView(allOf(withId(R.id.title), withText("Add"),
                        childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction editText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0),
                isDisplayed()));
        editText.perform(replaceText("Test Preset"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(allOf(withId(android.R.id.button1), withText("Save"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        materialButton2.perform(scrollTo(), click());
    }

    @After
    public void removePreset()
    {
        ViewInteraction overflowMenuButton4 = onView(allOf(
                withContentDescription("More options"), childAtPosition(childAtPosition(
                        withId(R.id.action_bar), 1), 0), isDisplayed()));
        overflowMenuButton4.perform(click());

        ViewInteraction materialTextView4 = onView(allOf(withId(R.id.title), withText("Remove"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                isDisplayed()));
        materialTextView4.perform(click());
    }

    @Test
    public void testRevertButton()
    {
        onView(allOf(withId(R.id.equalizerBandSeekbar1), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 0), 1)))
                .perform(SeekBarAction.setProgress(3000));

        ViewInteraction textView = onView(allOf(withId(R.id.equalizerBandValue1), withText("15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        textView.check(matches(withText("15dB")));

        onView(allOf(withId(R.id.equalizerBandSeekbar2), childAtPosition(childAtPosition(
                withId(R.id.equalizerTableLayout), 1), 1)))
                .perform(SeekBarAction.setProgress(3000));

        ViewInteraction textView2 = onView(allOf(withId(R.id.equalizerBandValue2), withText("15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                                2), isDisplayed()));
        textView2.check(matches(withText("15dB")));

        ViewInteraction materialButton3 = onView(allOf(withId(R.id.revertButton), withText("REVERT"),
                childAtPosition(childAtPosition(
                        withClassName(is("android.widget.LinearLayout")), 4), 1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView7 = onView(allOf(withId(R.id.equalizerBandValue1), withText("-15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0), 2),
                        isDisplayed()));
        textView7.check(matches(withText("-15dB")));

        ViewInteraction textView8 = onView(allOf(withId(R.id.equalizerBandValue2), withText("-8dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1), 2),
                        isDisplayed()));
        textView8.check(matches(withText("-8dB")));
    }

    @Test
    public void testRenameEqualizerPreset()
    {
        ViewInteraction overflowMenuButton2 = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                        isDisplayed()));
        overflowMenuButton2.perform(click());

        ViewInteraction materialTextView2 = onView(allOf(withId(R.id.title), withText("Rename"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction materialButton5 = onView(allOf(withId(android.R.id.button2), withText("CANCEL"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 2)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction overflowMenuButton3 = onView(allOf(
                withContentDescription("More options"), childAtPosition(childAtPosition(
                        withId(R.id.action_bar), 1), 0), isDisplayed()));
        overflowMenuButton3.perform(click());

        ViewInteraction materialTextView3 = onView(allOf(withId(R.id.title), withText("Rename"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction editText1 = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0), isDisplayed()));
        editText1.perform(replaceText("New Test Name"), closeSoftKeyboard());

        ViewInteraction materialButton1 = onView(allOf(withId(android.R.id.button1),
                withText("Save"), childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0),
                        3)));
        materialButton1.perform(scrollTo(), click());
    }

    @Test
    public void testApplyButton()
    {
        ViewInteraction materialButton4 = onView(allOf(withId(R.id.applyButton), withText("APPLY"),
                childAtPosition(childAtPosition(withClassName(is("android.widget.LinearLayout")),
                        4), 0),
                        isDisplayed()));
        materialButton4.perform(click());
    }

    @Test
    public void testSwitchingPresets()
    {
        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Default"))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString("Default"))));

        ViewInteraction textView = onView(allOf(withId(R.id.equalizerBandValue1), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        textView.check(matches(withText("3dB")));

        ViewInteraction textView2 = onView(allOf(withId(R.id.equalizerBandValue2), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        textView2.check(matches(withText("0dB")));

        ViewInteraction textView19 = onView(allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        textView19.check(matches(withText("0dB")));

        ViewInteraction textView20 = onView(allOf(withId(R.id.equalizerBandValue4), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        textView20.check(matches(withText("0dB")));

        ViewInteraction textView21 = onView(allOf(withId(R.id.equalizerBandValue5), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        textView21.check(matches(withText("3dB")));

        onView(withId(R.id.presetSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Test Preset"))).perform(click());
        onView(withId(R.id.presetSpinner)).check(matches(withSpinnerText(containsString("Test Preset"))));

        ViewInteraction textView1 = onView(allOf(withId(R.id.equalizerBandValue1), withText("-15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 0),
                        2), isDisplayed()));
        textView1.check(matches(withText("-15dB")));

        ViewInteraction textView3 = onView(allOf(withId(R.id.equalizerBandValue2), withText("-8dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        textView3.check(matches(withText("-8dB")));

        ViewInteraction textView4 = onView(allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        textView4.check(matches(withText("0dB")));

        ViewInteraction textView5 = onView(allOf(withId(R.id.equalizerBandValue4), withText("8dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        textView5.check(matches(withText("8dB")));

        ViewInteraction textView6 = onView(allOf(withId(R.id.equalizerBandValue5), withText("15dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        textView6.check(matches(withText("15dB")));
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
