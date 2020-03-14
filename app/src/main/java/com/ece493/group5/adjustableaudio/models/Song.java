package com.ece493.group5.adjustableaudio.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.ece493.group5.adjustableaudio.utils.TimeUtils;

public class Song implements Parcelable
{
    private String title;
    private String artist;
    private String album;
    private String filename;
    private String mediaId;
    private long duration;

    private static final String BUNDLE_SONG_TITLE = "BUNDLE_SONG_TITLE";
    private static final String BUNDLE_SONG_ARTIST = "BUNDLE_SONG_ARTIST";
    private static final String BUNDLE_SONG_ALBUM = "BUNDLE_SONG_ALBUM";
    private static final String BUNDLE_SONG_FILENAME = "BUNDLE_SONG_FILENAME";
    private static final String BUNDLE_SONG_MEDIA_ID = "BUNDLE_SONG_MEDIA_ID";
    private static final String BUNDLE_SONG_DURATION = "BUNDLE_SONG_DURATION";

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Song()
    {
    }

    public Song(Parcel in)
    {
        this.title = in.readString();
        this.album = in.readString();
        this.artist = in.readString();
        this.duration = in.readLong();
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
        return TimeUtils.durationAsString(getDuration());
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
        parcel.writeLong(this.duration);
        parcel.writeString(this.filename);
        parcel.writeString(this.mediaId);
    }

    public static Song fromBundle(Bundle bundle)
    {
        Song song = new Song();

        song.setTitle(bundle.getString(BUNDLE_SONG_TITLE));
        song.setArtist(bundle.getString(BUNDLE_SONG_ARTIST));
        song.setAlbum(bundle.getString(BUNDLE_SONG_ALBUM));
        song.setFilename(bundle.getString(BUNDLE_SONG_FILENAME));
        song.setMediaId(bundle.getString(BUNDLE_SONG_MEDIA_ID));
        song.setDuration(bundle.getLong(BUNDLE_SONG_DURATION));

        return song;
    }

    public Bundle toBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_SONG_TITLE, getTitle());
        bundle.putString(BUNDLE_SONG_ARTIST, getArtist());
        bundle.putString(BUNDLE_SONG_ALBUM, getAlbum());
        bundle.putString(BUNDLE_SONG_MEDIA_ID, getMediaId());
        bundle.putString(BUNDLE_SONG_FILENAME, getFilename());
        bundle.putLong(BUNDLE_SONG_DURATION, getDuration());

        return bundle;
    }
}
