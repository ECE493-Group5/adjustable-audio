package com.ece493.group5.adjustableaudio.controllers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;
import com.ece493.group5.adjustableaudio.interfaces.IServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.MicrophoneDataListener;
import com.ece493.group5.adjustableaudio.adapters.MicrophonePlayerAdapter;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;
import com.ece493.group5.adjustableaudio.services.MicrophoneService;

public class MicrophoneServiceInteractor
    implements IServiceInteractor, IAudioDevice
{
    private static final String TAG = MicrophoneServiceInteractor.class.getSimpleName();

    private Context context;
    private MicrophoneService service;
    private boolean bindAttempted;

    private final MicrophoneDataListener microphoneDataListener = new MicrophoneDataListener() {
        @Override
        public void onIsRecordingChanged(boolean isRecording) {
            MicrophoneServiceInteractor.this.onIsRecordingChanged(isRecording);
        }

        @Override
        public void onIsNoiseFilterEnabledChanged(boolean isEnabled) {
            MicrophoneServiceInteractor.this.onIsNoiseFilterEnabledChanged(isEnabled);
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

            onConnectionEstablished();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            service = null;
            onConnectionLost();
        }
    };

    public MicrophoneServiceInteractor(Context c)
    {
        context = c;
        bindAttempted = false;
    }

    public void connect()
    {
        if (!isBindAttempted()) {
            Intent intent = new Intent(context, MicrophoneService.class);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
            bindAttempted = true;
        }
    }

    public void disconnect()
    {
        if (isBindAttempted()) {
            context.unbindService(connection);
            bindAttempted = false;
        }
    }

    public void toggleRecording()
    {
        getMicrophonePlayer().toggleRecording();
    }

    public boolean isBindAttempted()
    {
        return bindAttempted;
    }

    private MicrophonePlayerAdapter getMicrophonePlayer()
    {
        return service.getMicrophonePlayer();
    }

    @Override
    public void setLeftVolume(double percent)
    {
        if (percent < 0)
            percent = 0;
        else if (percent > 1)
            percent = 1;

        getMicrophonePlayer().setLeftVolume(percent);
    }

    @Override
    public void setRightVolume(double percent)
    {
        if (percent < 0)
            percent = 0;
        else if (percent > 1)
            percent = 1;

        getMicrophonePlayer().setRightVolume(percent);
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

    public void toggleNoiseFilter()
    {
        getMicrophonePlayer().toggleNoiseFilter();
    }

    public void onConnectionEstablished() {}
    public void onConnectionLost() {}
    public void onIsRecordingChanged(boolean isRecording) {}
    public void onIsNoiseFilterEnabledChanged(boolean isEnabled) {}
}
