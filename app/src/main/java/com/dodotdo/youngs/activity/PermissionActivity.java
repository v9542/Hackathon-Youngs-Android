package com.dodotdo.youngs.activity;

/**
 * Created by KimYebon on 16. 7. 29..
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PermissionActivity extends AppCompatActivity {

    protected String[] permissionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            init();
            checkAndRequestPermission(permissionList);
        } else {
            startActivity(new Intent(PermissionActivity.this, SplashActivity.class));
            finish();
        }

    }

    public void init() {

        permissionList = new String[4];

        permissionList[0] = Manifest.permission.INTERNET;
        permissionList[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissionList[2] = Manifest.permission.MODIFY_AUDIO_SETTINGS;
        permissionList[3] = Manifest.permission.RECORD_AUDIO;

    }

    public void checkAndRequestPermission(String[] permissionList) {
        int result;

        for (int i = 0; i < permissionList.length; i++) {
            result = ContextCompat.checkSelfPermission(this, permissionList[i]);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionList, 1);
                return;
            }
        }
        startActivity(new Intent(this, SplashActivity.class));
        finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length == 0)
            return;
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
