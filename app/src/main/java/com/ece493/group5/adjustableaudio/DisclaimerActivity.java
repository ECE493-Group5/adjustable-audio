package com.ece493.group5.adjustableaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * The Disclaimer Activity shows a disclaimer for the application.
 */

public class DisclaimerActivity extends AppCompatActivity
{
    private static final String DISCLAIMER_FILE = "disclaimer";
    private static final String DISCLAIMER_FOLDER = "raw";
    private static final String DISCLAIMER_KEY = "DISCLAIMER";
    private static final String SHARED_PREFS_FILE = "DISCLAIMER_STORAGE";

    private static final int BUFFER_SIZE = 4096;

    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        String disclaimer = getDisclaimerStatement();
        TextView disclaimerView = findViewById(R.id.disclaimerText);
        disclaimerView.setText(disclaimer);

        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeToMainActivity();
            }
        });

        CheckBox agreeCheckBox = findViewById(R.id.checkBox);

        agreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if (isChecked)
                {
                    continueButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    continueButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private String getDisclaimerStatement()
    {
        InputStream disclaimerStream = getResources().openRawResource(
                getResources().getIdentifier(DISCLAIMER_FILE, DISCLAIMER_FOLDER, getPackageName()));

        StringBuilder builder = new StringBuilder();

        char[] buffer = new char[BUFFER_SIZE];
        Reader reader = new InputStreamReader(disclaimerStream, StandardCharsets.UTF_8);
        int charsRead;

        try {
            while((charsRead = reader.read(buffer, 0, buffer.length)) > 0)
            {
                builder.append(buffer, 0, charsRead);
            }

            disclaimerStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private void changeToMainActivity()
    {
        SharedPreferences.Editor editor = this
                .getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE).edit();

        editor.putBoolean(DISCLAIMER_KEY, true);
        editor.apply();

        Intent openMainActivity =  new Intent(DisclaimerActivity.this,
                MainActivity.class);
        startActivity(openMainActivity);
        finish();
    }
}
