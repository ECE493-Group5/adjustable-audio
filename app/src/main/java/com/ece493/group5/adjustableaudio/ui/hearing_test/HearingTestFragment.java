package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ece493.group5.adjustableaudio.R;

public class HearingTestFragment extends Fragment {

    private HearingTestViewModel hearingTestViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hearingTestViewModel =
                ViewModelProviders.of(this).get(HearingTestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hearing_test, container, false);
        final TextView textView = root.findViewById(R.id.text_hearing_test);
        hearingTestViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}