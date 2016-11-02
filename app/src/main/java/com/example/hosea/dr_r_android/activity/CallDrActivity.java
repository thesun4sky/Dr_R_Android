package com.example.hosea.dr_r_android.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hosea.dr_r_android.R;

import org.w3c.dom.Text;

public class CallDrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calldoctor);


        TextView tv = (TextView) findViewById(R.id.tv_doctor_phone_num);
        ImageView iv = (ImageView) findViewById(R.id.doctor_img);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:"+R.string.doctor_phone_num)));
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:114")));
            }
        });

    }



}