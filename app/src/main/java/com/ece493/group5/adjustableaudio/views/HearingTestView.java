package com.ece493.group5.adjustableaudio.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

/**
 * The HearingTestView helps implement the following requirements:
 *
 * #SRS: Performing a Hearing Test
 */

public class HearingTestView extends ConstraintLayout implements Observer
{

    private HearingTestActivity.HearingTestController controller;

    final private long BEEP_DURATION = 2000;
    final private long COUNTDOWN_INTERVAL = 100;
    final private int NUMBER_FREQUENCIES = 16;
    final private int MSEC_MODULUS_10 = 10;
    final private String PAUSE_TAG = "pause";
    final private String RIGHT_EAR_TAG = "R";

    private FloatingActionButton soundAckButton;
    private FloatingActionButton startTestButton;
    private TextView countdownIntegerText;
    private TextView countdownDecimalText;
    private TextView testProgressText;
    private TextView countdownInfoText;
    private TextView currentEarText;
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
        this.controller = controller;
    }

    public void update(Observable o, Object arg)
    {
        if (arg.getClass().equals(String.class))
        {
            if (arg.equals(PAUSE_TAG))
            {
                onPauseTest();
            }
            else if (arg.equals(RIGHT_EAR_TAG))
            {
                updateEarTextView();
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

    public void updateEarTextView()
    {
        currentEarText.setText(R.string.label_hearing_test_right_ear);
    }

    private void setBeepTimer()
    {
        this.timer = new CountDownTimer(BEEP_DURATION, COUNTDOWN_INTERVAL)
        {

            public void onTick(long millisUntilFinished)
            {
                countdownIntegerText.setText(
                        String.valueOf(TimeUtils.millisecondsToSeconds(millisUntilFinished)));

                countdownDecimalText.setText(String.valueOf
                        ((millisUntilFinished/COUNTDOWN_INTERVAL) % MSEC_MODULUS_10));
            }

            public void onFinish()
            {
                soundHeard = false;
                onSoundAck();
            }
        }.start();
    }

    private void setStartTimer()
    {
        this.timer = new CountDownTimer(BEEP_DURATION, COUNTDOWN_INTERVAL)
        {

            public void onTick(long millisUntilFinished)
            {
                countdownIntegerText.setText(
                        String.valueOf(TimeUtils.millisecondsToSeconds(millisUntilFinished)));

                countdownDecimalText.setText(String.valueOf
                        ((millisUntilFinished/COUNTDOWN_INTERVAL) % MSEC_MODULUS_10));
            }

            public void onFinish()
            {
                testRunning = true;
                countdownInfoText.setText(R.string.caption_next_beep);
                enableButtons();
                controller.onStartTest();
            }
        }.start();
    }

    public void onSoundAck()
    {
        this.controller.onSoundAck(soundHeard);
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
                controller.endActivity();
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

        countdownIntegerText = (TextView) findViewById(R.id.hearing_test_countdown_integer_textview);
        countdownDecimalText = (TextView) findViewById(R.id.hearing_test_countdown_decimal_textview);
        countdownInfoText = (TextView) findViewById(R.id.countdown_info_textview);
        testProgressText = (TextView) findViewById(R.id.hearing_test_progress_textview);
        currentEarText = (TextView) findViewById(R.id.hearing_test_ear_textview);

        testProgressBar = (SeekBar) findViewById(R.id.hearing_test_progress_bar);

        testProgressText.setText("0/" + Integer.toString(NUMBER_FREQUENCIES));
        currentEarText.setText(R.string.label_hearing_test_left_ear);
        testProgressBar.setMax(NUMBER_FREQUENCIES);
        soundAckButton.setRippleColor(getResources().getColor(R.color.lightGrey));

        testProgressBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                return true;
            }
        });
        
        enableButtons();
    }

    private void enableButtons()
    {
        soundAckButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                timer.cancel();
                soundHeard = true;
                onSoundAck();
            }
        });

        startTestButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startTestButton.setVisibility(View.GONE);
                soundAckButton.setVisibility(View.VISIBLE);

                disableButtons();
                setStartTimer();
            }
        });
    }

    private void disableButtons()
    {
        soundAckButton.setOnClickListener(null);
        startTestButton.setOnClickListener(null);
    }
}
