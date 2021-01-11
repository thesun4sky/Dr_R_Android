package com.coawesome.hosea.dr_r.dao;

public class SleepVO {
    private String userId;
    private String sStart;
    private String sEnd;
    private String sTotal;

    public SleepVO(String userId, String sStart, String sEnd, String sTotal) {
        this.userId = userId;
        this.sStart = sStart;
        this.sEnd = sEnd;
        this.sTotal = sTotal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getsStart() {
        return sStart;
    }

    public void setsStart(String sStart) {
        this.sStart = sStart;
    }

    public String getsEnd() {
        return sEnd;
    }

    public void setsEnd(String sEnd) {
        this.sEnd = sEnd;
    }

    public String getsTotal() {
        return sTotal;
    }

    public void setsTotal(String sTotal) {
        this.sTotal = sTotal;
    }

    public int getS_total() {
        return Integer.parseInt(sTotal);
    }

}