package com.ece493.group5.adjustableaudio.models;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.util.Log;
import android.widget.EditText;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.storage.SaveController;
import com.ece493.group5.adjustableaudio.views.HearingTestView;

import java.util.ArrayList;
import java.util.Observable;

import androidx.appcompat.app.AlertDialog;

public class HearingTestModel extends Observable
{
    private static final long BEEP_DURATION = 2000;
    private static final int SAMPLE_OFFSET = 0;
    public static final int MAX_DB = 100;
    private static final int[] TONES = {30, 90, 233, 250, 347,
                                        500, 907, 1000, 1353, 2000,
                                        3533, 4000, 5267, 8000, 11333, 15667};
    private static final float MUTE = 0.0f;
    private static final double[] REFERENCE_FREQUENCY_DBHL_VALUES = {
            60.0, 37.0, 19.0, 18.0,
            14.6, 11.0, 6.0, 5.5,
            5.5, 4.5, 6.5, 9.5,
            14.8, 17.5, 23.0, 52.5};
    private static final double DBHL_MIN = -5;
    private static final double DBHL_INCREMENT = 5;
    private static final String DEFAULT_NAME = "Hearing Test";
    public static final String PACKAGE_NAME = "com.ece493.group5.adjustableaudio";
    private static final String LEFT_EAR = "L";
    private static final String RIGHT_EAR = "R";
    private static final String PAUSE_TAG = "pause";
    private static final String UNPAUSE_TAG = "unpause";

    private Context mContext;
    private HearingTestView mView;

    private ArrayList<ToneData> toneDataArrayList;
    private String currentEar;
    private String testName;
    private Boolean testRunning;
    private Boolean isPaused;
    private Boolean maxVolumeReached;
    private AudioManager audioManager;
    private int currentSound;
    private int progress;
    private double dbHLLevel;
    private float LVolume;
    private float RVolume;
    private AudioFocusChecker audioFocusChecker;
    private ToneGenerator toneGenerator;
    private AudioTrack audioTrack;


    public HearingTestModel(Context mContext)
    {
        this.mContext = mContext;
        this.currentEar = LEFT_EAR;
        this.testRunning = false;
        this.audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        this.toneGenerator = new ToneGenerator(audioManager);
        this.audioFocusChecker= new AudioFocusChecker();
        initToneDataArrayList();
        initAudioTrack();
        initTest();
    }

    private void pauseTest()
    {
        testRunning = false;
        isPaused = true;
        notifyPaused();
    }

    private void unPauseTest()
    {
        testRunning = true;
        isPaused = false;
        notifyUnPaused();
    }

    private void notifyPaused()
    {
        setChanged();
        notifyObservers(PAUSE_TAG);
    }

    private void notifyUnPaused()
    {
        setChanged();
        notifyObservers(UNPAUSE_TAG);
    }

    public int getProgress()
    {
        return this.progress;
    }

    public boolean getTestState()
    {
        return testRunning;
    }

    private void setProgress(int progress)
    {
        this.progress = progress;
    }

    private void setTestName(String name)
    {
        testName = name;
    }

    private void initToneDataArrayList()
    {
        this.toneDataArrayList = new ArrayList<ToneData>();
    }

    private void initTest()
    {
        this.initTestState();
        this.initVolume();
        generateTone();
    }

    private void initTestState()
    {
        this.progress = 1;
        currentSound = 0;
    }

    private void initAudioTrack()
    {
        loadSounds();
        this.audioTrack = toneGenerator.generateTrack((int)BEEP_DURATION);
    }

    private void initVolume()
    {
        dbHLLevel = DBHL_MIN;
        maxVolumeReached = false;
        if (currentEar.equals(LEFT_EAR))
        {
            LVolume = dBToGain(dbHLLevel + REFERENCE_FREQUENCY_DBHL_VALUES[0] );
            RVolume = MUTE;
        }
        else
        {
            LVolume = MUTE;
            RVolume = dBToGain(dbHLLevel + REFERENCE_FREQUENCY_DBHL_VALUES[0]);
        }
    }

    private void loadSounds()
    {
        for (int i = 0; i < TONES.length; i++)
        {
            this.toneDataArrayList.add(new ToneData(TONES[i], REFERENCE_FREQUENCY_DBHL_VALUES[i]));
        }
    }

    private void generateTone()
    {
        short[] samples;
        if (currentEar.equals(LEFT_EAR))
        {
            samples = toneGenerator.generateTone(TONES[currentSound],
                    (int)BEEP_DURATION, LVolume, currentEar);
        }
        else
        {
            samples = toneGenerator.generateTone(TONES[currentSound],
                    (int)BEEP_DURATION, RVolume, currentEar);
        }
        audioTrack.write(samples, SAMPLE_OFFSET, samples.length);
    }

