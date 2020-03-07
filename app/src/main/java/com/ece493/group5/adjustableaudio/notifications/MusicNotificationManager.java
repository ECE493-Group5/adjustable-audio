package com.ece493.group5.adjustableaudio.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.services.MusicService;

import androidx.annotation.Nullable;

public class MusicNotificationManager extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 200;
    private static final int REQUEST_CODE = 100;

    private static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";
    private static final String ACTION_SKIP_NEXT = "ACTION_SKIP_NEXT";
    private static final String ACTION_SKIP_PREVIOUS = "ACTION_SKIP_PREVIOUS";

    private MusicService musicService;
    private NotificationManager notificationManager;

    private MediaController mediaController;
    private MediaMetadata currentMediaMetadata;
    private MediaSession.Token sessionToken;
    private PlaybackState currentPlaybackState;

    private PendingIntent playIntent;
    private PendingIntent pauseIntent;
    private PendingIntent skipNextIntent;
    private PendingIntent skipPreviousIntent;

    private boolean startedNotification;

    private MediaController.Callback mediaControllerCallback = new MediaController.Callback() {
        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            updateSession();
        }

        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackState state) {
            super.onPlaybackStateChanged(state);
            currentPlaybackState = state;

            if (state != null && (state.getState() == PlaybackState.STATE_STOPPED ||
                    state.getState() == PlaybackState.STATE_NONE))
            {
                stopNotification();
            }
            else
            {
                Notification notification = createNotification();
                if (notification != null)
                {
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadata metadata) {
            super.onMetadataChanged(metadata);
            currentMediaMetadata = metadata;

            Notification notification = createNotification();
            if (notification != null)
            {
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }
    };


    public MusicNotificationManager(MusicService service)
    {
        musicService = service;
        notificationManager =
                (NotificationManager) musicService.getSystemService(Context.NOTIFICATION_SERVICE);

        startedNotification = false;
        updateSession();

        String packageName = musicService.getPackageName();

        playIntent = PendingIntent.getBroadcast(musicService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);

        pauseIntent = PendingIntent.getBroadcast(musicService, REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);

        skipNextIntent = PendingIntent.getBroadcast(musicService, REQUEST_CODE,
                new Intent(ACTION_SKIP_NEXT).setPackage(packageName),
                PendingIntent.FLAG_CANCEL_CURRENT);

        skipPreviousIntent = PendingIntent.getBroadcast(musicService, REQUEST_CODE,
                new Intent(ACTION_SKIP_PREVIOUS).setPackage(packageName),
                PendingIntent.FLAG_CANCEL_CURRENT);

        notificationManager.cancelAll();
    }


    public NotificationManager getNotificationManager()
    {
        return notificationManager;
    }


    public void startNotification()
    {
        if(!startedNotification)
        {
            currentMediaMetadata = mediaController.getMetadata();
            currentPlaybackState = mediaController.getPlaybackState();

            Notification notification = createNotification();
            if (notification != null)
            {
                mediaController.registerCallback(mediaControllerCallback);
                IntentFilter intentFilter = new IntentFilter();

                intentFilter.addAction(ACTION_PAUSE);
                intentFilter.addAction(ACTION_PLAY);
                intentFilter.addAction(ACTION_SKIP_NEXT);
                intentFilter.addAction(ACTION_SKIP_PREVIOUS);

                musicService.registerReceiver(this, intentFilter);
                musicService.startForeground(NOTIFICATION_ID, notification);
                startedNotification = true;
            }
        }
    }


    public void stopNotification()
    {
        if(startedNotification)
        {
            startedNotification = false;
            mediaController.unregisterCallback(mediaControllerCallback);

            notificationManager.cancel(NOTIFICATION_ID);
            musicService.unregisterReceiver(this);
            musicService.stopForeground(true);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (action == null)
        {
            return;
        }

        if (action.equals(ACTION_PLAY))
        {
            mediaController.getTransportControls().play();
        }
        else if (action.equals(ACTION_PAUSE))
        {
            mediaController.getTransportControls().pause();
        }
        else if (action.equals(ACTION_SKIP_NEXT))
        {
            mediaController.getTransportControls().skipToNext();
        }
        else if (action.equals(ACTION_SKIP_PREVIOUS))
        {
            mediaController.getTransportControls().skipToPrevious();
        }
    }


    private Notification createNotification()
    {
        if (currentMediaMetadata == null || currentPlaybackState == null)
        {
            return null;
        }

        Notification.Builder builder = new Notification.Builder(musicService);

        if ((currentPlaybackState.getActions() & PlaybackState.ACTION_SKIP_TO_PREVIOUS) != 0)
        {
            builder.addAction(R.drawable.ic_skip_previous_light_grey_24dp, "Reverse",
                    skipPreviousIntent);
        }

        if (currentPlaybackState.getState() == PlaybackState.STATE_PLAYING)
        {
            builder.addAction(R.drawable.ic_pause_light_grey_24dp, "Pause", pauseIntent);
        }
        else
        {
            builder.addAction(R.drawable.ic_play_arrow_light_grey_24dp, "Play", playIntent);
        }

        if ((currentPlaybackState.getActions() & PlaybackState.ACTION_SKIP_TO_NEXT) != 0)
        {
            builder.addAction(R.drawable.ic_skip_next_light_grey_24dp, "Next", skipNextIntent);
        }

        if (currentPlaybackState == null || !startedNotification)
        {
            musicService.stopForeground(true);
        }

        builder.setUsesChronometer(true).setVisibility(Notification.VISIBILITY_PUBLIC)
                .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(
                    new int[]{1})  // show only play/pause in compact view
            .setMediaSession(sessionToken));

        if(currentPlaybackState.getState() == PlaybackState.STATE_PLAYING
                && currentPlaybackState.getPosition() > 0)
        {
            builder.setWhen(System.currentTimeMillis() - currentPlaybackState.getPosition())
                    .setShowWhen(true)
                    .setUsesChronometer(true);
        }
        else
        {
            builder.setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false);
        }

        builder.setOngoing(currentPlaybackState.getState() == PlaybackState.STATE_PLAYING);
        return builder.build();
    }


    private void setNotificationPlaybackState()
    {

    }


    private void updateSession()
    {
        MediaSession.Token token = musicService.getSessionToken();

        if (sessionToken == null || !sessionToken.equals(token))
        {
            if(mediaController != null)
            {
                mediaController.unregisterCallback(mediaControllerCallback);
            }
            sessionToken = token;

            mediaController = new MediaController(musicService, sessionToken);

            if (startedNotification)
            {
                mediaController.registerCallback(mediaControllerCallback);
            }
        }
    }

}
