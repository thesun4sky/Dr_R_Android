package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisteringActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        textView = (TextView) findViewById(R.id.textView);

        ImageView img = (ImageView) this.findViewById(R.id.img);
        aq.id(img).image("https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");


        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                textView.setText(html.toString());
                Toast.makeText(getApplicationContext(),html.toString(),Toast.LENGTH_SHORT).show();
            }
        };

        String url = "http://192.168.1.3:8080/expost";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("first_name", "name");
        params.put("last_name", "name");
        aq.ajax(url, params, JSONObject.class, cb);
    }


}