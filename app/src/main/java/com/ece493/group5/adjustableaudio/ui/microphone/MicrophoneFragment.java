package com.ece493.group5.adjustableaudio.ui.microphone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;

public class MicrophoneFragment extends Fragment {
    private MicrophoneViewModel microphoneViewModel;
    private ToggleButton microphoneEnableButton;
    private MicrophoneServiceInteractor microphoneServiceInteractor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        microphoneViewModel =
                ViewModelProviders.of(this).get(MicrophoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);

        microphoneEnableButton = root.findViewById(R.id.microphoneEnableButton);

        microphoneServiceInteractor = new MicrophoneServiceInteractor(getContext());

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        microphoneServiceInteractor.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        microphoneServiceInteractor.disconnect();
    }
}