package com.coawesome.hosea.dr_r.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.activity.TimeActivity;
import com.coawesome.hosea.dr_r.adapter.FeedAdapter;
import com.coawesome.hosea.dr_r.dao.FeedVO;
import com.coawesome.hosea.dr_r.dao.ResponseVO;
import com.coawesome.hosea.dr_r.dao.SleepVO;
import com.google.gson.Gson;

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

public class FeedTimeFragment extends Fragment {
    private AQuery aq;
    private TextView myOutput, myToday, myToggle, myPowderToggle;
    private ImageView myCircle,myPowderCircle;
    private FeedAdapter feedAdapter;
    private ArrayList<FeedVO> feedDataList;
    Date today;
    String user_id;
    long startTime ,endTime;
    Date s_start;
    Date s_end;
    long outTime;
    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;
    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    Boolean clicked_sleep;
    Boolean clicked_feed;
    long myBaseTime;
    long myPauseTime;
    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    private String feed;
    Date date = new Date();
    int year, month, day;
    SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    EditText powderAmount;
    public FeedTimeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        aq = new AQuery(getActivity());
        clicked_sleep = false;
        clicked_feed = true;
        user_id = getActivity().getIntent().getStringExtra("userId");
        feed = "좌";
        final View view = inflater.inflate(R.layout.fragment_feedtime, container, false);
        myToday = (TextView) view.findViewById(R.id.today_feed);
        myOutput = (TextView) view.findViewById(R.id.time_out_feed);
        myToggle = (TextView) view.findViewById(R.id.feed_toggle);
        myCircle = (ImageView) view.findViewById(R.id.feed_toggle_img);
        myCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });
        myToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });

        myPowderToggle = (TextView) view.findViewById(R.id.powder_toggle);
        myPowderCircle = (ImageView) view.findViewById(R.id.powder_toggle_img);
        myPowderCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });
        myPowderToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClick(v);
            }
        });

        powderAmount = (EditText) view.findViewById(R.id.powder_amount);
        final ListView listView = (ListView) view.findViewById(R.id.feed_listView);
        final Button right = (Button) view.findViewById(R.id.feed_right);
        final Button left = (Button) view.findViewById(R.id.feed_left);
        final Button powder = (Button) view.findViewById(R.id.feed_powder);
        final RelativeLayout feedLayout = (RelativeLayout) view.findViewById(R.id.feed);
        final RelativeLayout powderLayout = (RelativeLayout) view.findViewById(R.id.powder);

        powderLayout.setVisibility(View.GONE);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right.setAlpha(1);
                left.setAlpha(0.3f);
                powder.setAlpha(0.3f);
                feed = "우";
                powderLayout.setVisibility(View.GONE);
                feedLayout.setVisibility(View.VISIBLE);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right.setAlpha(0.3f);
                left.setAlpha(1);
                powder.setAlpha(0.3f);
                feed = "좌";
                powderLayout.setVisibility(View.GONE);
                feedLayout.setVisibility(View.VISIBLE);
            }
        });
        powder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right.setAlpha(0.3f);
                left.setAlpha(0.3f);
                powder.setAlpha(1f);
                feed = "분유";
                feedLayout.setVisibility(View.GONE);
                powderLayout.setVisibility(View.VISIBLE);
            }
        });

        //초기화
        feed="좌";
        right.setAlpha(0.3f);
        left.setAlpha(1);
        powder.setAlpha(0.3f);
        feedDataList = new ArrayList<>();
