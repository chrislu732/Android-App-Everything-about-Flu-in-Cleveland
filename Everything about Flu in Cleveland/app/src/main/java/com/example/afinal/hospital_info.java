package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//from https://blog.csdn.net/xuwenneng/article/details/80620278
public class hospital_info extends Activity {

    private final static String TAG = hospital_info.class.getSimpleName();
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    public static final String WEBURL = "WEBURL";
    public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private String mPhoneNumber;
    private String mHospitalName;
    private String latitude,longitue;
    private String address;
    private String weburl;
    private TextView t1,t2,t3,t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_info);
        final Intent intent = getIntent();
        mPhoneNumber = intent.getStringExtra(PHONE_NUMBER);
        mHospitalName = intent.getStringExtra(DEVICE_ADDRESS);
        address = intent.getStringExtra(DEVICE_ADDRESS);
        longitue = intent.getStringExtra(LAT);
        latitude = intent.getStringExtra(LNG);
        TextView phoneField = findViewById(R.id.phone_number);
        phoneField.setText(mPhoneNumber);
        if(phoneField != null){
            Linkify.addLinks(phoneField, Patterns.PHONE,"tel:",Linkify.sPhoneNumberMatchFilter,Linkify.sPhoneNumberTransformFilter);
            phoneField.setMovementMethod(LinkMovementMethod.getInstance());
        }
        t1 = findViewById(R.id.address);

        t1.setText(address);
        Button button = findViewById(R.id.button);
        button.setText("Navigation");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mHospitalName.replace(" ","+");
                final Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+name));
                intent1.setPackage("com.google.android.apps.maps");
                startActivity(intent1);
            }
        });


        phoneField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPhoneNumber.equals("No Appointment Needed")) showNormalDialog(mPhoneNumber);
            }
        });


    }

    private void showNormalDialog(final String phoneNumber){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("CALLING");
        normalDialog.setMessage("CALL "+phoneNumber+"?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkDialPermission(Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
                            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(phoneNumber));
                            startActivity(intent);
                        }
                    }
                });
        normalDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();
    }
    public static final int REQUEST_CALL_PERMISSION = 10111;
    public boolean checkDialPermission(String string_permission,int request_code){
        boolean flag = false;
        if(ContextCompat.checkSelfPermission(this,string_permission)== PackageManager.PERMISSION_GRANTED){
            flag = true;
        }else{
            ActivityCompat.requestPermissions(this,new String[]{string_permission},request_code);
        }
        return flag;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION:
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"ask permission",Toast.LENGTH_SHORT).show();
                } else {
                    call(mPhoneNumber);
                }
                break;
        }
    }
    public void call(String phoneNumber){
        if(checkDialPermission(Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(phoneNumber));
            startActivity(intent);
        }
    }
}
