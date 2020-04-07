package com.ece493.group5.adjustableaudio.listeners;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.os.Handler;

import java.util.Objects;

/**
 * The GlobalVolumeListener class helps implement the following requirement:
 *
 * #SRS: Media Player
 *
 * In particular, the GlobalVolumeListener waits for the global system volume for the music stream
 * to change.
 */

public abstract class GlobalVolumeListener extends ContentObserver
{
    private static final int START_TIME_MS = 0;
    private static final int COUNT_DOWN_TIME_MS = 1000;
    private Context context;
    private int previousVolume;
    private CountDownTimer countDownTimer;

    public GlobalVolumeListener(Context c)
    {
        super(new Handler());
        context = c;
        previousVolume = ((AudioManager) Objects.requireNonNull(context.getSystemService(Context.AUDIO_SERVICE)))
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        countDownTimer = null;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (currentVolume != previousVolume)
        {
            if (countDownTimer != null)
            {
                countDownTimer.cancel();
            }

            /**
             * Count down timer will be continuously reset until the user stops changing the volume.
             * This way, we don't "spam" the onVolumeChange() method.
             */
            countDownTimer = new CountDownTimer(START_TIME_MS, COUNT_DOWN_TIME_MS) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish()
                {
                    onVolumeChange(currentVolume);
                    previousVolume = currentVolume;
                    countDownTimer = null;
                }
            };
            countDownTimer.start();
        }
    }

    public abstract void onVolumeChange(int newVolumeAsPercent);
}
