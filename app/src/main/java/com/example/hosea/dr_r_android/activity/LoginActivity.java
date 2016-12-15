package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeeMoonSeong on 2016-11-10.
 */
public class LoginActivity extends AppCompatActivity {
    private AQuery aq = new AQuery(this);
    private Intent priviousIntent;
    private EditText id, password;
    private Button join, login;
    private String u_name;
    private int u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        priviousIntent = getIntent();

        id = (EditText) findViewById(R.id.login_id);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.request_login);
        join = (Button) findViewById(R.id.request_join);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("login_id", id.getText().toString());
                params.put("u_password", password.getText().toString());
                params.put("u_device" ,priviousIntent.getStringExtra("u_device"));

                aq.ajax("http://223.194.159.175:8080/login", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                        if (jsonObject != null) {
                            try {
                                u_id = jsonObject.getInt("num");
                                u_name = jsonObject.getString("msg");

                                if (u_id > 0) {
                                    Toast.makeText(getApplicationContext(), u_name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    mainIntent.putExtra("u_id", u_id);
                                    mainIntent.putExtra("u_name", u_name);
                                    mainIntent.putExtra("u_device", priviousIntent.getStringExtra("u_device"));
                                    startActivity(mainIntent);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                                    id.setText("");
                                    id.requestFocus();
                                    password.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });

            }
        });
    }


}
