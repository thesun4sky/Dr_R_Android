package com.coawesome.hosea.dr_r.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.dao.DiaryInfoVO;
import com.coawesome.hosea.dr_r.dao.ResponseVO;
import com.coawesome.hosea.dr_r.dao.UserVO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Hosea on 2016-11-01.
 */

public class DiaryAdapter extends BaseAdapter {
    private AQuery aq;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private Context dContext;
    private int dResource;
    private String dUserId;
    private String dShowDate;
    private TextView age;
    ProgressBar img_progress;
    private ArrayList<DiaryInfoVO> dItems = new ArrayList<>();
    private static final String RED = "#FF0000";
    private static final String BLUE = "#0000FF";
    private UserVO userVO;
    private DiaryInfoVO diary;

    public void getExpectedDate() {

        //User정보 받아오기
        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-User?userId="+dUserId)
                .get()
                .showLoading()
                .response((response, error) -> {
                    Gson gson = new Gson();
                    ResponseVO resVO = gson.fromJson(response, ResponseVO.class);
                    String json = gson.toJson(resVO.getItems()[0]);
                    this.userVO = gson.fromJson(json, UserVO.class);
                    try {
                        setDate(userVO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void setDate(UserVO userVO) throws JSONException, ParseException {
        calAge(userVO.getuExpectedDate());
    }

    public void calAge(String expectedDateStr) throws JSONException, ParseException {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = transFormat.parse(dShowDate.substring(0,10));
        Date expectedDate = transFormat.parse(expectedDateStr.substring(0,10));

        int compare = 0;
        compare = expectedDate.compareTo(nowDate);

        if (compare > 0) {            //예정일이 더 클경우
            Calendar c1 = Calendar.getInstance();    //예정일
            Calendar c2 = Calendar.getInstance();    //오늘

            c1.setTime(expectedDate);
            c2.setTime(nowDate);

            long d1, d2;
            d1 = c1.getTime().getTime();        //예정일 -> ms
            d2 = c2.getTime().getTime();        //오늘 ->ms

            int days = (int) ((d1 - d2) / (1000 * 60 * 60 * 24));
            measureDateLess(days);
        } else if (compare < 0) {           //계산날짜가 예정일보다 큰 경우
            age.setText(getDateDifferenceInDDMMYYYY(expectedDate, nowDate));
        } else {                           //예정일과 계산날짜가 같은 경우
            age.setText( 0 + "(오늘 태어났습니다.)");
        }
    }

    public static String getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;
        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        System.out.println("increment" + increment);
        //일 계산
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

        //월 계산
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

        //년 계산
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        return  year + "\t년\t\t" + month + "\t월\t\t" + day + "\t일";

    }

    public void measureDateLess(int days) {
        int Base = 280;

        int week = 0;
        int day = 0;
        int total = Base - days;
        if (total <= 6) {
            age.setText("0 주" + total + " 일");
        } else {
            while (total > 6) {
                day = total % 7;
                total = total - 7;
                week += 1;

            }
            age.setText( week + " 주" + day + " 일");
        }
    }

    public DiaryAdapter(Context context, int resource, ArrayList<DiaryInfoVO> items , String userId, String showDate) {
        dContext = context;
        dResource = resource;
        dItems = items;
        dUserId = userId;
        dShowDate = showDate;
    }

    @Override
    public int getCount() {
        return dItems.size();
    }

    @Override
    public Object getItem(int i) {
        return dItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) dContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(dResource, viewGroup, false);
        }
        aq = new AQuery(view);
//        TextView breakfast = (TextView) view.findViewById(R.id.diary_tv_breakfast);
//        TextView lunch = (TextView) view.findViewById(R.id.diary_tv_lunch);
//        TextView dinner = (TextView) view.findViewById(R.id.diary_tv_dinner);
//        TextView temperature = (TextView) view.findViewById(R.id.diary_tv_temp);
//        TextView humid = (TextView) view.findViewById(R.id.diary_tv_humid);
//        TextView sleeptime = (TextView) view.findViewById(R.id.diary_tv_sleepTime);
//        TextView bloodPressure = (TextView) view.findViewById(R.id.diary_tv_bloodPressure);
//        TextView drinking = (TextView) view.findViewById(R.id.diary_tv_drinking);

        age = (TextView) view.findViewById(R.id.diary_tv_age);
        TextView weight = (TextView) view.findViewById(R.id.diary_tv_weight);
        TextView height = (TextView) view.findViewById(R.id.diary_tv_height);
        TextView memo = (TextView) view.findViewById(R.id.diary_tv_memo);
        TextView hospital = (TextView) view.findViewById(R.id.diary_tv_hospital);
        TextView treat = (TextView) view.findViewById(R.id.diary_tv_treat);
        TextView shot = (TextView) view.findViewById(R.id.diary_tv_shot);
        TextView next = (TextView) view.findViewById(R.id.diary_tv_next);
        TextView depart = (TextView) view.findViewById(R.id.diary_tv_depart);
        ImageView diary_iv_photo = (ImageView) view.findViewById(R.id.diary_iv_photo);


        if (dItems != null) {
            diary = dItems.get(i);

            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
            getExpectedDate();
            weight.setText(diary.getWeight() + "(kg)");
            height.setText(diary.getHeight() + "(cm)");
            memo.setText(diary.getMemo());

            hospital.setText(diary.getHospital());
            treat.setText(diary.getTreat());
            shot.setText(diary.getShot());
            Date next_date = new Date();


            if (!diary.getNext().equals("0")) {
                try {
                    next_date = dateFormat.parse(diary.getNext());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int year = Integer.parseInt(curYearFormat.format(next_date));
                int month = Integer.parseInt(curMonthFormat.format(next_date));
                int day = Integer.parseInt(curDayFormat.format(next_date));
                next.setText(year + "-" + month + "-" + day);
            }
            depart.setText(diary.getDepart());
            //이미지 출력
            if(diary.getFileName().length()>0) {
                img_progress = (ProgressBar) view.findViewById(R.id.img_progressBar);
                img_progress.setVisibility(View.VISIBLE);
                diary_iv_photo.setVisibility(View.GONE);
                Amplify.Storage.downloadFile(
                        diary.getFileName(),
                        new File(dContext.getFilesDir() + diary.getFileName()),
                        result -> {
                            File imgFile = result.getFile();
                            String filePath = imgFile.getPath();
                            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                            img_progress.setVisibility(View.GONE);
                            diary_iv_photo.setVisibility(View.VISIBLE);
                            diary_iv_photo.setImageBitmap(bitmap);
                            Log.i("MyAmplifyApp", "Successfully downloaded: " + imgFile.getName());
                        },
                        error -> {
                            diary_iv_photo.setVisibility(View.GONE);
                            img_progress.setVisibility(View.GONE);
                            Log.e("MyAmplifyApp", "Download Failure", error);
                            Toast.makeText(dContext, "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
                        }
                );
            }
        } else {
            age.setText("");
            weight.setText("");
            height.setText("");
            memo.setText("");
            hospital.setText("");
            treat.setText("");
            shot.setText("");
            next.setText("");
            depart.setText("");
            diary_iv_photo.setVisibility(View.GONE);
        }

//        temperature.setText("" + diary.getTemperature());
//        if (diary.getTemperature() > 30) {
//           temperature.setTextColor(Color.parseColor(RED));
//        } else if (diary.getTemperature() < 10) {
//            temperature.setTextColor(Color.parseColor(BLUE));
//        }
//
//        humid.setText("" + diary.getHumid());
//
//        sleeptime.setText("" + diary.getSleepTime());
//        if (diary.getSleepTime() > 6) {
//            sleeptime.setTextColor(Color.parseColor(BLUE));
//        } else if (diary.getSleepTime() < 5) {
//            sleeptime.setTextColor(Color.parseColor(RED));
//        }
//
//        bloodPressure.setText("" + diary.getBloodPressure());
//        if (diary.getBloodPressure() >= 140) {
//            bloodPressure.setTextColor(Color.parseColor(RED));
//        } else if (diary.getBloodPressure() <= 100) {
//            bloodPressure.setTextColor(Color.parseColor(BLUE));
//        }
//
//        if (Integer.parseInt(String.valueOf(diary.getBloodPressure())) != 0) {
//            drinking.setTextColor(Color.parseColor(RED));
//            drinking.setText("O");
//        } else {
//            drinking.setTextColor(Color.parseColor(BLUE));
//            drinking.setText("X");
//        }

        return view;
    }
}
