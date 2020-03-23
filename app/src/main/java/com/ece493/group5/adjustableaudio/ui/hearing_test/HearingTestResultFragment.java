package com.ece493.group5.adjustableaudio.ui.hearing_test;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.icu.text.DecimalFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.androidplot.ui.Size;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.HearingTestResultListController;
import com.ece493.group5.adjustableaudio.storage.SaveController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HearingTestResultFragment extends Fragment {

    static private final int[] PLOT_FREQUENCIES = {250, 500, 1000, 2000, 4000, 8000};

    private HearingTestResultViewModel hearingTestResultViewModel;
    private HearingTestResult testResult;
    private View root;

    private Button createEqPresetButton;
    private Button shareTestResultButton;
    private Button renameTestResultButton;
    private Button deleteTestResultButton;
    private XYPlot audioGramPlot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hearingTestResultViewModel =
                ViewModelProviders.of(this).get(HearingTestResultViewModel.class);
        root = inflater.inflate(R.layout.fragment_hearing_test_result, container, false);
        int position = getArguments().getInt("position");
        setHearingTestResult(HearingTestResultListController
                .getResultList(getActivity()).get(position));
        createEqPresetButton = (Button) root.findViewById(R.id.hearing_test_result_eq_preset_button);
        shareTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_share_button);
        renameTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_rename_button);
        deleteTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_delete_button);
        audioGramPlot = (XYPlot) root.findViewById(R.id.AudiogramPlot);
        enableControls();
        generateAudioGramImage();

        return root;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home ) {
//            switchFragment();
//            return true;
//        }
//        // other menu select events may be present here
//        return super.onOptionsItemSelected(item);
//    }

    private void setHearingTestResult(HearingTestResult result)
    {
        testResult = result;
    }

    private HearingTestResult getHearingTestResult()
    {
        //TODO is this needed?
        return testResult;
    }

    public boolean contains(final int[] array, final int key) {
        for (final int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Double> getLTestResultPlotArray()
    {
        ArrayList<ToneData> toneList = testResult.getTestResults();
        ArrayList<Double> resultList = new ArrayList<Double>();
        for (ToneData data : toneList)
        {
            if (contains(PLOT_FREQUENCIES, data.getFrequency()))
            {
                double resultDB = ((data.getLHeardAtDB() - data.getdBHL()) * -1) + 90;
                resultList.add(resultDB);
            }
        }
        return resultList;
    }

    private ArrayList<Double> getRTestResultPlotArray()
    {
        ArrayList<ToneData> toneList = testResult.getTestResults();
        ArrayList<Double> resultList = new ArrayList<Double>();
        for (ToneData data : toneList)
        {
            if (contains(PLOT_FREQUENCIES, data.getFrequency()))
            {
                double resultDB = ((data.getRHeardAtDB() - data.getdBHL()) * -1) + 90;
                resultList.add(resultDB);
            }
        }
        return resultList;
    }

    private void generateAudioGramImage()
    {

        final Number[] domainLabels = {125, 250, 500, 1000, 2000, 4000, 8000, 16000};
        Number[] seriesXvals = {1,2,3,4,5,6};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(seriesXvals),
                getLTestResultPlotArray(), "Left Ear");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(seriesXvals),
                getRTestResultPlotArray(), "Right Ear");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.RED, null, null);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(Color.BLUE, Color.BLUE, null, null);

        series1Format.getVertexPaint().setStrokeWidth(30f);
        series2Format.getVertexPaint().setStrokeWidth(25f);


        // add a new series' to the xyplot:
        audioGramPlot.addSeries(series1, series1Format);
        audioGramPlot.addSeries(series2, series2Format);



        //audioGramPlot.setDomainBoundaries(250, 8000, BoundaryMode.FIXED);
        audioGramPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        audioGramPlot.setDomainBoundaries(0, 7, BoundaryMode.FIXED);
        audioGramPlot.setRangeBoundaries(-10, 100, BoundaryMode.FIXED);
        //audioGramPlot.setRangeValueFormat(new DecimalFormat("0"));
        audioGramPlot.setRangeStep(StepMode.INCREMENT_BY_FIT, 10.0);


        audioGramPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.TOP).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                if (i == 7 | i == 0){
                    return toAppendTo.append("");
                }
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

        audioGramPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append((i*-1) + 90);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }

    private void shareTestResult()
    {
        Bitmap audiogramBitmap = createAudioGramBitmap();
        Uri audiogramUri = createUriFromBitmap(audiogramBitmap);
        Intent sendIntent =new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setType("image/jpeg");
        sendIntent.putExtra(Intent.EXTRA_STREAM, audiogramUri);
        startActivity(Intent.createChooser(sendIntent,"Share via"));
    }

    private Bitmap createAudioGramBitmap()
    {
        audioGramPlot.setDrawingCacheEnabled(true);
        //int width = audioGramPlot.getWidth();
        //int height = audioGramPlot.getHeight();
        audioGramPlot.measure(audioGramPlot.getWidth(), audioGramPlot.getHeight());
        Bitmap audiogramBitmap = Bitmap.createBitmap(audioGramPlot.getDrawingCache());
        audioGramPlot.setDrawingCacheEnabled(false);
        return audiogramBitmap;
    }

    private Uri createUriFromBitmap(Bitmap bmp)
    {
        try {
            File tmpfile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "tmp_shared_audiogram_image.png");
            FileOutputStream out = new FileOutputStream(tmpfile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Uri audiogramUri = Uri.fromFile(tmpfile);
            return audiogramUri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



//    private void shareTestResult()
//    {
//        Resources resources = getResources();
//
//        Intent emailIntent = new Intent();
//        emailIntent.setAction(Intent.ACTION_SEND);
//        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Share!");
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Shareshareshare!");
//        emailIntent.setType("message/rfc822");
//
//        PackageManager pm = getActivity().getPackageManager();
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.setType("text/plain");
//
//
//        Intent openInChooser = Intent.createChooser(emailIntent, "i like to share");
//
////        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
////        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
////        for (int i = 0; i < resInfo.size(); i++) {
////            // Extract the label, append it, and repackage it in a LabeledIntent
////            ResolveInfo ri = resInfo.get(i);
////            String packageName = ri.activityInfo.packageName;
////            if(packageName.contains("android.email")) {
////                emailIntent.setPackage(packageName);
////            } else if(packageName.contains("mms") || packageName.contains("messaging") ) {
////                Intent intent = new Intent();
////                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
////                intent.setAction(Intent.ACTION_
////                SEND);
////                intent.setType("text/plain");
////                intent.putExtra(Intent.EXTRA_TEXT, "share with text!");
////                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
////            }
////        }
////
////        // convert intentList to array
////        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);
////
////        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
//        startActivity(openInChooser);
//    }

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
            SaveController.deleteResult(getActivity(), testResult);
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