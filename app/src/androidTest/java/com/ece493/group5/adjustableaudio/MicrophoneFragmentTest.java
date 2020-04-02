package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MicrophoneFragmentTest
{
    private static final String CHILD_AT = "Child at position";
    private static final String MICROPHONE = "Microphone";
    private static final String MICROPHONE_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String PARENT = " in parent ";
    private static final String READ_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant(READ_STORAGE,
                    MICROPHONE_PERMISSION,
                    WRITE_STORAGE);

    @Before
    public void setup()
    {
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_microphone),
                withContentDescription(MICROPHONE), childAtPosition(childAtPosition(
                        withId(R.id.nav_view), 0), 1), isDisplayed()));
        bottomNavigationItemView.perform(click());
    }

    @Test
    public void microphoneFragmentTest()
    {
        ViewInteraction microphoneToggleButton = onView(allOf(withId(R.id.microphoneEnableButton),
                childAtPosition(childAtPosition(withId(R.id.nav_host_fragment), 0),
                        0), isDisplayed()));
        microphoneToggleButton.perform(click());

        microphoneToggleButton.check(matches(isChecked()));

        microphoneToggleButton.perform(click());

        microphoneToggleButton.check(matches(isNotChecked()));
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
