package com.ece493.group5.adjustableaudio.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ece493.group5.adjustableaudio.microphone.MicrophonePlayer;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;

import java.util.Observer;

public class MicrophoneService extends Service
{
    private final IBinder binder = new MicrophoneBinder();
    private MicrophonePlayer microphonePlayer;

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
        microphonePlayer = new MicrophonePlayer();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

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

    public MicrophonePlayer getMicrophonePlayer()
    {
        return microphonePlayer;
    }
}
