package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;

public class HearingTestResultFragment extends Fragment {

    private HearingTestResultViewModel hearingTestResultViewModel;

    private Button testBackButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hearingTestResultViewModel =
                ViewModelProviders.of(this).get(HearingTestResultViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hearing_test_result, container, false);

        testBackButton = (Button) root.findViewById(R.id.TestBackButton);

        testBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("HTResultFragment", "testBackButton is Pressed");
                switchFragment();
            }
        });


        return root;
    }


    private void switchFragment()
    {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.nav_host_fragment, new HearingTestFragment());
        fr.commit();
    }

}