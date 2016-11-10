package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadDiaryActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    private ArrayList arrayList;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readdiary);

        tv = (TextView)findViewById(R.id.textView2);
        readDiary();

    }

    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", 1);
        aq.ajax("http://192.168.1.25:8080/getDiaries", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                if( html != null) {
                    tv.setText(html.toString());
                } else {
                    tv.setText("html이 null입니다.");
                }
            }
        });
    }


}