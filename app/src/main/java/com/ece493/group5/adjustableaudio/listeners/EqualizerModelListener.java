package com.ece493.group5.adjustableaudio.listeners;

import com.ece493.group5.adjustableaudio.models.EqualizerModel;

/**
 * The EqualizerModelListener class helps to implement the following requirements:
 *
 * #SRS: Controlling Volumes Separately for Each Ear
 * #SRS: Manually Controlling the Volumes through an Equalizer
 *
 * In particular, the EqualizerModelListener is used to retrieve the EqualizerModel object.
 */

public interface EqualizerModelListener
{
    EqualizerModel getEqualizerModel();

    void reloadPresets();
}
