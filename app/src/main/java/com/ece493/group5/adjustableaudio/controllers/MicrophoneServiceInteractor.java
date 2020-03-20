package com.ece493.group5.adjustableaudio.controllers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ece493.group5.adjustableaudio.interfaces.IServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.MicrophoneDataListener;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;
import com.ece493.group5.adjustableaudio.services.MicrophoneService;

import java.util.Observable;
import java.util.Observer;

public class MicrophoneServiceInteractor
    implements IServiceInteractor
{
    private static final String TAG = MicrophoneServiceInteractor.class.getSimpleName();

    private Context context;
    private MicrophoneService service;
    private boolean connected;

    private final MicrophoneDataListener microphoneDataListener = new MicrophoneDataListener() {
        @Override
        public void onIsRecordingChanged(boolean isRecording) {
            MicrophoneServiceInteractor.this.onIsRecordingChanged(isRecording);
        }
    };

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibinder)
        {
            MicrophoneService.MicrophoneBinder binder = (MicrophoneService.MicrophoneBinder) ibinder;

            service = binder.getService();
            service.addMicrophoneDataObserver(microphoneDataListener);

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

    public void toggleRecording()
    {
        service.toggleRecording();
    }

    public boolean isConnected()
    {
        return connected;
    }

    public void onConnectionEstablished() {}
    public void onConnectionLost() {}
    public void onIsRecordingChanged(boolean isRecording) {}
}
