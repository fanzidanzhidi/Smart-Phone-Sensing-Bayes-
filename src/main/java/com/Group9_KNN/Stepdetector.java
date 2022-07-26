package com.Group9_KNN;

import android.util.Log;

/**
 * Computing and processing accelerometer data.
 */
public class Stepdetector {

    private static final String TAG = Stepdetector.class.getSimpleName();

    private static final int MAX_STEPS_COUNT = 10000;

    /**
     * Step detecting parameter. For how many samples it is sleeping.
     * If accelerometer's DELAY_GAME is T ~= 20ms, this means that f = 50Hz and MAX_TEMPO = 240bpms
     * 60bpm  - 1000ms
     * 240bpm - 250ms
     * n is samples
     * n = 250ms / T
     * n = 250 / 20 ~= 12
     */
    private static final int INACTIVE_SAMPLE = 12;

    private int currentSample = 0;

    private int stepCount = 0;

    private boolean isActiveCounter;

    public Stepdetector() {
        isActiveCounter = true;
    }


    public boolean detect(double accelerometerValue,  double currentThreshold) {
        if (currentSample == INACTIVE_SAMPLE) {
            currentSample = 0;
            if (!isActiveCounter)
                isActiveCounter = true;
        }
        if (isActiveCounter && (accelerometerValue > currentThreshold)) {
            currentSample = 0;
            isActiveCounter = false;
            Log.d(TAG, "detect() true for threshold " + currentThreshold);
            stepCount++;
            if (stepCount == MAX_STEPS_COUNT)
                stepCount = 0;
            return true;
        }

        ++currentSample;
        return false;
    }

    public int getStepCount() {
        return stepCount;
    }
}
