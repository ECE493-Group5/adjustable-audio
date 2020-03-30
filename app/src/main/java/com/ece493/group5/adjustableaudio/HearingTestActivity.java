package com.ece493.group5.adjustableaudio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ece493.group5.adjustableaudio.models.HearingTestModel;
import com.ece493.group5.adjustableaudio.views.HearingTestView;

public class HearingTestActivity extends AppCompatActivity {

    private HearingTestView mView;
    private HearingTestModel mModel;
    private HearingTestController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mController = new HearingTestController(this);
        mView = (HearingTestView) View.inflate(this, R.layout.activity_hearing_test, null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mView.setController(mController);
        mModel = new HearingTestModel(this);
        mModel.addObserver(mView);
        setContentView(mView);
        testInstructionsDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home )
        {
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

    @Override
    public void onBackPressed()
    {
        if (mModel.getTestState())
        {
            mView.onCancelTest();
        }
        else
        {
            endActivity();
        }
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

    public void testInstructionsDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.title_dialog_hearing_test_insructions);
        alertDialogBuilder.setMessage(R.string.dialog_msg_hearing_test_instructions );

        alertDialogBuilder.setPositiveButton(R.string.got_it,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });
        alertDialogBuilder.show();
    }

    public class HearingTestController
    {

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


