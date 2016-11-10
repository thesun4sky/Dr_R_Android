package com.example.hosea.dr_r_android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hosea.dr_r_android.R;

public class CallDrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calldoctor);

        TextView tv = (TextView) findViewById(R.id.tv_doctor_phone_num);
        ImageView iv = (ImageView) findViewById(R.id.doctor_img);
        View[] views = {tv,iv};

        for (View view : views) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:01096280924")));
                }
            });
        }
    }



}