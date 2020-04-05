package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.MediaData;
import com.ece493.group5.adjustableaudio.models.Song;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class MediaDataUnitTest
{

    private int testState;
    private List<Song> testQueue;
    private int testQueueIndex;
    private long testTotalDuration;
    private long testElapsedDuration;
    private Song song1;
    private Song song2;
    private Song song3;

    @Before
    public void mediaDataTestSetup()
    {
        testState = 1;
        testQueue = new ArrayList<Song>();
        Song song1 = mock(Song.class);
        Song song2 = mock(Song.class);
        Song song3 = mock(Song.class);
        testQueue.add(song1);
        testQueue.add(song2);
        testQueue.add(song3);
        testQueueIndex = 0;
        testTotalDuration = 1;
        testElapsedDuration = 1;
    }

    @Test
    public void stateTest()
    {
        MediaData mediaData = new MediaData();

        assertNotEquals(testState, mediaData.getState());

        mediaData.setState(testState);

        assertEquals(testState, mediaData.getState());
    }

    @Test
    public void queueTest()
    {
        MediaData mediaData = new MediaData();

        assertTrue(mediaData.getQueue().isEmpty());

        mediaData.setQueue(testQueue);

        assertEquals(testQueue, mediaData.getQueue());
    }

    @Test
    public void queueIndexTest()
    {
        MediaData mediaData = new MediaData();

        assertNotEquals(testQueueIndex, mediaData.getQueueIndex());

        mediaData.setQueueIndex(testQueueIndex);

        assertEquals(testQueueIndex, mediaData.getQueueIndex());
    }

    @Test
    public void totalDurationTest()
    {
        MediaData mediaData = new MediaData();

        assertNotEquals(testTotalDuration, mediaData.getTotalDuration());

        mediaData.setTotalDuration(testTotalDuration);

        assertEquals(testTotalDuration, mediaData.getTotalDuration());
    }

    @Test
    public void elapsedDurationTest()
    {
        MediaData mediaData = new MediaData();

        assertNotEquals(testElapsedDuration, mediaData.getElapsedDuration());

        mediaData.setElapsedDuration(testElapsedDuration);

        assertEquals(testElapsedDuration, mediaData.getElapsedDuration());
    }

    @Test
    public void stateChangesTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.stateChanged());

        mediaData.setState(testState);

        assertTrue(mediaData.stateChanged());
    }

    @Test
    public void queueChangedTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.queueChanged());

        mediaData.setQueue(testQueue);

        assertTrue(mediaData.queueChanged());
    }

    @Test
    public void queueIndexChangedTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.queueIndexChanged());

        mediaData.setQueueIndex(testQueueIndex);

        assertTrue(mediaData.queueIndexChanged());
    }

    @Test
    public void totalDurationChangedTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.durationChanged());

        mediaData.setTotalDuration(testTotalDuration);

        assertTrue(mediaData.durationChanged());
    }

    @Test
    public void elapsedDurationChangedTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.durationChanged());

        mediaData.setElapsedDuration(testElapsedDuration);

        assertTrue(mediaData.durationChanged());
    }

    @Test
    public void hasCurrentSongTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.hasCurrentSong());

        mediaData.setQueue(testQueue);

        mediaData.setQueueIndex(0);

        assertTrue(mediaData.hasCurrentSong());
    }

    @Test public void hasNextSongTest()
    {
        MediaData mediaData = new MediaData();

        assertFalse(mediaData.hasNextSong());

        mediaData.setQueue(testQueue);

        mediaData.setQueueIndex(0);

        assertTrue(mediaData.hasNextSong());
    }

    @Test
    public void getCurrentSongTest()
    {
        MediaData mediaData = new MediaData();

        assertNull(mediaData.getCurrentSong());

        mediaData.setQueue(testQueue);

        mediaData.setQueueIndex(0);

        assertNotNull(mediaData.getCurrentSong());
    }

    @Test
    public void hasPreviousSongTest()
    {
        MediaData mediaData = new MediaData();

        mediaData.setQueue(testQueue);

        mediaData.setQueueIndex(0);

        assertFalse(mediaData.hasPreviousSong());

        mediaData.setQueueIndex(1);

        assertTrue(mediaData.hasPreviousSong());
    }

    @Test
    public void skipToNextSongTest()
    {
        MediaData mediaData = new MediaData();

        mediaData.setQueue(testQueue);

        int index = mediaData.getQueueIndex();

        mediaData.skipToNextSong();

        assertNotEquals(index, mediaData.getQueueIndex());

        assertEquals(index + 1, mediaData.getQueueIndex());
    }

    @Test
    public void skipToPreviousSongTest()
    {
        MediaData mediaData = new MediaData();

        mediaData.setQueue(testQueue);

        int index = mediaData.getQueueIndex();

        mediaData.skipToPreviousSong();

        assertNotEquals(index, mediaData.getQueueIndex());

        assertEquals(index - 1, mediaData.getQueueIndex());
    }

    @Test
    public void setAllChangesTest()
    {
        MediaData mediaData = new MediaData();

        mediaData.setAllChanges();

        assertTrue(mediaData.stateChanged());

        assertTrue(mediaData.queueChanged());

        assertTrue(mediaData.queueIndexChanged());

        assertTrue(mediaData.durationChanged());
    }

    @Test
    public void clearAllChangesTest()
    {
        MediaData mediaData = new MediaData();

        mediaData.setAllChanges();

        mediaData.clearAllChanges();

        assertFalse(mediaData.stateChanged());

        assertFalse(mediaData.queueChanged());

        assertFalse(mediaData.queueIndexChanged());

        assertFalse(mediaData.durationChanged());
    }
}
