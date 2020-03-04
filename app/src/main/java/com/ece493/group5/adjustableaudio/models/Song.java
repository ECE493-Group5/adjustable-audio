package com.ece493.group5.adjustableaudio.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

public class Song implements Parcelable
{
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    private String title;
    private String artist;
    private String album;
    private String filename;
    private String mediaId;
    private long duration;

    private static final long MILISECOND_PER_SECOND = 1000;

    public Song()
    {

    }

    public Song(String songTitle, String songArtist, String songAlbum, String filenamePath, String mediaID)
    {
        this.title = songTitle;
        this.artist = songArtist;
        this.album = songAlbum;
        this.filename = filenamePath;
        this.mediaId  = mediaID;
//        this.duration = songDuration;
    }

    public Song(Parcel in){
        this.title = in.readString();
        this.artist = in.readString();
        this.album =  in.readString();
        this.filename = in.readString();
        this.mediaId = in.readString();
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


    public long getDuration()
    {
        return this.duration;
    }

    public String getDurationAsString()
    {
        return DateUtils.formatElapsedTime(duration / MILISECOND_PER_SECOND);
    }

    public void setDuration(long newDuration)
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

    public String getMediaId()
    {
        return this.mediaId;
    }

    public void setMediaId(String mediaId)
    {
        this.mediaId = mediaId;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(this.title);
        parcel.writeString(this.album);
        parcel.writeString(this.artist);
        parcel.writeString(this.filename);
        parcel.writeString(this.mediaId);
    }
}
