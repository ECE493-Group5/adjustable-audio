package com.ece493.group5.adjustableaudio;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ece493.group5.adjustableaudio.listeners.EqualizerModelListener;
import com.ece493.group5.adjustableaudio.models.EqualizerModel;
import com.ece493.group5.adjustableaudio.services.MicrophoneService;
import com.ece493.group5.adjustableaudio.services.MusicService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements EqualizerModelListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private EqualizerModel equalizerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate Main Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_hearing_test, R.id.navigation_microphone, R.id.navigation_media_player, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        equalizerModel = new EqualizerModel(getApplicationContext());

        checkAndRequestPermissions();
    }

    @Override
    public void setEqualizerModel(EqualizerModel newEqualizerModel)
    {
        equalizerModel = newEqualizerModel;
    }

    @Override
    public EqualizerModel getEqualizerModel()
    {
        return equalizerModel;
    }

    private void checkAndRequestPermissions()
    {

        List<String> permissionsNeeded = new ArrayList<>();

        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!hasPermission(Manifest.permission.RECORD_AUDIO))
        {
            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (!permissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    REQUEST_CODE_PERMISSIONS);
        }
        else
        {
            onPermissionGranted();
        }
    }

    private boolean hasPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(getApplicationContext()), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        HashMap<String, Integer> perms = new HashMap<>();
        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);

        if (grantResults.length > 0)
        {
            for (int index = 0; index < permissions.length; index++)
            {
                perms.put(permissions[index], grantResults[index]);
            }

            if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            {
                onPermissionGranted();
            }
            else
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
                {
                    showPermissionsDialog();
                }
                else
                {
                    Toast.makeText(this, R.string.toast_msg_permissions, Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    private void onPermissionGranted()
    {
        startService(new Intent(this, MusicService.class));
        startService(new Intent(this, MicrophoneService.class));
    }

    private void showPermissionsDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(R.string.title_permissions_dialog);
        alertDialogBuilder.setMessage(R.string.dialog_msg_perms_main);

        alertDialogBuilder.setPositiveButton(R.string.positive_button_dialog, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                checkAndRequestPermissions();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.negative_button_dialog, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.show();
    }
}
