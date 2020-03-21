package com.ece493.group5.adjustableaudio.ui.microphone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;

import java.util.Objects;

public class MicrophoneFragment extends Fragment
{
    private static final String TAG = MicrophoneFragment.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSIONS = 0;

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
        checkAndRequestPermissions();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        microphoneServiceInteractor.disconnect();
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

    private void checkAndRequestPermissions()
    {
        boolean hasPermissions = hasPermission(Manifest.permission.RECORD_AUDIO);

        if (!hasPermissions)
        {
            String[] permissionsToRequest = { Manifest.permission.RECORD_AUDIO };
            requestPermissions(permissionsToRequest, REQUEST_CODE_PERMISSIONS);
        }
        else
        {
            onPermissionGranted();
        }
    }

    private boolean hasPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_CODE_PERMISSIONS &&
                grantResults.length == 1 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            onPermissionGranted();
        }
        else
        {
            String[] permissionsToRequest = { Manifest.permission.RECORD_AUDIO };
            requestPermissions(permissionsToRequest, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void onPermissionGranted()
    {
        microphoneServiceInteractor.connect();
    }
}