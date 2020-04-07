package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece493.group5.adjustableaudio.HearingTestActivity;
import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.adapters.TestResultListAdapter;
import com.ece493.group5.adjustableaudio.storage.HearingTestResultListController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The HearingTestFragment class helps implement the following requirements:
 *
 * #SRS: Performing a Hearing Test
 * #SRS: Viewable Hearing Test
 *
 * In particular, the HearingTestFragment has the user interface to take the hearing test and
 * displays a list of previous hearing test results.
 */

public class HearingTestFragment extends Fragment
{

    private static final String POSITION_TAG = "position";

    private TestResultListAdapter testResultListAdapter;
    private View root;

    private FloatingActionButton startHearingTestButton;
    private RecyclerView testResultRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_hearing_test, container, false);

        startHearingTestButton = (FloatingActionButton) root.findViewById(R.id.new_hearing_test_button);
        testResultRecyclerView = root.findViewById(R.id.hearing_test_result_recyclerview);
        testResultRecyclerView.setHasFixedSize(true);
        testResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        testResultRecyclerView.addItemDecoration(
                new DividerItemDecoration(testResultRecyclerView.getContext(),
                        DividerItemDecoration.VERTICAL));

        testResultListAdapter = new TestResultListAdapter();
        testResultListAdapter.setOnSelectedListener(new TestResultListAdapter.OnSelectedListener() {
            @Override
            public void onSelected(int position) {
                switchFragment(position);
            }
        });

        testResultRecyclerView.setAdapter(testResultListAdapter);
        testResultListAdapter.setResultList(HearingTestResultListController.getResultList(getActivity()));


        startHearingTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HearingTestActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume()
    {
        testResultListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void switchFragment(int testResultPosition)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_TAG, testResultPosition);
        Navigation.findNavController(root).navigate(R.id.navigation_hearing_test_result, bundle);
    }

}