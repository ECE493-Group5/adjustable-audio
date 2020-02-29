package com.ece493.group5.adjustableaudio.adapters;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.util.Log;

import java.io.IOException;


public class MediaPlayerAdapter {

    private Context applicationContext;
    private AudioManager audioManager;
    private AudioFocusChecker audioFocusChecker;
    private MediaPlayer mediaPlayer;
    private String currentMediaFile;
    private int state;

    private Boolean playbackDelayed;
    private Boolean mediaPlayedToCompletion;


    public MediaPlayerAdapter(Context context)
    {
        this.applicationContext = context.getApplicationContext();
        this.audioManager = (AudioManager)
                this.applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.audioFocusChecker = new AudioFocusChecker();
        playbackDelayed = false;
    }


    private void createMediaPlayer()
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                }
            });
        }
    }

    public void stop()
    {
        audioFocusChecker.abandonAudioFocus();
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

    private Boolean checkPlaying()
    {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying())
        {
            return true;
        }
        return false;
    }


    public void playMediaFile()
    {
        this.playFile("Placeholder");
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
            AssetFileDescriptor assetFileDescriptor = this.applicationContext.getAssets().openFd(filename);
            this.mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }

        try
        {
            this.mediaPlayer.prepare();
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
        this.playMedia();

    }


    public void playMedia()
    {
        if (this.audioFocusChecker.requestAudioFocus())
        {
            this.onPlay();
        }
    }


    public void onPlay()
    {
        if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.start();
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
        }
    }


    public void setVolume(float leftVolume, float rightVolume)
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }


    private void setMediaPlayerState(PlaybackState newPlayerState)
    {
        this.state = newPlayerState.getState();

        if (this.state == PlaybackState.STATE_STOPPED)
        {
            this.mediaPlayedToCompletion = true;

        }

        
    }


    class AudioFocusChecker implements AudioManager.OnAudioFocusChangeListener
    {
        @Override
        public void onAudioFocusChange(int focusChange) {

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
                    setVolume(1, 1);
                }
                playbackDelayed = false;
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                // Permanent loss of audio focus
                // Pause playback immediately
                audioManager.abandonAudioFocus(this);
                playbackDelayed = false;
                stop();
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
                setVolume(0, 0);
            }
        }


        private Boolean requestAudioFocus()
        {
           int request = audioManager
                   .requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                           AudioManager.AUDIOFOCUS_GAIN);
           if (request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
           {
               return true;
           }
           return false;
        }


        private void abandonAudioFocus()
        {
            audioManager.abandonAudioFocus(this);
        }
    }
}
