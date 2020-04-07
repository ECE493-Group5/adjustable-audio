package com.ece493.group5.adjustableaudio.utils;

import android.text.format.DateUtils;

/**
 * The TimeUtils class helps implement the following requirements:
 *
 * #SRS: Media Player
 * #SRS: Performing a Hearing Test
 *
 * In particular, the media player uses TimeUtils to properly display the total and elapsed duration
 * of an audio file. The hearing test uses TimeUtils to properly display the countdown timers.
 */

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
