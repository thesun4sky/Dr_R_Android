package com.example.hosea.dr_r_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.FeedVO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Hosea on 2016-11-01.
 */


public class FeedAdapter extends BaseAdapter {
    private Context fContext;
    private int fResource;
    private ArrayList<FeedVO> fItems = new ArrayList<>();

    public FeedAdapter(Context context, int resource, ArrayList<FeedVO> items) {
        fContext = context;
        fResource = resource;
        fItems = items;
    }

    @Override
    public int getCount() {
        return fItems.size();
    }

    @Override
    public Object getItem(int i) {
        return fItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FeedVO feedVO = fItems.get(i);

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(fResource, viewGroup,false);
        }


        // Set DayName
        TextView startTime = (TextView) view.findViewById(R.id.tv_item_feed_start);
        TextView feed = (TextView) view.findViewById(R.id.tv_item_feed_feed);
        TextView total = (TextView) view.findViewById(R.id.tv_item_feed_total);

        // Set Text 01
        SimpleDateFormat formatForTime = new SimpleDateFormat("HH:mm:ss");
        startTime.setText(formatForTime.format(feedVO.getF_start()));
        feed.setText(feedVO.getFeed());
//        endTime.setText(formatForTime.format(feedVO.getS_end()));
        total.setText(""+feedVO.getF_total());
        return view;
    }

}
