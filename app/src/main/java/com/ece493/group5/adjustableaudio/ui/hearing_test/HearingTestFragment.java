package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ece493.group5.adjustableaudio.HearingTestActivity;
import com.ece493.group5.adjustableaudio.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HearingTestFragment extends Fragment {

    private HearingTestViewModel hearingTestViewModel;
    private View root;

    private Button testForwardButton;
    private FloatingActionButton startHearingTestButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hearingTestViewModel =
                ViewModelProviders.of(this).get(HearingTestViewModel.class);
        root = inflater.inflate(R.layout.fragment_hearing_test, container, false);

        testForwardButton = (Button) root.findViewById(R.id.TestForwardButton);

        startHearingTestButton = (FloatingActionButton) root.findViewById(R.id.new_hearing_test_button);

        startHearingTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HTFragment", "startHearingTestButton is Pressed");
                Intent intent = new Intent(getActivity(), HearingTestActivity.class);
                startActivity(intent);
            }
        });

        testForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HTFragment", "testForwardButton is Pressed");
                switchFragment();
            }
        });

        return root;
    }

    private void switchFragment()
    {
        //FragmentTransaction fr = getFragmentManager().beginTransaction();
        //fr.replace(R.id.nav_host_fragment, new HearingTestResultFragment());
        //fr.commit();
        Navigation.findNavController(root).navigate(R.id.navigation_hearing_test_result);
    }


}