package com.coawesome.hosea.dr_r.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.dao.SleepVO;

import java.text.ParseException;
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
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Set DayName
        TextView sleepStartTime = (TextView) view.findViewById(R.id.tv_item_sleep_start_time);
        TextView sleepEndTime = (TextView) view.findViewById(R.id.tv_item_sleep_end_time);
        TextView total = (TextView) view.findViewById(R.id.tv_item_sleep_total);

        // Set Text 01
        SleepVO sleepVO = sItems.get(i);
        final SimpleDateFormat curHourFormat = new SimpleDateFormat("HH", Locale.KOREA);
        curHourFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        final SimpleDateFormat curMinuteFormat = new SimpleDateFormat("mm", Locale.KOREA);
        final SimpleDateFormat curSecFormat = new SimpleDateFormat("ss", Locale.KOREA);
        Date sleep_start;
        Date sleep_end;
        try {
            sleep_start = transFormat.parse(sleepVO.getsStart());
            sleep_end = transFormat.parse(sleepVO.getsEnd());

            sleepStartTime.setText(sleep_start.getHours()+"시"+sleep_start.getMinutes()+"분");
            sleepEndTime.setText(sleep_end.getHours()+"시"+sleep_end.getMinutes()+"분");
            total.setText(""+(sleepVO.getS_total()/60) + "분" + (sleepVO.getS_total()%60) + "초");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

}
