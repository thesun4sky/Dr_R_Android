package com.example.hosea.dr_r_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.SleepVO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        final SimpleDateFormat curHourFormat = new SimpleDateFormat("HH", Locale.KOREA);
        curHourFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        final SimpleDateFormat curMinuteFormat = new SimpleDateFormat("mm", Locale.KOREA);
        final SimpleDateFormat curSecFormat = new SimpleDateFormat("ss", Locale.KOREA);
        Date sleep_start = new Date(sleepVO.getS_start().getTime());

        int s_hour = Integer.parseInt(curHourFormat.format(sleep_start));
        int s_min = Integer.parseInt(curMinuteFormat.format(sleep_start));
        int s_sec = Integer.parseInt(curSecFormat.format(sleep_start));

        startTime.setText(s_hour+":"+s_min+":"+s_sec);
//        endTime.setText(formatForTime.format(sleepVO.getS_end()));
        total.setText(""+sleepVO.getS_total());

        return view;
    }

}
