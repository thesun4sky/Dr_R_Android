package com.example.hosea.dr_r_android.dao;

/**
 * Created by Hosea on 2016-11-09.
 */
public class DiaryVO {
    private int u_id;
    private String breakfast;
    private String lunch;
    private String dinner;
    private int temperature;
    private int humid;
    private int sleepTime;
    private int bloodPressure;
    private String drinking;
    private String memo;

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumid() {
        return humid;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}