package com.ece493.group5.adjustableaudio;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
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
public class AddEqualizerPresetTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
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

        setEqualizerBands();
    }

    @After
    public void tearDownTest()
    {
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction removeTextView = onView(allOf(withId(R.id.title), withText("Remove"),
                childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                isDisplayed()));
        removeTextView.perform(click());
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
        ViewInteraction overflowMenuButton = onView(allOf(withContentDescription("More options"),
                childAtPosition(childAtPosition(withId(R.id.action_bar), 1), 0),
                        isDisplayed()));
        overflowMenuButton.perform(click());

        ViewInteraction addTextView = onView(allOf(withId(R.id.title), withText("Add"),
                        childAtPosition(childAtPosition(withId(R.id.content), 0), 0),
                        isDisplayed()));
        addTextView.perform(click());

        ViewInteraction nameEditText = onView(allOf(childAtPosition(allOf(withId(R.id.custom),
                childAtPosition(withId(R.id.customPanel), 0)), 0), isDisplayed()));
        nameEditText.perform(replaceText("Test Preset"), closeSoftKeyboard());

        ViewInteraction saveButton = onView(allOf(withId(android.R.id.button1), withText("Save"),
                childAtPosition(childAtPosition(withId(R.id.buttonPanel), 0), 3)));
        saveButton.perform(scrollTo(), click());

        ViewInteraction verifyPresetName = onView(allOf(withId(android.R.id.text1),
                withText("Test Preset"), childAtPosition(allOf(withId(R.id.presetSpinner),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                1)), 0), isDisplayed()));
        verifyPresetName.check(matches(withText("Test Preset")));

        ViewInteraction applyButton = onView(allOf(withId(R.id.applyButton),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        4), 0), isDisplayed()));
        applyButton.check(matches(isDisplayed()));

        ViewInteraction revertButton = onView(allOf(withId(R.id.revertButton),
                childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                        4), 1), isDisplayed()));
        revertButton.check(matches(isDisplayed()));
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
