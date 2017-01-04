package com.example.hosea.dr_r_android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.activity.TimeActivity;
import com.example.hosea.dr_r_android.adapter.SleepAdapter;
import com.example.hosea.dr_r_android.dao.SleepVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hosea on 2016-12-29.
 */

public class SleepTimeFragment extends Fragment {

    private AQuery aq = new AQuery(getActivity());
    private TextView myOutput, myToday, myToggle;
    long startTime ,endTime;
    private SleepAdapter sleepAdapter;
    private ArrayList<SleepVO> sleepDataList;
    Date s_start;
    Date s_end;
    long outTime;
    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;
    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    long myBaseTime;
    long myPauseTime;
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    Date date = new Date();
    int year, month, day;
    SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

    public SleepTimeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sleeptime, container, false);
        myToday = (TextView) view.findViewById(R.id.today_sleep);
        myOutput = (TextView) view.findViewById(R.id.time_out);
        myToggle = (TextView) view.findViewById(R.id.sleep_toggle);
        myToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });
        final ListView listView = (ListView) view.findViewById(R.id.sleep_listView);
        //현재 날짜 받아오기
        // 년,월,일로 쪼갬
        year = Integer.parseInt(curYearFormat.format(date));
        month =Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));
        sleepDataList = new ArrayList<>();
        sleepDataList.add(new SleepVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(22123123), 3));
        sleepDataList.add(new SleepVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(12123123), 2));
        sleepDataList.add(new SleepVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(11123123), 1));
        sleepAdapter = new SleepAdapter(view.getContext(),R.layout.itemsforsleeplist, sleepDataList);
        listView.setAdapter(sleepAdapter); // uses the view to get the context instead of getActivity().

        myToday.setText(year+"년 "+(month)+"월 "+day+"일 "+ getDayKor() );
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void myOnClick(View v) {
        switch (v.getId()) {
            case R.id.sleep_toggle: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                switch (cur_Status) {
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        s_start = new Date();
                        startTime = s_start.getTime()/1000;
                        System.out.println(myBaseTime);
                        //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                        myTimer.sendEmptyMessage(0);
                        myToggle.setText("기록 중지"); //버튼의 문자"시작"을 "멈춤"으로 변경

                        cur_Status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run:
                        myTimer.removeMessages(0); //핸들러 메세지 제거
                        myPauseTime = SystemClock.elapsedRealtime();
                        s_end = new Date();
                        endTime = s_end.getTime() /1000;
                        myToggle.setText("기록 시작");
                        cur_Status = Pause;
                        outTime = 0;
                        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 3600 , (outTime / 1000 % 3600) / 60, (outTime / 1000 %3600 %60 ));
                        myOutput.setText(easy_outTime);
                        writeDiary();
                        break;
                    case Pause:
                        myBaseTime = SystemClock.elapsedRealtime();
                        s_start = new Date();
                        startTime = s_start.getTime()/1000;
                        myTimer.sendEmptyMessage(0);
                        myToggle.setText("기록 중지");
                        cur_Status = Run;
                        break;
                }
                break;
        }
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
    public void writeDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        //params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        params.put("u_id" , ((TimeActivity)getActivity()).u_id);
        params.put("s_start" , dateFormat.format(s_start));
        params.put("s_end" , dateFormat.format(s_end));
        params.put("s_total" , endTime-startTime);
        aq.ajax("http://52.41.218.18:8080/addSleepTime", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                try {
                    if (html.getString("msg").equals("정상 작동")) {
                        Toast.makeText(getActivity(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void readSleep() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", ((TimeActivity)getActivity()).u_id);
        params.put("s_start", date + "00:00:00");
        aq.ajax("http://52.41.218.18:8080/getSleepTimeByDate", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {
                        Toast.makeText(getActivity(),html.toString(), Toast.LENGTH_SHORT).show();
                        sleepDataList.clear();
                        jsonArrayToSleepArray(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(),"연결상태가 좋지않아 목록을 부를 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void jsonArrayToSleepArray(JSONArray jsonArr) throws JSONException {
        for (int i = 0; i < jsonArr.length(); i++) {

        }
    }
}

