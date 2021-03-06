package com.ece493.group5.adjustableaudio.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ece493.group5.adjustableaudio.listeners.MediaSessionListener;
import com.ece493.group5.adjustableaudio.models.AudioData;
import com.ece493.group5.adjustableaudio.interfaces.IAudioDevice;
import com.ece493.group5.adjustableaudio.models.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * The MediaPlayerAdapter helps implement the following requirements:
 *
 * #SRS: Media Player
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes Through an Equalizer
 *
 * In particular, the MediaPlayerAdapter class provides the logic for the media player, and applies
 * the left/right audio balance ratio and equalizer to the media player.
 */

public class MediaPlayerAdapter
        extends Observable
        implements IAudioDevice
{
    private static final String TAG = MediaPlayerAdapter.class.getSimpleName();
    private static final double FOCUS_DROP_FACTOR = 0.5;

    private Context applicationContext;
    private AudioManager audioManager;
    private AudioFocusChecker audioFocusChecker;
    private MediaPlayer mediaPlayer;
    private MediaData mediaData;
    private AudioData audioData;
    private Equalizer equalizer;

    private Boolean requestToStart;
    private Boolean prepared;
    private Boolean playbackDelayed;
    private Boolean audioNoisyReceiverRegistered;

    private final MediaPlayer.OnCompletionListener mediaCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (hasNextSong()) {
                skipToNextSong();
                play();
            } else if (hasCurrentSong()) {
                stop();
                onQueueIndexChanged();
            }
        }
    };

    private final MediaPlayer.OnPreparedListener mediaPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            setPrepared(true);

            if (getRequestToStart()) {
                start();
                setRequestToStart(false);
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
        super();

        this.applicationContext = context.getApplicationContext();
        this.audioManager = (AudioManager)
                this.applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.audioFocusChecker = new AudioFocusChecker();

        mediaData = new MediaData();
        audioData = new AudioData();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mediaPreparedListener);
        mediaPlayer.setOnCompletionListener(mediaCompletionListener);

        prepared = false;
        requestToStart = false;
        playbackDelayed = false;
        audioNoisyReceiverRegistered = false;

        setState(PlaybackState.STATE_PAUSED);
        setupEqualizer();
    }

    private void setupEqualizer()
    {
        try {
            equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize the Equalizer.");
            return;
        }

        //Initialized to normal equalizer preset
        equalizer.usePreset((short)0);
        equalizer.setEnabled(true);

        for (Map.Entry<Short, Short> entry: audioData.getEqualizerSettings())
            equalizer.setBandLevel(entry.getKey(), entry.getValue());
    }

    public Song getCurrentSong()
    {
        return mediaData.getCurrentSong();
    }

    public boolean hasCurrentSong()
    {
        return mediaData.hasCurrentSong();
    }

    public boolean hasNextSong()
    {
        return mediaData.hasNextSong();
    }

    public boolean hasPreviousSong()
    {
        return mediaData.hasPreviousSong();
    }

    public void skipToNextSong()
    {
        mediaData.skipToNextSong();

        if (mediaData.queueIndexChanged())
            onQueueIndexChanged();
    }

    public void skipToPreviousSong()
    {
        mediaData.skipToPreviousSong();

        if (mediaData.queueIndexChanged())
            onQueueIndexChanged();
    }

    public List<Song> getQueue()
    {
        return mediaData.getQueue();
    }

    public int getQueueIndex()
    {
        return mediaData.getQueueIndex();
    }

    private void setQueueIndex(int index)
    {
        mediaData.setQueueIndex(index);

        if (mediaData.queueIndexChanged())
            onQueueIndexChanged();
    }

    private void onQueueIndexChanged()
    {
        reset();

        if (hasCurrentSong())
        {
            setPrepared(false);

            try
            {
                mediaPlayer.setDataSource(getCurrentSong().getFilename());
                mediaPlayer.prepareAsync();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        notifyQueueIndexChanged();
        notifyDurationChanged();
    }

    public void play()
    {
        if (isPlaying())
            return;

        if (audioFocusChecker.requestAudioFocus())
        {
            registerAudioNoisyReceiver();
            start();
        }
    }

    public void start()
    {
        if (!isPrepared()) {
            setRequestToStart(true);
        } else {
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

    public void stop()
    {
        audioFocusChecker.abandonAudioFocus();
        unregisterAudioNoisyReceiver();

        mediaPlayer.stop();
        setState(PlaybackState.STATE_STOPPED);
    }

    public void release()
    {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void reset()
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

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public boolean isPrepared()
    {
        return prepared;
    }

    private void setPrepared(boolean prepared)
    {
        this.prepared = prepared;
    }

    private void setRequestToStart(boolean start)
    {
        requestToStart = start;
    }

    private boolean getRequestToStart()
    {
        return requestToStart;
    }

    private void setState(int newPlayerState)
    {
        mediaData.setState(newPlayerState);

        if (mediaData.stateChanged())
            notifyMediaDataChanged();
    }

    private int getState()
    {
        return mediaData.getState();
    }

    private long getElapsedDuration()
    {
        return mediaPlayer.getCurrentPosition();
    }

    private long getTotalDuration()
    {
        long total = 0;

        if (hasCurrentSong())
            total = getCurrentSong().getDuration();

        return total;
    }

    private float getPlaybackSpeed()
    {
        return 1.0f;
    }

    public void notifyAllChanged()
    {
        mediaData.setAllChanges();
        notifyMediaDataChanged();
    }

    public void notifyDurationChanged()
    {
        mediaData.setElapsedDuration(getElapsedDuration());
        mediaData.setTotalDuration(getTotalDuration());
        notifyMediaDataChanged();
    }

    private void notifyMediaDataChanged()
    {
        setChanged();
        notifyObservers(mediaData);
        mediaData.clearAllChanges();
    }

    private void notifyQueueChanged()
    {
        mediaData.setChanged(MediaData.Type.QUEUE, true);
        notifyMediaDataChanged();
    }

    private void notifyQueueIndexChanged()
    {
        mediaData.setChanged(MediaData.Type.QUEUE_INDEX, true);
        notifyMediaDataChanged();
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

        boolean removingSelectedIndex = false;
        int index = extras.getInt(MediaSessionListener.EXTRA_QUEUE_INDEX, -1);
        if (getQueueIndex() == index)
            removingSelectedIndex = true;

        getQueue().remove(index);
        notifyQueueChanged();

        if (removingSelectedIndex) {
            stop();

            if (hasCurrentSong()) {
                onQueueIndexChanged();
            } else if (hasPreviousSong()) {
                skipToPreviousSong();
            } else {
                setQueueIndex(-1);
            }
        } else if (getQueueIndex() > index) {
            if (hasPreviousSong()) {
                mediaData.setQueueIndex(getQueueIndex() - 1);
                notifyQueueIndexChanged();
            }
        }
    }

    public void onSongSelected(@Nullable Bundle extras)
    {
        if (extras == null)
            return;

        boolean wasPlaying = isPlaying();

        setQueueIndex(extras.getInt(MediaSessionListener.EXTRA_QUEUE_INDEX, -1));

        if (wasPlaying && !isPlaying())
            play();
    }

    public void setEqualizerBand(Bundle extras)
    {
        if (extras == null)
            return;

        short bandPosition = extras.getShort(MediaSessionListener.EXTRA_EQUALIZER_BAND);
        short decibelLevel = extras.getShort(MediaSessionListener.EXTRA_DECIBEL_LEVEL);
        setEqualizerBand(bandPosition, decibelLevel);
    }

    @Override
    public void setEqualizerBand(short band, short level)
    {
        audioData.setEqualizerBand(band, level);

        if (audioData.equalizerBandChanged())
        {
            equalizer.setBandLevel(band, level);
            audioData.clearAllChanges();
        }
    }

    @Override
    public void enableEqualizer()
    {
        setupEqualizer();
    }

    @Override
    public void disableEqualizer()
    {
        if (equalizer != null)
        {
            equalizer.release();
            equalizer = null;
        }
    }

    public double getLeftVolume()
    {
        return audioData.getLeftVolume();
    }

    public double getRightVolume()
    {
        return audioData.getRightVolume();
    }

    @Override
    public void setLeftRightVolumeRatio(double ratio)
    {
        audioData.setLeftRightVolumeRatio(ratio);

        if (audioData.leftRightVolumeRatioChanged())
        {
            mediaPlayer.setVolume((float) getLeftVolume(), (float) getRightVolume());
            audioData.clearAllChanges();
        }
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
                    mediaPlayer.setVolume((float) getLeftVolume(), (float) getRightVolume());
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
                mediaPlayer.setVolume(
                        (float) (getLeftVolume() * FOCUS_DROP_FACTOR),
                        (float) (getRightVolume() * FOCUS_DROP_FACTOR)
                );
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
