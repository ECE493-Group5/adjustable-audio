package com.ece493.group5.adjustableaudio;

import android.os.Build;
import android.os.Bundle;

import com.ece493.group5.adjustableaudio.adapters.MediaPlayerAdapter;
import com.ece493.group5.adjustableaudio.listeners.MediaSessionListener;
import com.ece493.group5.adjustableaudio.models.Song;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.shadows.util.DataSource.toDataSource;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class MediaPlayerAdapterUnitTest
{
    private static final String CURRENT_ALBUM = "Current Album";
    private static final String CURRENT_ARTIST = "Current Artist";
    private static final String CURRENT_SONG = "Current Song";
    private static final String DUMMY_SOURCE = "dummy-source";
    private static final String ERROR_MSG = "The Equalizer object fails to initialize in this testing environment";

    private MediaPlayerAdapter mediaPlayerAdapter;
    private ShadowMediaPlayer.MediaInfo info;
    private DataSource defaultSource;
    private Song dummySong;


    @Before
    public void setup()
    {
        mediaPlayerAdapter = new MediaPlayerAdapter(ApplicationProvider.getApplicationContext());
        defaultSource = toDataSource(DUMMY_SOURCE);
        info = new ShadowMediaPlayer.MediaInfo();
        ShadowMediaPlayer.addMediaInfo(defaultSource, info);

        dummySong = new Song();
        dummySong.setTitle(CURRENT_SONG);
        dummySong.setArtist(CURRENT_ARTIST);
        dummySong.setAlbum(CURRENT_ALBUM);
        dummySong.setFilename(DUMMY_SOURCE);
    }

    @Test
    public void testPlayAndPause()
    {
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        assertTrue(mediaPlayerAdapter.hasCurrentSong());

        mediaPlayerAdapter.play();

        assertTrue(mediaPlayerAdapter.isPlaying());

        mediaPlayerAdapter.pause();
        assertFalse(mediaPlayerAdapter.isPlaying());
    }

    @Test
    public void testEnqueue()
    {
        assertEquals(0, mediaPlayerAdapter.getQueue().size());
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        assertEquals(1, mediaPlayerAdapter.getQueue().size());
    }

    @Test
    public void testDequeue()
    {
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        assertEquals(1, mediaPlayerAdapter.getQueue().size());

        Bundle dummyBundle = new Bundle();
        dummyBundle.putInt(MediaSessionListener.EXTRA_QUEUE_INDEX, 0);
        mediaPlayerAdapter.dequeue(dummyBundle);

        assertEquals(0, mediaPlayerAdapter.getQueue().size());
    }

    @Test
    public void testSelectSong()
    {
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        mediaPlayerAdapter.enqueue(dummySong.toBundle());

        assertEquals(0, mediaPlayerAdapter.getQueueIndex());
        Bundle dummyBundle = new Bundle();
        dummyBundle.putInt(MediaSessionListener.EXTRA_QUEUE_INDEX, 1);

        mediaPlayerAdapter.onSongSelected(dummyBundle);

        assertEquals(1, mediaPlayerAdapter.getQueueIndex());
    }

    @Test
    public void testSkipNextSong()
    {
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        assertEquals(2, mediaPlayerAdapter.getQueue().size());
        assertEquals(0, mediaPlayerAdapter.getQueueIndex());

        mediaPlayerAdapter.play();
        assertTrue(mediaPlayerAdapter.hasNextSong());
        mediaPlayerAdapter.skipToNextSong();
        mediaPlayerAdapter.pause();

        assertEquals(1, mediaPlayerAdapter.getQueueIndex());
    }

    @Test
    public void testSkipPrevSong()
    {
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        mediaPlayerAdapter.enqueue(dummySong.toBundle());
        assertEquals(2, mediaPlayerAdapter.getQueue().size());
        assertEquals(0, mediaPlayerAdapter.getQueueIndex());

        Bundle dummyBundle = new Bundle();
        dummyBundle.putInt(MediaSessionListener.EXTRA_QUEUE_INDEX, 1);

        mediaPlayerAdapter.onSongSelected(dummyBundle);
        assertEquals(1, mediaPlayerAdapter.getQueueIndex());
        assertTrue(mediaPlayerAdapter.hasPreviousSong());

        mediaPlayerAdapter.play();
        mediaPlayerAdapter.skipToPreviousSong();
        mediaPlayerAdapter.pause();

        assertEquals(0, mediaPlayerAdapter.getQueueIndex());
    }

    @Test
    public void testSetLeftRightRatio()
    {
        mediaPlayerAdapter.setLeftRightVolumeRatio(0.75);

        assertEquals(0.75, mediaPlayerAdapter.getLeftVolume(), 0);
        assertEquals(1, mediaPlayerAdapter.getRightVolume(), 0);
    }

    @Test
    public void testSetEqualizerBand()
    {
        Bundle dummyBundle = new Bundle();
        dummyBundle.putShort(MediaSessionListener.EXTRA_EQUALIZER_BAND, Integer.valueOf(0).shortValue());
        dummyBundle.putShort(MediaSessionListener.EXTRA_DECIBEL_LEVEL, Integer.valueOf(1200).shortValue());

        try
        {
            mediaPlayerAdapter.setEqualizerBand(dummyBundle);
        }catch(NullPointerException e)
        {
            System.out.println(ERROR_MSG);
        }
    }
}
