package com.example.hosea.dr_r_android.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.ServiceState;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.activity.RegisteringActivity;
import com.example.hosea.dr_r_android.dao.DiaryVO;
import com.example.hosea.dr_r_android.dao.ResultVO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hosea on 2016-11-09.
 */
public class ServiceImpl implements DrService {

    private final String url = "http://192.168.1.3:8080/";
    Activity activity;
    public ServiceImpl(Activity activity){this.activity = activity;}
    private AQuery aq = new AQuery(activity);

    AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
        @Override
        public void callback(String url, JSONObject html, AjaxStatus status) {
            Toast.makeText(activity ,html.toString(),Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public ResultVO writeDiary(DiaryVO diary) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("device_id", "hosea");
        aq.ajax(url+"expost", params, JSONObject.class, cb);
        return new ResultVO("good",200);
    }

    @Override
    public ArrayList<DiaryVO> getDiaries(int num) {
        return null;
    }
}
