package com.ece493.group5.adjustableaudio.ui.settings;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.controllers.MusicServiceInteractor;
import com.ece493.group5.adjustableaudio.listeners.EqualizerModelListener;
import com.ece493.group5.adjustableaudio.models.AudioController;
import com.ece493.group5.adjustableaudio.models.EqualizerModel;
import com.ece493.group5.adjustableaudio.models.EqualizerPreset;

import java.util.HashMap;
import java.util.List;


public class SettingsFragment extends Fragment
{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static final String DECIBEL_UNITS = "dB";
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

    private MusicServiceInteractor musicServiceInteractor;
    private AudioController audioController;
    private EqualizerModelListener equalizerModelListener;

    private ArrayAdapter<String> equalizerPresetNamesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);

        equalizerModel = new EqualizerModel();

        presetSpinner = root.findViewById(R.id.presetSpinner);
        equalizerPresetNamesAdapter = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, equalizerModel.getEqualizerPresetNames());
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

        musicServiceInteractor = new MusicServiceInteractor(getContext()) {
            @Override
            public void onConnectionEstablished() {
                super.onConnectionEstablished();
                audioController.registerDevice(musicServiceInteractor);
                enableEqualizerControls();
            }

            @Override
            public void onConnectionLost() {
                super.onConnectionLost();
                audioController.unregisterDevice(musicServiceInteractor);
                disableEqualizerControls();
            }
        };

        audioController = new AudioController(getContext());

        setHasOptionsMenu(true);

        equalizerModelListener = (EqualizerModelListener) getContext();
        
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        musicServiceInteractor.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        musicServiceInteractor.disconnect();
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

    private void setPresetOptions()
    {
        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                EqualizerPreset selectedPreset = equalizerModelListener.getEqualizerModel().getEqualizerPreset(position);
                setEqualizerValues(selectedPreset);
                equalizerModelListener.getEqualizerModel().switchEqualizerSetting(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                EqualizerPreset defaultPreset = equalizerModelListener.getEqualizerModel().getEqualizerPreset(0);
                setEqualizerValues(defaultPreset);
            }
        });

        updateSpinner();
        presetSpinner.setSelection(equalizerModelListener.getEqualizerModel().getCurrentEqualizerSettingPosition());
    }

    private void updateSpinner()
    {
        equalizerPresetNamesAdapter.clear();
        equalizerPresetNamesAdapter.addAll(equalizerModel.getEqualizerPresetNames());
    }

    private void setEqualizerValues(EqualizerPreset equalizerPreset)
    {
        HashMap<Integer, Integer> equalizerBands = equalizerPreset.getEqualizerSettings();

        for (int index = 0; index < equalizerBands.size(); index ++)
        {
            int bandValue = equalizerBands.get(index);
            int seekBarPosition = bandValue - lowerEqualizerLevel;
            equalizerSeekbars[index].setProgress(seekBarPosition);
        }

        leftVolumeSeekbar.setProgress(equalizerPreset.getLeftVolume());

        rightVolumeSeekbar.setProgress(equalizerPreset.getRightVolume());

        globalVolumeSeekbar.setProgress(equalizerPreset.getGlobalVolume());
    }

    private void askForEqualizerNameAdd()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle("Add an Equalizer Setting");
        alertDialogBuilder.setMessage("Please Enter a Name for the Equalizer Setting");

        final EditText nameInput = new EditText(this.getContext());
        alertDialogBuilder.setView(nameInput);

        alertDialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String equalizerName = nameInput.getText().toString();
                addEqualizerSetting(equalizerName);
            }
        });

        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
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
        equalizerModelListener.getEqualizerModel().addEqualizerSetting(equalizerName);
        updateSpinner();
    }

    private void removeEqualizerSetting()
    {
        int removePosition = presetSpinner.getSelectedItemPosition();
        equalizerModelListener.getEqualizerModel().deleteEqualizerSetting(removePosition);
        updateSpinner();
    }

    private void askForEqualizerNameRename()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle("Rename the Equalizer Setting");
        alertDialogBuilder.setMessage("Please Enter a New Name for the Equalizer Setting");

        final EditText nameInput = new EditText(this.getContext());
        alertDialogBuilder.setView(nameInput);

        alertDialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String equalizerName = nameInput.getText().toString();
                renameEqualizerSetting(equalizerName);
            }
        });

        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
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
        for (int i = 0; i < equalizerSeekbars.length; i++) {
            final TextView textView = equalizerValues[i];

            final short equalizerBarPosition = Integer.valueOf(i).shortValue();

            equalizerSeekbars[i].setMax(difference);

            equalizerSeekbars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    short milliBelLevel = (short)(Integer.valueOf(seekBar.getProgress()).shortValue()
                            + lowerEqualizerLevel);
                    short decibelLevel = (short)(milliBelLevel/millibelToDecibelFactor);

                    textView.setText(String.valueOf(decibelLevel)+ DECIBEL_UNITS);
                    textView.setGravity(Gravity.CENTER);

                    audioController.setEqualizerBand(equalizerBarPosition, decibelLevel);
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
                globalVolumeValue.setText(String.valueOf(progress) + "%");

                audioController.setGlobalVolume((double) progress / (double) seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                equalizerModelListener.getEqualizerModel().setCurrentGlobalVolume(seekBar.getProgress());
            }
        });

        leftVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                leftVolumeValue.setText(String.valueOf(progress) + "%");

                double volume = (1.0 - (Math.log(leftVolumeSeekbar.getMax()
                        - leftVolumeSeekbar.getProgress()) / Math.log(leftVolumeSeekbar.getMax())));
                audioController.setLeftVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                audioController.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioController.enableEqualizer();
                equalizerModelListener.getEqualizerModel().setCurrentLeftVolume(seekBar.getProgress());
            }
        });

        rightVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rightVolumeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                rightVolumeValue.setText(String.valueOf(progress) + "%");

                double volume = (1.0 - (Math.log(rightVolumeSeekbar.getMax()
                        - rightVolumeSeekbar.getProgress()) / Math.log(rightVolumeSeekbar.getMax())));
                audioController.setRightVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                audioController.disableEqualizer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioController.enableEqualizer();
                equalizerModelListener.getEqualizerModel().setCurrentRightVolume(seekBar.getProgress());
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