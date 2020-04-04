package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.Song;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class SongUnitTest
{

    public static final String TEST_TITLE = "test title";
    public static final String TEST_ARTIST = "test artist";
    public static final String TEST_ALBUM = "test album";
    public static final String TEST_FILENAME = "test filename";
    public static final String TEST_MEDIA_ID = "testMediaId";
    public static final int TEST_DURATION = 300;
    public static final String TEST_DURATION_STRING = "5:00";
    private String testTitle;
    private String testArtist;
    private String testAlbum;
    private String testFilename;
    private String testMediaId;
    private String testDurationString;
    private long testDuration;

    @Before
    public void songTestSetup()
    {
        testTitle = TEST_TITLE;
        testArtist = TEST_ARTIST;
        testAlbum = TEST_ALBUM;
        testFilename = TEST_FILENAME;
        testMediaId = TEST_MEDIA_ID;
        testDuration = TEST_DURATION;
        testDurationString = TEST_DURATION_STRING;
    }

    @Test
    public void titleTest()
    {
        Song song = new Song();

        assertNull(song.getTitle());

        song.setTitle(testTitle);

        assertNotNull(song.getTitle());

        assertEquals(testTitle, song.getTitle());
    }

    @Test
    public void artistTest()
    {
        Song song = new Song();

        assertNull(song.getArtist());

        song.setArtist(testArtist);

        assertNotNull(song.getArtist());

        assertEquals(testArtist, song.getArtist());
    }

    @Test
    public void albumTest()
    {
        Song song = new Song();

        assertNull(song.getAlbum());

        song.setAlbum(testAlbum);

        assertNotNull(song.getAlbum());

        assertEquals(testAlbum, song.getAlbum());
    }

    @Test
    public void filenameTest()
    {
        Song song = new Song();

        assertNull(song.getFilename());

        song.setFilename(testFilename);

        assertNotNull(song.getFilename());

        assertEquals(testFilename, song.getFilename());
    }

    @Test
    public void mediaIdTest()
    {
        Song song = new Song();

        assertNull(song.getMediaId());

        song.setMediaId(testMediaId);

        assertNotNull(song.getMediaId());

        assertEquals(testMediaId, song.getMediaId());
    }

    @Test
    public void durationTest()
    {
        Song song = new Song();

        assertNotEquals(testDuration, song.getDuration());

        song.setDuration(testDuration);

        assertEquals(testDuration, song.getDuration());
    }

//    @Test
//    public void durationAsStringTest()
//    {
//        Song song = new Song();
//
//        assertNotEquals(testDuration, song.getDuration());
//
//        song.setDuration(testDuration);
//
//        assertEquals(testDuration, song.getDuration());
//
//        assertEquals(testDurationString, song.getDurationAsString());
//    }
}