    private void setVolume()
    {
        double newVolume = dbHLLevel + REFERENCE_FREQUENCY_DBHL_VALUES[currentSound];
        if (newVolume >= MAX_DB)
        {
            newVolume = MAX_DB;
            maxVolumeReached = true;
        }
        if (currentEar.equals(LEFT_EAR))
        {
            LVolume = dBToGain(newVolume);
            RVolume = MUTE;
        }
        else
        {
            LVolume = MUTE;
            RVolume = dBToGain(newVolume);
        }
    }

    private void playNextSound()
    {
        audioTrack.play();
    }

    private void updateResult()
    {
        if (currentEar.equals(LEFT_EAR))
        {
            toneDataArrayList.get(currentSound).setLHeardAtDB(dbHLLevel +
                    REFERENCE_FREQUENCY_DBHL_VALUES[currentSound]);
        }
        else
        {
            toneDataArrayList.get(currentSound).setRHeardAtDB(dbHLLevel +
                    REFERENCE_FREQUENCY_DBHL_VALUES[currentSound]);
        }
    }

    private void updateTestState(Boolean heard)
    {
        if (heard || maxVolumeReached)
        {
            if (currentSound < TONES.length-1)
            {
                currentSound += 1;
                setProgress(currentSound + 1);
                updateResult();
                resetVolume();
                maxVolumeReached = false;
            }
            else if (currentEar.equals(LEFT_EAR))
            {
                updateResult();
                currentEar = RIGHT_EAR;
                initTest();
                maxVolumeReached = false;
            }
            else
            {
                testRunning = false;
                onTestFinish();
            }
        }
        else
        {
            increaseVolume();
        }
    }

    private void increaseVolume()
    {
        this.dbHLLevel += DBHL_INCREMENT;
        setVolume();
        generateTone();
    }

    private void resetVolume()
    {
        this.dbHLLevel = DBHL_MIN;
        setVolume();
        generateTone();
    }

    public void runTest()
    {
        if (audioFocusChecker.requestAudioFocus())
        {
            testRunning = true;
            setChanged();
            this.playNextSound();
            notifyObservers(this.progress);
        }
    }

    private void saveResult(HearingTestResult result)
    {
        SaveController.saveResult(mContext, result);
    }

    public void onSoundAck(Boolean heard)
    {
        audioTrack.pause();
        audioTrack.flush();
        if (testRunning)
        {
            updateTestState(heard);
            setChanged();
            notifyObservers(this.progress);
            playNextSound();
        }
    }

    private void onTestFinish()
    {
        onTestExit();
        requestNameDialog();
    }

    public void onTestExit()
    {
        audioFocusChecker.abandonAudioFocus();
        audioFocusChecker = null;
        deleteObservers();
        audioTrack.pause();
        audioTrack.flush();
    }

    private void createResult()
    {
        HearingTestResult result;
        if (testName != null) {
            result = new HearingTestResult(testName, toneDataArrayList);
        } else {
            result = new HearingTestResult(DEFAULT_NAME, toneDataArrayList);
        }
        saveResult(result);
        closeActivity();
    }

    private void closeActivity()
    {
        ((Activity)(mContext)).finish();
    }


    private void requestNameDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.title_dialog_new_test_result);
        alertDialogBuilder.setMessage(R.string.dialog_msg_new_test_result);

        final EditText testName = new EditText(mContext);
        alertDialogBuilder.setView(testName);

        alertDialogBuilder.setPositiveButton(R.string.save_button,
                new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                setTestName(testName.getText().toString());
                createResult();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.negative_button_dialog,
                new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    private float dBToGain(double dBSPL)
    {
        return (float) Math.pow(10, (dBSPL-MAX_DB)*.05);
    }

    class AudioFocusChecker implements AudioManager.OnAudioFocusChangeListener
    {
        @Override
        public void onAudioFocusChange(int focusChange)
        {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                if (isPaused)
                {
                    unPauseTest();
                }

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                // Permanent loss of audio focus
                // Pause hearing test
                audioManager.abandonAudioFocus(this);
                if (!isPaused)
                {
                    pauseTest();
                }

            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                // Pause playback
                if (!isPaused)
                {
                    pauseTest();
                }
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                // Volume is important to the results of the hearing test, so still pause
                if (!isPaused)
                {
                    pauseTest();
                }
            }
        }

        private Boolean requestAudioFocus()
        {
            int request = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            return request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }

        private void abandonAudioFocus()
        {
            audioManager.abandonAudioFocus(this);
        }
    }
}
