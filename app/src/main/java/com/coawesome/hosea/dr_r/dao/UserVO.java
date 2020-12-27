package com.coawesome.hosea.dr_r.dao;

/**
 * Created by Hosea on 2016-11-03.
 */

public class UserVO {
    private String date;
    private String userId;
    private String uExpectedDate;
    private String uBornDate;
    private String uName;
    private String uWeek;
    private String uSex;
    private String uDate;
    private String uWeight;
    private String uHeight;

    public UserVO(String date, String userId, String uExpectedDate, String uBornDate, String uName, String uWeek, String uSex, String uDate, String uWeight, String uHeight) {
        this.date = date;
        this.userId = userId;
        this.uExpectedDate = uExpectedDate;
        this.uBornDate = uBornDate;
        this.uName = uName;
        this.uWeek = uWeek;
        this.uSex = uSex;
        this.uDate = uDate;
        this.uWeight = uWeight;
        this.uHeight = uHeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getuExpectedDate() {
        return uExpectedDate;
    }

    public void setuExpectedDate(String uExpected) {
        this.uExpectedDate = uExpected;
    }

    public String getuBornDate() {
        return uBornDate;
    }

    public void setuBornDate(String uBornDate) {
        this.uBornDate = uBornDate;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuWeek() {
        return uWeek;
    }

    public void setuWeek(String uWeek) {
        this.uWeek = uWeek;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }

    public String getuWeight() {
        return uWeight;
    }

    public void setuWeight(String uWeight) {
        this.uWeight = uWeight;
    }

    public String getuHeight() {
        return uHeight;
    }

    public void setuHeight(String uHeight) {
        this.uHeight = uHeight;
    }
}
