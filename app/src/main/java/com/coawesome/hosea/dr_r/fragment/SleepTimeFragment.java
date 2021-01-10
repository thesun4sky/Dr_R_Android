package com.coawesome.hosea.dr_r.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.activity.JoinActivity;
import com.coawesome.hosea.dr_r.activity.LoginActivity;
import com.coawesome.hosea.dr_r.activity.TimeActivity;
import com.coawesome.hosea.dr_r.activity.WriteDiaryActivity;
import com.coawesome.hosea.dr_r.adapter.SleepAdapter;
import com.coawesome.hosea.dr_r.dao.DiaryVO;
import com.coawesome.hosea.dr_r.dao.ResponseVO;
import com.coawesome.hosea.dr_r.dao.SleepVO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

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

    private AQuery aq;
    private TextView myOutput, myToday, myToggle;
    private ImageView myCircle;
    long startTime, endTime;
    private SleepAdapter sleepAdapter;
    private ArrayList<SleepVO> sleepDataList;
    String user_id = "";
    Date s_start;
    Date s_end;

    long outTime;
    Boolean clicked_sleep;
    Boolean clicked_feed;
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

    public SleepTimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        aq = new AQuery(getActivity());
        clicked_sleep = true;
        clicked_feed = false;
        final View view = inflater.inflate(R.layout.fragment_sleeptime, container, false);
        user_id = getActivity().getIntent().getStringExtra("userId");;
        myToday = (TextView) view.findViewById(R.id.today_sleep);
        myOutput = (TextView) view.findViewById(R.id.time_out);
        myToggle = (TextView) view.findViewById(R.id.sleep_toggle);
        myToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });
        myCircle = (ImageView) view.findViewById(R.id.sleep_toggle_img);
        myCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });

        final ListView listView = (ListView) view.findViewById(R.id.sleep_listView);
        //현재 날짜 받아오기
        // 년,월,일로 쪼갬
        year = Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));
        sleepDataList = new ArrayList<>();
        sleepAdapter = new SleepAdapter(view.getContext(), R.layout.itemsforsleeplist, sleepDataList);
        listView.setAdapter(sleepAdapter); // uses the view to get the context instead of getActivity().

        myToday.setText(year + "년 " + (month) + "월 " + day + "일 " + getDayKor());


        readSleep();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        if (getView() == null) {
            return;
        }

        final Button feeding = (Button) ((TimeActivity) getActivity()).findViewById(R.id.measureFeedingTime);
        final Button sleeping = (Button) ((TimeActivity) getActivity()).findViewById(R.id.measureSleepingTime);


        sleeping.setClickable(false);
        feeding.setClickable(true);
        feeding.setAlpha(0.3f);
        sleeping.setAlpha(1);
        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked_feed = true;
                clicked_sleep = false;
                if (cur_Status == Run) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("수면시간 측정 중입니다");
                    alert.setMessage("저장하지 않고 넘어가시겠습니까?");

                    alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().getFragmentManager().beginTransaction().replace(R.id.time_fragment, new FeedTimeFragment()).commit();
                            myTimer.removeMessages(0);
                            cur_Status = Init;
                        }
                    });

                    alert.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    sleeping.setAlpha(1);
                                    feeding.setAlpha(0.3f);
                                }
                            });

                    alert.show();
                } else if(clicked_feed){
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.time_fragment, new FeedTimeFragment()).commit();
                }
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (cur_Status == Run) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("수면시간 측정 중입니다");
                        alert.setMessage("기록을 저장하지 않고 종료하시겠습니까?");

                        alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getActivity().finish();
                            }
                        });

                        alert.setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                });

                        alert.show();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void myOnClick(View v) {
        switch (v.getId()) {
            case R.id.sleep_toggle: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
            case R.id.sleep_toggle_img:
                switch (cur_Status) {
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        s_start = new Date();
                        startTime = s_start.getTime() / 1000;
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
                        endTime = s_end.getTime() / 1000;
                        myToggle.setText("기록 시작");
                        cur_Status = Pause;
                        outTime = 0;
                        String easy_outTime = String.format(
                                "%02d:%02d:%02d", outTime / 1000 / 3600, (outTime / 1000 % 3600) / 60
                                , (outTime / 1000 % 3600 % 60));
                        myOutput.setText(easy_outTime);
                        writeSleep();
                        break;
                    case Pause:
                        myBaseTime = SystemClock.elapsedRealtime();
                        s_start = new Date();
                        startTime = s_start.getTime() / 1000;
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
            switch (msg.what) {
                case 0:
                    myOutput.setText(getTimeOut());
                    this.sendEmptyMessageDelayed(0, 1000);
                    CircleTouchAnimation();
                    break;
            }
        }
    };

    private void CircleTouchAnimation() {
        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1000);
        animation.setStartOffset(30);
        animation.setStartOffset(1);

        if (cur_Status == Run) {
            myCircle.startAnimation(animation);
        }
    }


    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut() {
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 3600, (outTime / 1000 % 3600) / 60, (outTime / 1000 % 3600 % 60));
        return easy_outTime;
    }

    public static String getDayKor() {
        Calendar cal = Calendar.getInstance();
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        return "( " + week[cnt] + " )";
    }

    public void writeSleep() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user_id);
        params.put("sStart", dateFormat.format(s_start));
        params.put("sStartTime", String.valueOf(s_start.getTime()));
        params.put("sEnd", dateFormat.format(s_end));
        params.put("sTotal", String.valueOf(endTime - startTime));
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Sleep")
                .post(params)
                .showLoading()
                .response((response, error) -> {
                    if (response != null) {
                        Toast.makeText(getActivity(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                        readSleep();
                    } else {
                        Toast.makeText(getActivity(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void readSleep() {
        Map<String, Object> params = new HashMap<String, Object>();
        year = Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));
        Date rStart = new Date(year+"/"+month+"/"+day+"/00:00:00");
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Sleep?userId=" + user_id + "&sStartTime=" + rStart.getTime())
                .get()
                .showLoading()
                .response((response, error) -> {
                    Gson gson = new Gson();
                    ResponseVO resVO = gson.fromJson(response, ResponseVO.class);
                    if(resVO.getItems().length>0){
                        sleepDataList.clear();
                        try {
                            jsonArrayToSleepArray(resVO.getItems());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                });
    }

    public void jsonArrayToSleepArray(Object[] jsonArr) throws JSONException {
        Gson gson = new Gson();
        for (int i = 0; i < jsonArr.length; i++) {
            String json = gson.toJson(jsonArr[i]);
            sleepDataList.add(gson.fromJson(json, SleepVO.class));
            sleepAdapter.notifyDataSetChanged();
        }
    }
}

