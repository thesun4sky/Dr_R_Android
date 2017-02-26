package com.coawesome.hosea.dr_r.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.coawesome.hosea.dr_r.R;

import java.util.ArrayList;
import java.util.List;

public class SlideActivity extends AppCompatActivity {
    List<Integer> galleryId = new ArrayList<>();
    GestureDetector mGestureDetector;
    AdapterViewFlipper avf;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_slide);

        for (int i = 1; i < 7; i++) {
            galleryId.add(getResources().getIdentifier("t" + i, "drawable", this.getPackageName()));
        }

        avf = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper);
        avf.setAdapter(new GalleryAdapter(this));


        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                if(Math.abs(velocityX) > 1000 && Math.abs(velocityY) < 500){

                    if(velocityX < 0){
                        avf.showNext();
                    } else if(velocityX > 0){
                        avf.showPrevious();
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(mGestureDetector.onTouchEvent(ev)){
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public class GalleryAdapter extends BaseAdapter {

        private final Context mContext;
        LayoutInflater inflater;

        public GalleryAdapter(Context c) {
            mContext = c;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return galleryId.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.itemsforslide, parent, false);
            }
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.img_slide);
            imageView.setImageResource(galleryId.get(position));
            return convertView;
        }
    }


}
