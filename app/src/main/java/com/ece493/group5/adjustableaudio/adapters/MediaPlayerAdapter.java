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
import android.util.Log;

import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.enums.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;


public class MediaPlayerAdapter extends Observable
{
    private static final String TAG = MediaPlayerAdapter.class.getSimpleName();

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
    private final MediaPlayer.OnCompletionListener mediaCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (hasNextSong()) {
                skipToNextSong();
                play();
            }
            else {
                stop();
            }
        }
    };

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
        mediaPlayer.setOnCompletionListener(mediaCompletionListener);

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

    public boolean hasCurrentSong()
    {
        return getCurrentSong() != null;
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
        Log.d(TAG, "skipToNext()");
        setQueueIndex(queueIndex + 1);
    }

    public void skipToPreviousSong()
    {
        Log.d(TAG, "skipToPrevious()");
        setQueueIndex(queueIndex - 1);
    }

    public ArrayList<Song> getQueue()
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

            notifyQueueIndexChanged();
            onQueueIndexChanged();
        }
    }

    private void onQueueIndexChanged()
    {
        reset();

        if (hasCurrentSong())
        {
            try
            {
                mediaPlayer.setDataSource(getCurrentSong().getFilename());
                mediaPlayer.prepare();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void stop()
    {
        audioFocusChecker.abandonAudioFocus();
        unregisterAudioNoisyReceiver();
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

    private void unregisterAudioNoisyReceiver()
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

        unregisterAudioNoisyReceiver();
        mediaPlayer.pause();
        setState(PlaybackState.STATE_PAUSED);
    }

    public void seekTo(long position)
    {
        mediaPlayer.seekTo((int) position);
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
            notifyStateChanged();
        }
    }

    public int getState()
    {
        return state;
    }

    public long getElapsedDuration()
    {
        return mediaPlayer.getCurrentPosition();
    }

    public long getTotalDuration()
    {
        long total = 0;

        if (hasCurrentSong())
            total = getCurrentSong().getDuration();

        return total;
    }

    public float getPlaybackSpeed()
    {
        return 1.0f;
    }

    public void notifyAllChanged()
    {
        notifyStateChanged();
        notifyQueueChanged();
        notifyQueueIndexChanged();
        notifyDurationChanged();
    }

    public void notifyDurationChanged()
    {
        setChanged();

        Bundle extras = new Bundle();
        extras.putSerializable(MediaData.EXTRA_DATA_CHANGED, MediaData.DURATION);
        extras.putLong(MediaData.EXTRA_DURATION, getElapsedDuration());

        notifyObservers(buildPlaybackState(extras));
    }

    public void notifyStateChanged()
    {
        setChanged();

        Bundle extras = new Bundle();
        extras.putSerializable(MediaData.EXTRA_DATA_CHANGED, MediaData.STATE);

        notifyObservers(buildPlaybackState(extras));
    }

    public void notifyQueueChanged()
    {
        setChanged();

        Bundle extras = new Bundle();
        extras.putSerializable(MediaData.EXTRA_DATA_CHANGED, MediaData.QUEUE);
        extras.putParcelableArrayList(MediaData.EXTRA_QUEUE, getQueue());

        notifyObservers(buildPlaybackState(extras));
    }

    public void notifyQueueIndexChanged()
    {
        setChanged();

        Bundle extras = new Bundle();
        extras.putSerializable(MediaData.EXTRA_DATA_CHANGED, MediaData.QUEUE_INDEX);
        extras.putInt(MediaData.EXTRA_QUEUE_INDEX, getQueueIndex());
        extras.putParcelable(MediaData.EXTRA_SONG, getCurrentSong());

        notifyObservers(buildPlaybackState(extras));
    }

    private PlaybackState buildPlaybackState(Bundle extras)
    {
        @SuppressLint("WrongConstant")
        PlaybackState playbackState = new PlaybackState.Builder()
                .setExtras(extras)
                .setState(
                    getState(),
                    getElapsedDuration(),
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

        notifyQueueChanged();
    }

    public void dequeue(@Nullable Bundle extras)
    {
        if (extras == null)
            return;

        int index = extras.getInt(MediaData.EXTRA_QUEUE_INDEX, -1);
        int oldIndex = getQueueIndex();

        getQueue().remove(index);
        notifyQueueChanged();

        if (oldIndex == index)
        {
            if (hasCurrentSong()) {
                notifyQueueIndexChanged();
                onQueueIndexChanged();
            } else if (hasPreviousSong()) {
                skipToPreviousSong();
            } else {
                setQueueIndex(-1);
            }
        }
    }

    public void onSongSelected(@Nullable Bundle extras)
    {
        if (extras == null)
            return;

        setQueueIndex(extras.getInt(MediaData.EXTRA_QUEUE_INDEX, -1));
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
