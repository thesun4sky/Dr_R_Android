package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.adapter.MyAdapter;
import com.example.hosea.dr_r_android.dao.DiaryVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadDiaryActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    ArrayList<DiaryVO> list;
    DiaryVO diaryVO;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        //이름 설정
        tv = (TextView) findViewById(R.id.diary_tv_userName);
        tv.setText("이호세아");
        list = new ArrayList<>();

        readDiary();
    }

    public ArrayList<DiaryVO> readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", 39);
        aq.ajax("http://52.41.218.18:8080/getDiaries", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
//                    Log.d("readDiary", html.toString());
//                    Toast.makeText(getApplicationContext(),html.toString(),Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < html.length(); i++) {
                        try {
                            JSONObject jsonObject = html.getJSONObject(i);
                            diaryVO = new DiaryVO();
                            diaryVO.setBreakfast(jsonObject.getString("c_breakfast"));
                            diaryVO.setLunch(jsonObject.getString("c_lunch"));
                            diaryVO.setDinner(jsonObject.getString("c_dinner"));
                            diaryVO.setTemperature(jsonObject.getInt("c_temperature"));
                            diaryVO.setHumid(jsonObject.getInt("c_humid"));
                            diaryVO.setSleepTime(jsonObject.getInt("c_sleepTime"));
                            diaryVO.setBloodPressure(jsonObject.getInt("c_bloodPressure"));
                            diaryVO.setDrinking(jsonObject.getString("c_drinking"));
                            diaryVO.setMemo(jsonObject.getString("c_memo"));
//                            lunch = json.getString("c_lunch");
//                            dinner = json.getString("c_dinner");
//                            temperature = json.getInt("c_temperature");
//                            humid = json.getInt("c_humid");
//                            sleepTime = json.getInt("c_sleepTime");
//                            bloodPressure = json.getInt("c_bloodPressure");
//                            drinking = json.getString("c_drinking");
//                            memo = json.getString("c_memo");
                            list.add(diaryVO);
                            Toast.makeText(getApplicationContext(), list.toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });
        //어댑터 생성
        MyAdapter myAdapter = new MyAdapter(this, R.layout.itemsfordiarylist, list);
        //어댑터 연결
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(myAdapter);
        return list;
    }

    public DiaryVO jsonToDiaryVO(JSONObject json) {

//        Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT).show();
//

//        diary.setBreakfast(json.getString("c_breakfast"));
//        diary.setLunch(json.getString("c_lunch"));
//        diary.setDinner(json.getString("c_dinner"));
//        diary.setTemperature(json.getInt("c_temperature"));
//        diary.setHumid(json.getInt("c_humid"));
//        diary.setSleepTime(json.getInt("c_sleepTime"));
//        diary.setBloodPressure(json.getInt("c_bloodPressure"));
//        diary.setDrinking(json.getString("c_drinking"));
//        diary.setMemo(json.getString("c_memo"));

//        Toast.makeText(getApplicationContext(), diary.toString(), Toast.LENGTH_SHORT).show();

        return new DiaryVO(json);
    }


}