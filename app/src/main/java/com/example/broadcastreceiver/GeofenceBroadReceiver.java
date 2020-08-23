package com.example.broadcastreceiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadReceiver extends BroadcastReceiver {
    private String TAG = "Broadcast";
    int notifyID = 20;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        if(geofencingEvent.hasError()){
            Log.e(TAG,"has error");
        }

        // from https://github.com/android/location-samples
        //
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.e(TAG,"Entering the influenza area");
            Toast.makeText(context,"ENTER",Toast.LENGTH_LONG).show();

        }
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.e(TAG, "Exiting the influenza area");
            Toast.makeText(context, "EXIT", Toast.LENGTH_LONG).show();
        }
    }
}
