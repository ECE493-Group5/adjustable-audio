package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.storage.HearingTestResultListController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HearingTestResultFragment extends Fragment {

    private HearingTestResultViewModel hearingTestResultViewModel;
    private HearingTestResult testResult;
    private View root;

    private Button createEqPresetButton;
    private Button shareTestResultButton;
    private Button renameTestResultButton;
    private Button deleteTestResultButton;
    private ImageView audioGramImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hearingTestResultViewModel =
                ViewModelProviders.of(this).get(HearingTestResultViewModel.class);
        root = inflater.inflate(R.layout.fragment_hearing_test_result, container, false);
        int position = getArguments().getInt("position");
//        Log.d("TestResultFragment", "Array Position: " + Integer.toString(position));
        testResult = HearingTestResultListController.getResultList(getActivity()).get(position);
//        Log.d("TestResultFragment", "TestResultDate: " + result.getTestDate().toString());
        createEqPresetButton = (Button) root.findViewById(R.id.hearing_test_result_eq_preset_button);
        shareTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_share_button);
        renameTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_rename_button);
        deleteTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_delete_button);
        audioGramImageView = (ImageView) root.findViewById(R.id.audiogram_image);
        enableControls();

        return root;
    }

    private void generateAudioGramImage()
    {

    }

    private void shareTestResult()
    {

    }

    private void shareResultWithMMS()
    {

    }

    private void shareResultWithEmail()
    {

    }

    private void deleteResult()
    {
        requestDeleteDialog();
    }

    private void requestDeleteDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Delete this Hearing Test Result?");
        alertDialogBuilder.setMessage("If deleted, the hearing test result cannot be recovered after");

        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                deleteTestResult();
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

    private void deleteTestResult()
    {
        if (testResult != null)
        {
            HearingTestResultListController.remove(getActivity(), testResult);
        }
        switchFragment();
    }

    private void renameResult()
    {
        requestNewNameDialog();
    }

    private void requestNewNameDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Enter a New Name");
        alertDialogBuilder.setMessage("Press cancel to keep the old name");

        final EditText newName = new EditText(this.getContext());
        alertDialogBuilder.setView(newName);

        alertDialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                setTestName(newName.getText().toString());
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

    private void setTestName(String newName)
    {
        if (testResult != null)
        {
            testResult.setTestName(newName);
        }
    }

    private void generateEqualizerPreset()
    {

    }

    private void enableControls()
    {
        createEqPresetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                generateEqualizerPreset();
            }
        });

        shareTestResultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                shareTestResult();
            }
        });

        renameTestResultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                renameResult();
            }
        });

        deleteTestResultButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleteResult();
            }
        });
    }

    private void disableControls()
    {
        createEqPresetButton.setOnClickListener(null);
        shareTestResultButton.setOnClickListener(null);
        renameTestResultButton.setOnClickListener(null);
        deleteTestResultButton.setOnClickListener(null);
    }
    private void switchFragment()
    {
        Navigation.findNavController(root).navigate(R.id.navigation_hearing_test);
    }

}