package com.coawesome.hosea.dr_r.dao;

/**
 * Created by thesun.kim on 2020-12-26.
 */
public class DiaryInfoVO {
    private String weight;
    private String height;
    private String memo;
    private String treat;
    private String next;
    private String hospital;
    private String depart;
    private String shot;

    public DiaryInfoVO(String weight, String height, String memo, String treat, String next, String hospital, String depart, String shot) {
        this.weight = weight;
        this.height = height;
        this.memo = memo;
        this.treat = treat;
        this.next = next;
        this.hospital = hospital;
        this.depart = depart;
        this.shot = shot;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTreat() {
        return treat;
    }

    public void setTreat(String treat) {
        this.treat = treat;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getShot() {
        return shot;
    }

    public void setShot(String shot) {
        this.shot = shot;
    }
}