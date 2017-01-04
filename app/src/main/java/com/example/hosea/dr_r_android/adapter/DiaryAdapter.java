package com.example.hosea.dr_r_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.DiaryVO;

import org.w3c.dom.Text;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hosea on 2016-11-01.
 */

public class DiaryAdapter extends BaseAdapter {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    private Context dContext;
    private int dResource;
    private ArrayList<DiaryVO> dItems = new ArrayList<>();
    private static final String RED = "#FF0000";
    private static final String BLUE = "#0000FF";
    AQuery mAq;

    public DiaryAdapter(Context context, int resource, ArrayList<DiaryVO> items) {
        dContext = context;
        dResource = resource;
        dItems = items;
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
        mAq = new AQuery(view);
//        TextView breakfast = (TextView) view.findViewById(R.id.diary_tv_breakfast);
//        TextView lunch = (TextView) view.findViewById(R.id.diary_tv_lunch);
//        TextView dinner = (TextView) view.findViewById(R.id.diary_tv_dinner);
//        TextView temperature = (TextView) view.findViewById(R.id.diary_tv_temp);
//        TextView humid = (TextView) view.findViewById(R.id.diary_tv_humid);
//        TextView sleeptime = (TextView) view.findViewById(R.id.diary_tv_sleepTime);
//        TextView bloodPressure = (TextView) view.findViewById(R.id.diary_tv_bloodPressure);
//        TextView drinking = (TextView) view.findViewById(R.id.diary_tv_drinking);

        TextView age = (TextView) view.findViewById(R.id.diary_tv_age);
        TextView weight = (TextView) view.findViewById(R.id.diary_tv_weight);
        TextView height = (TextView) view.findViewById(R.id.diary_tv_height);
        TextView memo = (TextView) view.findViewById(R.id.diary_tv_memo);
        TextView hospital = (TextView) view.findViewById(R.id.diary_tv_hospital);
        TextView treat = (TextView) view.findViewById(R.id.diary_tv_treat);
        TextView shot = (TextView) view.findViewById(R.id.diary_tv_shot);
        TextView next = (TextView) view.findViewById(R.id.diary_tv_next);
        TextView depart = (TextView) view.findViewById(R.id.diary_tv_depart);


        if (dItems != null) {
            DiaryVO diary = dItems.get(i);

            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
            age.setText(diary.getAge() + "");
            weight.setText(diary.getWeight() + "");
            height.setText(diary.getHeight() + "");
            memo.setText(diary.getMemo());
            hospital.setText(diary.getHospital_name());
            treat.setText(diary.getTreat());
            shot.setText(diary.getShot());
            Date next_date = new Date();


            if (!diary.getNext().equals("0")) {
                try {
                    next_date = dateFormat.parse(diary.getNext());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long next_time = next_date.getTime();
                int year = Integer.parseInt(curYearFormat.format(next_date));
                int month = Integer.parseInt(curMonthFormat.format(next_date));
                int day = Integer.parseInt(curDayFormat.format(next_date));
                next.setText(year + "-" + month + "-" + day);
            }
            depart.setText(diary.getDepart());
            String IMG_URL = "http://52.41.218.18/storedimg/" + diary.getC_img();
            mAq.id(R.id.diary_iv_photo).image(IMG_URL);
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
