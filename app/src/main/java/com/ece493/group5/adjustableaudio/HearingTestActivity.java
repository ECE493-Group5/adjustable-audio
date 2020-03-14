package com.ece493.group5.adjustableaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
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
        mView.setController(mController);
        mModel = new HearingTestModel(this);
        mModel.addObserver(mView);
        setContentView(mView);
    }


    public void onStartTest()
    {
        this.mModel.runTest();
    }

    public void onSoundAck(boolean heard)
    {
        this.mModel.onSoundAck(heard);
    }

    public void onCancelTest()
    {
        // TODO implement
    }


    public class HearingTestController{

        private HearingTestActivity mActivity;

        public HearingTestController(HearingTestActivity activity)
        {
            super();
        }

        public void onStartTest()
        {
            this.mActivity.onStartTest();
        }

        public void onSoundAck(boolean heard)
        {
            this.mActivity.onSoundAck(heard);
        }

        public void onCancelTest()
        {
            this.mActivity.onCancelTest();
        }
    }
}


