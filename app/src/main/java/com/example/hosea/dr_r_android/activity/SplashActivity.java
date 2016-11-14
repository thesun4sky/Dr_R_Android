package com.example.hosea.dr_r_android.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LeeMoonSeong on 2016-11-04.
 */
public class SplashActivity extends Activity {
    private AQuery aq = new AQuery(this);
    public static final int MY_READ_PHONE_STATE =0;
    public static String deviceId;
    public int result_last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            getUUID();
        } catch (Exception e) {
            checkPermission();
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_device", deviceId);
        aq.ajax("http://192.168.0.2:8080/checkUserDevice", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                Toast.makeText(getApplicationContext(), html.toString(), Toast.LENGTH_SHORT).show();
                String result_json = html.toString();
                try {
                    JSONObject jsonRoot  = new JSONObject(result_json);
                    int result = jsonRoot.getInt("num");
                    result_last = result;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                /* 메뉴액티비티를 실행하고 로딩화면을 죽인다.*/
                if(result_last == 1) {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                }
                else if(result_last ==0){
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(loginIntent);
                }
                SplashActivity.this.finish();
            }
        }, 2000);

        Toast toast = Toast.makeText(this, deviceId+"님 어서오세요.^^*",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0 , 50);
        toast.show();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){    //사용자에게 디바이스 정보 받아오는거 확인
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_READ_PHONE_STATE);

                // MY_PERMISSION_REQUEST_STORAGE is an
                // app-defined int constant

            } else {
                // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
                getUUID();
            }

    }

    @Override   //디바이스정보 접근 권한 확인
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test", "It has permission of getUUID");
                    getUUID();

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    Log.d("test", "Permission always deny");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    private void getUUID() {   //디바이스 정보 받아오기
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();

        Log.d("test","Device UUID : "+ deviceId);
    }
}