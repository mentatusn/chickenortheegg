package com.dilemma.chickenortheegg.javaunits;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dilemma.chickenortheegg.Event;
import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.MyMessage;
import com.dilemma.chickenortheegg.Position;
import com.dilemma.chickenortheegg.ProducerConsumer;
import com.dilemma.chickenortheegg.R;

import java.util.Random;

public class Unit5 extends AppCompatActivity {

    String THREAD_TAG = "thread_tag";
    private TextView tv_philosopher_points_1, tv_philosopher_points_2;
    private TextView tv_status_philosopher_1, tv_status_philosopher_2;
    private LinearLayout ll_status_philosopher_1, ll_status_philosopher_2;
    private ProgressBar pb_philosopher_1, pb_philosopher_2;
    Button btn_start;
    Handler mHandler;
    PhilosopherView leftPhilosopherView;
    PhilosopherView rightPhilosopherView;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit5);
        getSupportActionBar().hide();

        init();

        mHandler = new Handler(new Handler.Callback() {// неявно привязываемся к текущему(UI) отоку
            public boolean handleMessage(Message msg) {
                Log.d("ProducerConsumer", "поток "+ Thread.currentThread().getName() + " выполняет задание из очереди");
                proceedMessage(msg);
                return false; //True if no further handling is desired
            }
        });
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
        Unit1.Debater opponent;

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


    public int getTimeSleep() {
        final Random random = new Random();
        int tm = random.nextInt(1500) + 1500;
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

        new PhilosopherWorker(Position.LEFT);
        new PhilosopherWorker(Position.RIGHT);

        Log.d(THREAD_TAG, "Конец симуляции (" + (System.currentTimeMillis() - time) + " ms )");
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

            postMessage(new MyMessage(position, Event.CLEAN));
            for (int i = 0; i < 5; i++) {
                postMessage(new MyMessage(position, Event.THINKING));
                thinkingSimulation();
                postMessage(new MyMessage(position, Event.SPEECH));
                speechSimulation();
                postMessage(new MyMessage(position, Event.WAITING));
            }
            Log.d("ProducerConsumer", "Поток " + Thread.currentThread().getName() + " завершил работу");
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




    private void proceedMessage(Message message) {
        MyMessage myMessage = (MyMessage) message.obj; // извлекаем MyMessage
        //дальше все по старому
        PhilosopherView currentPhilosoph = (myMessage.position.equals(Position.RIGHT)) ? (rightPhilosopherView) : (leftPhilosopherView);
        switch (myMessage.event) {
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
    private void postMessage(MyMessage myMessage) {
        Log.d("ProducerConsumer", "поток "+ Thread.currentThread().getName() + " поместил задание в очередь");
        Message message = new Message();
        message.obj = myMessage; // помещаем наш MyMessage внутрь Message
        mHandler.sendMessage(message); // отправляем сообщение в MessageQueue
    }
}
