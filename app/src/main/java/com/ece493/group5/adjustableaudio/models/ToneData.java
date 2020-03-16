package com.ece493.group5.adjustableaudio.models;

public class ToneData
{
    private double frequency;
    private double dBHL;
    private double lHeardAtDB;
    private double rHeardAtDB;

    public ToneData(double frequency, double dbHL)
    {
        this.frequency = frequency;
        this.dBHL = dBHL;
    }

    public double getdBHL()
    {
        return dBHL;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public Double getLHeardAtDB()
    {
        return lHeardAtDB;
    }

    public void setLHeardAtDB(Double dBSPLValue)
    {
        this.lHeardAtDB = dBSPLValue;
    }

    public Double getRHeardAtDB()
    {
        return rHeardAtDB;
    }

    public void setRHeardAtDB(Double dBSPLValue)
    {
        this.rHeardAtDB = dBSPLValue;
    }


    public void setdBHL(double dBHL)
    {
        this.dBHL = dBHL;
    }

    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }


}
