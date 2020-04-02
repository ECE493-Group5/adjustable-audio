package com.ece493.group5.adjustableaudio.ui.settings;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;
import com.ece493.group5.adjustableaudio.controllers.MusicServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.EqualizerModelListener;
import com.ece493.group5.adjustableaudio.controllers.AudioController;
import com.ece493.group5.adjustableaudio.listeners.GlobalVolumeListener;
import com.ece493.group5.adjustableaudio.models.AudioData;

import java.util.HashMap;
import java.util.Objects;

public class SettingsFragment extends Fragment
{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static final String DECIBEL_UNITS = "dB";
    private static final String DEFAULT = "Default";
    private static final String PERCENT = "%";
    private static final String FREQUENCY_UNITS = "Hz";

    private static final double MILLI_HZ_TO_HZ = 0.001;
    private static final int MILLIBEL_TO_DECIBEL = 100;
    private static final int DEFAULT_POSITION = 0;
    private static short lowerEqualizerLevel = -1500;
    private static short upperEqualizerLevel = 1500;

    private Spinner presetSpinner;

    private SeekBar[] equalizerSeekbars;
    private TextView[] equalizerValues;
    private TextView[] equalizerTitles;

    private SeekBar globalVolumeSeekbar;
    private TextView globalVolumeValue;

    private SeekBar leftRightVolumeRatioSeekbar;
    private TextView leftVolumeLabel;
    private TextView rightVolumeLabel;

    private Button applyButton;
    private Button revertButton;

    private MusicServiceInteractor musicServiceInteractor;
    private MicrophoneServiceInteractor microphoneServiceInteractor;

    private GlobalVolumeListener globalVolumeListener;
    private AudioController audioController;
    private EqualizerModelListener equalizerModelListener;

    private ArrayAdapter<String> equalizerPresetNamesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        equalizerModelListener = (EqualizerModelListener) getContext();

