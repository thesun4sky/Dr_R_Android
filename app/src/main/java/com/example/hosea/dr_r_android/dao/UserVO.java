package com.example.hosea.dr_r_android.dao;

/**
 * Created by Hosea on 2016-11-03.
 */

public class UserVO {
    private int u_id;
    private String login_id;
    private String u_name;

    public UserVO(){}

    public UserVO(int id, String uid, String u_name){
        this.u_id = id; this.login_id = uid; this.u_name = u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getU_name() {
        return u_name;
    }

    public int getU_id() {
        return u_id;
    }

    public String getLogin_id() {
        return login_id;
    }
}
