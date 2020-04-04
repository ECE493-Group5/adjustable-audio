package com.ece493.group5.adjustableaudio;

import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;

import com.ece493.group5.adjustableaudio.controllers.MusicServiceInteractor;
import com.ece493.group5.adjustableaudio.models.Song;
import com.ece493.group5.adjustableaudio.services.MusicService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

@RunWith(RobolectricTestRunner.class)
public class MusicServiceInteractorTest
{
    private MusicServiceInteractor musicServiceInteractor;
    private MusicService musicService;
    private Song dummySong;

    @Before
    public void setup()
    {
        Robolectric.buildService(MusicService.class);
        musicServiceInteractor = new MusicServiceInteractor(ApplicationProvider.getApplicationContext());

        musicServiceInteractor.connect();

        dummySong = new Song();
        dummySong.setTitle("Current Song");
        dummySong.setArtist("Current Artist");
        dummySong.setAlbum("CurrentAlbum");
        dummySong.setFilename("fsm_team_escp_infinite.mp3");
    }

    @Test
    public void testEnqueue()
    {
        musicServiceInteractor.enqueue(dummySong);
    }
}
