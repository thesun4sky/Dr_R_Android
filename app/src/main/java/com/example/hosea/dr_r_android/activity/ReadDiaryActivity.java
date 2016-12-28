package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import im.dacer.androidcharts.ClockPieHelper;
import im.dacer.androidcharts.ClockPieView;

public class ReadDiaryActivity extends AppCompatActivity {
    ArrayList<ClockPieHelper> clockPieHelperArrayList;
    ClockPieView pieView;
    private Intent previousIntent;
    private AQuery aq = new AQuery(this);
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        previousIntent = getIntent();

        //이름 설정
        tv = (TextView) findViewById(R.id.tv_listView_title);
        tv.setText(previousIntent.getStringExtra("u_name"));

        pieView = (ClockPieView)findViewById(R.id.clock_pie_view);
        ArrayList<ClockPieHelper> pieHelperArrayList = new ArrayList<ClockPieHelper>();
        pieView.setDate(pieHelperArrayList);

        set();
        readDiary();

    }
    private void set(){
        clockPieHelperArrayList = new ArrayList<ClockPieHelper>();
        readSleep();
    }

    public void readSleep(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", 1);
        params.put("s_start", "2016-12-14 12:12:12");
        aq.ajax("http://52.41.218.18:8080/getSleepTime", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {
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
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        final SimpleDateFormat curHourFormat = new SimpleDateFormat("HH", Locale.KOREA);
        curHourFormat.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        final SimpleDateFormat curMinuteFormat = new SimpleDateFormat("mm", Locale.KOREA);
        final SimpleDateFormat curSecFormat = new SimpleDateFormat("ss", Locale.KOREA);
        for (int i = 0; i < jsonArr.length(); i++) {

            long s_time = Long.parseLong(jsonArr.getJSONObject(i).getString("s_start")) ;
            long e_time = Long.parseLong(jsonArr.getJSONObject(i).getString("s_end")) ;
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
        }
    }
    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        aq.ajax("http://52.41.218.18:8080/getDiaries", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    jsonArrayToArrayList(html);
                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });
    }
    public void jsonArrayToArrayList(JSONArray jsonArr) {
        ArrayList<DiaryVO> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                arrayList.add(new DiaryVO(jsonArr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        linktoAdapter(arrayList);
    }


    public void linktoAdapter(ArrayList<DiaryVO> list) {
        //어댑터 생성
        DiaryAdapter diaryAdapter = new DiaryAdapter(this, R.layout.itemsfordiarylist, list);
        //어댑터 연결
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(diaryAdapter);
    }
}