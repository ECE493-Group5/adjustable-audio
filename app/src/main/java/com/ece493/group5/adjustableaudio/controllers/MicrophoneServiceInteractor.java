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

/**
 * The MicrophoneServiceInteractor helps implement the following requirements:
 *
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes through an Equalizer
 * #SRS: Makeshift Hearing Aid
 */

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
        public void onModeChanged(int mode) {
            MicrophoneServiceInteractor.this.onModeChanged(mode);
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
    public void setLeftRightVolumeRatio(double ratio)
    {
        getMicrophonePlayer().setLeftRightVolumeRatio(ratio);
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

    public void setMode(int mode)
    {
        getMicrophonePlayer().setMode(mode);
    }

    public void onConnectionEstablished() {}
    public void onConnectionLost() {}
    public void onIsRecordingChanged(boolean isRecording) {}
    public void onModeChanged(int mode) {}
}
