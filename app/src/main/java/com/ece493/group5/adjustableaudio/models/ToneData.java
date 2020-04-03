package com.ece493.group5.adjustableaudio.models;

public class ToneData
{
    private static final double DELTA = 0.0001;

    private int frequency;
    private double dBHL;
    private double lHeardAtDB;
    private double rHeardAtDB;

    public ToneData(int frequency, double dBHLVal)
    {
        this.frequency = frequency;
        this.dBHL = dBHLVal;
        this.lHeardAtDB = 0.0;
        this.rHeardAtDB = 0.0;
    }

    public double getdBHL()
    {
        return dBHL;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public Double getLHeardAtDB()
    {
        return lHeardAtDB;
    }

    public void setLHeardAtDB(double dBSPLValue)
    {
        this.lHeardAtDB = dBSPLValue;
    }

    public Double getRHeardAtDB()
    {
        return rHeardAtDB;
    }

    public void setRHeardAtDB(double dBSPLValue)
    {
        this.rHeardAtDB = dBSPLValue;
    }

    public void setdBHL(double dBHL)
    {
        this.dBHL = dBHL;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }

        if (this.getClass() != other.getClass())
        {
            return false;
        }

        if (this.frequency != ((ToneData) other).frequency)
        {
            return false;
        }

        if (this.dBHL - ((ToneData) other).dBHL > DELTA)
        {
            return false;
        }

        if (this.lHeardAtDB - ((ToneData) other).lHeardAtDB > DELTA)
        {
            return false;
        }

        if (this.rHeardAtDB - ((ToneData) other).rHeardAtDB > DELTA)
        {
            return false;
        }

        return true;
    }
}
