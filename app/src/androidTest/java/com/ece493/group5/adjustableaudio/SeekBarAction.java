package com.ece493.group5.adjustableaudio;

import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

public class SeekBarAction
{
    public static ViewAction setProgress(final int progress)
    {
        return new ViewAction()
        {
            @Override
            public void perform(UiController uiController, View view)
            {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription()
            {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints()
            {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }
}
