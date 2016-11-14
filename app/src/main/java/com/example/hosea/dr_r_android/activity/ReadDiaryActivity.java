package com.example.hosea.dr_r_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    MyAdapter myAdapter;
    TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readdiary);

        //이름 설정
        tv = (TextView)findViewById(R.id.textView2);
        tv.setText("이호세아");
//        ArrayList<DiaryVO> data = readDiary();
        ArrayList<DiaryVO> data = new ArrayList<>();
        DiaryVO diaryVO = new DiaryVO();
        diaryVO.setBloodPressure(1);
        data.add(diaryVO);

        //어댑터 생성
        myAdapter = new MyAdapter(this, R.layout.itemsfordiarylist, data);
        //어댑터 연결
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(myAdapter);


    }

    public ArrayList<DiaryVO> readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", 1);
        list = new ArrayList<>();
        aq.ajax("http://192.168.0.2:8080/getDiaries", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if( html != null) {
                        for (int i=0; i < html.length(); i++) {
                            try {
                                list.add(jsonToDiaryVO(html.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });
        return list;
    }

    public DiaryVO jsonToDiaryVO (JSONObject json) throws JSONException {
        DiaryVO diary = new DiaryVO();
        diary.setBreakfast((String) json.get("c_breakfast"));
        diary.setLunch((String) json.get("c_lunch"));
        diary.setDinner((String) json.get("c_dinner"));
        diary.setTemperature((int) json.get("c_temperature"));
        diary.setHumid((int) json.get("c_humid"));
        diary.setSleepTime((int) json.get("c_sleepTime"));
        diary.setBloodPressure((int) json.get("c_bloodPressure"));
        diary.setDrinking((String) json.get("c_drinking"));
        diary.setMemo((String) json.get("c_memo"));
        return diary;
    }


}