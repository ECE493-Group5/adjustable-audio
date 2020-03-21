package com.ece493.group5.adjustableaudio.interfaces;

public interface IServiceInteractor
{
    void connect();
    void disconnect();
    void onConnectionEstablished();
    void onConnectionLost();
}
