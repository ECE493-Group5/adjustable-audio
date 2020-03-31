package com.ece493.group5.adjustableaudio;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ece493.group5.adjustableaudio.ui.media_player.MediaPlayerFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void mediaPlayerFragmentTest() {
//        FragmentFactory factory = new FragmentFactory();
//        FragmentScenario scenario = FragmentScenario.launchInContainer(MediaPlayerFragment.class, null, factory);

        Fragment test = new MediaPlayerFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout,test)
                .commit();

//        ViewInteraction materialCheckBox = onView(
//                allOf(withId(R.id.checkBox), withText("I have read and understand the above disclaimer."),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.ScrollView")),
//                                        0),
//                                1)));
//        materialCheckBox.perform(scrollTo(), click());
//
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.continueButton), withText("Continue"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.ScrollView")),
//                                        0),
//                                2)));
//        materialButton.perform(scrollTo(), click());

//        ViewInteraction viewGroup = onView(
//                allOf(withId(R.id.action_bar),
//                        childAtPosition(
//                                allOf(withId(R.id.action_bar_container),
//                                        childAtPosition(
//                                                withId(R.id.decor_content_parent),
//                                                0)),
//                                0),
//                        isDisplayed()));
//        viewGroup.check(matches(isDisplayed()));
//
//        ViewInteraction textView = onView(
//                allOf(withText("Media Player"),
//                        childAtPosition(
//                                allOf(withId(R.id.action_bar),
//                                        childAtPosition(
//                                                withId(R.id.action_bar_container),
//                                                0)),
//                                0),
//                        isDisplayed()));
//        textView.check(matches(withText("Media Player")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.addMediaButton),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction seekBar = onView(
                allOf(withId(R.id.progressTrack),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                3),
                        isDisplayed()));
        seekBar.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

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
