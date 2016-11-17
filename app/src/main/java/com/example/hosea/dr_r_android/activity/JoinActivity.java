package com.example.hosea.dr_r_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private EditText login_id, name, password1, password2, phone, disease;
    private Button checkId, submit;
    private String array, deviceId;
    private Object hospitalName;
    private String[] string;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        try {
            getUUID();
        } catch (Exception e) {
        }
        login_id = (EditText) findViewById(R.id.login_id);
        name = (EditText) findViewById(R.id.user_name);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        phone = (EditText) findViewById(R.id.user_phone);
        disease = (EditText) findViewById(R.id.disease);
        spinner = (Spinner) findViewById(R.id.spinner_hospital);
        checkId = (Button) findViewById(R.id.checkId);
        submit = (Button) findViewById(R.id.joinSubmit);
        aq.ajax("http://52.41.218.18:8080/getHospital", JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                string = new String[html.length()];
                for (int i = 0; i < html.length(); i++) {
                    try {
                        JSONObject jsonobject = html.getJSONObject(i);
                        array = jsonobject.getString("a_hospital");
                        string[i] = array;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                makeSpinner(spinner);
            }
        });


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
                } else if ((phone.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    phone.setText("");
                    phone.requestFocus();
                } else if ((disease.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "병 명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    disease.setText("");
                    disease.requestFocus();
                } else {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("login_id", login_id.getText().toString());
                    params.put("u_name", name.getText().toString());
                    params.put("u_password", password1.getText().toString());
                    params.put("u_phone", phone.getText().toString());
                    params.put("u_disease", disease.getText().toString());
                    params.put("u_hospital", hospitalName.toString());
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


    //병원리스트 받아와서 선택하는 스피너 생성
    public void makeSpinner(Spinner spinner) {
        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, string);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hospitalName = parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}