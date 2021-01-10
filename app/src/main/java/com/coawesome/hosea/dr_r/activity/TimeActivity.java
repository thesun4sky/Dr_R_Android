package com.coawesome.hosea.dr_r.activity;

/**
 * Created by LeeMoonSeong on 2016-12-14.
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.fragment.FeedTimeFragment;
import com.coawesome.hosea.dr_r.fragment.SleepTimeFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;


public class TimeActivity extends AppCompatActivity {
    private Intent previousIntent;
    Button sleeping;
    Button feeding;
    final SleepTimeFragment sleepTimeFragment = new SleepTimeFragment();
    final FeedTimeFragment feedTimeFragment = new FeedTimeFragment();
    final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    public String userId;

    FragmentManager fm = getFragmentManager();
    TextView myOutput;
    TextView myToday;
    TextView myToggle;
    long outTime;
    final static int Init = 0;
    long myBaseTime;
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    Date date = new Date();
    int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        previousIntent = getIntent();
        userId = previousIntent.getStringExtra("userId");
         sleeping = (Button) findViewById(R.id.measureSleepingTime);
          feeding = (Button) findViewById(R.id.measureFeedingTime);


        sleeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.time_fragment, sleepTimeFragment).commit();
            }
        });

        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.time_fragment, feedTimeFragment).commit();
            }
        });
        sleeping.setAlpha(1);
        feeding.setAlpha(0.3f);
        myOutput = (TextView) findViewById(R.id.time_out);
        myToday = (TextView) findViewById(R.id.today_sleep);
        myToggle = (TextView) findViewById(R.id.sleep_toggle);

        //현재 날짜 받아오기
        // 년,월,일로 쪼갬
        year = Integer.parseInt(curYearFormat.format(date));
        month =Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));

          myToday.setText(year+"년 "+(month)+"월 "+day+"일 "+ getDayKor() );

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    protected void switchFragment(int id) {
        final android.app.FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        if (id == 0) {
            fragmentTransaction.replace(R.id.time_fragment, sleepTimeFragment).addToBackStack(null);
        }
        else if (id == 1) {
            fragmentTransaction.replace(R.id.time_fragment, sleepTimeFragment).addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    Handler myTimer = new Handler() {
        public void handleMessage(Message msg) {
            myOutput.setText(getTimeOut());

            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            myTimer.sendEmptyMessage(0);
        }
    };

    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut() {
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 3600 , (outTime / 1000 % 3600) / 60, (outTime / 1000 %3600 %60 ));
        return easy_outTime;

    }

    public static String getDayKor(){
        Calendar cal = Calendar.getInstance();
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = { "일", "월", "화", "수", "목", "금", "토" };

        return "( "+week[cnt]+" )";
    }
}