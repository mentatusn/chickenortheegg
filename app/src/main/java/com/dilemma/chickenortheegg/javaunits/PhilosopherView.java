package com.dilemma.chickenortheegg.javaunits;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.R;

public class PhilosopherView {
    String LOG_TAG = "PhilosopherView";
    TextView tv_philosopher_points;
    TextView tv_status_philosopher;
    LinearLayout ll_status_philosopher;
    ProgressBar pb_philosopher;

    String position;
    String beliefs;

    int answerCounter = 0;

    PhilosopherView(TextView tv_philosopher_points, TextView tv_status_philosopher, LinearLayout ll_status_philosopher,ProgressBar pb_philosopher,String position,String beliefs) {
        this.ll_status_philosopher = ll_status_philosopher;
        this.tv_philosopher_points = tv_philosopher_points;
        this.tv_status_philosopher = tv_status_philosopher;
        this.pb_philosopher =pb_philosopher;
        this.position =position;
        this.beliefs =beliefs;
    }



    void thinking() {
        ll_status_philosopher.setBackgroundResource(R.color.colorPrimary);
        pb_philosopher.setVisibility(View.VISIBLE);
        tv_status_philosopher.setText("Думает");
        Log.d(LOG_TAG,position+" философ думает");
    }

    void speech() {
        ll_status_philosopher.setBackgroundResource(R.color.colorPrimary);
        pb_philosopher.setVisibility(View.INVISIBLE);
        tv_status_philosopher.setText("Говорит");
        tv_philosopher_points.setText((++answerCounter)+"");
        Log.d(LOG_TAG,position+" философ говорит -"+beliefs+"!");
    }

    void waiting() {
        ll_status_philosopher.setBackgroundResource(R.color.colorWhiteMaterial);
        pb_philosopher.setVisibility(View.INVISIBLE);
        tv_status_philosopher.setText("Ждет");
        Log.d(LOG_TAG,position+" философ ждет");
    }

    void bloking() {
        ll_status_philosopher.setBackgroundResource(R.color.colorRedMaterial);
        pb_philosopher.setVisibility(View.INVISIBLE);
        tv_status_philosopher.setText("Заблокирован");
        Log.d(LOG_TAG,position+" философ Заблокирован");
    }

    void clean() {
        ll_status_philosopher.setBackgroundResource(R.color.colorWhiteMaterial);
        pb_philosopher.setVisibility(View.INVISIBLE);
        tv_status_philosopher.setText("");
        //answerCounter = 0;
        tv_philosopher_points.setText(answerCounter+"");
    }

}
