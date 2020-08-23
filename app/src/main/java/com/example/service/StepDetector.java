package com.example.service;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//from https://my.oschina.net/u/1049180/blog/471473?utm_source=tuicool&utm_medium=referral
public class StepDetector implements SensorEventListener {
    public static float SENSITIVITY = 10.00f;
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;
    private static long end = 0;
    private static long start = 0;
    private int walk_step = 0;
    private int run_step = 0;

    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;
    private Context context1;
    LocalBroadcastManager mLocalBroadcastManager;
    /**
     *
     * @param context
     */
    public StepDetector(Context context) {
        super();
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
        context1 = context;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            synchronized (this) {
                if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                } else {
                    int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                    if (j == 1) {
                        float vSum = 0;
                        for (int i = 0; i < 3; i++) {
                            final float v = mYOffset + event.values[i] * mScale[j];
                            vSum += v;
                        }
                        int k = 0;
                        float v = vSum / 3;

                        float direction = (v > mLastValues[k] ? 1: (v < mLastValues[k] ? -1 : 0));
                        if (direction == -mLastDirections[k]) {
                            // Direction changed
                            int extType = (direction > 0 ? 0 : 1); // minumum or
                            // maximum?
                            mLastExtremes[extType][k] = mLastValues[k];
                            float diff = Math.abs(mLastExtremes[extType][k]- mLastExtremes[1 - extType][k]);

                            if (diff > SENSITIVITY) {
                                boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                                boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                                boolean isNotContra = (mLastMatch != 1 - extType);

                                if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                    end = System.currentTimeMillis();
                                    if (end - start > 500) {
                                        walk_step++;

                                        Intent intent = new Intent("com.example.walk_step");
                                        intent.putExtra("walking",String.valueOf(walk_step));
                                        mLocalBroadcastManager.sendBroadcast(intent);
                                        mLastMatch = extType;
                                        start = end;
                                    }else if(end - start > 300){

                                        Intent intent = new Intent("com.example.run_step");
                                        intent.putExtra("running",String.valueOf(run_step));
                                        mLocalBroadcastManager.sendBroadcast(intent);
                                        mLastMatch = extType;
                                        start = end;
                                    }
                                    else{
                                    }
                                } else {
                                    mLastMatch = -1;
                                }
                            }
                            mLastDiff[k] = diff;
                        }
                        mLastDirections[k] = direction;
                        mLastValues[k] = v;
                    }
                }
            }
        }
    }
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }
    //
}
