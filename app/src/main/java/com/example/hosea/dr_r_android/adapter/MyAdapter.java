package com.example.hosea.dr_r_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.DiaryVO;

import java.util.ArrayList;

/**
 * Created by Hosea on 2016-11-01.
 */

public class MyAdapter extends BaseAdapter {
    private Context dContext;
    private int dResource;
    ViewHolder holder = new ViewHolder();
    private ArrayList<DiaryVO> dItems = new ArrayList<>();

    public MyAdapter(Context context, int resource, ArrayList<DiaryVO> items) {
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
            view = inflater.inflate(dResource, viewGroup,false);

            holder.breakfast = (TextView) view.findViewById(R.id.diary_tv_breakfast);
            holder.lunch = (TextView) view.findViewById(R.id.diary_tv_lunch);
            holder.dinner = (TextView) view.findViewById(R.id.diary_tv_dinner);
            holder.temperature = (TextView) view.findViewById(R.id.diary_tv_temp);
            holder.humid = (TextView) view.findViewById(R.id.diary_tv_humid);
            holder.sleeptime = (TextView) view.findViewById(R.id.diary_tv_sleeptime);
            holder.bloodPressure = (TextView) view.findViewById(R.id.diary_tv_bloodPressure);
            holder.drinking = (TextView) view.findViewById(R.id.diary_tv_drinking);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set Text 01
        DiaryVO diary = dItems.get(i);
        holder.breakfast.setText(diary.getBreakfast());
        holder.lunch.setText(diary.getLunch());
        holder.dinner.setText(diary.getDinner());
        holder.temperature.setText("" + diary.getTemperature());
        holder.humid.setText("" + diary.getHumid());
        holder.sleeptime.setText("" + diary.getSleepTime());
        holder.bloodPressure.setText("" + diary.getBloodPressure());
        holder.drinking.setText(diary.getDrinking());

        return view;
    }
}
