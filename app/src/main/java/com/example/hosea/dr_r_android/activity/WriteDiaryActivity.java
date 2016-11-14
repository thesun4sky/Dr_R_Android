package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
    EditText breakfast, lunch, dinner;
    RadioButton drinking;
    Button submit;
    Object temperature ,humidity ,sleepTime, bloodPressure ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);

        breakfast = (EditText) findViewById(R.id.breakfast);
        lunch = (EditText) findViewById(R.id.lunch);
        dinner = (EditText) findViewById(R.id.diner);
        drinking = (RadioButton) findViewById(R.id.drink_yes);


        Spinner tempSpinner = (Spinner)findViewById(R.id.spinner_temperature);
        final ArrayAdapter tempAdapter = ArrayAdapter.createFromResource(this,
                R.array.temp, android.R.layout.simple_spinner_item);
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tempSpinner.setAdapter(tempAdapter);

        tempSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temperature = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        Spinner humiditySpinner = (Spinner)findViewById(R.id.spinner_humidity);
        final ArrayAdapter humidityAdapter = ArrayAdapter.createFromResource(this,
                R.array.humidity, android.R.layout.simple_spinner_item);
        humidityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        humiditySpinner.setAdapter(humidityAdapter);

        humiditySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                humidity = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        Spinner sleepSpinner = (Spinner)findViewById(R.id.spinner_sleep);
        final ArrayAdapter sleepAdapter = ArrayAdapter.createFromResource(this,
                R.array.sleep, android.R.layout.simple_spinner_item);
        sleepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleepSpinner.setAdapter(sleepAdapter);

        sleepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sleepTime = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        Spinner pressureSpinner = (Spinner)findViewById(R.id.spinner_pressure);
        final ArrayAdapter pressureAdapter = ArrayAdapter.createFromResource(this,
                R.array.pressure, android.R.layout.simple_spinner_item);
        pressureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pressureSpinner.setAdapter(pressureAdapter);

        pressureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodPressure = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

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
        params.put("temperature", Integer.parseInt(temperature.toString()));
        params.put("humid", Integer.parseInt((humidity.toString())));
        params.put("sleepTime", Integer.parseInt((sleepTime.toString())));
        params.put("bloodPressure", Integer.parseInt((bloodPressure.toString())));
        if (drinking.isChecked()) {
            params.put("drinking", "true");
        } else {
            params.put("drinking", "false");
        }
        aq.ajax("http://192.168.0.2:8080/writeDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                Toast.makeText(getApplicationContext(), html.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}