package com.dilemma.chickenortheegg.javaunits;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.R;

import java.util.Random;

public class PhilosopherWorker {
    public static int getTimeSleep() {
        final Random random = new Random();
        int tm = random.nextInt(1900) + 100;
        return tm;
    }

    public void thinkingSimulation() {
        try {
            int sleep = getTimeSleep();
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
        }
    }

    public void speechSimulation() {
        try {
            int sleep = getTimeSleep();
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
        }
    }

}
