package com.ece493.group5.adjustableaudio.ui.microphone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;
import com.ece493.group5.adjustableaudio.microphone.MicrophonePlayer;

import java.util.Objects;

public class MicrophoneFragment extends Fragment
{
    private static final String TAG = MicrophoneFragment.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSIONS = 0;

    private MicrophoneViewModel microphoneViewModel;
    private ToggleButton microphoneToggleButton;
    private MicrophoneServiceInteractor microphoneServiceInteractor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        microphoneViewModel =
                ViewModelProviders.of(this).get(MicrophoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);

        microphoneToggleButton = root.findViewById(R.id.microphoneEnableButton);

        microphoneServiceInteractor = new MicrophoneServiceInteractor(getContext()) {
            @Override
            public void onConnectionEstablished()
            {
                Log.d("MicrophoneFragment", "controls enabled");
                enableControls();
            }

            @Override
            public void onConnectionLost()
            {
                Log.d("MicrophoneFragment", "controls disabled");
                disableControls();
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
        microphoneToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    microphoneServiceInteractor.startRecording();
                else
                    microphoneServiceInteractor.stopRecording();
            }
        });
    }

    private void disableControls()
    {
        microphoneToggleButton.setOnCheckedChangeListener(null);
    }

    private void checkAndRequestPermissions()
    {
        boolean hasPermissions = hasPermission(Manifest.permission.RECORD_AUDIO);
        Log.d(TAG, "Has Permissions: " + hasPermissions);
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