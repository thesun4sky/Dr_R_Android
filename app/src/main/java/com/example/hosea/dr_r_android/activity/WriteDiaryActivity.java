package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WriteDiaryActivity extends AppCompatActivity  {

    private Intent previousIntent;
    private AQuery aq = new AQuery(this);
    RadioButton drinking;
    Button submit;
    Object b_menu1,b_menu2,b_menu3;
    Button recordAudio;
    TextView audioToText;
    Object l_menu1,l_menu2,l_menu3;
    Object d_menu1,d_menu2,d_menu3;
    Object temperature ,humidity ,sleepTime, bloodPressure ;
    SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);


        previousIntent = getIntent();
        recordAudio = (Button)findViewById(R.id.audio);
        audioToText = (TextView)findViewById(R.id.breakfast);

        recordAudio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent= new Intent(getApplicationContext(),VoiceToTextActivity.class);
                intent.putExtra("tospeach", "아침 머드셧나요");
                startActivityForResult(intent,1001);
            }
        });


        drinking = (RadioButton) findViewById(R.id.drink_yes);
        //아침 메뉴
        //메뉴 1 선택
//        Spinner b_menu1Spinner = (Spinner)findViewById(R.id.b_spinner_menu1);
//        final ArrayAdapter b_menu1Adapter = ArrayAdapter.createFromResource(this,
//                R.array.menu1, android.R.layout.simple_spinner_item);
//        b_menu1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        b_menu1Spinner.setAdapter(b_menu1Adapter);
//
//        b_menu1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                b_menu1 = parent.getItemAtPosition(position);
//            }
//            public void onNothingSelected(AdapterView<?>  parent) {
//            }
//        });
//
//        //메뉴 2 선택
//        Spinner b_menu2Spinner = (Spinner)findViewById(R.id.b_spinner_menu2);
//        final ArrayAdapter b_menu2Adapter = ArrayAdapter.createFromResource(this,
//                R.array.menu2, android.R.layout.simple_spinner_item);
//        b_menu2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        b_menu2Spinner.setAdapter(b_menu2Adapter);
//
//        b_menu2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                b_menu2 = parent.getItemAtPosition(position);
//            }
//            public void onNothingSelected(AdapterView<?>  parent) {
//            }
//        });
//
//        //메뉴 3 선택
//        Spinner b_menu3Spinner = (Spinner)findViewById(R.id.b_spinner_menu3);
//        final ArrayAdapter b_menu3Adapter = ArrayAdapter.createFromResource(this,
//                R.array.menu3, android.R.layout.simple_spinner_item);
//        b_menu3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        b_menu3Spinner.setAdapter(b_menu3Adapter);
//
//        b_menu3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                b_menu3 = parent.getItemAtPosition(position);
//            }
//            public void onNothingSelected(AdapterView<?>  parent) {
//            }
//        });

        //점심 메뉴
        //메뉴 1 선택
        Spinner l_menu1Spinner = (Spinner)findViewById(R.id.l_spinner_menu1);
        final ArrayAdapter l_menu1Adapter = ArrayAdapter.createFromResource(this,
                R.array.menu1, android.R.layout.simple_spinner_item);
        l_menu1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        l_menu1Spinner.setAdapter(l_menu1Adapter);

        l_menu1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                l_menu1 = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        //메뉴 2 선택
        Spinner l_menu2Spinner = (Spinner)findViewById(R.id.l_spinner_menu2);
        final ArrayAdapter l_menu2Adapter = ArrayAdapter.createFromResource(this,
                R.array.menu2, android.R.layout.simple_spinner_item);
        l_menu2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        l_menu2Spinner.setAdapter(l_menu2Adapter);

        l_menu2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                l_menu2 = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        //메뉴 3 선택
        Spinner l_menu3Spinner = (Spinner)findViewById(R.id.l_spinner_menu3);
        final ArrayAdapter l_menu3Adapter = ArrayAdapter.createFromResource(this,
                R.array.menu3, android.R.layout.simple_spinner_item);
        l_menu3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        l_menu3Spinner.setAdapter(l_menu3Adapter);

        l_menu3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                l_menu3 = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });


        //저녁 메뉴
        //메뉴 1 선택
        Spinner d_menu1Spinner = (Spinner)findViewById(R.id.d_spinner_menu1);
        final ArrayAdapter d_menu1Adapter = ArrayAdapter.createFromResource(this,
                R.array.menu1, android.R.layout.simple_spinner_item);
        d_menu1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d_menu1Spinner.setAdapter(d_menu1Adapter);

        d_menu1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                d_menu1 = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        //메뉴 2 선택
        Spinner d_menu2Spinner = (Spinner)findViewById(R.id.d_spinner_menu2);
        final ArrayAdapter d_menu2Adapter = ArrayAdapter.createFromResource(this,
                R.array.menu2, android.R.layout.simple_spinner_item);
        d_menu2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d_menu2Spinner.setAdapter(d_menu2Adapter);

        d_menu2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                d_menu2 = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        //메뉴 3 선택
        Spinner d_menu3Spinner = (Spinner)findViewById(R.id.d_spinner_menu3);
        final ArrayAdapter d_menu3Adapter = ArrayAdapter.createFromResource(this,
                R.array.menu3, android.R.layout.simple_spinner_item);
        d_menu3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d_menu3Spinner.setAdapter(d_menu3Adapter);

        d_menu3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                d_menu3 = parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });


        //온도 선택
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

        //습도 선택
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

        //수면시간 선택
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

        //혈압 선택
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
        MyApplication myapp = (MyApplication)getApplicationContext();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        params.put("breakfast",b_menu1.toString() +","+ b_menu2.toString() +","+ b_menu3.toString());
        params.put("lunch",l_menu1.toString() +","+ l_menu2.toString() +","+ l_menu3.toString());
        params.put("dinner",d_menu1.toString() +","+ d_menu2.toString() +","+ d_menu3.toString());
        params.put("temperature", Integer.parseInt(temperature.toString()));
        params.put("humid", Integer.parseInt((humidity.toString())));
        params.put("sleepTime", Integer.parseInt((sleepTime.toString())));
        params.put("bloodPressure", Integer.parseInt((bloodPressure.toString())));
        if (drinking.isChecked()) {
            params.put("drinking", 1);
        } else {
            params.put("drinking", 0);
        }
        aq.ajax("http://52.41.218.18:8080/writeDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                try {
                    if (html.getString("msg").equals("정상 작동")) {
                        Toast.makeText(getApplicationContext(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode ==1001 &&resultCode == RESULT_OK) {
            String result = data.getExtras().getString("result_speach");
            audioToText.setText(result);
        }
    }
//    public void onClick(View v) {
//        int view = v.getId();
//        if(view == 2131034112) {
//            Intent i = new Intent("android.speech.action.RECOGNIZE_SPEECH");
//            i.putExtra("calling_package", this.getPackageName());
//            i.putExtra("android.speech.extra.LANGUAGE", "ko-KR");
//            i.putExtra("android.speech.extra.PROMPT", "말을 하세요.");
//            this.startActivityForResult(i, 1000);
//        } else if(view == 2131034113) {
//            this.startActivityForResult(new Intent(this, CustomUIActivity.class), 1001);
//        }


}