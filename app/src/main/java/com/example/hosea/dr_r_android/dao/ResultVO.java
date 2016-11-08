package com.example.hosea.dr_r_android.dao;

/**
 * Created by Hosea on 2016-11-09.
 */
public class ResultVO {
    private String result;
    private int resultCode;

    public ResultVO(String result, int resultCode) {
        this.result = result;
        this.resultCode = resultCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
