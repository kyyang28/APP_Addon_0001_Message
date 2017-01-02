package com.young.app_addon_0001_message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButton = null;
    private static final String TAG = "MessageTest";
    private int ButtonCnt = 0;
    private Thread internalThread = null;
    private MyThread myThread = null;

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
        @Override
        public void run() {
            super.run();

            int count = 0;
            for (;;) {
                Log.d(TAG, "MyThread " + count);
                count++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
            }
        });

        /* Use internal Thread class to instantiate object */
        internalThread = new Thread(new myRunnable(), "MessageTestThread");
        internalThread.start();

        /* Use MyThread class to instantiate object */
        myThread = new MyThread();
        myThread.start();
    }
}