//        feedDataList.add(new FeedVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(12123123), 3,"우"));
//        feedDataList.add(new FeedVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(12123123), 3,"우"));
//        feedDataList.add(new FeedVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(12123123), 3,"우"));
//        feedDataList.add(new FeedVO(1,2,"ghtpdk",new Timestamp(12123123), new Timestamp(12123123), 3,"우"));
        feedAdapter = new FeedAdapter(view.getContext(),R.layout.itemsforfeedlist, feedDataList);
        try {
            listView.setAdapter(feedAdapter); // uses the view to get the context instead of getActivity().
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (feedDataList.get(position).getfType().equals("분유")) {
                        LayoutInflater inflater=getActivity().getLayoutInflater();
                        final View dialogView= inflater.inflate(R.layout.dialog_change_feed, null);
                        AlertDialog.Builder buider= new AlertDialog.Builder(getActivity()); //AlertDialog.Builder 객체 생성
                        buider.setTitle("분유 량 변경"); //Dialog 제목
                        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                        buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) dialogView.findViewById(R.id.dialog_change_feed);

                                if (!editText.getText().toString().equals("")) {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("userId", user_id);
                                    params.put("fType", feed);
                                    params.put("fStart", feedDataList.get(position).getfStart());
                                    params.put("fStartTime", feedDataList.get(position).getfStartTime());
                                    params.put("fTotal", editText.getText().toString());
                                    aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Feed")
                                            .post(params)
                                            .showLoading()
                                            .response((response, error) -> {
                                                if (response != null) {
                                                    Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                                    readFeed();
                                                } else {
                                                    Toast.makeText(getActivity(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        buider.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "분유량 변경이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog dialog=buider.create();
                        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                        dialog.show();
                    }
                    return false;
                }
            });
        } catch (NullPointerException ignored) {

        }

        //현재 날짜 받아오기
        // 년,월,일로 쪼갬
        year = Integer.parseInt(curYearFormat.format(date));
        month =Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));

        myToday.setText(year+"년 "+(month)+"월 "+day+"일 "+ getDayKor());
        readFeed();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        final Button feeding = (Button) ((TimeActivity) getActivity()).findViewById(R.id.measureFeedingTime);
        final Button sleeping = (Button) ((TimeActivity) getActivity()).findViewById(R.id.measureSleepingTime);


        feeding.setAlpha(1);
        sleeping.setAlpha(0.3f);
        feeding.setClickable(false);
        sleeping.setClickable(true);
        sleeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_feed = false;
                clicked_sleep = true;
                final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (cur_Status == Run) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("수유시간 측정 중입니다");
                    alert.setMessage("저장하지 않고 넘어가시겠습니까?");

                    alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            fragmentTransaction.replace(R.id.time_fragment, new SleepTimeFragment()).commit();
                            myTimer.removeMessages(0);
                            cur_Status = Init;
                        }
                    });

                    alert.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    feeding.setAlpha(1);
                                    sleeping.setAlpha(0.3f);
                                }
                            });

                    alert.show();
                } else if (clicked_sleep){
                    fragmentTransaction.replace(R.id.time_fragment, new SleepTimeFragment()).commit();
                }
            }
        });



        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(cur_Status == Run) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("수유시간 측정 중입니다");
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
            case R.id.feed_toggle: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현
            case R.id.feed_toggle_img:
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
                        writeFeed();
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
            case R.id.powder_toggle:
            case R.id.powder_toggle_img:
                String pAmount = powderAmount.getText().toString();
                if(!pAmount.isEmpty() && !pAmount.equals("")) {
                    writePowder(Integer.parseInt(pAmount));
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
        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 3600 , (outTime / 1000 % 3600) / 60, (outTime / 1000 %3600 %60 ));
        return easy_outTime;

    }

    public static String getDayKor(){
        Calendar cal = Calendar.getInstance();
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = { "일", "월", "화", "수", "목", "금", "토" };

        return "( "+week[cnt]+" )";
    }
    public void writeFeed() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user_id);
        params.put("fType", feed);
        params.put("fStart", dateFormat.format(s_start));
        params.put("fStartTime", String.valueOf(s_start.getTime()));
        params.put("fEnd", dateFormat.format(s_end));
        params.put("fTotal", String.valueOf(endTime - startTime));
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Feed")
                .post(params)
                .showLoading()
                .response((response, error) -> {
                    if (response != null) {
                        Toast.makeText(getActivity(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                        readFeed();
                    } else {
                        Toast.makeText(getActivity(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void readFeed() {
        year = Integer.parseInt(curYearFormat.format(date));
        month = Integer.parseInt(curMonthFormat.format(date));
        day = Integer.parseInt(curDayFormat.format(date));
        Date rStart = new Date(year+"/"+month+"/"+day+"/00:00:00");
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Feed?userId=" + user_id + "&fStartTime=" + rStart.getTime())
                .get()
                .showLoading()
                .response((response, error) -> {
                    Gson gson = new Gson();
                    ResponseVO resVO = gson.fromJson(response, ResponseVO.class);
                    if(resVO.getItems().length>0){
                        feedDataList.clear();
                        try {
                            jsonArrayToFeedArray(resVO.getItems());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                });
    }
    public void writePowder(int amount) {
        today = new Date();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", user_id);
        params.put("fType", feed);
        params.put("fStart", dateFormat.format(today));
        params.put("fStartTime", String.valueOf(today.getTime()));
        params.put("fTotal", String.valueOf(amount));
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Feed")
                .post(params)
                .showLoading()
                .response((response, error) -> {
                    if (response != null) {
                        Toast.makeText(getActivity(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                        powderAmount.setText("");
                        readFeed();
                    } else {
                        Toast.makeText(getActivity(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void jsonArrayToFeedArray(Object[] jsonArr) throws JSONException {
        Gson gson = new Gson();
        for (int i = 0; i < jsonArr.length; i++) {
            String json = gson.toJson(jsonArr[i]);
            feedDataList.add(gson.fromJson(json, FeedVO.class));
            feedAdapter.notifyDataSetChanged();
        }
    }
}

