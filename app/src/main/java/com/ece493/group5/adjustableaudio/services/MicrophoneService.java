package com.ece493.group5.adjustableaudio.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ece493.group5.adjustableaudio.adapters.MicrophonePlayerAdapter;
import com.ece493.group5.adjustableaudio.listeners.GlobalVolumeListener;

import java.util.Objects;

/**
 * The MicrophoneService class helps implement the following requirements:
 *
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes through an Equalizer
 * #SRS: Makeshift Hearing Aid
 */

public class MicrophoneService extends Service
{
    private final IBinder binder = new MicrophoneBinder();
    private MicrophonePlayerAdapter microphonePlayer;
    private GlobalVolumeListener globalVolumeListener;

    public class MicrophoneBinder extends Binder
    {
        public MicrophoneService getService()
        {
            return MicrophoneService.this;
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        microphonePlayer = new MicrophonePlayerAdapter(this);

        globalVolumeListener = new GlobalVolumeListener(this) {
            @Override
            public void onVolumeChange(int newVolumeAsPercent)
            {
                microphonePlayer.disableEqualizer();
                microphonePlayer.enableEqualizer();
            }
        };

        Objects.requireNonNull(this).getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                globalVolumeListener);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Objects.requireNonNull(this).getContentResolver().unregisterContentObserver(globalVolumeListener);

        if (microphonePlayer.isRecording())
            microphonePlayer.stopRecording();

        microphonePlayer.release();
        microphonePlayer = null;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    public MicrophonePlayerAdapter getMicrophonePlayer()
    {
        return microphonePlayer;
    }
}
