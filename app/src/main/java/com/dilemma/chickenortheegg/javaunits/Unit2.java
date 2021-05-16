package com.dilemma.chickenortheegg.javaunits;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.R;

import java.util.Random;

public class Unit2 extends AppCompatActivity {

    String THREAD_TAG = "AndroidRuntime";
    private TextView tv_philosopher_points_1, tv_philosopher_points_2;
    private TextView tv_status_philosopher_1, tv_status_philosopher_2;
    private LinearLayout ll_status_philosopher_1, ll_status_philosopher_2;
    private ProgressBar pb_philosopher_1, pb_philosopher_2;

    PhilosopherView leftPhilosopherView;
    PhilosopherView rightPhilosopherView;
    Handler h;
    Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit1);
        getSupportActionBar().hide();
        init();

        //runOnUiThread();

        /*h = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg) {
                // обновляем TextView
                tv_philosopher_points_1.setText("Закачано файлов: " + msg.what);
                //if (msg.what == 10) btnStart.setEnabled(true);
            };
        };
        h.sendEmptyMessage(2);*/


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void init() {
        tv_philosopher_points_1 = findViewById(R.id.tv_philosopher_points_1);
        tv_philosopher_points_2 = findViewById(R.id.tv_philosopher_points_2);
        tv_status_philosopher_1 = findViewById(R.id.tv_status_philosopher_1);
        tv_status_philosopher_2 = findViewById(R.id.tv_status_philosopher_2);
        ll_status_philosopher_1 = findViewById(R.id.ll_status_philosopher_1);
        ll_status_philosopher_2 = findViewById(R.id.ll_status_philosopher_2);
        pb_philosopher_1 = findViewById(R.id.pb_philosopher_1);
        pb_philosopher_2 = findViewById(R.id.pb_philosopher_2);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftPhilosopherView = new PhilosopherView(tv_philosopher_points_1, tv_status_philosopher_1, ll_status_philosopher_1, pb_philosopher_1, "Левый", "Яйцо");
                rightPhilosopherView = new PhilosopherView(tv_philosopher_points_2, tv_status_philosopher_2, ll_status_philosopher_2, pb_philosopher_2, "Правый", "Курица");
                test(leftPhilosopherView, rightPhilosopherView);
            }
        });
    }


    class Debater implements Runnable {
        Thread thread;
        String position;
        Debater opponent;

        Debater(String position) {
            this.position = position;
            thread = new Thread(this, position);
            thread.start();
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    // Приостанавливаем поток
                    int sleep = getTimeSleep();
                    Thread.sleep(sleep);
                    Log.d(THREAD_TAG, position + " " + sleep);
                } catch (InterruptedException e) {
                }
            }
            /*if(!opponent.isAlive()){
                Log.d(THREAD_TAG, "Первым появилось "+position+" !!!" );
            }*/
        }
    }


    public static int getTimeSleep() {
        final Random random = new Random();
        int tm = random.nextInt(1900) + 100;
        return tm;
    }


    private void test(PhilosopherView leftPhilosopherView, PhilosopherView rightPhilosopherView) {
        Long time = System.currentTimeMillis();
        Log.d(THREAD_TAG, "Старт симуляции ");
        // Debater egg = new Debater("Яйцо");
        // Debater chiken = new Debater("Курица");
       /* egg.position = "Яйцо";
        chiken.position =  "Курица";*/

        //egg.opponent = chiken;
        //chiken.opponent =  egg;
        /*chiken.start();
        egg.start();*/
        PhilosopherWorker leftPhilosopherWork = new PhilosopherWorker();
        PhilosopherWorker rightPhilosopherWork = new PhilosopherWorker();

        Thread t = new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i < 5; i++) {
                    if (i % 2 == 0) {
                        leftPhilosopherView.thinking();
                        leftPhilosopherWork.thinkingSimulation();
                        leftPhilosopherView.speech();
                        leftPhilosopherWork.speechSimulation();
                        leftPhilosopherView.waiting();
                    } else {
                        {rightPhilosopherView.thinking();
                        rightPhilosopherWork.thinkingSimulation();
                        rightPhilosopherView.speech();
                        rightPhilosopherWork.speechSimulation();
                        rightPhilosopherView.waiting();} // то же самое для философа справа
                    }
                }
            }
        });
        t.start();




        Log.d(THREAD_TAG, "Конец симуляции (" + (System.currentTimeMillis() - time) + " ms )");
    }


}
