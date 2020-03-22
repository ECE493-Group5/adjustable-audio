package com.ece493.group5.adjustableaudio.ui.settings;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MicrophoneServiceInteractor;
import com.ece493.group5.adjustableaudio.controllers.MusicServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.EqualizerModelListener;
import com.ece493.group5.adjustableaudio.controllers.AudioController;

import java.util.HashMap;


public class SettingsFragment extends Fragment
{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static final String ADD_EQUALIZER_PROMPT = "Please Enter a Name for the Equalizer Setting";
    private static final String ADD_EQUALIZER_TITLE = "Add an Equalizer Setting";
    private static final String CANCEL = "CANCEL";
    private static final String DECIBEL_UNITS = "dB";
    private static final String DEFAULT = "Default";
    private static final String PERCENT = "%";
    private static final String RENAME_EQUALIZER_PROMPT = "Please Enter a New Name for the Equalizer Setting";
    private static final String RENAME_EQUALIZER_TITLE = "Rename the Equalizer Setting";
    private static final String SAVE = "SAVE";

    private static final short lowerEqualizerLevel = -1500;
    private static final short upperEqualizerLevel = 1500;
    private static final int millibelToDecibelFactor = 100;

    private SettingsViewModel settingsViewModel;

    private Spinner presetSpinner;

    private SeekBar[] equalizerSeekbars;
    private TextView[] equalizerValues;

    private SeekBar globalVolumeSeekbar;
    private TextView globalVolumeValue;

    private SeekBar leftVolumeSeekbar;
    private TextView leftVolumeValue;

    private SeekBar rightVolumeSeekbar;
    private TextView rightVolumeValue;

    private Button applyButton;
    private Button revertButton;

    private MusicServiceInteractor musicServiceInteractor;
    private MicrophoneServiceInteractor microphoneServiceInteractor;

    private AudioController audioController;
    private EqualizerModelListener equalizerModelListener;

