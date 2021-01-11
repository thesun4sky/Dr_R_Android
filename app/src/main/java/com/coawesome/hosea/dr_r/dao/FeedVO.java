package com.coawesome.hosea.dr_r.dao;

public class FeedVO {
    private String userId;
    private String fType;
    private String fStartTime;
    private String fStart;
    private String fEnd;
    private String fTotal;

    public FeedVO(String userId, String fType, String fStartTime, String fStart, String fEnd, String fTotal) {
        this.userId = userId;
        this.fType = fType;
        this.fStartTime = fStartTime;
        this.fStart = fStart;
        this.fEnd = fEnd;
        this.fTotal = fTotal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getfStartTime() {
        return fStartTime;
    }

    public void setfStartTime(String fStartTime) {
        this.fStartTime = fStartTime;
    }

    public String getfStart() {
        return fStart;
    }

    public void setfStart(String fStart) {
        this.fStart = fStart;
    }

    public String getfEnd() {
        return fEnd;
    }

    public void setfEnd(String fEnd) {
        this.fEnd = fEnd;
    }

    public String getfTotal() {
        return fTotal;
    }

    public void setfTotal(String fTotal) {
        this.fTotal = fTotal;
    }

    public int getF_total() {
        return Integer.parseInt(fTotal);
    }
}

