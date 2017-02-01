package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Intent previousIntent, intent;
    private TextView correctedAge, joiningDate;
    private AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previousIntent = getIntent();

        View button1 = findViewById(R.id.btn_main_1);
        button1.setOnClickListener(mClick);
//        correctedAge = (TextView)findViewById(R.id.tv_main_corrected_age);
//        joiningDate = (TextView)findViewById(R.id.tv_main_joining_date);
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
//        aq.ajax("http://52.41.218.18:8080/getBornDate", params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject html, AjaxStatus status) {
//                if (html != null) {
//                    try {
//                        correctedAge.setText("" + html.getString(1));
//                        joiningDate.setText("");
//                    } catch (JSONException | ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//                }
//            }
//        });

        findViewById(R.id.btn_main_2).setOnClickListener(mClick);

        findViewById(R.id.btn_main_3).setOnClickListener(mClick);

        findViewById(R.id.btn_main_4).setOnClickListener(mClick);



        //로그아웃 버튼
        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent logoutIntent = new Intent(getApplicationContext(),LoginActivity.class);
                logoutIntent.putExtra("u_device", previousIntent.getStringExtra("u_device"));
                startActivity(logoutIntent);
                MainActivity.this.finish();
            }
        });
    }


    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_main_1:
                    intent = new Intent(getApplicationContext(), TimeActivity.class);
                    intent.putExtra("u_id", previousIntent.getIntExtra("u_id", 0))
                            .putExtra("u_name", previousIntent.getStringExtra("u_name"));
                    startActivity(intent);
                    break;
                case R.id.btn_main_2:
                    intent = new Intent(getApplicationContext(), WriteDiaryActivity.class);
                    intent.putExtra("u_id", previousIntent.getIntExtra("u_id", 0))
                            .putExtra("u_name", previousIntent.getStringExtra("u_name"));
                    startActivity(intent);
                    break;
                case R.id.btn_main_3:
                    intent = new Intent(getApplicationContext(), GraphActivity.class);
                    intent.putExtra("u_id", previousIntent.getIntExtra("u_id", 0))
                            .putExtra("u_name", previousIntent.getStringExtra("u_name"));
                    startActivity(intent);
                    break;
                case R.id.btn_main_4:
                    intent = new Intent(getApplicationContext(), ReadDiaryActivity.class);
                    intent.putExtra("u_id", previousIntent.getIntExtra("u_id", 0))
                            .putExtra("u_name", previousIntent.getStringExtra("u_name"));
                    startActivity(intent);
                    break;
                //TODO shere버튼 생성 예정
//                    case R.id.share:
//                        Intent msg = new Intent(Intent.ACTION_SEND);
//                        msg.addCategory(Intent.CATEGORY_DEFAULT);
//                        msg.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
//                        msg.putExtra(Intent.EXTRA_TEXT, "애플리케이션 공유하세요");
//                        msg.putExtra(Intent.EXTRA_TITLE, "제목");
//                        msg.setType("text/plain");
//                        startActivity(Intent.createChooser(msg, "공유하기"));
//                        break;
                default:
                    break;
            }
        }
    };

}