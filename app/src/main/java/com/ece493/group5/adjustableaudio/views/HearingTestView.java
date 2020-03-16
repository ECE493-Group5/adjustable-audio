package com.ece493.group5.adjustableaudio.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ece493.group5.adjustableaudio.HearingTestActivity;
import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.HearingTestModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Calendar;

import androidx.constraintlayout.widget.ConstraintLayout;

public class HearingTestView extends ConstraintLayout implements Observer {

    private HearingTestActivity.HearingTestController mController;

    final private long BEEP_DURATION = 3000;

    private FloatingActionButton soundAckButton;
    private FloatingActionButton startTestButton;
    private TextView countdownText;
    private TextView testProgressText;
    private SeekBar testProgressBar;

    Boolean soundHeard;
    CountDownTimer timer;


    public HearingTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setController(HearingTestActivity.HearingTestController controller)
    {
        this.mController = controller;
    }

    public void update(Observable o, Object arg) {
        Log.d("HearingTestView", "Update Received");
        String progress = (String) arg;
        updateProgress(progress);
        soundHeard = false;
        setTimer();

        soundHeard = false;
    }

    public void updateProgress(String progress)
    {
        testProgressText.setText(progress);
        int progressValue = 0; //TODO get progress number from string
        testProgressBar.setProgress(progressValue);
    }

    public void setTimer()
    {
        this.timer = new CountDownTimer(BEEP_DURATION, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("HearingTestView", "SettingText");
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                onSoundAck();
                soundAckButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                // TODO Set warning red (?)
            }
        }.start();
    }

    public void onSoundAck()
    {
        this.mController.onSoundAck(soundHeard);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        soundAckButton = (FloatingActionButton) findViewById(R.id.hearing_test_beep_ack_button);
        startTestButton = (FloatingActionButton) findViewById(R.id.hearing_test_start_test_button);
        countdownText = (TextView) findViewById(R.id.hearing_test_countdown_textview);
        testProgressText = (TextView) findViewById(R.id.hearing_test_progress_textview);
        testProgressBar = (SeekBar) findViewById(R.id.hearing_test_progress_bar);


        soundAckButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("HearingTestView", "Sound Acknowledgement Button Pressed");
                soundHeard = true;
                soundAckButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            }
        });

        startTestButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("HearingTestView", "Start Test Button Pressed");
                startTestButton.setVisibility(View.GONE);
                soundAckButton.setVisibility(View.VISIBLE);
                mController.onStartTest();
            }
        });

    }

}
