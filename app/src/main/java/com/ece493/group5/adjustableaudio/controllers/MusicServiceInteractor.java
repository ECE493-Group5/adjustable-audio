package com.ece493.group5.adjustableaudio.controllers;

import android.content.ComponentName;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.interfaces.IServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.MediaSessionListener;
import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;
import com.ece493.group5.adjustableaudio.models.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;

import java.util.Objects;

/**
 * The MusicServiceInteractor helps implement the following requirements:
 *
 * #SRS: Media Player
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes Through an Equalizer
 */

public class MusicServiceInteractor
    implements IServiceInteractor, IAudioDevice
{
    private static final String TAG = MusicServiceInteractor.class.getSimpleName();
    private MediaBrowser mediaBrowser;
    private MediaController mediaController;
    private Context context;

    private final MediaController.Callback controllerCallback = new MediaController.Callback()
    {
        @Override
        public void onExtrasChanged(@Nullable Bundle extras) {
            super.onExtrasChanged(extras);
            assert extras != null;
            onDataChanged(MediaData.extract(extras));
        }
    };

    public MusicServiceInteractor(Context c)
    {
        context = c;

        MediaBrowser.ConnectionCallback connectionCallback = new MediaBrowser.ConnectionCallback() {
            @Override
            public void onConnected() {
                MediaSession.Token token = mediaBrowser.getSessionToken();

                mediaController = new MediaController(context, token);
                mediaController.registerCallback(controllerCallback);

                requestAllChanges();
                onConnectionEstablished();
            }

            @Override
            public void onConnectionSuspended() {
                super.onConnectionSuspended();
                onConnectionLost();
            }

            @Override
            public void onConnectionFailed() {
                super.onConnectionFailed();
                Log.e(TAG, "Failed to connect to MediaBrowserService.");
                onConnectionLost();
            }
        };

        mediaBrowser = new MediaBrowser(
                context,
                new ComponentName(context, MusicService.class),
                connectionCallback, null);
    }

    public void onConnectionEstablished() {}
    public void onConnectionLost() {}
    public void onDataChanged(MediaData data) {}

    public void connect()
    {
        if (!isConnected())
            mediaBrowser.connect();
    }

    public void disconnect()
    {
        if (isConnected())
            mediaBrowser.disconnect();
    }

    public boolean isConnected()
    {
        return mediaBrowser.isConnected();
    }

    public void requestAllChanges()
    {
        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_REQUEST_ALL_CHANGES,
                        null);
    }

    public void selectSong(int position)
    {
        Bundle extras = new Bundle();
        extras.putInt(MediaSessionListener.EXTRA_QUEUE_INDEX, position);
        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_SONG_SELECTED, extras);
    }

    public void enqueue(Song song)
    {
        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_ENQUEUE, song.toBundle());
    }

    public void dequeue(int position)
    {
        Bundle extras = new Bundle();
        extras.putInt(MediaSessionListener.EXTRA_QUEUE_INDEX, position);
        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_DEQUEUE, extras);
    }

    public void skipToPrevious()
    {
        mediaController.getTransportControls().skipToPrevious();
    }

    public void skipToNext()
    {
        mediaController.getTransportControls().skipToNext();
    }

    public void togglePlayPause()
    {
        mediaController
                .getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_TOGGLE, null);
    }

    public void seekTo(long position)
    {
        mediaController.getTransportControls().seekTo(position);
    }

    public MediaData getMediaData()
    {
        return MediaData.extract(Objects.requireNonNull(mediaController.getExtras()));
    }

    @Override
    public void setLeftRightVolumeRatio(double ratio)
    {
        Bundle extras = new Bundle();
        extras.putDouble(MediaSessionListener.EXTRA_VOLUME, ratio);

        mediaController
                .getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_LEFT_RIGHT_VOLUME_RATIO_CHANGED, extras);
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        Bundle args = new Bundle();
        args.putShort(MediaSessionListener.EXTRA_EQUALIZER_BAND, band);
        args.putShort(MediaSessionListener.EXTRA_DECIBEL_LEVEL, level);

        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_EQUALIZER_BAND_CHANGED, args);
    }

    @Override
    public void enableEqualizer()
    {
        Bundle extras = new Bundle();
        extras.putBoolean(MediaSessionListener.EXTRA_EQUALIZER_ENABLED, true);

        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_EQUALIZER_STATE, extras);
    }

    @Override
    public void disableEqualizer()
    {
        Bundle extras = new Bundle();
        extras.putBoolean(MediaSessionListener.EXTRA_EQUALIZER_ENABLED, false);

        mediaController.getTransportControls()
                .sendCustomAction(MediaSessionListener.ACTION_EQUALIZER_STATE, extras);
    }
}
