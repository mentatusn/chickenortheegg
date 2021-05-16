package com.dilemma.chickenortheegg;

import android.os.Looper;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ProducerConsumer {
    private static final int BUFFER_MAX_SIZE=1;
    Queue<MyMessage> buffer = new LinkedList<>();
    synchronized public void produce(MyMessage value) {
        /*while(buffer.size()==BUFFER_MAX_SIZE){
            wait();
        }*///
        buffer.add(value);
        //notify();
    }
    public synchronized MyMessage consume() {
        /*while(buffer.size()==0){
            wait();
        }*/
        MyMessage message = buffer.poll();
        //notify();
        return message;
    }
}
