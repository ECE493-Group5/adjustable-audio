package com.ece493.group5.adjustableaudio.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TestResultListAdapter extends RecyclerView.Adapter<TestResultListAdapter.ViewHolder>
{
    private List<HearingTestResult> resultList;
    private OnSelectedListener onSelectedListener;

    public interface OnSelectedListener
    {
        void onSelected(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public View parent;
        public TextView testName;
        public TextView testDate;

        public ViewHolder(View view)
        {
            super(view);

            parent = view;
            testName = view.findViewById(R.id.testResultName);
            testDate = view.findViewById(R.id.testResultDate);
        }

    }

    public TestResultListAdapter()
    {
        this.resultList = new ArrayList<>();
    }

    public void setResultList(List<HearingTestResult> resultList)
    {
        if (this.resultList != resultList)
        {
            this.resultList = resultList;
            notifyDataSetChanged();
        }
    }

    @Override
    public TestResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.view_testresult_data, parent, false);

        TestResultListAdapter.ViewHolder viewHolder = new TestResultListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TestResultListAdapter.ViewHolder holder, final int position)
    {

        holder.parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSelectedListener.onSelected(position);
            }
        });

        HearingTestResult testResult = resultList.get(position);

        holder.testName.setText(testResult.getTestName());
        holder.testDate.setText(testResult.getTestDate().toString());
    }

    @Override
    public int getItemCount()
    {
        return resultList.size();
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener)
    {
        this.onSelectedListener = onSelectedListener;
    }

}
