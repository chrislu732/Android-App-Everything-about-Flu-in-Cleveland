package com.example.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.ListViewAdapter;
import com.example.afinal.MainActivity;
import com.example.afinal.R;
import com.example.afinal.hospital_info;
import com.google.android.gms.maps.model.LatLng;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tab4Fragment extends Fragment{
    private ListView listView;
    List<Map<String,Object>> list;
    private ListViewAdapter listViewAdapter;
    private View view;
    private LocalBroadcastManager LBM;
    private LatLng mylocation = new LatLng(0,0);
    private boolean mScanning;
    private TextView t1,t2,t3,t4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_tab4_fragment,null);
        listView = view.findViewById(R.id.Tab4listview);
        list = getData();



        listViewAdapter = new ListViewAdapter(getActivity(),list);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("n", "onListItemClick call shod");
                String phoneNumber = listViewAdapter.getNumber(position);
                Intent intent = new Intent(getActivity(), hospital_info.class);
                intent.putExtra(hospital_info.PHONE_NUMBER,phoneNumber);
                intent.putExtra(hospital_info.DEVICE_NAME,listViewAdapter.getName(position));
                intent.putExtra(hospital_info.DEVICE_ADDRESS,listViewAdapter.getAddress(position));
                intent.putExtra(hospital_info.LAT,listViewAdapter.getLat(position));
                intent.putExtra(hospital_info.LNG,listViewAdapter.getLng(position));
                intent.putExtra(hospital_info.WEBURL,listViewAdapter.getWeb(position));
                startActivity(intent);
            }
        });
        LBM = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter();{
            filter.addAction("com.example.location");
        }
        LBM.registerReceiver(mMessageReceiver,filter);
        Button bt4 = view.findViewById(R.id.bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<Map<String,Object>> comp = new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        double dis1 = ((double)o1.get("lat")-mylocation.latitude)*((double)o1.get("lat")-mylocation.latitude)
                                + ((double)o1.get("lng")-mylocation.longitude)*((double)o1.get("lng")-mylocation.longitude);
                        double dis2 = ((double)o2.get("lat")-mylocation.latitude)*((double)o2.get("lat")-mylocation.latitude)
                                + ((double)o2.get("lng")-mylocation.longitude)*((double)o2.get("lng")-mylocation.longitude);
                        if(dis1>dis2) return 1;
                        if(dis1<dis2) return -1;
                        return 0;
                    }
                };
                list = getData();
                Collections.sort(list,comp);
                listViewAdapter = new ListViewAdapter(getActivity(),list);
                listView.setAdapter(listViewAdapter);
            }
        });
        return view;
    }





    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.example.location")) {
                double[] word = intent.getDoubleArrayExtra("com.example.location");
                mylocation = new LatLng(word[0], word[1]);
            }
        }
    };

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int i = 0;
        try {
            InputStream is = getResources().openRawResource(R.raw.flu_shot_inf);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String line = "";

            while((line = reader.readLine())!=null){
                String[] nextLine = line.split(",");
                String[] nextLine2 = line.split("\"");
                Map<String, Object> m = new HashMap<>();
                m.put("device_name", nextLine[0]);
                m.put("device_address", nextLine2[1]);
                m.put("phoneNumber", nextLine[1]);
                m.put("lat", Double.valueOf(nextLine[2]));
                m.put("lng", Double.valueOf(nextLine[3]));
                m.put("webUrl", nextLine2[2]);
                Log.i("Tag",nextLine[2]);
                list.add(m);
            }
        }catch(IOException e){

        }
        return list;
    }



}
