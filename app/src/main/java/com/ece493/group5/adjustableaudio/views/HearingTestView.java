package com.ece493.group5.adjustableaudio.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ece493.group5.adjustableaudio.HearingTestActivity;
import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.utils.TimeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Observable;
import java.util.Observer;

import androidx.constraintlayout.widget.ConstraintLayout;

public class HearingTestView extends ConstraintLayout implements Observer {

    private HearingTestActivity.HearingTestController mController;

    final private long BEEP_DURATION = 3000;
    final private long COUNTDOWN_INTERVAL = 1000;
    final private int NUMBER_FREQUENCIES = 16;
    final private String PAUSE_TAG = "pause";
    final private String UNPAUSE_TAG = "unpause";

    private FloatingActionButton soundAckButton;
    private FloatingActionButton startTestButton;
    private TextView countdownText;
    private TextView testProgressText;
    private TextView countdownInfoText;
    private SeekBar testProgressBar;

    Boolean soundHeard;
    Boolean testRunning;
    CountDownTimer timer;


    public HearingTestView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setController(HearingTestActivity.HearingTestController controller)
    {
        this.mController = controller;
    }

    public void update(Observable o, Object arg)
    {
        if (arg.getClass().equals(String.class))
        {
            if (arg.equals(PAUSE_TAG))
            {
                onPauseTest();
            }
            else
            {
                onUnpauseTest();
            }
        }
        else
        {
            int progress = (int) arg;
            updateProgress(progress);
            soundHeard = false;
            setBeepTimer();
        }
    }

    public void updateProgress(int progress)
    {
        String progressText = Integer.toString(progress) + "/" +
                Integer.toString(NUMBER_FREQUENCIES);
        testProgressText.setText(progressText);
        testProgressBar.setProgress(progress);
    }

    private void setBeepTimer()
    {
        this.timer = new CountDownTimer(BEEP_DURATION, COUNTDOWN_INTERVAL)
        {

            public void onTick(long millisUntilFinished)
            {
                countdownText.setText(
                        String.valueOf(TimeUtils.millisecondsToSeconds(millisUntilFinished)));
            }

            public void onFinish()
            {
                onSoundAck();
                soundAckButton.setBackgroundTintList(
                        ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            }
        }.start();
    }

    private void setStartTimer()
    {
        this.timer = new CountDownTimer(BEEP_DURATION, COUNTDOWN_INTERVAL)
        {

            public void onTick(long millisUntilFinished)
            {
                countdownText.setText(
                        String.valueOf(TimeUtils.millisecondsToSeconds(millisUntilFinished)));
            }

            public void onFinish()
            {
                testRunning = true;
                countdownInfoText.setText(R.string.caption_next_beep);
                mController.onStartTest();
            }
        }.start();
    }

    public void onSoundAck()
    {
        this.mController.onSoundAck(soundHeard);
    }

    public void onCancelTest()
    {
        onPauseTest();
        cancelTestDialog();
    }

    public void onPauseTest()
    {
        this.timer.cancel();
        soundAckButton.setEnabled(false);
    }

    public void onUnpauseTest()
    {
        setBeepTimer();
        soundAckButton.setEnabled(true);
    }

    private void cancelTestDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle(R.string.title_dialog_cancel_hearing_test);
        alertDialogBuilder.setMessage(R.string.dialog_msg_cancel_hearing_test);

        alertDialogBuilder.setPositiveButton(R.string.resume_button,
                new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                onUnpauseTest();
                dialogInterface.cancel();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel_test_button,
                new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                mController.endActivity();
            }
        });
        alertDialogBuilder.show();
    }
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        soundAckButton = (FloatingActionButton) findViewById(R.id.hearing_test_beep_ack_button);
        startTestButton = (FloatingActionButton) findViewById(R.id.hearing_test_start_test_button);
        countdownText = (TextView) findViewById(R.id.hearing_test_countdown_textview);
        countdownInfoText = (TextView) findViewById(R.id.countdown_info_textview);
        testProgressText = (TextView) findViewById(R.id.hearing_test_progress_textview);
        testProgressBar = (SeekBar) findViewById(R.id.hearing_test_progress_bar);
        testProgressBar.setMax(NUMBER_FREQUENCIES);
        testProgressText.setText("0/" + Integer.toString(NUMBER_FREQUENCIES));
        soundAckButton.setRippleColor(getResources().getColor(R.color.lightGrey));


        soundAckButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                soundHeard = true;
                soundAckButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightGrey)));
            }
        });

        startTestButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startTestButton.setVisibility(View.GONE);
                soundAckButton.setVisibility(View.VISIBLE);
                setStartTimer();
            }
        });
    }
}
