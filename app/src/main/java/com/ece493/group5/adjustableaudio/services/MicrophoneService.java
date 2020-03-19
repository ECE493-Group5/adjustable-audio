package com.ece493.group5.adjustableaudio.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ece493.group5.adjustableaudio.microphone.MicrophonePlayer;

public class MicrophoneService extends Service
{
    private final IBinder binder = new MicrophoneBinder();
    private final MicrophonePlayer microphonePlayer = new MicrophonePlayer();

    public class MicrophoneBinder extends Binder
    {
        public MicrophoneService getService()
        {
            return MicrophoneService.this;
        }
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
