package com.ece493.group5.adjustableaudio.models;

public class Song {

    private String title;
    private String artist;
    private String album;
    private String filename;
    private String duration;

    public Song(String songTitle, String songArtist, String songAlbum, String filenamePath)
    {
        this.title = songTitle;
        this.artist = songArtist;
        this.album = songAlbum;
        this.filename = filenamePath;
//        this.duration = songDuration;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setTitle(String newTitle)
    {
        this.title = newTitle;
    }


    public String getArtist()
    {
        return this.artist;
    }


    public void setArtist(String newArtist)
    {
        this.artist = newArtist;
    }


    public String getDuration()
    {
        return this.duration;
    }


    public void setDuration(String newDuration)
    {
        this.duration = newDuration;
    }


    public String getAlbum()
    {
        return this.album;
    }


    public void setAlbum(String newAlbum)
    {
        this.album = newAlbum;
    }


    public String getFilename()
    {
        return this.filename;
    }


    public void setFilename(String newFilename)
    {
        this.filename = newFilename;
    }
}
