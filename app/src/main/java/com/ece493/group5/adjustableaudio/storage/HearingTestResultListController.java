package com.ece493.group5.adjustableaudio.storage;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.util.ArrayList;

public class HearingTestResultListController {

    private static ArrayList<HearingTestResult> resultList = null;

    static public ArrayList<HearingTestResult> getResultList() {
        if (resultList == null) {
            resultList = Saver.loadResults();
        }
        return resultList ;
    }

    public static void add(HearingTestResult preset)
    {
        getResultList().add(preset);
    }
}
