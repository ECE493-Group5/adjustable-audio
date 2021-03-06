package com.ece493.group5.adjustableaudio.storage;

import android.content.Context;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.util.ArrayList;

/**
 * The HearingTestResultListController helps implement the following requirement:
 *
 * #SRS: Viewable Hearing Test Result
 */

public class HearingTestResultListController
{

    private static ArrayList<HearingTestResult> resultList = null;

    static public ArrayList<HearingTestResult> getResultList(Context context) {
        if (resultList == null)
        {
            resultList = SaveController.loadResults(context);
            if (resultList == null)
            {
                resultList = new ArrayList<HearingTestResult>();
            }
        }
        return resultList ;
    }

    public static void add(Context context, HearingTestResult result)
    {
        getResultList(context).add(result);
    }

    public static void remove(Context context, HearingTestResult result)
    {
        getResultList(context).remove(result);
    }

    public static void removeAtPosition(Context context, int position)
    {
        getResultList(context).remove(position);
    }

}
