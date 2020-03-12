package com.ece493.group5.adjustableaudio.adapters;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class MediaPlayerAdapter extends Observable
{
    private static final String TAG = MediaPlayerAdapter.class.getSimpleName();

    public static final String BUNDLE_QUEUE =  "BUNDLE_QUEUE";
    public static final String BUNDLE_QUEUE_INDEX =  "BUNDLE_QUEUE_INDEX";

    private Context applicationContext;
    private AudioManager audioManager;
    private AudioFocusChecker audioFocusChecker;
    private MediaPlayer mediaPlayer;
    private int state;
    private int queueIndex;
    private ArrayList<Song> queue;

    private Boolean playbackDelayed;
    public Boolean mediaPlayedToCompletion;
    private Boolean audioNoisyReceiverRegistered;

    private final BroadcastReceiver audioNoisyReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction()))
                    {
                        if (isPlaying())
                        {
                            pause();
                        }
                    }
                }
            };

    private static IntentFilter AUDIO_NOISY_INTENT_FILTER = new IntentFilter
            (AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    public MediaPlayerAdapter(Context context)
    {
        this.applicationContext = context.getApplicationContext();
        this.audioManager = (AudioManager)
                this.applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.audioFocusChecker = new AudioFocusChecker();

        queueIndex = -1;
        queue = new ArrayList<>();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (hasNextSong())
                    skipToNextSong();
                else
                    stop();
            }
        });

        playbackDelayed = false;
        audioNoisyReceiverRegistered = false;
        mediaPlayedToCompletion = false;
        setState(PlaybackState.STATE_PAUSED);
    }

    public Song getSong(Integer index)
    {
        if (!isValidQueueIndex(index))
            return null;

        return queue.get(index);
    }

    public Song getCurrentSong()
    {
        return getSong(queueIndex);
    }

    private boolean isValidQueueIndex(Integer index)
    {
        return !(index == null || index < 0 || index >= queue.size());
    }

    public boolean hasNextSong()
    {
        return isValidQueueIndex(queueIndex + 1);
    }

    public boolean hasPreviousSong()
    {
        return isValidQueueIndex(queueIndex - 1);
    }

    public void skipToNextSong()
    {
        setQueueIndex(queueIndex + 1);
    }

    public void skipToPreviousSong()
    {
        setQueueIndex(queueIndex - 1);
    }

    public List<Song> getQueue()
    {
        return queue;
    }

    public int getQueueIndex()
    {
        return queueIndex;
    }

    public void setQueueIndex(int index)
    {
        if (queueIndex != index)
        {
            queueIndex = index;
            onQueueIndexChanged();
        }
    }

    private void onQueueIndexChanged()
    {
        boolean wasPlaying = isPlaying();

        if (wasPlaying)
            stop();
        reset();

        try
        {
            mediaPlayer.setDataSource(getCurrentSong().getFilename());
            mediaPlayer.prepare();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (wasPlaying)
            play();

        notifyPlaybackStateChanged();
    }

    public void stop()
    {
        audioFocusChecker.abandonAudioFocus();
        unRegisterAudioNoisyReceiver();
        setState(PlaybackState.STATE_STOPPED);
    }

    public void release()
    {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void reset()
    {
        mediaPlayer.reset();
    }

    private void registerAudioNoisyReceiver()
    {
        if (!audioNoisyReceiverRegistered)
        {
            applicationContext.registerReceiver(audioNoisyReceiver,AUDIO_NOISY_INTENT_FILTER);
            audioNoisyReceiverRegistered = true;
        }
    }

    private void unRegisterAudioNoisyReceiver()
    {
        if (audioNoisyReceiverRegistered)
        {
            applicationContext.unregisterReceiver(audioNoisyReceiver);
            audioNoisyReceiverRegistered = false;
        }
    }

    private Boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public void play()
    {
        if (isPlaying())
            return;

        if (audioFocusChecker.requestAudioFocus())
        {
            registerAudioNoisyReceiver();

            mediaPlayer.start();
            setState(PlaybackState.STATE_PLAYING);
        }
    }

    public void pause()
    {
        if (!isPlaying())
            return;

        if (!playbackDelayed)
            audioFocusChecker.abandonAudioFocus();

        unRegisterAudioNoisyReceiver();
        mediaPlayer.pause();
        setState(PlaybackState.STATE_PAUSED);
    }

    public void seekTo(int position)
    {
        mediaPlayer.seekTo(position);
    }

    public void setVolume(float leftVolume, float rightVolume)
    {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    private void setState(int newPlayerState)
    {
        if (state != newPlayerState)
        {
            state = newPlayerState;
            notifyPlaybackStateChanged();
        }
    }

    public int getState()
    {
        return state;
    }

    public long getPosition()
    {
        return mediaPlayer.getCurrentPosition();
    }

    public float getPlaybackSpeed()
    {
        return 1.0f;
    }

    public void notifyPlaybackStateChanged()
    {
        setChanged();
        notifyObservers(getPlaybackState());
    }

    public PlaybackState getPlaybackState()
    {
        Bundle extras = new Bundle();
        extras.putInt(BUNDLE_QUEUE_INDEX, queueIndex);
        extras.putParcelableArrayList(BUNDLE_QUEUE, queue);

        @SuppressLint("WrongConstant")
        PlaybackState playbackState = new PlaybackState.Builder()
                .setExtras(extras)
                .setState(
                    getState(),
                    getPosition(),
                    getPlaybackSpeed())
                .build();

        return playbackState;
    }

    public void enqueue(@Nullable Bundle extras)
    {
        if (extras == null)
            return;

        getQueue().add(Song.fromBundle(extras));
        if (getQueue().size() == 1)
            setQueueIndex(0);
        else
            notifyPlaybackStateChanged();
    }

    public void dequeue(@Nullable Bundle extras)
    {
        if (extras == null)
            return;

        int index = extras.getInt(BUNDLE_QUEUE_INDEX, -1);
        if (getQueueIndex() == index)
        {
            if (hasNextSong())
                skipToNextSong();
            else if (hasPreviousSong())
                skipToPreviousSong();
        }

        getQueue().remove(index);
        notifyPlaybackStateChanged();
    }

    public void onSongSelected(@Nullable Bundle extras)
    {
        if (extras == null)
            return;

        setQueueIndex(extras.getInt(BUNDLE_QUEUE_INDEX, -1));
    }

    class AudioFocusChecker implements AudioManager.OnAudioFocusChangeListener
    {
        @Override
        public void onAudioFocusChange(int focusChange)
        {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                if (playbackDelayed && !isPlaying())
                {
                    play();
                }
                else if (isPlaying())
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
                stop();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                // Pause playback
                if (isPlaying())
                {
                    playbackDelayed = true;
                    pause();
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
