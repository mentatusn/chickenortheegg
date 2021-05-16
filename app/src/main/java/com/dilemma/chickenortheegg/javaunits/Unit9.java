package com.dilemma.chickenortheegg.javaunits;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
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
import com.dilemma.chickenortheegg.R;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Unit9 extends AppCompatActivity {
    String THREAD_TAG = "thread_tag";
    private TextView tv_philosopher_points_1, tv_philosopher_points_2;
    private TextView tv_status_philosopher_1, tv_status_philosopher_2;
    private TextView tv_timer;
    private LinearLayout ll_status_philosopher_1, ll_status_philosopher_2;
    private ProgressBar pb_philosopher_1, pb_philosopher_2;
    Button btn_start;
    Handler mHandler = new Handler();
    PhilosopherView leftPhilosopherView;
    PhilosopherView rightPhilosopherView;
    PhilosopherWorker left;
    PhilosopherWorker right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit9);
        getSupportActionBar().hide();
        left = new PhilosopherWorker(Position.LEFT);
        right = new PhilosopherWorker(Position.RIGHT);
        mHandler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                Log.d("ProducerConsumer", "handleMessage " + Thread.currentThread().getName() + " завершил работу");
                proceedMessage(msg);
                return false; //True if no further handling is desired
            }
        });
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
        MessageQueue queue = Looper.myQueue();
        queue.addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.d(THREAD_TAG, "queueIdle ");
                return false;
            }
        });

        tv_philosopher_points_1 = findViewById(R.id.tv_philosopher_points_1);
        tv_philosopher_points_2 = findViewById(R.id.tv_philosopher_points_2);
        tv_status_philosopher_1 = findViewById(R.id.tv_status_philosopher_1);
        tv_status_philosopher_2 = findViewById(R.id.tv_status_philosopher_2);
        ll_status_philosopher_1 = findViewById(R.id.ll_status_philosopher_1);
        ll_status_philosopher_2 = findViewById(R.id.ll_status_philosopher_2);
        pb_philosopher_1 = findViewById(R.id.pb_philosopher_1);
        pb_philosopher_2 = findViewById(R.id.pb_philosopher_2);
        tv_timer = findViewById(R.id.tv_timer);
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


    public int getTimeSleep() {
        final Random random = new Random();
        int tm = random.nextInt(500) + 500;
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

        new DirectorWorker();

        Log.d(THREAD_TAG, "Конец симуляции (" + (System.currentTimeMillis() - time) + " ms )");
    }


    class PhilosopherWorker implements Runnable {
        Position position;//
        Handler mHandlerInner;
        boolean busyness = false;
        Thread thread;

        PhilosopherWorker(Position position) {
            this.position = position;
            thread = new Thread(this, position.toString());
            thread.start();
        }

        @Override
        public void run() {

            mHandlerInner = new Handler(new Handler.Callback() {
                public boolean handleMessage(Message msg) {
                    busyness = true;

                    busyness = false;
                    return false;
                }
            });

            Looper.loop();
        }


    }


    class MyRunnable implements Runnable {
        MyMessage message;
        boolean isRunning = true;

        MyRunnable(MyMessage message) {
            this.message = message;
        }

        @Override
        public void run() {
            isRunning = true;
            processMessage(message);
            isRunning = false;
        }

        private void processMessage(MyMessage message) {

            switch (message.event) {
                case CLEAN:

                    break;
                case THINKING:
                    thinkingSimulation();
                    break;
                case SPEECH:
                    speechSimulation();
                    break;
            }
        }

        public void thinkingSimulation() {
            try {

                int sleep = getTimeSleep();
                for (int i = 0; i < 10; i++) {
                    Log.d("BORODA", "thinkingSimulation() " + isRunning);
                    Thread.sleep(sleep);
                }

            } catch (InterruptedException e) {
            }
        }


        public void speechSimulation() {
            try {
                int sleep = getTimeSleep();
                for (int i = 0; i < 10; i++) {
                    Log.d("BORODA", "speechSimulation() " + isRunning);
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    class DirectorWorker implements Runnable {

        DirectorWorker() {
            new Thread(this, "Director").start();
        }

        MyMessage leftMyMessage;
        MyMessage rightMyMessage;
        MyRunnable leftMyRunnable;
        MyRunnable rightMyRunnable;

        @Override
        public void run() {
            Queue<MyMessage> bufferLeft = new LinkedList<>();
            Queue<MyMessage> bufferRight = new LinkedList<>();
            for (int i = 0; i < 5; i++)
                for (Event event : Event.values()) {
                    bufferLeft.add(new MyMessage(left.position, event));
                    bufferRight.add(new MyMessage(right.position, event));
                }
            MyMessage idleLeftMyMessage = new MyMessage(left.position, Event.WAITING);
            MyMessage idleRightMyMessage = new MyMessage(right.position, Event.WAITING);
            leftMyMessage = bufferLeft.element();
            rightMyMessage = bufferRight.element();
            leftMyRunnable = new MyRunnable(leftMyMessage);
            rightMyRunnable = new MyRunnable(rightMyMessage);
            rightMyRunnable.isRunning = false;
            leftMyRunnable.isRunning = false;
            while (bufferLeft.size() > 0 || bufferRight.size() > 0) {
                if (bufferLeft.size() > 0 && (!leftMyRunnable.isRunning)) {
                    if (!(bufferLeft.element().event.equals(Event.SPEECH) && rightMyMessage.event.equals(Event.SPEECH))) {
                        leftMyMessage = bufferLeft.remove();
                        leftMyRunnable = new MyRunnable(leftMyMessage);
                        postMessageToPhilosopher(left.mHandlerInner, leftMyRunnable);

                        postMessageToUI(leftMyMessage);
                        setCountDownTimer(left);
                    } else {
                        postMessageToUI(idleLeftMyMessage);
                    }
                }


                if (bufferRight.size() > 0 && !rightMyRunnable.isRunning) {

                    if (!(bufferRight.element().event.equals(Event.SPEECH) && leftMyMessage.event.equals(Event.SPEECH))) {
                        rightMyMessage = bufferRight.remove();
                        rightMyRunnable = new MyRunnable(rightMyMessage);

                        postMessageToPhilosopher(right.mHandlerInner, rightMyRunnable);
                        postMessageToUI(rightMyMessage);
                        setCountDownTimer(right);
                    } else {
                        postMessageToUI(idleRightMyMessage);
                    }
                }


            }
            /*right.mHandlerInner.post(new Runnable() {
                @Override
                public void run() {
                    processMessage(new MyMessage(right.position, Event.THINKING));
                    thinkingSimulation();
                }
            });*/

        }

        CountDownTimer timer;

        private void setCountDownTimer(PhilosopherWorker philosopherWorker) {
            MyRunnable myRunnable = (philosopherWorker.position.equals(Position.RIGHT)) ? (rightMyRunnable) : (leftMyRunnable);
            //Log.d("BORODA", "onTick33 " + (philosopherWorker.mHandlerInner.hasMessages(myMessage.event.ordinal())));

            if (myRunnable.message.event.equals(Event.SPEECH)) {

                //Log.d("BORODA", "reg" + Event.SPEECH);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("onTick", "setCountDownTimer " + myRunnable.message.event.equals(Event.SPEECH));
                        timer = new CountDownTimer(5000, 200) {

                            public void onTick(long millisUntilFinished) {
                                Log.d("onTick", "onTick " + millisUntilFinished);
                               /* Log.d("BORODA", "onTick1" + Event.SPEECH);
                                Log.d("BORODA", "onTick2 " + philosopherWorker.mHandlerInner.hasMessages(Event.THINKING.ordinal()));
                                Log.d("BORODA", "onTick3 " + philosopherWorker.mHandlerInner.hasMessages(Event.SPEECH.ordinal()));
                                Log.d("BORODA", "onTick4 " + philosopherWorker.mHandlerInner.hasMessages(Event.WAITING.ordinal()));
                                Log.d("BORODA", "onTick  ") ;*/
                                // if(myRunnable.message.event.equals(Event.SPEECH))
                                tv_timer.setText("Осталось секунд: " + millisUntilFinished / 1000);

                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {

                                tv_timer.setText("Время вышло ");
                                if (philosopherWorker.position.equals(Position.RIGHT)) {
                                    right.mHandlerInner.getLooper().quit();
                                    right = new PhilosopherWorker(Position.RIGHT);
                                } else {
                                    left.mHandlerInner.getLooper().quit();
                                    left = new PhilosopherWorker(Position.LEFT);
                                }

                            }

                        };
                        timer.start();
                    }
                });


            } else {

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(timer!=null){
                            timer.cancel();
                            //tv_timer.setText("");
                        }
                    }
                });*/

            }
        }
    }


    private void proceedMessage(Message message) {
        MyMessage myMessage = (MyMessage) message.obj;
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

    private void postMessageToUI(MyMessage myMessage) {
        Log.d("ProducerConsumer", "myMessage.event " + myMessage.event);
        Message message = new Message();
        message.obj = myMessage;
        mHandler.sendMessage(message);
    }

    private void postMessageToPhilosopher(Handler handler, MyRunnable myRunnable) {
        //Log.d("ProducerConsumer", "myMessage.event " + event);
        //handler.sendEmptyMessage(event.ordinal());
        handler.post(myRunnable);
    }
}