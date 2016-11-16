package com.example.hosea.dr_r_android.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Hosea on 2016-11-09.
 */
public class QnaVO {
    private int u_id;
    private String u_name;
    private String qna_title;
    private String qna_content;
    private Date date;
    private int count;

    public QnaVO (JSONObject jsonObject) {
        try {
            u_id = jsonObject.getInt("c_breakfast");
            u_name = jsonObject.getString("c_lunch");
            qna_title = jsonObject.getString("c_dinner");
            qna_content = jsonObject.getString("c_temperature");
            date = (Date) jsonObject.get("c_humid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //테스트를 위한 생성자
    public QnaVO (int id, String name, String title, String content) {
        u_id = id;
        u_name = name;
        qna_title = title;
        qna_content = content;
        date = new Date();
        count = 0;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getQna_title() {
        return qna_title;
    }

    public void setQna_title(String qna_title) {
        this.qna_title = qna_title;
    }

    public String getQna_content() {
        return qna_content;
    }

    public void setQna_content(String qna_content) {
        this.qna_content = qna_content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}