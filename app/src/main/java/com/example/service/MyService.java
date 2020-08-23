package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.example.service.StepDetector;

//from https://my.oschina.net/u/1049180/blog/471473?utm_source=tuicool&utm_medium=referral
public class MyService extends Service {
    public static Boolean flag = false;
    private SensorManager sensorManager;
    private StepDetector stepDetector;
    Runnable runnable;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
            }
        }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        if (stepDetector != null) {
            sensorManager.unregisterListener(stepDetector);
        }
    }

    private void startStepDetector() {
        flag = true;
        stepDetector = new StepDetector(this);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
