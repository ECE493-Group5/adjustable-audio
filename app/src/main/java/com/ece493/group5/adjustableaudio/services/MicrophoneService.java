package com.ece493.group5.adjustableaudio.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ece493.group5.adjustableaudio.microphone.MicrophonePlayer;

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

        if (microphonePlayer.isActive())
            microphonePlayer.stopRecording();

        microphonePlayer.release();
        microphonePlayer = null;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    public void startRecording()
    {
        microphonePlayer.startRecording();
    }

    public void stopRecording()
    {
        microphonePlayer.stopRecording();
    }
}
