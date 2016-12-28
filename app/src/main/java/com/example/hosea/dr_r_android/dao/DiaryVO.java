package com.example.hosea.dr_r_android.dao;

import org.json.JSONException;
import org.json.JSONObject;

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

    public double getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    //테스트 변수
    private double age;
    private double weight;
    private double height;
    private String memo2;

    public DiaryVO() {}

    public void setAge(double age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public DiaryVO(double age , double weight , float height, String memo2){
        this.age = age;
        this.weight = weight;
        this.height =height;
        this.memo2 = memo2;
    }



    public void setAge(float age) {
        this.age = age;
    }


    public void setWeight(float weight) {
        this.weight = weight;
    }


    public void setHeight(float height) {
        this.height = height;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public DiaryVO (JSONObject json) {
        try {
            breakfast = json.getString("c_breakfast");
            lunch = json.getString("c_lunch");
            dinner = json.getString("c_dinner");
            temperature = json.getInt("c_temperature");
            humid = json.getInt("c_humid");
            sleepTime = json.getInt("c_sleepTime");
            bloodPressure = json.getInt("c_bloodPressure");
            drinking = json.getString("c_drinking");
            memo = json.getString("c_memo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "{" + "u_id:" + u_id + "breakfast:" + u_id + "lunch:" + u_id + "dinner:"
                + u_id + "temperature:" + u_id + "humid:" + u_id + "sleepTime:" + u_id
                + "bloodPressure:" + u_id + "drinking:" + u_id + "memo:" + u_id;
    }

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