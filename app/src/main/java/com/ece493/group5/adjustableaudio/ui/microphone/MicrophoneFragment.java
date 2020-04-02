package com.ece493.group5.adjustableaudio.ui.microphone;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;
import com.ece493.group5.adjustableaudio.models.MicrophoneData;

import java.util.Objects;

public class MicrophoneFragment extends Fragment
{
    private static final String TAG = MicrophoneFragment.class.getSimpleName();

    private MicrophoneViewModel microphoneViewModel;
    private MicrophoneServiceInteractor microphoneServiceInteractor;

    private ToggleButton microphoneToggleButton;
    private ToggleButton noiseFilterToggleButton;
    private ToggleButton normalToggleButton;
    private ToggleButton speechFocusToggleButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        microphoneViewModel =
                ViewModelProviders.of(this).get(MicrophoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);

        microphoneToggleButton = root.findViewById(R.id.microphoneEnableButton);
        noiseFilterToggleButton = root.findViewById(R.id.noiseFilterToggleButton);
        normalToggleButton = root.findViewById(R.id.normalToggleButton);
        speechFocusToggleButton = root.findViewById(R.id.speechFocusToggleButton);

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
            public void onModeChanged(int mode) {
                Log.d(TAG, "mode: " + mode);

                speechFocusToggleButton.setChecked(false);
                normalToggleButton.setChecked(false);
                noiseFilterToggleButton.setChecked(false);

                switch(mode)
                {
                    case MicrophoneData.MODE_NORMAL:
                        normalToggleButton.setChecked(true);
                        break;
                    case MicrophoneData.MODE_NOISE_SUPPRESSION:
                        noiseFilterToggleButton.setChecked(true);
                        break;
                    case MicrophoneData.MODE_SPEECH_FOCUS:
                        speechFocusToggleButton.setChecked(true);
                        break;
                }
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

    private boolean isNoiseFilterSupported()
    {
        return NoiseSuppressor.isAvailable() ||
                AcousticEchoCanceler.isAvailable() ||
                AutomaticGainControl.isAvailable();
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
                if (isNoiseFilterSupported())
                {
                    microphoneServiceInteractor.setMode(MicrophoneData.MODE_NOISE_SUPPRESSION);
                }
                else
                {
                    CharSequence text = "This device does support noise filtering.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                }
            }
        });

        normalToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalToggleButton.setChecked(!normalToggleButton.isChecked());
                microphoneServiceInteractor.setMode(MicrophoneData.MODE_NORMAL);
            }
        });

        speechFocusToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechFocusToggleButton.setChecked(!speechFocusToggleButton.isChecked());
                microphoneServiceInteractor.setMode(MicrophoneData.MODE_SPEECH_FOCUS);
            }
        });
    }

    private void disableControls()
    {
        microphoneToggleButton.setOnClickListener(null);
        noiseFilterToggleButton.setOnClickListener(null);
        normalToggleButton.setOnClickListener(null);
        speechFocusToggleButton.setOnClickListener(null);
    }
}