package com.ece493.group5.adjustableaudio.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MediaData implements Parcelable
{
    public static final String EXTRA = "MEDIA_DATA_EXTRA";

    private EnumSet<Type> changed;
    private int state;
    private List<Song> queue;
    private int queueIndex;
    private long totalDuration;
    private long elapsedDuration;

    public enum Type {
        STATE, QUEUE, QUEUE_INDEX, TOTAL_DURATION, ELAPSED_DURATION
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public MediaData createFromParcel(Parcel in) {
            return new MediaData(in);
        }
        public MediaData[] newArray(int size) {
            return new MediaData[size];
        }
    };


    public MediaData()
    {
        changed = EnumSet.noneOf(Type.class);
        queueIndex = -1;
        queue = new ArrayList<>();
    }

    public MediaData(Parcel in)
    {
        changed = (EnumSet<Type>) in.readSerializable();
        state = in.readInt();
        queue = new ArrayList<>();
        in.readList(queue, Song.class.getClassLoader());
        queueIndex = in.readInt();
        totalDuration = in.readLong();
        elapsedDuration = in.readLong();
    }

    public static MediaData extract(Bundle extras)
    {
        extras.setClassLoader(MediaData.class.getClassLoader());
        MediaData mediaData = extras.getParcelable(MediaData.EXTRA);
        return mediaData;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeSerializable(changed);
        parcel.writeInt(state);
        parcel.writeList(queue);
        parcel.writeInt(queueIndex);
        parcel.writeLong(totalDuration);
        parcel.writeLong(elapsedDuration);
    }

    public void setState(int state)
    {
        if (this.state != state) {
            this.state = state;
            setChanged(Type.STATE, true);
        }
    }

    public void setQueue(List<Song> queue)
    {
        if (!this.queue.equals(queue)) {
            this.queue = queue;
            setChanged(Type.QUEUE, true);
        }
    }

    public void setQueueIndex(int queueIndex)
    {
        if (this.queueIndex != queueIndex) {
            this.queueIndex = queueIndex;
            setChanged(Type.QUEUE_INDEX, true);
        }
    }

    public void setTotalDuration(long totalDuration)
    {
        if (this.totalDuration != totalDuration) {
            this.totalDuration = totalDuration;
            setChanged(Type.TOTAL_DURATION, true);
        }
    }

    public void setElapsedDuration(long elapsedDuration)
    {
        if (this.elapsedDuration != elapsedDuration) {
            this.elapsedDuration = elapsedDuration;
            setChanged(Type.ELAPSED_DURATION, true);
        }
    }

    public int getState()
    {
        return state;
    }

    public int getQueueIndex()
    {
        return queueIndex;
    }

    public List<Song> getQueue()
    {
        return queue;
    }

    public long getTotalDuration()
    {
        return totalDuration;
    }

    public long getElapsedDuration()
    {
        return elapsedDuration;
    }

    public void setAllChanges()
    {
        changed = EnumSet.allOf(Type.class);
    }

    public void clearAllChanges()
    {
        changed = EnumSet.noneOf(Type.class);
    }

    public void setChanged(Type type, boolean value)
    {
        if (value) {
            changed.add(type);
        } else {
            changed.remove(type);
        }
    }

    public boolean getChanged(Type type)
    {
        return changed.contains(type);
    }

    public boolean stateChanged()
    {
        return getChanged(Type.STATE);
    }

    public boolean queueChanged()
    {
        return getChanged(Type.QUEUE);
    }

    public boolean queueIndexChanged()
    {
        return getChanged(Type.QUEUE_INDEX);
    }

    public boolean durationChanged()
    {
        return getChanged(Type.TOTAL_DURATION) || getChanged(Type.ELAPSED_DURATION);
    }

    public Song getSong(Integer index)
    {
        if (!isValidQueueIndex(index))
            return null;

        return getQueue().get(index);
    }

    public Song getCurrentSong()
    {
        return getSong(getQueueIndex());
    }

    public boolean hasCurrentSong()
    {
        return getCurrentSong() != null;
    }

    public boolean isValidQueueIndex(Integer index)
    {
        return !(index == null || index < 0 || index >= getQueue().size());
    }

    public boolean hasNextSong()
    {
        return isValidQueueIndex(getQueueIndex() + 1);
    }

    public boolean hasPreviousSong()
    {
        return isValidQueueIndex(getQueueIndex() - 1);
    }

    public void skipToNextSong()
    {
        setQueueIndex(getQueueIndex() + 1);
    }

    public void skipToPreviousSong()
    {
        setQueueIndex(getQueueIndex() - 1);
    }
}
