package com.example.hosea.dr_r_android.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.adapter.DiaryAdapter;
import com.example.hosea.dr_r_android.dao.DiaryVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import im.dacer.androidcharts.ClockPieHelper;
import im.dacer.androidcharts.ClockPieView;

public class ReadDiaryActivity extends AppCompatActivity {
    ArrayList<ClockPieHelper> clockPieHelperArrayList;
    ClockPieView pieView;
    ClockPieView pieView2;
    private Intent previousIntent;
    private AQuery aq = new AQuery(this);
    long result_sleep=0;
    int start_year =0 , start_month=0, start_day =0;
    int year, month, day;
    String date;
    TextView tv , today, sleepTotal, feedTotal;
    SimpleDateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        previousIntent = getIntent();
        dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        clockPieHelperArrayList = new ArrayList<ClockPieHelper>();
        //이름 설정
//        tv = (TextView) findViewById(R.id.tv_listView_title);
//        tv.setText(previousIntent.getStringExtra("u_name"));

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tv = (TextView)findViewById(R.id.date) ;
        sleepTotal = (TextView)findViewById(R.id.tv_sleepTotal);
        feedTotal = (TextView)findViewById(R.id.tv_feedTotal);
        today = (TextView)findViewById(R.id.dateForList);

        date = year+"-"+(month+1)+"-"+day+" ";

        findViewById(R.id.dateForList).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(ReadDiaryActivity.this, dateSetListener2, year, month, day);
                datePickerDialog2.show();
            }
        });

        pieView = (ClockPieView)findViewById(R.id.clock_pie_view);
        pieView2 = (ClockPieView)findViewById(R.id.clock_pie_view2);
        ArrayList<ClockPieHelper> pieHelperArrayList = new ArrayList<ClockPieHelper>();
        pieView.setDate(pieHelperArrayList);

        pieView2.setDate(pieHelperArrayList);

        today.setText(year+"년 "+(month+1)+"월 "+day+"일 "+ getDayKor() );
        set();
        readDiary();

    }
    private void set(){
        readSleep();
    }

    public void readSleep(){
        result_sleep = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", 1);
        params.put("s_start", date+"00:00:00");
        aq.ajax("http://52.41.218.18:8080/getSleepTime", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {
                        clockPieHelperArrayList.clear();
                        jsonArrayToSleepArray(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });
    }

    public void jsonArrayToSleepArray(JSONArray jsonArr) throws JSONException {
        final SimpleDateFormat curHourFormat = new SimpleDateFormat("HH", Locale.KOREA);
        curHourFormat.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        final SimpleDateFormat curMinuteFormat = new SimpleDateFormat("mm", Locale.KOREA);
        final SimpleDateFormat curSecFormat = new SimpleDateFormat("ss", Locale.KOREA);
        for (int i = 0; i < jsonArr.length(); i++) {

            long s_time = Long.parseLong(jsonArr.getJSONObject(i).getString("s_start")) ;
            long e_time = Long.parseLong(jsonArr.getJSONObject(i).getString("s_end")) ;
            result_sleep += e_time-s_time;
            Date start = new Date(s_time );
            Date end = new Date(e_time );
            int s_hour = Integer.parseInt(curHourFormat.format(start));
            int s_min = Integer.parseInt(curMinuteFormat.format(start));
            int s_sec = Integer.parseInt(curSecFormat.format(start));
            int e_hour = Integer.parseInt(curHourFormat.format(end));
            int e_min =  Integer.parseInt(curMinuteFormat.format(end));
            int e_sec =  Integer.parseInt(curSecFormat.format(end));

            clockPieHelperArrayList.add(new ClockPieHelper(s_hour,s_min,s_sec,e_hour,e_min,e_sec));
            pieView.setDate(clockPieHelperArrayList);
            pieView2.setDate(clockPieHelperArrayList);
        }
        sleepTotal.setText( result_sleep / 1000 / 3600 +" 시간 "+ (result_sleep / 1000 % 3600) / 60 +" 분 "+ (result_sleep / 1000 %3600 %60 ) +" 초 ");
        feedTotal.setText( result_sleep / 1000 / 3600 +" 시간 "+ (result_sleep / 1000 % 3600) / 60 +" 분 "+ (result_sleep / 1000 %3600 %60 ) +" 초 "); //임시
    }
    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        params.put("c_date", date+"00:00:00");
        aq.ajax("http://52.41.218.18:8080/getDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                if (html != null) {
                    jsonArrayToArrayList(html);
                } else {

                }
            }
        });
    }
    public void jsonArrayToArrayList(JSONObject jsonObject) {
        ArrayList<DiaryVO> arrayList = new ArrayList<>();

        arrayList.add(new DiaryVO(jsonObject));


        linktoAdapter(arrayList);
    }


    public void linktoAdapter(ArrayList<DiaryVO> list) {
        //어댑터 생성
        DiaryAdapter diaryAdapter = new DiaryAdapter(this, R.layout.itemsfordiarylist, list);
        //어댑터 연결
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(diaryAdapter);
    }

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

    private DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            start_year = year;
            start_month = monthOfYear +1;
            start_day = dayOfMonth;
            date = start_year+"-"+start_month+"-"+start_day+" ";
            clockPieHelperArrayList.clear();
            pieView.setDate(clockPieHelperArrayList);
            pieView2.setDate(clockPieHelperArrayList);
            set();
            readDiary();
            today.setText(start_year+"년 "+(start_month)+"월 "+start_day+"일 "+ getChangeDayKor() );
        }
    };
}