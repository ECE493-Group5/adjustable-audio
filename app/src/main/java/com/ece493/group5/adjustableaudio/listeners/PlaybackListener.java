package com.ece493.group5.adjustableaudio.listeners;

import android.media.session.PlaybackState;

public interface PlaybackListener {

    void onPlaybackStateChange(PlaybackState state);
}
