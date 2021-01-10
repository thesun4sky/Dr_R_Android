package com.coawesome.hosea.dr_r.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.adapter.DiaryAdapter;
import com.coawesome.hosea.dr_r.dao.DiaryInfoVO;
import com.coawesome.hosea.dr_r.dao.DiaryVO;
import com.coawesome.hosea.dr_r.dao.ResponseVO;
import com.coawesome.hosea.dr_r.dao.SleepVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import im.dacer.androidcharts.ClockPieHelper;
import im.dacer.androidcharts.ClockPieView;

public class ReadDiaryActivity extends AppCompatActivity {
    ArrayList<ClockPieHelper> clockPieHelperArrayListForSleep;
    ArrayList<ClockPieHelper> clockPieHelperArrayListForFeed;
    ClockPieView pieView;
    ClockPieView pieView2;
    private Intent previousIntent;
    private AQuery aq;
    long result_sleep = 0;
    long result_feed = 0;

    int start_year = 0, start_month = 0, start_day = 0;
    String date;
    TextView tv, today, sleepTotal, feedTotal , powderTotal;
    SimpleDateFormat dateFormat;
    DiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        previousIntent = getIntent();
        aq = new AQuery(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        clockPieHelperArrayListForSleep = new ArrayList<ClockPieHelper>();
        clockPieHelperArrayListForFeed = new ArrayList<ClockPieHelper>();
        //이름 설정
//        tv = (TextView) findViewById(R.id.tv_listView_title);
//        tv.setText(previousIntent.getStringExtra("u_name"));

        GregorianCalendar calendar = new GregorianCalendar();
        start_year = calendar.get(Calendar.YEAR);
        start_month = calendar.get(Calendar.MONTH);
        start_day = calendar.get(Calendar.DAY_OF_MONTH);
        tv = (TextView) findViewById(R.id.date);
        sleepTotal = (TextView) findViewById(R.id.tv_sleepTotal);
        feedTotal = (TextView) findViewById(R.id.tv_feedTotal);
        powderTotal = (TextView)findViewById(R.id.tv_powder);
        today = (TextView) findViewById(R.id.dateForList);

        int real_mon = (start_month + 1);
        String monStr = (real_mon<10)? "0"+real_mon : ""+real_mon;
        String dayStr = (start_day<10)? "0"+start_day : ""+start_day;

        date = start_year + "-" + monStr + "-" + dayStr + " ";

        findViewById(R.id.dateForList).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(ReadDiaryActivity.this, dateSetListener2, start_year, start_month, start_day);
                datePickerDialog2.show();
            }
        });

        pieView = (ClockPieView) findViewById(R.id.clock_pie_view);
        pieView2 = (ClockPieView) findViewById(R.id.clock_pie_view2);
        ArrayList<ClockPieHelper> pieHelperArrayListForSleep = new ArrayList<ClockPieHelper>();
        ArrayList<ClockPieHelper> pieHelperArrayListForFeed = new ArrayList<ClockPieHelper>();
        pieView.setDate(pieHelperArrayListForSleep);

        pieView2.setDate(pieHelperArrayListForFeed);

        today.setText(start_year + "년 " + real_mon + "월 " + start_day + "일 " + getDayKor());
        set();
        readDiary();

    }

    public int getUserId(){
        return previousIntent.getIntExtra("u_id",0);
    }

    private void set() {
        readSleep();
        readFeed();
    }

    public void readSleep() {
        result_sleep = 0;
        clockPieHelperArrayListForSleep.clear();
        sleepTotal.setText("수면 데이터 읽어오는 중...");
        String userId = previousIntent.getStringExtra("userId");
        int real_month = start_month + 1;
        Date rStart = new Date(start_year+"/"+real_month+"/"+start_day+"/00:00:00");
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Sleep?userId=" + userId + "&sStartTime=" + rStart.getTime())
                .get()
                .showLoading()
                .response((response, error) -> {
                    Gson gson = new Gson();
                    ResponseVO resVO = gson.fromJson(response, ResponseVO.class);
                    if(resVO.getItems().length>0){
                        try {
                            jsonArrayToSleepArray(resVO.getItems());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sleepTotal.setText("0 시간 0 분 0 초");
                    }
                });
        /*aq.ajax("http://52.205.170.152:8080/getSleepTimeByDate", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {
                        clockPieHelperArrayListForSleep.clear();
                        jsonArrayToSleepArray(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });*/
    }

    public void readFeed() {
        result_feed = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id",0));
        params.put("f_start", date + "00:00:00");
        /*aq.ajax("http://52.205.170.152:8080/getFeedTimeByDate", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {

                        clockPieHelperArrayListForFeed.clear();
                        jsonArrayToFeedArray(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });*/
    }

    public void jsonArrayToSleepArray(Object[] jsonArr) throws JSONException, ParseException {
        Gson gson = new Gson();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat curHourFormat = new SimpleDateFormat("HH");
        curHourFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int i = 0; i < jsonArr.length; i++) {
            String json = gson.toJson(jsonArr[i]);
            SleepVO vo = gson.fromJson(json, SleepVO.class);
            long s_time = Objects.requireNonNull(transFormat.parse(vo.getsStart())).getTime();
            long e_time = Objects.requireNonNull(transFormat.parse(vo.getsEnd())).getTime();
            result_sleep += e_time - s_time;
            Date start = transFormat.parse(vo.getsStart());
            Date end = transFormat.parse(vo.getsEnd());
            int s_hour = start.getHours();
            int s_min = start.getMinutes();
            int s_sec = start.getSeconds();
            int e_hour = end.getHours();
            int e_min = end.getMinutes();
            int e_sec = end.getSeconds();

            clockPieHelperArrayListForSleep.add(new ClockPieHelper(s_hour, s_min, s_sec, e_hour, e_min, e_sec));
            pieView.setDate(clockPieHelperArrayListForSleep);
        }
        sleepTotal.setText(result_sleep / 1000 / 3600 + " 시간 " + (result_sleep / 1000 % 3600) / 60 + " 분 " + (result_sleep / 1000 % 3600 % 60) + " 초 ");
    }

    //수유시간 리스트 받아오기
    public void jsonArrayToFeedArray(JSONArray jsonArr) throws JSONException {
        long result_powder = 0;
        final SimpleDateFormat curHourFormat = new SimpleDateFormat("HH", Locale.KOREA);
        curHourFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        final SimpleDateFormat curMinuteFormat = new SimpleDateFormat("mm", Locale.KOREA);
        final SimpleDateFormat curSecFormat = new SimpleDateFormat("ss", Locale.KOREA);
        for (int i = 0; i < jsonArr.length(); i++) {
            if(jsonArr.getJSONObject(i).getString("feed").equals("분유")){
                result_powder += Long.parseLong(jsonArr.getJSONObject(i).getString("f_total"));
                long powder_time = Long.parseLong(jsonArr.getJSONObject(i).getString("f_start"));
                Date powder_start = new Date(powder_time);
                int s_hour = Integer.parseInt(curHourFormat.format(powder_start));
                int s_min = Integer.parseInt(curMinuteFormat.format(powder_start));
                int s_sec = Integer.parseInt(curSecFormat.format(powder_start));
                clockPieHelperArrayListForFeed.add(new ClockPieHelper(s_hour, s_min, s_sec, s_hour, s_min+1, s_sec));
                pieView2.setDate(clockPieHelperArrayListForFeed);

            }
            else {
                long s_time = Long.parseLong(jsonArr.getJSONObject(i).getString("f_start"));
                long e_time = Long.parseLong(jsonArr.getJSONObject(i).getString("f_end"));
                result_feed += e_time - s_time;
                Date start = new Date(s_time);
                Date end = new Date(e_time);
                int s_hour = Integer.parseInt(curHourFormat.format(start));
                int s_min = Integer.parseInt(curMinuteFormat.format(start));
                int s_sec = Integer.parseInt(curSecFormat.format(start));
                int e_hour = Integer.parseInt(curHourFormat.format(end));
                int e_min = Integer.parseInt(curMinuteFormat.format(end));
                int e_sec = Integer.parseInt(curSecFormat.format(end));

                clockPieHelperArrayListForFeed.add(new ClockPieHelper(s_hour, s_min, s_sec, e_hour, e_min, e_sec));
                pieView2.setDate(clockPieHelperArrayListForFeed);
            }
        }
        feedTotal.setText(result_feed / 1000 / 3600 + " 시간 " + (result_feed / 1000 % 3600) / 60 + " 분 " + (result_feed / 1000 % 3600 % 60) + " 초 ");
        powderTotal.setText(result_powder+"ml");
    }

    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = previousIntent.getStringExtra("userId");
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Diary?userId=" + userId + "&wDate=" + date)
                .get()
                .showLoading()
                .response((response, error) -> {
                    Gson gson = new Gson();
                    ResponseVO resVO = gson.fromJson(response, ResponseVO.class);
                    if(resVO.getItems().length>0){
                        jsonArrayToArrayList(resVO.getItems()[0]);
                    } else {
                        Toast.makeText(getApplicationContext(),"해당날짜의 일지가 없습니다.",Toast.LENGTH_SHORT).show();
                        jsonArrayToArrayListNoData();
                    }
                });
        /*aq.ajax("http://52.205.170.152:8080/getDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                if (html != null) {
                    jsonArrayToArrayList(html);
                } else {
                   Toast.makeText(getApplicationContext(),"해당날짜의 일지가 없습니다.",Toast.LENGTH_SHORT).show();
                    jsonArrayToArrayListNoData();
                }
            }
        });*/
    }

    public void jsonArrayToArrayList(Object jsonObject) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        DiaryVO vo = gson.fromJson(json, DiaryVO.class);
        ArrayList<DiaryInfoVO> arrayList = new ArrayList<>();
        arrayList.add(gson.fromJson(vo.getwDiary(), DiaryInfoVO.class));
        linktoAdapter(arrayList);
    }

    public void jsonArrayToArrayListNoData( ) {
        ArrayList<DiaryInfoVO> arrayList = new ArrayList<>();
        linktoAdapter(arrayList);
    }


    public void linktoAdapter(ArrayList<DiaryInfoVO> list) {
        //어댑터 생성
        DiaryAdapter diaryAdapter = new DiaryAdapter(this, R.layout.itemsfordiarylist, list , previousIntent.getStringExtra("userId"));
        //어댑터 연결
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(diaryAdapter);
    }

    public static String getDayKor() {
        Calendar cal = Calendar.getInstance();
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        return "( " + week[cnt] + " )";
    }

    public String getChangeDayKor() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, start_year);
        cal.set(Calendar.MONTH, start_month - 1);
        cal.set(Calendar.DATE, start_day);
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        return "( " + week[cnt] + " )";
    }

    private DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            start_year = year;
            start_month = monthOfYear;
            int real_month = start_month + 1;
            start_day = dayOfMonth;

            String monStr = (real_month<10)? "0"+real_month : ""+real_month;
            String dayStr = (start_day<10)? "0"+start_day : ""+start_day;

            date = start_year + "-" + monStr + "-" + dayStr + " ";
            clockPieHelperArrayListForSleep.clear();
            clockPieHelperArrayListForFeed.clear();
            pieView.setDate(clockPieHelperArrayListForSleep);
            pieView2.setDate(clockPieHelperArrayListForFeed);
            set();
            readDiary();
            today.setText(start_year + "년 " + (real_month) + "월 " + start_day + "일 " + getChangeDayKor());
        }
    };


}