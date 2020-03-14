package com.ece493.group5.adjustableaudio.utils;

import android.text.format.DateUtils;

public class TimeUtils
{
    public static long millisecondsToSeconds(long ms)
    {
        return ms / 1000;
    }

    public static String durationAsString(long ms)
    {
        return DateUtils.formatElapsedTime(TimeUtils.millisecondsToSeconds(ms));
    }
}
