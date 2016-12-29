package com.example.hosea.dr_r_android.dao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hosea on 2016-11-09.
 */
public class DiaryVO {
    private int u_id;
    private double height;
    private double weight;
    private String hospital_name;
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


    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public DiaryVO (JSONObject json) {
        try {
            height = json.getDouble("height");
            weight = json.getDouble("weight");
            hospital_name = json.getString("hospital_name");
            memo = json.getString("memo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "{" + "u_id:" + u_id + "height:" + height + "weight:" + weight + "hospital_name:"
                + hospital_name + "memo:" + memo + "}";
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}