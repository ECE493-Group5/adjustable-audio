package com.ece493.group5.adjustableaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ece493.group5.adjustableaudio.models.HearingTestModel;
import com.ece493.group5.adjustableaudio.views.HearingTestView;

public class HearingTestActivity extends AppCompatActivity {

    private HearingTestView mView;
    private HearingTestModel mModel;
    private HearingTestController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new HearingTestController(this);
        mView = (HearingTestView) View.inflate(this, R.layout.activity_hearing_test, null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mView.setController(mController);
        mModel = new HearingTestModel(this);
        mModel.addObserver(mView);
        setContentView(mView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            if (mModel.getTestState())
            {
                mView.onCancelTest();
            }
            else
            {
                endActivity();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


        public void onStartTest()
    {
        this.mModel.runTest();
    }

    public void onSoundAck(boolean heard)
    {
        this.mModel.onSoundAck(heard);
    }

    public void endActivity()
    {
        finish();
    }

    public class HearingTestController{

        private HearingTestActivity mActivity;

        private HearingTestController(HearingTestActivity activity)
        {
            this.mActivity = activity;
        }

        public void onStartTest()
        {
            this.mActivity.onStartTest();
        }

        public void onSoundAck(boolean heard)
        {
            this.mActivity.onSoundAck(heard);
        }

        public void endActivity()
        {
            this.mActivity.endActivity();
        }
    }
}


