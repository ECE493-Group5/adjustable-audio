package com.ece493.group5.adjustableaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity
{
    private static final String SHARED_PREFS_FILE = "DATATYPE_STORAGE";
    private static final String DISCLAIMER_KEY = "DISCLAIMER";

    private int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = this.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        final boolean disclaimerUnderstood = prefs.getBoolean(DISCLAIMER_KEY, false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent openNextActivity;

                if (disclaimerUnderstood)
                {
                    openNextActivity =  new Intent(SplashActivity.this,
                            MainActivity.class);
                }
                else
                {
                    openNextActivity = new Intent(SplashActivity.this,
                            DisclaimerActivity.class);
                }
                startActivity(openNextActivity);
                finish();
            }
        }, SPLASH_TIME);
    }
}
