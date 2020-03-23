package com.ece493.group5.adjustableaudio.ui.microphone;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;

import java.util.Objects;

public class MicrophoneFragment extends Fragment
{
    private static final String TAG = MicrophoneFragment.class.getSimpleName();

    private MicrophoneViewModel microphoneViewModel;
    private MicrophoneServiceInteractor microphoneServiceInteractor;

    private ToggleButton microphoneToggleButton;
    private ToggleButton noiseFilterToggleButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        microphoneViewModel =
                ViewModelProviders.of(this).get(MicrophoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);

        microphoneToggleButton = root.findViewById(R.id.microphoneEnableButton);
        noiseFilterToggleButton = root.findViewById(R.id.noiseFilterToggleButton);

        microphoneServiceInteractor = new MicrophoneServiceInteractor(getContext()) {
            @Override
            public void onConnectionEstablished()
            {
                enableControls();
            }

            @Override
            public void onConnectionLost()
            {
                disableControls();
            }

            @Override
            public void onIsRecordingChanged(boolean isRecording)
            {
                microphoneToggleButton.setChecked(isRecording);
            }

            @Override
            public void onIsNoiseFilterEnabledChanged(boolean isEnabled) {
                noiseFilterToggleButton.setChecked(isEnabled);
            }
        };

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        boolean checkPermissions = checkRecordAudioPermissions();
        if (checkPermissions)
        {
            microphoneServiceInteractor.connect();
        }
        else
        {
           disableControls();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        microphoneServiceInteractor.disconnect();
    }

    private boolean checkRecordAudioPermissions()
    {
        boolean hasRecordPermission =
                ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        if (!hasRecordPermission)
        {
            showRecordPermissionsDialog();
            return false;
        }

        return true;
    }

    private void showRecordPermissionsDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.title_permissions_dialog);
        alertDialogBuilder.setMessage(R.string.dialog_msg_perms_record);

        alertDialogBuilder.setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    private void enableControls()
    {
        microphoneToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphoneToggleButton.setChecked(!microphoneToggleButton.isChecked());
                microphoneServiceInteractor.toggleRecording();
            }
        });

        noiseFilterToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noiseFilterToggleButton.setChecked(!noiseFilterToggleButton.isChecked());
                microphoneServiceInteractor.toggleNoiseFilter();
            }
        });
    }

    private void disableControls()
    {
        microphoneToggleButton.setOnClickListener(null);
        noiseFilterToggleButton.setOnClickListener(null);
    }
}