        presetSpinner = root.findViewById(R.id.presetSpinner);
        equalizerPresetNamesAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                equalizerModelListener.getEqualizerModel().getEqualizerPresetNames());
        equalizerPresetNamesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        presetSpinner.setAdapter(equalizerPresetNamesAdapter);

        equalizerSeekbars = new SeekBar[] {
                root.findViewById(R.id.equalizerBandSeekbar1),
                root.findViewById(R.id.equalizerBandSeekbar2),
                root.findViewById(R.id.equalizerBandSeekbar3),
                root.findViewById(R.id.equalizerBandSeekbar4),
                root.findViewById(R.id.equalizerBandSeekbar5)
        };

        equalizerValues = new TextView[] {
                root.findViewById(R.id.equalizerBandValue1),
                root.findViewById(R.id.equalizerBandValue2),
                root.findViewById(R.id.equalizerBandValue3),
                root.findViewById(R.id.equalizerBandValue4),
                root.findViewById(R.id.equalizerBandValue5)
        };

        equalizerTitles = new TextView[] {
                root.findViewById(R.id.equalizerBandTitle1),
                root.findViewById(R.id.equalizerBandTitle2),
                root.findViewById(R.id.equalizerBandTitle3),
                root.findViewById(R.id.equalizerBandTitle4),
                root.findViewById(R.id.equalizerBandTitle5)
        };

        globalVolumeSeekbar = root.findViewById(R.id.settingsGlobalVolumeSeekbar);
        globalVolumeValue = root.findViewById(R.id.settingsGlobalVolumeValue);

        leftRightVolumeRatioSeekbar = root.findViewById(R.id.settingsLeftRightVolumeRatioSeekbar);
        leftVolumeLabel = root.findViewById(R.id.leftVolumeLabel);
        rightVolumeLabel = root.findViewById(R.id.rightVolumeLabel);

        setupInitialUIState();

        applyButton = root.findViewById(R.id.applyButton);
        revertButton = root.findViewById(R.id.revertButton);
        checkApplyAndRevertButtons();

        musicServiceInteractor = new MusicServiceInteractor(getContext())
        {
            @Override
            public void onConnectionEstablished() {
                audioController.registerDevice(musicServiceInteractor);
            }

            @Override
            public void onConnectionLost() {
                audioController.unregisterDevice(musicServiceInteractor);
            }
        };

        microphoneServiceInteractor = new MicrophoneServiceInteractor(getContext())
        {
            @Override
            public void onConnectionEstablished() {
                audioController.registerDevice(microphoneServiceInteractor);
            }

            @Override
            public void onConnectionLost() {
                audioController.unregisterDevice(microphoneServiceInteractor);
            }
        };

        audioController = new AudioController(getContext());
        globalVolumeListener = new GlobalVolumeListener(getContext()) {
            @Override
            public void onVolumeChange(int newVolumeAsPercent)
            {
                globalVolumeSeekbar.setProgress(newVolumeAsPercent);
            }
        };

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        enableEqualizerControls();

        musicServiceInteractor.connect();
        microphoneServiceInteractor.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        disableEqualizerControls();

        musicServiceInteractor.disconnect();
        microphoneServiceInteractor.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        menuInflater.inflate(R.menu.equalizer_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.add_equalizer_setting:
                askForEqualizerNameAdd();
                break;
            case R.id.remove_equalizer_setting:
                removeEqualizerSetting();
                break;
            case R.id.rename_equalizer_setting:
                askForEqualizerNameRename();
                break;
            default:
                break;
        }
        return true;
    }

    private void setupInitialUIState()
    {
        MediaPlayer dummyMediaPlayer = new MediaPlayer();
        Equalizer dummyEqualizer = new Equalizer(0, dummyMediaPlayer.getAudioSessionId());

        short[] bandLevelRange = dummyEqualizer.getBandLevelRange();
        lowerEqualizerLevel = bandLevelRange[0];
        upperEqualizerLevel = bandLevelRange[1];

        for (int i = 0; i < equalizerTitles.length; i++)
        {
            short frequency = (short) ((double) dummyEqualizer.getCenterFreq((short)i) * MILLI_HZ_TO_HZ);
            equalizerTitles[i].setText(String.valueOf(frequency) + " " + FREQUENCY_UNITS);
        }

        dummyEqualizer.release();
        dummyMediaPlayer.release();

        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int volumeLevel= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        globalVolumeSeekbar.setMax(maxVolume);
        globalVolumeSeekbar.setProgress(volumeLevel);
        globalVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        globalVolumeValue.setText(volumeLevel + " / " + maxVolume);

        HashMap<Integer, Integer> equalizerBands = equalizerModelListener.getEqualizerModel()
                .getCurrentEqualizerBandValues();

        for (int index = 0; index < equalizerBands.size(); index ++)
        {
            int seekBarPosition = equalizerBands.get(index) - lowerEqualizerLevel;
            equalizerSeekbars[index].setProgress(seekBarPosition);
            int decibelLevel = equalizerBands.get(index) / MILLIBEL_TO_DECIBEL;

            equalizerValues[index].setText(String.valueOf(decibelLevel)+ DECIBEL_UNITS);
            equalizerValues[index].setGravity(Gravity.CENTER);
        }

        int percent = AudioData.volumeRatioToPercent(equalizerModelListener.getEqualizerModel().getCurrentLeftRightVolumeRatio());
        leftRightVolumeRatioSeekbar.setProgress(percent);
        leftVolumeLabel.setText(String.format(getString(R.string.title_left), 100 - percent));
        rightVolumeLabel.setText(String.format(getString(R.string.title_right), percent));
    }

    private void setPresetOptions()
    {
        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if (position != equalizerModelListener.getEqualizerModel().getCurrentEqualizerSettingPosition())
                {
                    equalizerModelListener.getEqualizerModel().switchEqualizerSetting(position);
                }
                setEqualizerValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                equalizerModelListener.getEqualizerModel()
                        .switchEqualizerSetting(0);
                setEqualizerValues();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                equalizerModelListener.getEqualizerModel().updateEqualizerPreset(getContext());
            }
        });

        revertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                equalizerModelListener.getEqualizerModel().revertEqualizerChanges();
                setEqualizerValues();
            }
        });

        updateSpinner();

        setEqualizerValues();
    }

    private void updateSpinner()
    {
        equalizerPresetNamesAdapter.clear();
        equalizerPresetNamesAdapter.addAll(equalizerModelListener.getEqualizerModel()
                .getEqualizerPresetNames());
        presetSpinner.setAdapter(equalizerPresetNamesAdapter);
        presetSpinner.setSelection(equalizerModelListener.getEqualizerModel().getCurrentEqualizerSettingPosition());
    }

    private void checkApplyAndRevertButtons()
    {
        String currentEqualizerPresetName = equalizerModelListener.getEqualizerModel()
                .getCurrentEqualizerName();

        if (currentEqualizerPresetName.equals(DEFAULT))
        {
            applyButton.setVisibility(View.INVISIBLE);
            revertButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            applyButton.setVisibility(View.VISIBLE);
            revertButton.setVisibility(View.VISIBLE);
        }
    }

    private void setEqualizerValues()
    {
        checkApplyAndRevertButtons();
        HashMap<Integer, Integer> equalizerBands = equalizerModelListener.getEqualizerModel()
                .getCurrentEqualizerBandValues();

        for (int index = 0; index < equalizerBands.size(); index ++)
        {
            int bandValue = equalizerBands.get(index);
            int seekBarPosition = bandValue - lowerEqualizerLevel;
            equalizerSeekbars[index].setProgress(seekBarPosition);
        }

        leftRightVolumeRatioSeekbar.setProgress(
                AudioData.volumeRatioToPercent(equalizerModelListener
                        .getEqualizerModel().getCurrentLeftRightVolumeRatio()));

        audioController.restartEqualizer();
    }

    private void askForEqualizerNameAdd()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.title_dialog_add_preset);
        alertDialogBuilder.setMessage(R.string.dialog_msg_add_preset);

        final EditText nameInput = new EditText(this.getContext());
        alertDialogBuilder.setView(nameInput);

        alertDialogBuilder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String equalizerName = nameInput.getText().toString();
                addEqualizerSetting(equalizerName);
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

    private void addEqualizerSetting(String equalizerName)
    {
        equalizerModelListener.getEqualizerModel().addEqualizerSetting(getContext(), equalizerName);
        updateSpinner();
        presetSpinner.setSelection(equalizerModelListener.getEqualizerModel().getCurrentEqualizerSettingPosition());
    }

    private void removeEqualizerSetting()
    {
        int removePosition = presetSpinner.getSelectedItemPosition();
        if (removePosition != DEFAULT_POSITION)
        {
            equalizerModelListener.getEqualizerModel().deleteEqualizerSetting(getContext(), removePosition);
            updateSpinner();
        }
        else
        {
            Toast.makeText(getContext(), R.string.error_delete_default, Toast.LENGTH_SHORT).show();
        }
    }

    private void askForEqualizerNameRename()
    {
        int renamePostion = presetSpinner.getSelectedItemPosition();
        if (renamePostion == DEFAULT_POSITION)
        {
            Toast.makeText(getContext(), R.string.error_rename_default, Toast.LENGTH_SHORT).show();
            return;
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.title_dialog_preset_rename);
        alertDialogBuilder.setMessage(R.string.dialog_msg_preset_rename);

        final EditText nameInput = new EditText(this.getContext());
        alertDialogBuilder.setView(nameInput);

        alertDialogBuilder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String equalizerName = nameInput.getText().toString();
                renameEqualizerSetting(equalizerName);
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

    private void renameEqualizerSetting(String equalizerName)
    {
        equalizerModelListener.getEqualizerModel().renameEqualizerSetting(getContext(), equalizerName);
        updateSpinner();
    }

    private void enableEqualizerControls()
    {
        int difference = upperEqualizerLevel - lowerEqualizerLevel;
        for (int i = 0; i < equalizerSeekbars.length; i++)
        {
            final TextView textView = equalizerValues[i];

            final short equalizerBarPosition = Integer.valueOf(i).shortValue();

            equalizerSeekbars[i].setMax(difference);

            equalizerSeekbars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    short milliBelLevel = (short)(Integer.valueOf(progress).shortValue()
                            + lowerEqualizerLevel);
                    short decibelLevel = (short)(milliBelLevel/ MILLIBEL_TO_DECIBEL);

                    textView.setText(String.valueOf(decibelLevel)+ DECIBEL_UNITS);
                    textView.setGravity(Gravity.CENTER);

                    audioController.setEqualizerBand(equalizerBarPosition, milliBelLevel);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    int millibelLevel = seekBar.getProgress() + lowerEqualizerLevel;
                    equalizerModelListener.getEqualizerModel().setFrequencyBand(Short.valueOf(equalizerBarPosition).intValue(),
                            millibelLevel);
                }
            });
        }

        globalVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                globalVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                globalVolumeValue.setText(seekBar.getProgress() + " / " + seekBar.getMax());

                if (fromUser)
                    audioController.setGlobalVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        leftRightVolumeRatioSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                double ratio = AudioData.percentToVolumeRatio(progress);

                leftVolumeLabel.setText(String.format(getString(R.string.title_left), 100 - progress));
                rightVolumeLabel.setText(String.format(getString(R.string.title_right), progress));

                audioController.setLeftRightVolumeRatio(ratio);
                equalizerModelListener.getEqualizerModel().setCurrentLeftRightVolumeRatio(ratio);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                audioController.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                audioController.enableEqualizer();
            }
        });

        setPresetOptions();

        Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                globalVolumeListener);
    }

    private void disableEqualizerControls()
    {
        for(SeekBar equalizerSeekbar : equalizerSeekbars)
        {
            equalizerSeekbar.setOnSeekBarChangeListener(null);
        }

        globalVolumeSeekbar.setOnSeekBarChangeListener(null);
        leftRightVolumeRatioSeekbar.setOnSeekBarChangeListener(null);

        Objects.requireNonNull(getContext()).getContentResolver().unregisterContentObserver(globalVolumeListener);
    }
}