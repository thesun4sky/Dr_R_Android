package com.example.hosea.dr_r_android.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.UserVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    private EditText login_id, name, password1, password2;
    private EditText a_week, a_date;
    private EditText b_month, b_date;
    private EditText b_weight, b_height;
    private static final int MY_READ_PHONE_STATE = 0;
    private Button checkId, submit;
    private String array, deviceId;
    private String[] string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        try {
            getUUID();
        } catch (Exception e) {
            checkPermission();
        }

        a_week = (EditText)findViewById(R.id.a_week);
        a_date = (EditText)findViewById(R.id.a_date);
        b_month = (EditText)findViewById(R.id.b_month);
        b_date = (EditText)findViewById(R.id.b_date);
        b_weight =(EditText)findViewById(R.id.b_weight);
        b_height =(EditText)findViewById(R.id.b_height);
        login_id = (EditText) findViewById(R.id.login_id);
        name = (EditText) findViewById(R.id.user_name);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        checkId = (Button) findViewById(R.id.checkId);
        submit = (Button) findViewById(R.id.joinSubmit);

        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioForSex);


        checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("login_id", login_id.getText().toString());
                aq.ajax("http://52.41.218.18:8080/checkLoginId", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject html, AjaxStatus status) {
                        try {
                            Toast.makeText(getApplicationContext(), html.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);

                //비밀번호 테스트
                if (!password1.getText().toString().equals(password2.getText().toString()) || password1.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요. (5자 이상)", Toast.LENGTH_SHORT).show();
                    password1.setText("");
                    password2.setText("");
                    password1.requestFocus();
                } else if ((login_id.getText().toString().equals("")) || login_id.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "id를 다시 입력하세요. (5자 이상)", Toast.LENGTH_SHORT).show();
                    login_id.setText("");
                    login_id.requestFocus();
                } else if ((name.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    name.requestFocus();
                } else {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("login_id", login_id.getText().toString());
                    params.put("u_password", password1.getText().toString());
                    params.put("u_name", name.getText().toString());
                    params.put("u_a_week", a_week.getText().toString());
                    params.put("u_a_date", a_date.getText().toString());
                    params.put("u_b_month", b_month.getText().toString());
                    params.put("u_b_date", b_date.getText().toString());
                    params.put("u_w", b_weight.getText().toString());
                    params.put("u_h", b_height.getText().toString());
                    params.put("u_sex", rb.getText().toString());
                    params.put("u_device", deviceId);
                    aq.ajax("http://52.41.218.18:8080/joinUser", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject html, AjaxStatus status) {
                            try {
                                Toast.makeText(getApplicationContext(), html.getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                            {
                                intent.putExtra("u_device" ,deviceId);
                                startActivity(intent);
                                JoinActivity.this.finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getUUID() {   //디바이스 정보 받아오기
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {    //사용자에게 디바이스 정보 받아오는거 확인
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

}