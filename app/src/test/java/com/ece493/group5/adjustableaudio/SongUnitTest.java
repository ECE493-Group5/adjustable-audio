package com.ece493.group5.adjustableaudio;

import com.ece493.group5.adjustableaudio.models.Song;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SongUnitTest
{

    private String testTitle;
    private String testArtist;
    private String testAlbum;
    private String testFilename;
    private String testMediaId;
    private long testDuration;

    @Before
    public void songTestSetup()
    {
        testTitle = "test title";
        testArtist = "test artist";
        testAlbum = "test album";
        testFilename = "test filename";
        testMediaId = "testMediaId";
        testDuration = 300;
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
}
