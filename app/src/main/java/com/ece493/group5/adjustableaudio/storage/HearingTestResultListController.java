package com.ece493.group5.adjustableaudio.storage;

import com.ece493.group5.adjustableaudio.models.HearingTestResult;

import java.util.ArrayList;

public class HearingTestResultListController {
    private static ArrayList<HearingTestResult> resultList = null;

    static public ArrayList<HearingTestResult> getEmotionList() {
        if (resultList == null) {
            resultList = Saver.loadResults();
        }
        return resultList ;
    }
}
