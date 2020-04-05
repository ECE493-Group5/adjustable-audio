package com.ece493.group5.adjustableaudio.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AudioData
        extends ChangableData<AudioData.Type>
        implements Parcelable
{
    public static final String EXTRA = "AUDIO_DATA_EXTRA";

    private double leftRightVolumeRatio;
    private HashMap<Short, Short> equalizerSettings;

    public enum Type
    {
        LEFT_RIGHT_VOLUME_RATIO, EQUALIZER
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

        leftRightVolumeRatio = 1;
        equalizerSettings = new HashMap<>();
    }

    private AudioData(Parcel in)
    {
        super(Type.class);

        changed = (EnumSet<Type>) in.readSerializable();
        leftRightVolumeRatio = in.readDouble();
        equalizerSettings = (HashMap<Short, Short>) in.readSerializable();
    }

    public static int volumeRatioToPercent(double ratio)
    {
        if (ratio >= 100)
            return 0;

        if (ratio <= 0)
            return 100;

        return 100 - (int) ((ratio * 100)/(1 + ratio));
    }

    public static double percentToVolumeRatio(int percent)
    {
        double percentAsDouble = percent;

        if (percentAsDouble == 0) // precent divide by 0
            percentAsDouble = 0.0000001;

        if (percentAsDouble > 100.0)
            return 0.0;

        if (percentAsDouble < 0.0)
            return 100.0;

        return (100.0 / (double) percentAsDouble) - 1;
    }

    public double getLeftVolume()
    {
        if (leftRightVolumeRatio <= 1)
            return leftRightVolumeRatio;
        else
            return 1;
    }

    public double getRightVolume()
    {
        double rightLeftVolumeRatio = 1 / leftRightVolumeRatio;
        if (rightLeftVolumeRatio <= 1)
            return rightLeftVolumeRatio;
        else
            return 1;
    }

    public double getLeftRightVolumeRatio()
    {
        return leftRightVolumeRatio;
    }

    public short getEqualizerBand(short band)
    {
        return equalizerSettings.get(band);
    }

    public Set<Map.Entry<Short, Short>> getEqualizerSettings()
    {
        return equalizerSettings.entrySet();
    }

    public void setLeftRightVolumeRatio(double ratio)
    {
        if (leftRightVolumeRatio != ratio) {
            leftRightVolumeRatio = ratio;
            setChanged(Type.LEFT_RIGHT_VOLUME_RATIO, true);
        }
    }

    public void setEqualizerBand(short band, short level)
    {
        if (!equalizerSettings.containsKey(band) || getEqualizerBand(band) != level) {
            equalizerSettings.put(band, level);
            setChanged(Type.EQUALIZER, true);
        }
    }

    public boolean leftRightVolumeRatioChanged()
    {
        return getChanged(Type.LEFT_RIGHT_VOLUME_RATIO);
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
        dest.writeDouble(leftRightVolumeRatio);
        dest.writeSerializable(equalizerSettings);
    }
}
