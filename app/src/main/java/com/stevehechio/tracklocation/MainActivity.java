package com.stevehechio.tracklocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mText;
    Button mBtnStart, mBtnStop;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver==null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                mText.append("\n" +intent.getExtras().get("updates"));


                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_updates"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText=findViewById(R.id.txtShowCoordinates);
        mBtnStart=findViewById(R.id.btnStartLocation);
        mBtnStop=findViewById(R.id.btnStopLocation);
        if (!userPermission()){
            btnFuntionaliy();
        }
    }

    private void btnFuntionaliy() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GPS_Service.class);
                startService(intent);

            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GPS_Service.class);
                stopService(intent);

            }
        });

    }

    private boolean userPermission() {
        if (Build.VERSION.SDK_INT>= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION
        )!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;

        }
            return false;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                grantResults[1]==PackageManager.PERMISSION_GRANTED){
            btnFuntionaliy();
        }
        else {
            userPermission();
        }
    }
}


