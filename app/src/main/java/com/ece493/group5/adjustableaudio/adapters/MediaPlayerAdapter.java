package com.ece493.group5.adjustableaudio.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.drm.DrmStore;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.ece493.group5.adjustableaudio.listeners.PlaybackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MediaPlayerAdapter
{
    private static final String TAG = MediaPlayerAdapter.class.getSimpleName();

    private Context applicationContext;
    private PlaybackListener mediaListener;
    private AudioManager audioManager;
    private AudioFocusChecker audioFocusChecker;
    private MediaPlayer mediaPlayer;
    private String currentMediaFile;
    private MediaMetadata currentMediaMetadata;
    private int state;

    private Boolean playbackDelayed;
    private Boolean mediaPlayedToCompletion;
    private Boolean audioNoisyReceiverRegistered;

    private final BroadcastReceiver audioNoisyReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                        if (checkPlaying()) {
                            pauseMedia();
                        }
                    }
                }
            };

    private static IntentFilter AUDIO_NOISY_INTENT_FILTER = new IntentFilter
            (AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    public MediaPlayerAdapter(Context context, PlaybackListener newMediaListener)
    {
        this.applicationContext = context.getApplicationContext();
        this.mediaListener = newMediaListener;
        this.audioManager = (AudioManager)
                this.applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.audioFocusChecker = new AudioFocusChecker();

        playbackDelayed = false;
        audioNoisyReceiverRegistered = false;
        mediaPlayedToCompletion = false;
        setMediaPlayerState(PlaybackState.STATE_PAUSED);
    }


    private void createMediaPlayer()
    {
        if (this.mediaPlayer == null)
        {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    setMediaPlayerState(PlaybackState.STATE_PAUSED);
                }
            });
        }
    }


    public void stopMedia()
    {
       this.audioFocusChecker.abandonAudioFocus();
       unRegisterAudioNoisyReceiver();
       onStop();
    }


    public void onStop()
    {
        setMediaPlayerState(PlaybackState.STATE_STOPPED);
        this.release();
    }


    private void release()
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }


    private void registerAudioNoisyReceiver()
    {
        if (!audioNoisyReceiverRegistered)
        {
            this.applicationContext.registerReceiver(audioNoisyReceiver,AUDIO_NOISY_INTENT_FILTER);
            audioNoisyReceiverRegistered = true;
        }
    }


    private void unRegisterAudioNoisyReceiver()
    {
        if (audioNoisyReceiverRegistered)
        {
            this.applicationContext.unregisterReceiver(audioNoisyReceiver);
            audioNoisyReceiverRegistered = false;
        }
    }


    private Boolean checkPlaying()
    {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying())
        {
            return true;
        }
        return false;
    }


    public void playMediaFile(MediaMetadata mediaFile)
    {
        this.currentMediaMetadata = mediaFile;
        String mediaID = mediaFile.getDescription().getMediaId();
        this.playFile(mediaID);
    }


    private void playFile(String filename)
    {
        Boolean mediaChange = false;

        if (!filename.equals(currentMediaFile))
        {
            mediaChange = true;
        }

        if (this.mediaPlayedToCompletion)
        {
            mediaChange = true;
            this.mediaPlayedToCompletion = false;
        }

        if (mediaChange)
        {
            this.release();
        }
        else
        {
            if (!checkPlaying())
            {
                playMedia();
            }
            return;
        }


        this.createMediaPlayer();

        currentMediaFile = filename;

        try
        {
            this.mediaPlayer.setDataSource(filename);
            this.mediaPlayer.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.playMedia();

    }


    public void playMedia()
    {
        if (this.audioFocusChecker.requestAudioFocus())
        {
            registerAudioNoisyReceiver();
            this.onPlay();
        }
    }


    public void onPlay()
    {
        if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.start();
            setMediaPlayerState(PlaybackState.STATE_PLAYING);
        }
    }


    public void pauseMedia()
    {
        onPause();
    }


    public void onPause()
    {
        if(this.mediaPlayer != null && this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.pause();
            setMediaPlayerState(PlaybackState.STATE_PAUSED);
        }
    }


    public void setVolume(float leftVolume, float rightVolume)
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }


    private void setMediaPlayerState(int newPlayerState)
    {
        this.state = newPlayerState;

        if (this.state == PlaybackState.STATE_STOPPED)
        {
            this.mediaPlayedToCompletion = true;
        }

        long position;
        if (this.mediaPlayer == null)
        {
            position = 0;
        }
        else
        {
            position = this.mediaPlayer.getCurrentPosition();
        }

        PlaybackState.Builder stateBuilder = new PlaybackState.Builder();
        stateBuilder.setActions(this.setAvailableMediaActions());
        stateBuilder.setState(this.state, position,1.0f,
                SystemClock.elapsedRealtime());
        this.mediaListener.onPlaybackStateChange(stateBuilder.build());
    }


    private long setAvailableMediaActions()
    {
        long availableActions = PlaybackState.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS;

        if (this.state == PlaybackState.STATE_PLAYING)
        {
            availableActions = availableActions | PlaybackState.ACTION_PAUSE
                    | PlaybackState.ACTION_STOP;
        }
        else if (this.state == PlaybackState.STATE_PAUSED)
        {
            availableActions = availableActions | PlaybackState.ACTION_PLAY
                    | PlaybackState.ACTION_STOP;
        }
        else if (this.state == PlaybackState.STATE_STOPPED)
        {
            availableActions = availableActions | PlaybackState.ACTION_PLAY
                    | PlaybackState.ACTION_PAUSE;
        }
        return availableActions;
    }


    class AudioFocusChecker implements AudioManager.OnAudioFocusChangeListener
    {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "AudioFocusReceived");
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                if (playbackDelayed && !checkPlaying())
                {
                    playMedia();
                }
                else if (checkPlaying())
                {
                    //Set the volume to normal
                    setVolume(1.0f, 1.0f);
                }
                playbackDelayed = false;
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                // Permanent loss of audio focus
                // Pause playback immediately
                audioManager.abandonAudioFocus(this);
                playbackDelayed = false;
                stopMedia();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                // Pause playback
                if (checkPlaying()) {
                    playbackDelayed = true;
                    pauseMedia();
                }

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                // Lower the volume, keep playing
                setVolume(1.0f, 1.0f);
            }
        }


        private Boolean requestAudioFocus()
        {
           int request = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                           AudioManager.AUDIOFOCUS_GAIN);
            return request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }


        private void abandonAudioFocus()
        {
            audioManager.abandonAudioFocus(this);
        }
    }
}
