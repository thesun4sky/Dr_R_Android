package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.adapter.QnaAdapter;
import com.example.hosea.dr_r_android.dao.DiaryVO;
import com.example.hosea.dr_r_android.dao.FeedVO;
import com.example.hosea.dr_r_android.dao.QnaVO;
import com.example.hosea.dr_r_android.dao.SleepVO;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {
    private Intent previousIntent;
    private AQuery aq = new AQuery(this);

    LineGraphSeries<DataPoint> sleeps;
    LineGraphSeries<DataPoint> feeds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_graph);
        previousIntent = getIntent();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        aq.ajax("http://52.41.218.18:8080/getDateSleepTime", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {
                        getDateSleepTime(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "해당하는 데이터가 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aq.ajax("http://52.41.218.18:8080/getDateFeedTime", params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray html, AjaxStatus status) {
                if (html != null) {
                    try {
                        getDateFeedTime(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "해당하는 데이터가 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void getDateSleepTime(JSONArray jsonArray) throws JSONException {

        double y,x=0;

        GraphView sleep_graph = (GraphView) findViewById(R.id.sleep_graph);

        sleeps = new LineGraphSeries<DataPoint>();
        for(int i=0; i< jsonArray.length(); i++){
            SleepVO sleepVO = new SleepVO(jsonArray.getJSONObject(i));
            x = x+0.1;
            y = sleepVO.getS_total();
            sleeps.appendData(new DataPoint(x, y), true, 500);
        }
        sleep_graph.addSeries(sleeps);
    }
    public void getDateFeedTime(JSONArray jsonArray) throws JSONException {

        double y,x=0;

        GraphView feed_graph = (GraphView) findViewById(R.id.feed_graph);

        feeds = new LineGraphSeries<DataPoint>();
        for(int i=0; i< jsonArray.length(); i++){
            FeedVO feedVO= new FeedVO(jsonArray.getJSONObject(i));
            x = x+0.1;
            y = feedVO.getF_total();
            feeds.appendData(new DataPoint(x, y), true, 500);
        }
        feed_graph.addSeries(feeds);
    }
}


