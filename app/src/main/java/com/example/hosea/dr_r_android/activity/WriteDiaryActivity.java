package com.example.hosea.dr_r_android.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.hosea.dr_r_android.R;
import com.example.hosea.dr_r_android.dao.DiaryVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WriteDiaryActivity extends AppCompatActivity {

    private AQuery aq = new AQuery(this);
    int year, month, day;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    private EditText weight, height;
    EditText memo;
    Date diary_date;
    Spinner spinner;
    Date next_date;
    String date;
    Object depart;
    String diary_date_string;
    String next_date_string;
    EditText hospital_name;
    private Intent previousIntent;
    private Button submit, addPhoto;
    private ImageView ivImg;
    boolean photo_has_changed;
    private Bitmap bitmapPhoto;
    String result;
    CheckBox fever, cough, diarrhea;
    TextView tv;
    TextView today;
    TextView shot;
    int result_year = 0, result_month = 0, result_day = 0;
    int next_year = 0, next_month = 0, next_day = 0;
    int start_year = 0, start_month = 0, start_day = 0;
    private final int PICK_FROM_ALBUM = 1;
    TextView start;
    TextView end;
    private byte[] byteArray;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);
        photo_has_changed = false;
        previousIntent = getIntent();
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        tv = (TextView) findViewById(R.id.date);
        today = (TextView) findViewById(R.id.today);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        memo = (EditText) findViewById(R.id.memo);
        hospital_name = (EditText) findViewById(R.id.hospital_name);
        fever = (CheckBox) findViewById(R.id.fever);
        cough = (CheckBox) findViewById(R.id.cough);
        diarrhea = (CheckBox) findViewById(R.id.diarrhea);
        shot = (TextView) findViewById(R.id.shot);

        addPhoto = (Button) findViewById(R.id.add_photo);
        ivImg = (ImageView) findViewById(R.id.photo);

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        spinner = (Spinner) findViewById(R.id.b_spinner1);
        final ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.hospital, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depart = parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                depart = parent.getItemAtPosition(0);
            }
        });

        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(WriteDiaryActivity.this, dateSetListener, year, month, day);
                datePickerDialog1.show();
            }
        });

        findViewById(R.id.today).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(WriteDiaryActivity.this, dateSetListener2, year, month, day);
                datePickerDialog2.show();
            }
        });
        today.setText(year + "년 " + (month + 1) + "월 " + day + "일 " + getDayKor());

        //아무 선택없을때 db에 보낼 Date;
        result_year = year;
        result_month = month + 1;
        result_day = day;

        date = result_year + "-" + result_month + "-" + result_day + " ";


        readDiary();


        submit = (Button) findViewById(R.id.submit);
        submit.setText("등록");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    writeDiary();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        params.put("c_date", date + "00:00:00");
        aq.ajax("http://52.41.218.18:8080/getDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                if (html != null) {
                    inputDataForUpdate(html);
                } else {
                    Toast.makeText(getApplicationContext(), "해당하는 데이터가 없습니다", Toast.LENGTH_SHORT).show();
                    inputForNewData();

                }
            }
        });
    }

    public void inputForNewData(){
        submit.setText("등록");
        weight.setText("");
        height.setText("");
        memo.setText("");
        hospital_name.setText("");
        fever.setChecked(false);
        cough.setChecked(false);
        diarrhea.setChecked(false);
        shot.setText("");
        tv.setText("날짜 선택");
        next_year=0;
        next_month=0;
        next_day=0;
        spinner.setSelection(0);
        depart = spinner.getItemAtPosition(0);
        ivImg.setImageDrawable(null);
        addPhoto.setVisibility(addPhoto.VISIBLE);

    }

    public void inputDataForUpdate(JSONObject jsonObject) {
        submit.setText("수정");
        DiaryVO diaryVO = new DiaryVO(jsonObject);
        weight.setText(diaryVO.getWeight() + "");
        height.setText(diaryVO.getHeight() + "");
        memo.setText(diaryVO.getMemo());
        hospital_name.setText(diaryVO.getHospital_name());

        //질병 체크 박스
        String original_treat = diaryVO.getTreat();
        String[] splitValue = original_treat.split(",");
        for (int i = 0; i < splitValue.length; i++) {
            if (splitValue[i].equals("열")) {
                fever.setChecked(true);
            } else if (splitValue[i].equals("기침")) {
                cough.setChecked(true);
            } else if (splitValue[i].equals("설사")) {
                diarrhea.setChecked(true);
            }
        }

        shot.setText(diaryVO.getShot());
        if(!diaryVO.getNext().equals("0")) {
            Date next_date = new Date();
            try {
                next_date = dateFormat.parse(diaryVO.getNext());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long next_time = next_date.getTime();

            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
            next_date = new Date(next_time);
            int year = Integer.parseInt(curYearFormat.format(next_date));
            int month = Integer.parseInt(curMonthFormat.format(next_date));
            int day = Integer.parseInt(curDayFormat.format(next_date));
            tv.setText(year + "/" + month + "/" + day);

            next_year = year;
            next_month = month;
            next_day = day;
        }
        else{
            tv.setText("날짜 선택");
            next_year = 0;
            next_month = 0;
            next_day = 0;
        }

        //스피너 선택 적용
        if (diaryVO.getDepart().equals("안과")) {
            spinner.setSelection(0);
            depart = spinner.getItemAtPosition(0);
        } else if (diaryVO.getDepart().equals("소아청소년과")) {
            spinner.setSelection(1);
            depart = spinner.getItemAtPosition(1);
        }
        if(!diaryVO.getC_img().equals(null) && !diaryVO.getC_img().equals("") && !diaryVO.getC_img().equals("null")  ) {
            String IMG_URL = "http://52.41.218.18/storedimg/" + diaryVO.getC_img();
            aq.id(R.id.photo).image(IMG_URL);
            addPhoto.setVisibility(addPhoto.GONE);
        }
        else{
            ivImg.setImageDrawable(null);
            addPhoto.setVisibility(View.VISIBLE);
        }



    }

    public void writeDiary() throws ParseException {
        Map<String, Object> params = new HashMap<String, Object>();
        diary_date_string = result_year + "-" + result_month + "-" + result_day + " " + "00:00:00";
        if(next_year != 0) {
            next_date_string = next_year + "-" + next_month + "-" + next_day + " " + "00:00:00";
            next_date = dateFormat.parse(next_date_string);
            params.put("c_next", dateFormat.format(next_date));
        }
        else{
            params.put("c_next",next_year);
        }
        diary_date = dateFormat.parse(diary_date_string);


        params.put("u_id", previousIntent.getIntExtra("u_id", 0));
        params.put("c_memo", memo.getText().toString());
        params.put("c_w", Double.parseDouble(weight.getText().toString()));
        params.put("c_h", Double.parseDouble(height.getText().toString()));
        params.put("c_hospital", hospital_name.getText().toString());
        params.put("c_treat", printdisease());
        params.put("c_shot", shot.getText().toString());
        params.put("c_depart", depart.toString());
        params.put("c_date", dateFormat.format(diary_date));


        //c_date 보내기


        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapPhoto = ((BitmapDrawable) ivImg.getDrawable()).getBitmap();
            bitmapPhoto.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byteArray = stream.toByteArray();
            params.put("file", byteArray);
            params.put("c_img", fileName);
            aq.ajax("http://52.41.218.18:8080/writeDiaryWithImg", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject html, AjaxStatus status) {
                    if (html != null) {
                        Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
                        WriteDiaryActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (NullPointerException ignored) {
            aq.ajax("http://52.41.218.18:8080/writeDiary", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject html, AjaxStatus status) {
                    if (html != null) {
                        Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
                        WriteDiaryActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_ALBUM) {
            if (data != null) {
                addPhoto.setVisibility(addPhoto.GONE);
                Uri mImageCaptureUri = data.getData();
                bitmapPhoto = null;
                try {
                    bitmapPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                    Cursor c = getContentResolver().query(Uri.parse(mImageCaptureUri.toString()), null, null, null, null);
                    c.moveToNext();
                    String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                    fileName = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
                    if (bitmapPhoto != null) {
                        ivImg.setImageBitmap(bitmapPhoto);
                        photo_has_changed = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            start_year = year;
            start_month = monthOfYear + 1;
            start_day = dayOfMonth;

            tv.setText(start_year + "/" + start_month + "/" + start_day);
            next_year = start_year;
            next_month = start_month;
            next_day = start_day;

        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            start_year = year;
            start_month = monthOfYear + 1;
            start_day = dayOfMonth;
            today.setText(start_year + "년 " + (start_month) + "월 " + start_day + "일 " + getChangeDayKor());
            //날짜 선택후  db에 보탤 Date;
            result_year = start_year;
            result_month = start_month;
            result_day = start_day;

            date = result_year + "-" + result_month + "-" + result_day + " ";
            readDiary();
        }
    };

    public static String getDayKor() {
        Calendar cal = Calendar.getInstance();
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        return "( " + week[cnt] + " )";
    }

    public String getChangeDayKor() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, start_year);
        cal.set(Calendar.MONTH, start_month - 1);
        cal.set(Calendar.DATE, start_day);
        int cnt = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] week = {"일", "월", "화", "수", "목", "금", "토"};

        return "( " + week[cnt] + " )";
    }

    public String printdisease() {
        result = "";
        if (fever.isChecked()) {
            result += fever.getText().toString() + ",";
        }
        if (cough.isChecked()) {
            result += cough.getText().toString() + ",";
        }
        if (diarrhea.isChecked()) {
            result += diarrhea.getText().toString() + ",";
        }
        return result;
    }
}