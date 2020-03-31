package com.ece493.group5.adjustableaudio.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ece493.group5.adjustableaudio.adapters.MicrophonePlayerAdapter;

public class MicrophoneService extends Service
{
    private final IBinder binder = new MicrophoneBinder();
    private MicrophonePlayerAdapter microphonePlayer;

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
        Log.d("Microphone Service", "Oncreate");
        super.onCreate();
        microphonePlayer = new MicrophonePlayerAdapter();
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

    public MicrophonePlayerAdapter getMicrophonePlayer()
    {
        return microphonePlayer;
    }
}
