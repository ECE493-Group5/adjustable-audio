package com.ece493.group5.adjustableaudio.models;

public class Song {

    private String title;
    private String artist;
    private String duration;

    public Song(String songTitle, String songArtist, String songDuration)
    {
        this.title = songTitle;
        this.artist = songArtist;
        this.duration = songDuration;
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
}
