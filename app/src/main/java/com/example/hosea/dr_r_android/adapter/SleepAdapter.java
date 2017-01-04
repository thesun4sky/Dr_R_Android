package com.example.hosea.dr_r_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.SleepVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Hosea on 2016-11-01.
 */


public class SleepAdapter extends BaseAdapter {
    private Context sContext;
    private int sResource;
    private ArrayList<SleepVO> sItems = new ArrayList<>();

    public SleepAdapter(Context context, int resource, ArrayList<SleepVO> items) {
        sContext = context;
        sResource = resource;
        sItems = items;
    }

    @Override
    public int getCount() {
        return sItems.size();
    }

    @Override
    public Object getItem(int i) {
        return sItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) sContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(sResource, viewGroup,false);
        }

        // Set DayName
        TextView startTime = (TextView) view.findViewById(R.id.tv_item_sleep_start);
//        TextView endTime = (TextView) view.findViewById(R.id.tv_item_sleep_end);
        TextView total = (TextView) view.findViewById(R.id.tv_item_sleep_total);

        // Set Text 01
        SleepVO sleepVO = sItems.get(i);
        SimpleDateFormat formatForTime = new SimpleDateFormat("HH:mm:ss");
        startTime.setText(formatForTime.format(sleepVO.getS_start()));
//        endTime.setText(formatForTime.format(sleepVO.getS_end()));
        total.setText(""+sleepVO.getS_total());

        return view;
    }

}
