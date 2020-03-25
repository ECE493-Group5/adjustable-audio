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
import android.os.Build;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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
import com.ece493.group5.adjustableaudio.BuildConfig;
import com.ece493.group5.adjustableaudio.R;
import com.ece493.group5.adjustableaudio.models.HearingTestResult;
import com.ece493.group5.adjustableaudio.models.ToneData;
import com.ece493.group5.adjustableaudio.storage.HearingTestResultListController;
import com.ece493.group5.adjustableaudio.storage.SaveController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

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

    static private final int[] PLOT_FREQUENCIES = {125, 250, 500, 1000, 2000, 4000, 8000, 16000};
    static private final Number[] DOMAIN_INDEX_ARRAY = {1, 2, 3, 4, 5, 6};
    static private final String AUDIOGRAM_PATH = "Audiogram_Images/";
    static private final String POSITION_TAG = "position";
    static private final int FREQUENCY_INDEX_MAX = 7;
    static private final int FREQUENCY_INDEX_MIN = 0;
    static private final int FREQUENCY_INDEX_INCR = 1;
    static private final int DBHL_MAX = 100;
    static private final int DBHL_MIN = -10;
    static private final int DBHL_INCR = 10;

    private HearingTestResultViewModel hearingTestResultViewModel;
    private HearingTestResult testResult;
    private View root;

    private TextView testResultNameText;
    private Button createEqPresetButton;
    private Button shareTestResultButton;
    private Button renameTestResultButton;
    private Button deleteTestResultButton;
    private XYPlot audioGramPlot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        hearingTestResultViewModel =
                ViewModelProviders.of(this).get(HearingTestResultViewModel.class);
        root = inflater.inflate(R.layout.fragment_hearing_test_result, container, false);
        int position = getArguments().getInt(POSITION_TAG);
        setHearingTestResult(HearingTestResultListController
                .getResultList(getActivity()).get(position));
        testResultNameText = (TextView) root.findViewById(R.id.hearing_test_result_textview);
        createEqPresetButton = (Button) root.findViewById(R.id.hearing_test_result_eq_preset_button);
        shareTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_share_button);
        renameTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_rename_button);
        deleteTestResultButton = (Button) root.findViewById(R.id.hearing_test_result_delete_button);
        audioGramPlot = (XYPlot) root.findViewById(R.id.AudiogramPlot);

        enableControls();
        testResultNameText.setText(testResult.getTestName());
        generateAudioGramImage();

        return root;
    }


    private void setHearingTestResult(HearingTestResult result)
    {
        testResult = result;
    }

    public boolean contains(final int[] array, final int key)
    {
        for (final int i : array)
        {
            if (i == key)
            {
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
                double resultDB = flipPlotRangeValue(data.getLHeardAtDB() - data.getdBHL());
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
                double resultDB = flipPlotRangeValue(data.getRHeardAtDB() - data.getdBHL());
                resultList.add(resultDB);
            }
        }
        return resultList;
    }

    private void generateAudioGramImage()
    {
        XYSeries leftEarSeries = new SimpleXYSeries(Arrays.asList(DOMAIN_INDEX_ARRAY),
                getLTestResultPlotArray(), "Left Ear");
        XYSeries rightEarSeries = new SimpleXYSeries(Arrays.asList(DOMAIN_INDEX_ARRAY),
                getRTestResultPlotArray(), "Right Ear");

        // create formatters to use for drawing a series using LineAndPointRenderer
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.RED, null, null);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(Color.BLUE, Color.BLUE, null, null);

        series1Format.getVertexPaint().setStrokeWidth(30f);
        series2Format.getVertexPaint().setStrokeWidth(25f);

        audioGramPlot.addSeries(leftEarSeries, series1Format);
        audioGramPlot.addSeries(rightEarSeries, series2Format);

        audioGramPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, FREQUENCY_INDEX_INCR);
        audioGramPlot.setDomainBoundaries(FREQUENCY_INDEX_MIN, FREQUENCY_INDEX_MAX, BoundaryMode.FIXED);
        audioGramPlot.setRangeBoundaries(DBHL_MIN, DBHL_MAX, BoundaryMode.FIXED);
        audioGramPlot.setRangeStep(StepMode.INCREMENT_BY_FIT, DBHL_INCR);

        audioGramPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.TOP).setFormat(new Format()
        {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
            {
                int i = Math.round(((Number) obj).floatValue());
                if (i == 7 | i == 0){
                    return toAppendTo.append("");
                }
                return toAppendTo.append(PLOT_FREQUENCIES[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos)
            {
                return null;
            }
        });

        audioGramPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new Format()
        {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
            {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(flipPlotRangeValue(i));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos)
            {
                return null;
            }
        });
    }

    private int flipPlotRangeValue(int value)
    {
        return ((value * -1) + 90);
    }

    private double flipPlotRangeValue(double value)
    {
        return ((value * -1) + 90);
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
        audioGramPlot.measure(audioGramPlot.getWidth(), audioGramPlot.getHeight());
        Bitmap audiogramBitmap = Bitmap.createBitmap(audioGramPlot.getDrawingCache());
        audioGramPlot.setDrawingCacheEnabled(false);
        return audiogramBitmap;
    }

    private Uri createUriFromBitmap(Bitmap bmp)
    {
        try {
            File audiogramDir = new File(getActivity().getExternalFilesDir(null),
                    AUDIOGRAM_PATH);
            if (!audiogramDir.exists())
            {
                audiogramDir.mkdir();
            }
            File file = new File(audiogramDir, "shared_audiogram_image.jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            if (Build.VERSION.SDK_INT < 24)
            {
                return Uri.fromFile(file);
            }
            else
            {
                return FileProvider.getUriForFile(getActivity(),
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        file);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteResult()
    {
        requestDeleteDialog();
    }

    private void requestDeleteDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle(R.string.title_dialog_delete_test_result);
        alertDialogBuilder.setMessage(R.string.dialog_msg_delete_test_result);

        alertDialogBuilder.setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                deleteTestResult();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.negative_button_dialog, new DialogInterface.OnClickListener()
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
        alertDialogBuilder.setTitle(R.string.title_dialog_rename_test_result);
        alertDialogBuilder.setMessage(R.string.dialog_msg_rename_test_result);

        final EditText newName = new EditText(this.getContext());
        alertDialogBuilder.setView(newName);

        alertDialogBuilder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                setTestName(newName.getText().toString());
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.negative_button_dialog, new DialogInterface.OnClickListener()
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
            testResultNameText.setText(testResult.getTestName());
        }
    }

    private void generateEqualizerPreset()
    {
        //TODO implement
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