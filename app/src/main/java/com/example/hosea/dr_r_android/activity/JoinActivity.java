package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    EditText login_id, name, password1,password2, phone, disease, hopitalName;
    Button checkId, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        login_id = (EditText) findViewById(R.id.login_id);
        name = (EditText) findViewById(R.id.user_name);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        phone = (EditText) findViewById(R.id.user_phone);
        disease = (EditText) findViewById(R.id.disease);
        hopitalName = (EditText) findViewById(R.id.hospitalName);
        checkId = (Button) findViewById(R.id.checkId);
        submit = (Button) findViewById(R.id.joinSubmit);

        checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("login_id", login_id.getText().toString());
                aq.ajax("http://192.168.0.73/checkLoginId", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject html, AjaxStatus status) {
                        Toast.makeText(getApplicationContext(), html.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            login_id.setText(html.getString("login_id"));
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

                if (password1.getText().toString().equals(password2.getText().toString())) {
                    //비밀번호 일치
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("login_id", login_id.getText().toString());
                    params.put("u_name", name.getText().toString());
                    params.put("password", password1.getText().toString());
                    params.put("phone", phone.getText().toString());
                    params.put("disease", disease.getText().toString());
                    params.put("hospital_name", hopitalName.getText().toString());
                    aq.ajax("http://192.168.0.73:8080/joinUser", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject html, AjaxStatus status) {
                            Toast.makeText(getApplicationContext(), html.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                login_id.setText(html.getString("login_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    password1.setText("");
                    password2.setText("");
                    password1.requestFocus();
                }
            }
        });
    }





}