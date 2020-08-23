package com.example.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MySQL.MySQLiteHelper;
import com.example.afinal.running;
import com.example.afinal.solver;
import com.example.broadcastreceiver.GeofenceBroadReceiver;
import com.example.service.MyService;
import com.example.afinal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

//https://blog.csdn.net/baekkie/article/details/80282041
public class Tab2Fragment extends Fragment{
    private EditText et_title,et_content;
    private Button btn_submit,btn_show,btn_stop;
    private SQLiteDatabase db;
    private List<DataPoint> dp = new ArrayList<>();
    GraphView graphView1;
    MySQLiteHelper helper;
    String TAG = "TAG";
    LineGraphSeries seriesX;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_tab2_fragment,null);
        Button button = view.findViewById(R.id.bt1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), running.class);
                startActivity(intent);
            }
        });
        graphView1 = view.findViewById(R.id.graph1);
        seriesX = new LineGraphSeries(new DataPoint[]{});
        graphView1.addSeries(seriesX);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        seriesX.setColor(Color.RED);
        graphView1.getViewport().setXAxisBoundsManual(true);
        graphView1.getViewport().setMaxX(6);
        graphView1.getViewport().setMinX(0);
        graphView1.getViewport().setYAxisBoundsManual(true);
        graphView1.getViewport().setMaxY(700);
        graphView1.getViewport().setMinY(0);
        graphView1.getViewport().setScrollable(true);


        helper = new MySQLiteHelper(getActivity(),"diary",null,1);


        btn_show = (Button)getActivity().findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                db = helper.getWritableDatabase();

                Query();

            }
        });

        btn_stop = (Button)getActivity().findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAll();
            }
        });
    }
    public void Query(){
        int count = 0;
        Cursor cursor= db.query("diary",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Log.i(TAG, "title:" + title);
                Log.i(TAG, "content:" + content);
                if(containsTitle(dp,Integer.valueOf(title))!=-1){
                    int index = containsTitle(dp,Integer.valueOf(title));
                    dp.set(index,new DataPoint(dp.get(index).getX(),dp.get(index).getY()+Integer.valueOf(content)));
                }
                else dp.add(new DataPoint(Integer.valueOf(title),Integer.valueOf(content)));
                if(dp.size()>7){
                    dp.remove(0);
                }

            }while(cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(dp,comp);
        DataPoint[] array = new DataPoint[dp.size()];
        for(int i=0;i<dp.size();i++){
            array[i] = new DataPoint(i,dp.get(i).getY());
        }
        seriesX.resetData(array);
    }

    public static Comparator<DataPoint> comp = new Comparator<DataPoint>(){
        @Override
        public int compare(DataPoint p1,DataPoint p2){
            if(p1.getX()>p2.getX()) return 1;
            if(p1.getX()<p2.getX()) return -1;
            return 0;
        }
    };

    int containsTitle(List<DataPoint> dp,int title){
        for(int i=0;i<dp.size();i++){
            if(dp.get(i).getX()==title) return i;
        }
        return -1;
    }
    public void Insert(){
        ContentValues values = new ContentValues();
        values.put("title", et_title.getText().toString());
        values.put("content", et_content.getText().toString());
        db.insertWithOnConflict("diary", null, values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeAll(){
        SQLiteDatabase db = helper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete("diary",null,null);
    }
}