    private ArrayAdapter<String> equalizerPresetNamesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);

        equalizerModelListener = (EqualizerModelListener) getContext();

        presetSpinner = root.findViewById(R.id.presetSpinner);
        equalizerPresetNamesAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                equalizerModelListener.getEqualizerModel().getEqualizerPresetNames());
        equalizerPresetNamesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        presetSpinner.setAdapter(equalizerPresetNamesAdapter);

        equalizerSeekbars = new SeekBar[]{
                root.findViewById(R.id.equalizerBandSeekbar1),
                root.findViewById(R.id.equalizerBandSeekbar2),
                root.findViewById(R.id.equalizerBandSeekbar3),
                root.findViewById(R.id.equalizerBandSeekbar4),
                root.findViewById(R.id.equalizerBandSeekbar5)
        };

        equalizerValues = new TextView[]{
                root.findViewById(R.id.equalizerBandValue1),
                root.findViewById(R.id.equalizerBandValue2),
                root.findViewById(R.id.equalizerBandValue3),
                root.findViewById(R.id.equalizerBandValue4),
                root.findViewById(R.id.equalizerBandValue5)
        };

        globalVolumeSeekbar = root.findViewById(R.id.settingsGlobalVolumeSeekbar);
        globalVolumeValue = root.findViewById(R.id.settingsGlobalVolumeValue);

        leftVolumeSeekbar = root.findViewById(R.id.settingsLeftVolumeSeekbar);
        leftVolumeValue = root.findViewById(R.id.settingsLeftVolumeValue);

        rightVolumeSeekbar = root.findViewById(R.id.settingsRightVolumeSeekbar);
        rightVolumeValue = root.findViewById(R.id.settingsRightVolumeValue);

        setupInitialUIState();

        applyButton = root.findViewById(R.id.applyButton);
        revertButton = root.findViewById(R.id.revertButton);
        checkApplyAndRevertButtons();

        musicServiceInteractor = new MusicServiceInteractor(getContext()) {
            @Override
            public void onConnectionEstablished() {
                audioController.registerDevice(musicServiceInteractor);
            }

            @Override
            public void onConnectionLost() {
                audioController.unregisterDevice(musicServiceInteractor);
            }
        };

        microphoneServiceInteractor = new MicrophoneServiceInteractor(getContext()) {
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
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int volumeLevel= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        globalVolumeSeekbar.setProgress(volumeLevel);
        globalVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        globalVolumeValue.setText(String.valueOf(volumeLevel) + PERCENT);

        HashMap<Integer, Integer> equalizerBands = equalizerModelListener.getEqualizerModel()
                .getCurrentEqualizerBandValues();

        for (int index = 0; index < equalizerBands.size(); index ++)
        {
            int seekBarPosition = equalizerBands.get(index) - lowerEqualizerLevel;
            equalizerSeekbars[index].setProgress(seekBarPosition);
        }

        leftVolumeSeekbar.setProgress(equalizerModelListener.getEqualizerModel().getCurrentLeftVolume());
        leftVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        leftVolumeValue.setText(String.valueOf(leftVolumeSeekbar.getProgress()) + PERCENT);

        rightVolumeSeekbar.setProgress(equalizerModelListener.getEqualizerModel().getCurrentRightVolume());
        rightVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        rightVolumeValue.setText(String.valueOf(rightVolumeSeekbar.getProgress()) + PERCENT);
    }

    private void setPresetOptions()
    {
        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                equalizerModelListener.getEqualizerModel().switchEqualizerSetting(position);
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
                equalizerModelListener.getEqualizerModel().updateEqualizerPreset();
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

        leftVolumeSeekbar.setProgress(equalizerModelListener.getEqualizerModel()
                .getCurrentLeftVolume());
        rightVolumeSeekbar.setProgress(equalizerModelListener.getEqualizerModel()
                .getCurrentRightVolume());
    }

    private void askForEqualizerNameAdd()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(ADD_EQUALIZER_TITLE);
        alertDialogBuilder.setMessage(ADD_EQUALIZER_PROMPT);

        final EditText nameInput = new EditText(this.getContext());
        alertDialogBuilder.setView(nameInput);

        alertDialogBuilder.setPositiveButton(SAVE, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String equalizerName = nameInput.getText().toString();
                addEqualizerSetting(equalizerName);
            }
        });

        alertDialogBuilder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener()
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
    }

    private void removeEqualizerSetting()
    {
        int removePosition = presetSpinner.getSelectedItemPosition();
        equalizerModelListener.getEqualizerModel().deleteEqualizerSetting(getContext(), removePosition);
        updateSpinner();
    }

    private void askForEqualizerNameRename()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(RENAME_EQUALIZER_TITLE);
        alertDialogBuilder.setMessage(RENAME_EQUALIZER_PROMPT);

        final EditText nameInput = new EditText(this.getContext());
        alertDialogBuilder.setView(nameInput);

        alertDialogBuilder.setPositiveButton(SAVE, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String equalizerName = nameInput.getText().toString();
                renameEqualizerSetting(equalizerName);
            }
        });

        alertDialogBuilder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener()
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
        int renameSettingPosition = presetSpinner.getSelectedItemPosition();
        equalizerModelListener.getEqualizerModel().renameEqualizerSetting(renameSettingPosition, equalizerName);
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

            equalizerSeekbars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    short milliBelLevel = (short)(Integer.valueOf(progress).shortValue()
                            + lowerEqualizerLevel);
                    short decibelLevel = (short)(milliBelLevel/millibelToDecibelFactor);

                    textView.setText(String.valueOf(decibelLevel)+ DECIBEL_UNITS);
                    textView.setGravity(Gravity.CENTER);

                    audioController.setEqualizerBand(equalizerBarPosition, milliBelLevel);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    int millibelLevel = seekBar.getProgress() + lowerEqualizerLevel;
                    equalizerModelListener.getEqualizerModel().setFrequencyBand(Short.valueOf(equalizerBarPosition).intValue(),
                            millibelLevel);
                }
            });
        }

        globalVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                globalVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                globalVolumeValue.setText(String.valueOf(progress) + PERCENT);

                audioController.setGlobalVolume((double) progress / (double) seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        leftVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                leftVolumeValue.setText(String.valueOf(progress) + PERCENT);

                double volume = (1.0 - (Math.log(leftVolumeSeekbar.getMax()
                        - leftVolumeSeekbar.getProgress()) / Math.log(leftVolumeSeekbar.getMax())));
                audioController.setLeftVolume(volume);
                equalizerModelListener.getEqualizerModel().setCurrentLeftVolume(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                audioController.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioController.enableEqualizer();
            }
        });

        rightVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rightVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                rightVolumeValue.setText(String.valueOf(progress) + PERCENT);

                double volume = (1.0 - (Math.log(rightVolumeSeekbar.getMax()
                        - rightVolumeSeekbar.getProgress()) / Math.log(rightVolumeSeekbar.getMax())));
                audioController.setRightVolume(volume);
                equalizerModelListener.getEqualizerModel().setCurrentRightVolume(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                audioController.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioController.enableEqualizer();
            }
        });

        setPresetOptions();
    }

    private void disableEqualizerControls()
    {
        for(SeekBar equalizerSeekbar : equalizerSeekbars)
        {
            equalizerSeekbar.setOnSeekBarChangeListener(null);
        }

        globalVolumeSeekbar.setOnSeekBarChangeListener(null);
        leftVolumeSeekbar.setOnSeekBarChangeListener(null);
        rightVolumeSeekbar.setOnSeekBarChangeListener(null);
    }
}