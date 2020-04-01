package com.ece493.group5.adjustableaudio;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RemoveEqualizerPresetTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Before
    public void setupTest()
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

        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction addTextView = onView(allOf(withId(R.id.title), withText("Add"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                isDisplayed()));
        addTextView.perform(click());

        ViewInteraction nameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0),
                isDisplayed()));
        nameEditText.perform(replaceText("Test Preset"), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1), withText("Save"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        saveButton.perform(scrollTo(), click());
    }

    @Test
    public void testRemoveEqualizerPreset()
    {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction removeTextView = onView(allOf(withId(R.id.title), withText("Remove"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                isDisplayed()));
        removeTextView.perform(click());

        ViewInteraction checkPresetName = onView(allOf(withId(android.R.id.text1), withText("Default"),
                childAtPosition(allOf(withId(R.id.presetSpinner), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        1)), 0), isDisplayed()));
        checkPresetName.check(matches(withText("Default")));

        ViewInteraction checkEqBand1 = onView(allOf(withId(R.id.equalizerBandValue1), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout),
                        0), 2), isDisplayed()));
        checkEqBand1.check(matches(withText("3dB")));

        ViewInteraction checkEqBand2 = onView(allOf(withId(R.id.equalizerBandValue2), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 1),
                        2), isDisplayed()));
        checkEqBand2.check(matches(withText("0dB")));

        ViewInteraction checkEqBand3 = onView(allOf(withId(R.id.equalizerBandValue3), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 2),
                        2), isDisplayed()));
        checkEqBand3.check(matches(withText("0dB")));

        ViewInteraction checkEqBand4 = onView(allOf(withId(R.id.equalizerBandValue4), withText("0dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 3),
                        2), isDisplayed()));
        checkEqBand4.check(matches(withText("0dB")));

        ViewInteraction checkEqBand5 = onView(allOf(withId(R.id.equalizerBandValue5), withText("3dB"),
                childAtPosition(childAtPosition(withId(R.id.equalizerTableLayout), 4),
                        2), isDisplayed()));
        checkEqBand5.check(matches(withText("3dB")));
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
