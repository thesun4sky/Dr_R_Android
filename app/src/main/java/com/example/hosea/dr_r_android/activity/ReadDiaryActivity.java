package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.adapter.DiaryAdapter;
import com.example.hosea.dr_r_android.dao.DiaryVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadDiaryActivity extends AppCompatActivity {
    private Intent previousIntent;
    private AQuery aq = new AQuery(this);
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        previousIntent = getIntent();

        //이름 설정
        tv = (TextView) findViewById(R.id.tv_listView_title);
        tv.setText(previousIntent.getStringExtra("u_name"));

        readDiary();

    }

    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        aq.ajax("http://52.41.218.18:8080/getDiaries", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    jsonArrayToArrayList(html);
                } else {
                    tv.setText("해당하는 데이터가 없습니다.");
                }
            }
        });
    }

    public void jsonArrayToArrayList(JSONArray jsonArr) {
        ArrayList<DiaryVO> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                arrayList.add(new DiaryVO(jsonArr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        linktoAdapter(arrayList);
    }

    public void linktoAdapter(ArrayList<DiaryVO> list) {
        //어댑터 생성
        DiaryAdapter diaryAdapter = new DiaryAdapter(this, R.layout.itemsfordiarylist, list);
        //어댑터 연결
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(diaryAdapter);
    }
}