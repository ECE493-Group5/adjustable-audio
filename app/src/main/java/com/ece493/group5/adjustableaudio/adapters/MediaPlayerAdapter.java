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
    public Boolean mediaPlayedToCompletion;
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
                    Log.d(TAG, "On Completion");

                    setMediaPlayerState(PlaybackState.STATE_SKIPPING_TO_NEXT);
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
        Log.d(TAG, "stopping song");
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


    public void playFile(String filename)
    {
        Log.d(TAG, "About to play file");
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
        if (!playbackDelayed) {
            Log.d(TAG, "abandoning audio focus");
            this.audioFocusChecker.abandonAudioFocus();
        }

        this.unRegisterAudioNoisyReceiver();
        onPause();
    }


    public void onPause()
    {
        if(this.mediaPlayer != null && this.mediaPlayer.isPlaying())
        {
            Log.d(TAG, "Will pause now");
            this.mediaPlayer.pause();
            setMediaPlayerState(PlaybackState.STATE_PAUSED);
        }
    }


    public void onSeekTo(long position)
    {
        if(this.mediaPlayer != null && this.mediaPlayer.isPlaying())
        {

            this.mediaPlayer.seekTo((int) position);
        }
        setMediaPlayerState(this.state);
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
        Log.d(TAG, Integer.toString(newPlayerState));

        if (this.state == PlaybackState.STATE_STOPPED ||
                this.state == PlaybackState.STATE_SKIPPING_TO_NEXT)
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

    public int getState()
    {
        return state;
    }

    public long getPosition()
    {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();

        return 0;
    }

    public float getPlaybackSpeed()
    {
        return 1.0f;
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
        else if (this.state == PlaybackState.STATE_SKIPPING_TO_NEXT)
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
                    Log.d(TAG, "AudioFocusReceived Gain");
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
                Log.d(TAG, "AudioFocusReceived Loss");

                stopMedia();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                // Pause playback
                if (checkPlaying()) {
                    playbackDelayed = true;
                    Log.d(TAG, "AudioFocusReceived Loss Transient");
                    pauseMedia();
                }

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                // Lower the volume, keep playing
                Log.d(TAG, "AudioFocusReceived Loss Transient DUCK");
                setVolume(1.0f, 1.0f);
            }
        }


        private Boolean requestAudioFocus()
        {
           int request = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                           AudioManager.AUDIOFOCUS_GAIN);
           Log.d(TAG, "Request Audio Focus");
           return request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }


        private void abandonAudioFocus()
        {
            Log.d(TAG, "AudioManager to abandon AudioFocus");
            audioManager.abandonAudioFocus(this);
        }
    }
}
