package com.dilemma.chickenortheegg.javaunits;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dilemma.chickenortheegg.Event;
import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.MyMessage;
import com.dilemma.chickenortheegg.Position;
import com.dilemma.chickenortheegg.ProducerConsumer;
import com.dilemma.chickenortheegg.R;

import java.util.Random;

public class Unit4 extends AppCompatActivity {

    String THREAD_TAG = "AndroidRuntime";
    private TextView tv_philosopher_points_1, tv_philosopher_points_2;
    private TextView tv_status_philosopher_1, tv_status_philosopher_2;
    private LinearLayout ll_status_philosopher_1, ll_status_philosopher_2;
    private ProgressBar pb_philosopher_1, pb_philosopher_2;

    PhilosopherView leftPhilosopherView;
    PhilosopherView rightPhilosopherView;
    Handler h;
    Button btn_start, btn_manual_looper;

    ProducerConsumer producerConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit4);
        getSupportActionBar().hide();
        producerConsumer = new ProducerConsumer();
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
        btn_manual_looper = findViewById(R.id.btn_manual_looper);
        btn_start = findViewById(R.id.btn_start);
        btn_manual_looper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMessage message = producerConsumer.consume();
                if (message != null)
                    processMessage(message);
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftPhilosopherView = new PhilosopherView(tv_philosopher_points_1, tv_status_philosopher_1, ll_status_philosopher_1, pb_philosopher_1, "Левый", "Яйцо");
                rightPhilosopherView = new PhilosopherView(tv_philosopher_points_2, tv_status_philosopher_2, ll_status_philosopher_2, pb_philosopher_2, "Правый", "Курица");
                test(leftPhilosopherView, rightPhilosopherView);
            }
        });

    }

    private void processMessage(MyMessage message) {
        PhilosopherView currentPhilosoph = (message.position.equals(Position.RIGHT)) ? (rightPhilosopherView) : (leftPhilosopherView);
        switch (message.event) {
            case THINKING:
                currentPhilosoph.thinking();
                break;
            case WAITING:
                currentPhilosoph.waiting();
                break;
            case SPEECH:
                currentPhilosoph.speech();
                break;
            case CLEAN:
                currentPhilosoph.clean();
                break;
        }
    }

    class PhilosopherWorker implements Runnable {
        Position position;//

        PhilosopherWorker(Position position) {
            this.position = position;
            new Thread(this, position.toString()).start();
        }

        @Override
        public void run() {
            Log.d("ProducerConsumer", "Поток " + Thread.currentThread().getName() + " начал работу");
            producerConsumer.produce(new MyMessage(position, Event.CLEAN));
            for (int i = 0; i < 5; i++) {
                producerConsumer.produce(new MyMessage(position, Event.THINKING));
                thinkingSimulation();
                producerConsumer.produce(new MyMessage(position, Event.SPEECH));
                speechSimulation();
                producerConsumer.produce(new MyMessage(position, Event.WAITING));
            }
            Log.d("ProducerConsumer", "Поток " + Thread.currentThread().getName() + " завершил работу");
        }

        public int getTimeSleep() {
            final Random random = new Random();
            int tm = random.nextInt(1500) + 1500;
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


    public static int getTimeSleep() {
        final Random random = new Random();
        int tm = random.nextInt(1900) + 100;
        return tm;
    }


    private void test(PhilosopherView leftPhilosopherView, PhilosopherView rightPhilosopherView) {
        Long time = System.currentTimeMillis();
        //Log.d(THREAD_TAG, "Старт симуляции ");
        // старт симуляции
        new PhilosopherWorker(Position.LEFT);
        new PhilosopherWorker(Position.RIGHT);

        //Log.d(THREAD_TAG, "Конец симуляции (" + (System.currentTimeMillis() - time) + " ms )");
    }

}
