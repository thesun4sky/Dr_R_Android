package com.example.hosea.dr_r_android.dao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hosea on 2016-11-09.
 */
public class DiaryVO {
    private int u_id;
    private double height;
    private double age;
    private double weight;
    private String hospital_name;
    private String treat;
    private String shot;
    private String next;
    private String depart;
    private String memo;

    public String getTreat() {
        return treat;
    }

    public void setTreat(String treat) {
        this.treat = treat;
    }

    public String getShot() {
        return shot;
    }

    public void setShot(String shot) {
        this.shot = shot;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public double getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }


    public DiaryVO() {
    }

    public void setAge(double age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public DiaryVO(JSONObject json) {
        try {
            age = 3.5;
            height = json.getDouble("c_h");
            weight = json.getDouble("c_w");
            hospital_name = json.getString("c_hospital");
            memo = json.getString("c_memo");
            treat = json.getString("c_treat");
            shot = json.getString("c_shot");
            next = json.getString("c_next");
            depart = json.getString("c_depart");
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