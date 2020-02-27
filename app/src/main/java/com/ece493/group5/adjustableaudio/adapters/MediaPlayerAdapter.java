package com.ece493.group5.adjustableaudio.adapters;

import android.content.Context;
import android.drm.DrmStore;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;

public class MediaPlayerAdapter {

    private Context applicationContext;
    private AudioManager audioManager;
    private AudioFocusChecker audioFocusChecker;
    private MediaPlayer mediaPlayer;
    private int state;


    public MediaPlayerAdapter(Context context)
    {
        this.applicationContext = context.getApplicationContext();
        this.audioManager = (AudioManager) this.applicationContext
                .getSystemService(Context.AUDIO_SERVICE);
        this.audioFocusChecker = new AudioFocusChecker();
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


    public void release()
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
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


    private void setMediaPlayerState(int newPlayerState)
    {
        this.state = newPlayerState;
    }

    class AudioFocusChecker implements AudioManager.OnAudioFocusChangeListener
    {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {

            }
        }


        private Boolean requestAudioFocus()
        {
           int request = audioManager
                   .requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
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
