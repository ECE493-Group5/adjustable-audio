package com.ece493.group5.adjustableaudio.controllers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ece493.group5.adjustableaudio.services.MicrophoneService;

public class MicrophoneServiceInteractor
{
    private Context context;
    private MicrophoneService service;
    private boolean connected;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibinder)
        {
            MicrophoneService.MicrophoneBinder binder = (MicrophoneService.MicrophoneBinder) ibinder;
            service = binder.getService();
            connected = true;

            onConnectionEstablished();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            service = null;
            connected = false;

            onConnectionLost();
        }
    };

    public MicrophoneServiceInteractor(Context c)
    {
        context = c;
        connected = false;
    }

    public void connect()
    {
        if (!isConnected()) {
            Intent intent = new Intent(context, MicrophoneService.class);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public void disconnect()
    {
        if (isConnected())
            context.unbindService(connection);
    }

    public boolean isConnected()
    {
        return connected;
    }

    public void onConnectionEstablished() {}
    public void onConnectionLost() {}
}
