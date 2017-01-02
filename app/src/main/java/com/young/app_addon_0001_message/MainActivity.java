package com.young.app_addon_0001_message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.os.HandlerThread;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private static final String TAG = "MessageTest";
    private int ButtonCnt = 0;
    private Thread internalThread;
    private MyThread myThread;
    private Handler mHandler;
    private Handler mHandler2;
    private int mMessageCount = 0;

    private HandlerThread myThread2;

    class myRunnable implements Runnable {
        @Override
        public void run() {
            int count = 0;

            for (;;) {
                Log.d(TAG, "InternalThread " + count);
                count++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Ctrl + O */
    class MyThread extends Thread {
        private Looper mLooper;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            synchronized (this) {
                mLooper = Looper.myLooper();
                notifyAll();    /* The mLooper is ready, notify getLooper() function */
            }
            Looper.loop();
        }

        public Looper getLooper() {
            if (!isAlive()) {
                return null;
            }

            // If the thread has been started, wait until the looper has been created.
            synchronized (this) {
                while (isAlive() && mLooper == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            return mLooper;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Send message " + ButtonCnt);
                ButtonCnt++;
                Message msg = new Message();
                mHandler.sendMessage(msg);

                mHandler2.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Get message for HandlerThread " + mMessageCount);
                        mMessageCount++;
                    }
                });
            }
        });

        /* Use internal Thread class to instantiate object */
        internalThread = new Thread(new myRunnable(), "MessageTestThread");
        internalThread.start();

        /* Use MyThread class to instantiate object */
        myThread = new MyThread();
        myThread.start();

        /* public Handler(Looper looper, Callback callback) */
        mHandler = new Handler(myThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d(TAG, "Get message " + mMessageCount);
                mMessageCount++;
                return false;
            }
        });

        myThread2 = new HandlerThread("MessageHandlerThread");
        myThread2.start();

        mHandler2 = new Handler(myThread2.getLooper());
    }
}
