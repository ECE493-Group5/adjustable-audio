package com.ece493.group5.adjustableaudio.models;

public class ToneData
{
    private int frequency;
    private double dBHL;
    private double lHeardAtDB;
    private double rHeardAtDB;

    public ToneData(int frequency, double dBHLVal)
    {
        this.frequency = frequency;
        this.dBHL = dBHLVal;
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
}
