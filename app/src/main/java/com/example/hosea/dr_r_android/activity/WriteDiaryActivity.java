package com.example.hosea.dr_r_android.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class WriteDiaryActivity extends AppCompatActivity  {

    int year, month, day;
    EditText weight, height;
    EditText memo;
    EditText hospital_name;
    String result;
    CheckBox fever, cough ,diarrhea;
    TextView tv;
    TextView today;
    int start_year =0 , start_month=0, start_day =0;
    TextView start;
    TextView end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tv = (TextView)findViewById(R.id.date) ;
        today = (TextView)findViewById(R.id.today);

        weight =(EditText)findViewById(R.id.weight);
        height = (EditText)findViewById(R.id.height);
        memo = (EditText)findViewById(R.id.memo);
        memo = (EditText)findViewById(R.id.hospital_name);

        fever = (CheckBox)findViewById(R.id.fever);
        cough =(CheckBox)findViewById(R.id.cough) ;
        diarrhea = (CheckBox)findViewById(R.id.diarrhea);


        fever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printdisease();
            }
        });

        cough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printdisease();
            }
        });

        diarrhea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printdisease();
            }
        });
        Spinner spinner = (Spinner)findViewById(R.id.b_spinner1);
        final ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.hospital, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(WriteDiaryActivity.this, dateSetListener, year, month, day);
                datePickerDialog1.show();
            }
        });

        findViewById(R.id.today).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(WriteDiaryActivity.this, dateSetListener2, year, month, day);
                datePickerDialog2.show();
            }
        });
        today.setText(year+"년 "+(month+1)+"월 "+day+"일 "+ getDayKor() );


//    public void writeDiary() {
//        MyApplication myapp = (MyApplication)getApplicationContext();
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
//        params.put("breakfast", breakfast.getText().toString());
//        params.put("lunch", lunch.getText().toString());
//        params.put("dinner",dinner.getText().toString());
//        params.put("temperature", Integer.parseInt(temperature.toString()));
//        params.put("humid", Integer.parseInt((humidity.toString())));
//        params.put("sleepTime", Integer.parseInt((sleepTime.toString())));
//        params.put("bloodPressure", Integer.parseInt((bloodPressure.toString())));
//        if (drinking.isChecked()) {
//            params.put("drinking", 1);
//        } else {
//            params.put("drinking", 0);
//        }
//        aq.ajax("http://52.41.218.18:8080/writeDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject html, AjaxStatus status) {
//                try {
//                    if (html.getString("msg").equals("정상 작동")) {
//                        Toast.makeText(getApplicationContext(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            start_year = year;
            start_month = monthOfYear +1;
            start_day = dayOfMonth;

            tv.setText(start_year +"/"+ start_month +"/"+ start_day);
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            start_year = year;
            start_month = monthOfYear +1;
            start_day = dayOfMonth;


            today.setText(start_year+"년 "+(start_month)+"월 "+start_day+"일 "+ getChangeDayKor() );
        }
    };

    public static String getDayKor(){
        Calendar cal = Calendar.getInstance();
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = { "일", "월", "화", "수", "목", "금", "토" };

        return "( "+week[cnt]+" )";
    }
    public String getChangeDayKor(){
        Calendar cal= Calendar.getInstance ();
        cal.set(Calendar.YEAR, start_year);
        cal.set(Calendar.MONTH, start_month-1);
        cal.set(Calendar.DATE, start_day);
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = { "일", "월", "화", "수", "목", "금", "토" };

        return "( "+week[cnt]+" )";
    }

    public void printdisease(){
        if(fever.isChecked()){
            result += fever.getText().toString();
        }
        if(cough.isChecked()){
            result+=cough.getText().toString();
        }
        if(diarrhea.isChecked()){
            result+=diarrhea.getText().toString();
        }
    }
}