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

import androidx.appcompat.app.AppCompatActivity;

import com.dilemma.chickenortheegg.Event;
import com.dilemma.chickenortheegg.Log;
import com.dilemma.chickenortheegg.MyMessage;
import com.dilemma.chickenortheegg.Position;
import com.dilemma.chickenortheegg.R;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Unit8 extends AppCompatActivity {
    String THREAD_TAG = "thread_tag";
    private TextView tv_philosopher_points_1, tv_philosopher_points_2;
    private TextView tv_status_philosopher_1, tv_status_philosopher_2;
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
        setContentView(R.layout.unit5);
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

        new DirectorWorker();

        Log.d(THREAD_TAG, "Конец симуляции (" + (System.currentTimeMillis() - time) + " ms )");
    }


    class PhilosopherWorker implements Runnable {
        Position position;//
        Handler mHandlerInner;
        boolean busyness = false;

        PhilosopherWorker(Position position) {
            this.position = position;
            new Thread(this, position.toString()).start();
        }

        @Override
        public void run() {
            Looper.prepare();//Созданный лупер будет связан с потоком, в котором вызван этот метод.
            mHandlerInner = new Handler(new Handler.Callback() { //создаем Handler, который будет обрабатывать сообщения, приходящие в MessageQueue лупера.
                public boolean handleMessage(Message msg) {
                    processMessage(msg); // обрабатываем очередное сообщение
                    return true;
                }
            });
            Looper.loop(); // запускаем лупер
        }

        private void processMessage(Message message) {
            Log.d("BORODA", "myMessage.event " + message.what+" "+Event.values()[message.what]);
            switch (Event.values()[message.what]) {
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
                Thread.sleep(sleep*2);
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

    class DirectorWorker implements Runnable {
        Handler mHandlerInner;

        DirectorWorker() {
            new Thread(this, "Director").start();
        }

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
            MyMessage leftMyMessage = bufferLeft.element();
            MyMessage rightMyMessage = bufferRight.element();
            while (bufferLeft.size() > 0 || bufferRight.size() > 0) {
                if (bufferLeft.size() > 0 && (!left.busyness)) {
                    if(!(bufferLeft.element().event.equals(Event.SPEECH)&&rightMyMessage.event.equals(Event.SPEECH))){
                        leftMyMessage = bufferLeft.remove();
                        postMessageToPhilosopher(left.mHandlerInner, leftMyMessage.event);
                        postMessageToUI(leftMyMessage);
                    }else{postMessageToUI(idleLeftMyMessage);}
                }
                if (bufferRight.size() > 0 && (!right.busyness)) {
                    if(!(bufferRight.element().event.equals(Event.SPEECH)&&leftMyMessage.event.equals(Event.SPEECH))) {
                        rightMyMessage = bufferRight.remove();
                        postMessageToPhilosopher(right.mHandlerInner, rightMyMessage.event);
                        postMessageToUI(rightMyMessage);
                    }else{postMessageToUI(idleRightMyMessage);}
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

    private void postMessageToPhilosopher(Handler handler, Event event) {
        Log.d("ProducerConsumer", "myMessage.event " + event);
        handler.sendEmptyMessage(event.ordinal());
    }
}