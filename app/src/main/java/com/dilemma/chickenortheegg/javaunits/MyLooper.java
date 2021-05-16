package com.dilemma.chickenortheegg.javaunits;


import android.os.Looper;

public class MyLooper {
    final MyMessageQueue mQueue;
    final Thread mThread;
    private static Looper sMainLooper = Looper.getMainLooper();
    static final ThreadLocal<MyLooper> sThreadLocal = new ThreadLocal<MyLooper>();

    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new MyLooper(quitAllowed));
    }

    private MyLooper(boolean quitAllowed) {
        mQueue = new MyMessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }


    public static MyLooper myMyLooper() {
        return sThreadLocal.get();
    }
}
