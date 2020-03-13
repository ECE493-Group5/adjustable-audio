package com.ece493.group5.adjustableaudio.enums;

public enum MediaData
{
    QUEUE,
    QUEUE_INDEX,
    STATE,
    DURATION;

    public static final String EXTRA_QUEUE =  "BUNDLE_QUEUE";
    public static final String EXTRA_QUEUE_INDEX =  "BUNDLE_QUEUE_INDEX";
    public static final String EXTRA_SONG =  "BUNDLE_SONG";
    public static final String EXTRA_DURATION = "BUNDLE_DURATION";
    public static final String EXTRA_DATA_CHANGED = "BUNDLE_DATA_CHANGED";
}
