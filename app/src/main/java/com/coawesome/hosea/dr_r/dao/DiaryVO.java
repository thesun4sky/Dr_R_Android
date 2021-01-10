package com.coawesome.hosea.dr_r.dao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hosea on 2016-11-09.
 */
public class DiaryVO {
    private String userId;
    private String wDate;
    private String wDiary;

    public DiaryVO(String userId, String wDate, String wDiary) {
        this.userId = userId;
        this.wDate = wDate;
        this.wDiary = wDiary;
    }

    public DiaryVO(JSONObject jsonObject) {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getwDate() {
        return wDate;
    }

    public void setwDate(String wDate) {
        this.wDate = wDate;
    }

    public String getwDiary() {
        return wDiary;
    }

    public void setwDiary(String wDiary) {
        this.wDiary = wDiary;
    }
}