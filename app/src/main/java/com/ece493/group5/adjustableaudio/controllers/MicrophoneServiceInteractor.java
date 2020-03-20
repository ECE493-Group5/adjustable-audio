package com.ece493.group5.adjustableaudio.controllers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;
import com.ece493.group5.adjustableaudio.interfaces.IServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.MicrophoneDataListener;
import com.ece493.group5.adjustableaudio.microphone.MicrophonePlayer;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;
import com.ece493.group5.adjustableaudio.services.MicrophoneService;

import java.util.Observable;
import java.util.Observer;

public class MicrophoneServiceInteractor
    implements IServiceInteractor, IAudioDevice
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

            MicrophoneData data = getMicrophonePlayer().getMicrophoneData();
            data.addObserver(microphoneDataListener);
            // Update the observer with all the microphone data
            data.setAllChanges();
            microphoneDataListener.update(data, null);
            data.clearAllChanges();

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
        getMicrophonePlayer().toggleRecording();
    }

    public boolean isConnected()
    {
        return connected;
    }

    private MicrophonePlayer getMicrophonePlayer()
    {
        return service.getMicrophonePlayer();
    }

    @Override
    public void setLeftVolume(double percent)
    {
        if (percent < 0)
            percent = 0;
        else if (percent > 0.95)
            percent = 0.95;

        getMicrophonePlayer().setLeftVolume(1 - percent);
    }

    @Override
    public void setRightVolume(double percent)
    {
        if (percent < 0)
            percent = 0;
        else if (percent > 0.95)
            percent = 0.95;

        getMicrophonePlayer().setRightVolume(1 - percent);
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        getMicrophonePlayer().setEqualizerBand(band, level);
    }

    @Override
    public void enableEqualizer()
    {
        getMicrophonePlayer().enableEqualizer();
    }

    @Override
    public void disableEqualizer()
    {
        getMicrophonePlayer().disableEqualizer();
    }

    public void onConnectionEstablished() {}
    public void onConnectionLost() {}
    public void onIsRecordingChanged(boolean isRecording) {}
}
