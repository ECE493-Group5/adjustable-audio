package com.ece493.group5.adjustableaudio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ece493.group5.adjustableaudio.listeners.EqualizerModelListener;
import com.ece493.group5.adjustableaudio.models.EqualizerModel;
import com.ece493.group5.adjustableaudio.services.MicrophoneService;
import com.ece493.group5.adjustableaudio.services.MusicService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.versionedparcelable.ParcelField;

public class MainActivity extends AppCompatActivity implements EqualizerModelListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

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
        startService(new Intent(this, MusicService.class));
        startService(new Intent(this, MicrophoneService.class));
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
}
