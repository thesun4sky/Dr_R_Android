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
    public int result_last;
    private AQuery aq = new AQuery(this);
    private EditText id;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.login_id);
        password = (EditText)findViewById(R.id.login_password);
        Button login = (Button)findViewById(R.id.request_login);
        Button join = (Button)findViewById(R.id.request_join);

        join.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("login_id", id.getText().toString());
                params.put("u_password",password.getText().toString());

                aq.ajax("http://52.41.218.18:8080/login", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject html, AjaxStatus status) {
                        Toast.makeText(getApplicationContext(), html.toString(), Toast.LENGTH_SHORT).show();
                        String result_json = html.toString();
                        try {
                            JSONObject jsonRoot  = new JSONObject(result_json);
                            int result = jsonRoot.getInt("num");
                            result_last = result;

                            if(result == 1){
                                Toast.makeText(getApplicationContext(),id  +"님 환영",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                LoginActivity.this.finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"아이디,비밀번호 다시 확인하세요.",Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


}
