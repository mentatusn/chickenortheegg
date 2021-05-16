package com.dilemma.chickenortheegg.javaunits;

import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dilemma.chickenortheegg.Event;
import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.R;

import java.util.Random;

public class Unit3 extends AppCompatActivity implements CallbackPhilosopherView {

    String THREAD_TAG = "AndroidRuntime";
    private TextView tv_philosopher_points_1, tv_philosopher_points_2;
    private TextView tv_status_philosopher_1, tv_status_philosopher_2;
    private LinearLayout ll_status_philosopher_1, ll_status_philosopher_2;
    private ProgressBar pb_philosopher_1, pb_philosopher_2;

    PhilosopherView leftPhilosopherView;
    PhilosopherView rightPhilosopherView;
    Handler h;
    Button btn_start;
    public static Unit3 activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
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
        Log.d(THREAD_TAG, "Старт симуляции в потоке " + Thread.currentThread().getName() + " ");
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

            }
        });
        t.start();
        Log.d(THREAD_TAG, "Регистрируем функцию обратного вызова в потоке " + Thread.currentThread().getName() + " ");
        callbackPhilosopherView = activity;

        new MyThread();

        //Log.d(THREAD_TAG, "Конец симуляции (" + (System.currentTimeMillis() - time) + " ms )");
    }
    CallbackPhilosopherView callbackPhilosopherView;
    class MyThread implements Runnable
    {
        Thread thread;
        MyThread() {
            String threadName = "Дополнительный поток";
            Log.d(THREAD_TAG, "Создали поток "+threadName+ " из потока " + Thread.currentThread().getName() + " ");
            thread = new Thread(this,threadName );
            thread.start();
        }
        @Override
        public void run() {
            Log.d(THREAD_TAG, "Выполняем run() в потоке " + Thread.currentThread().getName());
            Log.d(THREAD_TAG, "Происходит событие в потоке " + Thread.currentThread().getName());
            //callbackPhilosopherView.callingBackPhilosopherView(Event.THINKING);
        }
    }

    @Override
    public void callingBackPhilosopherView(Event event) {
        Log.d(THREAD_TAG, "Работаем в потоке " + Thread.currentThread().getName() + " ");
        switch (event) {
            case THINKING:
                leftPhilosopherView.thinking();
                break;
            case WAITING:
                leftPhilosopherView.waiting();
                break;
        }
    }
        /*leftPhilosopherView.thinking();
        leftPhilosopherView.speech();
        leftPhilosopherView.waiting();*/
}
