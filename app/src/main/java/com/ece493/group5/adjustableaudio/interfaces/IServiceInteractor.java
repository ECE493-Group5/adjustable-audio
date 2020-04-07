package com.ece493.group5.adjustableaudio.interfaces;

/**
 * The IServiceInteractor helps implement the following requirements:
 *
 * #SRS: Media Player
 * #SRS: Makeshift Hearing Aid
 */

public interface IServiceInteractor
{
    void connect();
    void disconnect();
    void onConnectionEstablished();
    void onConnectionLost();
}
