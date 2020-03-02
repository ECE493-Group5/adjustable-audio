package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.os.Bundle;
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

public class HearingTestFragment extends Fragment {

    private HearingTestViewModel hearingTestViewModel;

    private Button testForwardButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hearingTestViewModel =
                ViewModelProviders.of(this).get(HearingTestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hearing_test, container, false);

        testForwardButton = (Button) root.findViewById(R.id.TestForwardButton);

        testForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment();
            }
        });

        return root;
    }

    private void switchFragment()
    {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.nav_host_fragment, new HearingTestResultFragment());
        fr.commit();
    }


}