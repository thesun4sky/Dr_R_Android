package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WriteDiaryActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    EditText breakfast, lunch, dinner, temperature, humid, sleepTime, bloodPressure;
    RadioButton drinking;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);

        breakfast = (EditText) findViewById(R.id.breakfast);
        lunch = (EditText) findViewById(R.id.lunch);
        dinner = (EditText) findViewById(R.id.diner);
        temperature = (EditText) findViewById(R.id.temperature);
        humid = (EditText) findViewById(R.id.humidity);
        sleepTime = (EditText) findViewById(R.id.time_sleep);
        bloodPressure = (EditText) findViewById(R.id.bloodpressure);
        drinking = (RadioButton) findViewById(R.id.drink_yes);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeDiary();
            }
        });

    }

    public void writeDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", 1);
        params.put("breakfast", breakfast.getText().toString());
        params.put("lunch", lunch.getText().toString());
        params.put("dinner", dinner.getText().toString());
        params.put("temperature", Integer.parseInt(String.valueOf(temperature.getText())));
        params.put("humid", Integer.parseInt(String.valueOf(humid.getText())));
        params.put("sleepTime", Integer.parseInt(String.valueOf(sleepTime.getText())));
        params.put("bloodPressure", Integer.parseInt(String.valueOf(bloodPressure.getText())));
        if (drinking.isChecked()) {
            params.put("drinking", "true");
        } else {
            params.put("drinking", "false");
        }
        aq.ajax("http://192.168.0.73:8080/writeDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                Toast.makeText(getApplicationContext(), html.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}