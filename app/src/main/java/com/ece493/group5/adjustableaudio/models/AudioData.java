package com.ece493.group5.adjustableaudio.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.EnumSet;
import java.util.HashMap;

public class AudioData
        extends ChangableData<AudioData.Type>
        implements Parcelable
{
    public static final String EXTRA = "AUDIO_DATA_EXTRA";

    private double leftVolume;
    private double rightVolume;
    private HashMap<Short, Short> equalizerSettings;

    public enum Type
    {
        LEFT_VOLUME, RIGHT_VOLUME, EQUALIZER
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public AudioData createFromParcel(Parcel in)
        {
            return new AudioData(in);
        }

        public AudioData[] newArray(int size)
        {
            return new AudioData[size];
        }
    };

    public AudioData()
    {
        super(Type.class);

        leftVolume = 0;
        rightVolume = 0;
        equalizerSettings = new HashMap<>();
    }

    protected AudioData(Parcel in)
    {
        super(Type.class);

        changed = (EnumSet<Type>) in.readSerializable();
        leftVolume = in.readDouble();
        rightVolume = in.readDouble();
        equalizerSettings = (HashMap<Short, Short>) in.readSerializable();
    }

    public double getLeftVolume()
    {
        return leftVolume;
    }

    public double getRightVolume()
    {
        return rightVolume;
    }

    public short getEqualizerBand(short band)
    {
        return equalizerSettings.get(band);
    }

    public void setLeftVolume(double percent)
    {
        if (leftVolume != percent) {
            leftVolume = percent;
            setChanged(Type.LEFT_VOLUME, true);
        }
    }

    public void setRightVolume(double percent)
    {
        if (rightVolume != percent) {
            rightVolume = percent;
            setChanged(Type.RIGHT_VOLUME, true);
        }
    }

    public void setEqualizerBand(short band, short level)
    {
        if (!equalizerSettings.containsKey(band) || getEqualizerBand(band) != level) {
            equalizerSettings.put(band, level);
            setChanged(Type.EQUALIZER, true);
        }
    }

    public boolean leftVolumeChanged()
    {
        return getChanged(Type.LEFT_VOLUME);
    }

    public boolean rightVolumeChanged()
    {
        return getChanged(Type.RIGHT_VOLUME);
    }

    public boolean equalizerBandChanged()
    {
        return getChanged(Type.EQUALIZER);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeSerializable(changed);
        dest.writeDouble(leftVolume);
        dest.writeDouble(rightVolume);
        dest.writeSerializable(equalizerSettings);
    }
}
