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
import com.example.hosea.dr_r_android.dao.QnaVO;

import java.util.ArrayList;

/**
 * Created by Hosea on 2016-11-01.
 */

public class QnaAdapter extends BaseAdapter {
    private Context dContext;
    private int dResource;
    private QnaHolder holder = new QnaHolder();
    private ArrayList<QnaVO> dItems = new ArrayList<>();
    private QnaVO qnaVO;

    public QnaAdapter(Context context, int resource, ArrayList<QnaVO> items) {
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

            holder.u_name = (TextView) view.findViewById(R.id.qna_tv_writer);
            holder.qna_title = (TextView) view.findViewById(R.id.qna_tv_title);
            holder.date = (TextView) view.findViewById(R.id.qna_tv_writeDate);
            holder.count = (TextView) view.findViewById(R.id.qna_tv_count);

            view.setTag(holder);
        } else {
            holder = (QnaHolder) view.getTag();
        }

        qnaVO = dItems.get(i);

        holder.u_name.setText(qnaVO.getU_name());
        holder.qna_title.setText(qnaVO.getQna_title());
        holder.date.setText(qnaVO.getDate());
        holder.count.setText("" + qnaVO.getCount());

        return view;
    }
}
