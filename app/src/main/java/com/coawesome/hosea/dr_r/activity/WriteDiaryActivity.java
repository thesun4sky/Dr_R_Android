package com.coawesome.hosea.dr_r.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;
import com.coawesome.hosea.dr_r.dao.DiaryInfoVO;
import com.coawesome.hosea.dr_r.dao.DiaryVO;
import com.coawesome.hosea.dr_r.dao.ResponseVO;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class WriteDiaryActivity extends AppCompatActivity {

    private AQuery aq;
    int year, month, day;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private EditText weight, height;
    EditText memo, etc;
    Date diary_date;
    Spinner spinner_hospital;
    Spinner spinner_depart;
    Spinner spinner_shot;
    String date;
    Object depart, hospital_depart, shot;
    String diary_date_string;
    String next_date_string;
    private Intent previousIntent;
    private Button submit, addPhoto;
    private ImageView ivImg;
    boolean photo_has_changed;
    private Bitmap bitmapPhoto;
    String img_path;
    String result;
    CheckBox fever, cough, diarrhea, cb_etc;
    TextView tv;
    TextView today;
    TextView age;
    int result_year = 0, result_month = 0, result_day = 0;
    int next_year = 0, next_month = 0, next_day = 0;
    int start_year = 0, start_month = 0, start_day = 0;
    private final int PICK_FROM_ALBUM = 1;
    private String fileName = "";
    private String originFileName = "";
    private DiaryVO diaryVO;
    private Uri mImageCaptureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);
        photo_has_changed = false;
        previousIntent = getIntent();
        aq = new AQuery(this);
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        age = (TextView) findViewById(R.id.todayAge);
        tv = (TextView) findViewById(R.id.date);
        today = (TextView) findViewById(R.id.today);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        memo = (EditText) findViewById(R.id.memo);
        fever = (CheckBox) findViewById(R.id.fever);
        cough = (CheckBox) findViewById(R.id.cough);
        diarrhea = (CheckBox) findViewById(R.id.diarrhea);
        cb_etc = (CheckBox) findViewById(R.id.cb_etc);
        etc = (EditText) findViewById(R.id.et_etc);
        etc.setVisibility(View.GONE);

        cb_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_etc.isChecked()) {
                    etc.setVisibility(View.VISIBLE);
                } else if (!cb_etc.isChecked()) {
                    etc.setVisibility(View.GONE);
                    etc.setText("");
                }
            }
        });

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


        //다음 진료 과
        spinner_hospital = (Spinner) findViewById(R.id.b_spinner1);
        final ArrayAdapter spinnerAdapter_hospital = ArrayAdapter.createFromResource(this,
                R.array.depart, android.R.layout.simple_spinner_item);
        spinnerAdapter_hospital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hospital.setAdapter(spinnerAdapter_hospital);

        spinner_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depart = parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                depart = parent.getItemAtPosition(0);
            }
        });
        //오늘 다녀온 과
        spinner_depart = (Spinner) findViewById(R.id.spinner_hospital);
        final ArrayAdapter spinnerAdapter_depart = ArrayAdapter.createFromResource(this,
                R.array.hospital, android.R.layout.simple_spinner_item);
        spinnerAdapter_depart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_depart.setAdapter(spinnerAdapter_depart);

        spinner_depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hospital_depart = parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                hospital_depart = parent.getItemAtPosition(0);
            }
        });

        //접종
        spinner_shot = (Spinner) findViewById(R.id.spinner_shot);
        final ArrayAdapter spinnerAdapter_shot = ArrayAdapter.createFromResource(this,
                R.array.shot, android.R.layout.simple_spinner_item);
        spinnerAdapter_shot.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner_shot.setAdapter(spinnerAdapter_shot);

        spinner_shot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shot = parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                shot = parent.getItemAtPosition(0);
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
        String monStr = (result_month<10)? "0"+result_month : ""+result_month;
        result_day = day;
        String dayStr = (result_day<10)? "0"+result_day : ""+result_day;

        date = result_year + "-" + monStr + "-" + dayStr + " ";


        readDiary();
        //getExpectedDate();  교정일 계산 개발 필요

        submit = (Button) findViewById(R.id.submit);
        submit.setText("등록");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    writeDiary();
                } catch (ParseException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void getExpectedDate() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", previousIntent.getStringExtra("userId"));
        /*aq.ajax("http://52.205.170.152:8080/getUserDate", params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject html, AjaxStatus status) {
                if (html != null) {
                    try {
                        calAge(html);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });*/
    }


    public void readDiary() {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = previousIntent.getStringExtra("userId");

        aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Diary?userId=" + userId + "&wDate=" + date)
                .get()
                .showLoading()
                .response((response, error) -> {
                    Gson gson = new Gson();
                    ResponseVO resVO = gson.fromJson(response, ResponseVO.class);
                    if(resVO.getItems().length>0){
                        String json = gson.toJson(resVO.getItems()[0]);
                        this.diaryVO = gson.fromJson(json, DiaryVO.class);
                        Toast.makeText(getApplicationContext(), date+" 일지를 불러왔습니다.", Toast.LENGTH_SHORT).show();
                        inputDataForUpdate();
                    } else {
                        Toast.makeText(getApplicationContext(), date+" 일지를 입력 해주세요.", Toast.LENGTH_SHORT).show();
                        inputForNewData();

                    }
                });
    }

    public void calAge(JSONObject jsonObject) throws JSONException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowString = simpleDateFormat.format(new Date());

        Date nowDate = simpleDateFormat.parse(nowString);


        Long expectedTime = Long.parseLong(jsonObject.getString("u_expected"));
        Date expectedDate = new Date(expectedTime);

        int compare = 0;
        compare = expectedDate.compareTo(nowDate);

        if (compare > 0) {            //예정일이 더 클경우
            Calendar c1 = Calendar.getInstance();    //예정일
            Calendar c2 = Calendar.getInstance();    //오늘

            c1.setTime(expectedDate);
            c2.setTime(nowDate);

            long d1, d2;
            d1 = c1.getTime().getTime();        //예정일 -> ms
            d2 = c2.getTime().getTime();        //오늘 ->ms

            int days = (int) ((d1 - d2) / (1000 * 60 * 60 * 24));
            measureDateLess(days);
        } else if (compare < 0) {           //계산날짜가 예정일보다 큰 경우
            age.setText(getDateDifferenceInDDMMYYYY(expectedDate, nowDate));
        } else {                           //예정일과 계산날짜가 같은 경우
            age.setText( 0 + "(오늘 태어났습니다.)");
        }
    }


    public static String getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;
        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        System.out.println("increment" + increment);
        //일 계산
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

        //월 계산
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

        //년 계산
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        return  year + "\t년\t\t" + month + "\t월\t\t" + day + "\t일";

    }

    public void measureDateLess(int days) {
        int Base = 280;

        int week = 0;
        int day = 0;
        int total = Base - days;
        if (total <= 6) {
            age.setText("0 주" + total + " 일");
        } else {
            while (total > 6) {
                day = total % 7;
                total = total - 7;
                week += 1;

            }
            age.setText( week + " 주" + day + " 일");
        }
    }

    public void inputForNewData() {
        submit.setText("등록");
        weight.setText("");
        height.setText("");
        memo.setText("");
        fever.setChecked(false);
        cough.setChecked(false);
        diarrhea.setChecked(false);
        cb_etc.setChecked(false);
        etc.setText("");
        etc.setVisibility(View.GONE);
        tv.setText("날짜 선택");
        next_year = 0;
        next_month = 0;
        next_day = 0;
        img_path = "";
        fileName = "";
        originFileName = "";
        spinner_depart.setSelection(0);
        hospital_depart = spinner_depart.getItemAtPosition(0);
        spinner_hospital.setSelection(0);
        depart = spinner_hospital.getItemAtPosition(0);
        spinner_shot.setSelection(0);
        shot = spinner_shot.getItemAtPosition(0);
        ivImg.setImageDrawable(null);
        addPhoto.setVisibility(addPhoto.VISIBLE);

    }

    public void inputDataForUpdate() {
        Gson gson = new Gson();
        DiaryInfoVO infoVO = gson.fromJson(this.diaryVO.getwDiary(), DiaryInfoVO.class);
        submit.setText("수정");
        weight.setText(infoVO.getWeight() + "");
        height.setText(infoVO.getHeight() + "");
        memo.setText(infoVO.getMemo());

        //이미지 출력
        if(infoVO.getFileName().length()>0) {
            Amplify.Storage.downloadFile(
                    infoVO.getFileName(),
                    new File(getApplicationContext().getFilesDir() + infoVO.getFileName()),
                    result -> {
                        originFileName = infoVO.getFileName();
                        fileName = infoVO.getFileName();
                        File imgFile = result.getFile();
                        String filePath = imgFile.getPath();
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        ivImg.setImageBitmap(bitmap);
                        addPhoto.setVisibility(addPhoto.GONE);
                        Log.i("MyAmplifyApp", "Successfully downloaded: " + imgFile.getName());
                    },
                    error -> {
                        Log.e("MyAmplifyApp", "Download Failure", error);
                        Toast.makeText(getApplicationContext(), "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
                    }
            );
        }
        //질병 체크 박스
        String original_treat = infoVO.getTreat();
        String[] splitValue = original_treat.split(",");
        //초기화
        fever.setChecked(false);
        cough.setChecked(false);
        diarrhea.setChecked(false);
        cb_etc.setChecked(false);
        etc.setText("");
        for (int i = 0; i < splitValue.length; i++) {
            if (splitValue[i].equals("열")) {
                fever.setChecked(true);
            } else if (splitValue[i].equals("기침")) {
                cough.setChecked(true);
            } else if (splitValue[i].equals("설사")) {
                diarrhea.setChecked(true);
            } else if (splitValue[i].contains("기타")) {
                cb_etc.setChecked(true);
                etc.setVisibility(View.VISIBLE);
                String[] splitValueForEtc = original_treat.split(":");
                etc.setText(splitValueForEtc[1]);
            }
        }


        if (!infoVO.getNext().equals("0")) {
            Date next_date = new Date();
            try {
                next_date = dateFormat.parse(infoVO.getNext());
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
        } else {
            tv.setText("날짜 선택");
            next_year = 0;
            next_month = 0;
            next_day = 0;
        }

        //오늘 다녀온 과 스피너 선택 적용
        if (infoVO.getHospital().equals("")) {
            spinner_depart.setSelection(0);
            hospital_depart = spinner_depart.getItemAtPosition(0);
        } else if (infoVO.getHospital().equals("소아청소년과")) {
            spinner_depart.setSelection(1);
            hospital_depart = spinner_depart.getItemAtPosition(1);
        } else if (infoVO.getHospital().equals("내과")) {
            spinner_depart.setSelection(2);
            hospital_depart = spinner_depart.getItemAtPosition(2);
        } else if (infoVO.getHospital().equals("안과")) {
            spinner_depart.setSelection(3);
            hospital_depart = spinner_depart.getItemAtPosition(3);
        } else if (infoVO.getHospital().equals("이비인후과")) {
            spinner_depart.setSelection(4);
            hospital_depart = spinner_depart.getItemAtPosition(4);
        } else if (infoVO.getHospital().equals("재활의학과")) {
            spinner_depart.setSelection(5);
            hospital_depart = spinner_depart.getItemAtPosition(5);
        } else if (infoVO.getHospital().equals("정형외과")) {
            spinner_depart.setSelection(6);
            hospital_depart = spinner_depart.getItemAtPosition(6);
        } else if (infoVO.getHospital().equals("치과")) {
            spinner_depart.setSelection(7);
            hospital_depart = spinner_depart.getItemAtPosition(7);
        } else if (infoVO.getHospital().equals("피부과")) {
            spinner_depart.setSelection(8);
            hospital_depart = spinner_depart.getItemAtPosition(8);
        } else {

        }

        //다음 진료 과 스피너 선택 적용
        if (infoVO.getDepart().equals("")) {
            spinner_hospital.setSelection(0);
            depart = spinner_hospital.getItemAtPosition(0);
        } else if (infoVO.getDepart().equals("소아청소년과")) {
            spinner_hospital.setSelection(1);
            depart = spinner_hospital.getItemAtPosition(1);
        } else if (infoVO.getDepart().equals("내과")) {
            spinner_hospital.setSelection(2);
            depart = spinner_hospital.getItemAtPosition(2);
        } else if (infoVO.getDepart().equals("안과")) {
            spinner_hospital.setSelection(3);
            depart = spinner_hospital.getItemAtPosition(3);
        } else if (infoVO.getDepart().equals("이비인후과")) {
            spinner_hospital.setSelection(4);
            depart = spinner_hospital.getItemAtPosition(4);
        } else if (infoVO.getDepart().equals("재활의학과")) {
            spinner_hospital.setSelection(5);
            depart = spinner_hospital.getItemAtPosition(5);
        } else if (infoVO.getDepart().equals("정형외과")) {
            spinner_hospital.setSelection(6);
            depart = spinner_hospital.getItemAtPosition(6);
        } else if (infoVO.getDepart().equals("치과")) {
            spinner_hospital.setSelection(7);
            depart = spinner_hospital.getItemAtPosition(7);
        } else if (infoVO.getDepart().equals("피부과")) {
            spinner_hospital.setSelection(8);
            depart = spinner_hospital.getItemAtPosition(8);
        } else {

        }

        //접종 드롭다운
        if (infoVO.getShot().equals("")) {
            spinner_shot.setSelection(0);
            shot = spinner_shot.getItemAtPosition(0);
        } else if (infoVO.getShot().equals("일본뇌염")) {
            spinner_shot.setSelection(1);
            shot = spinner_shot.getItemAtPosition(1);
        } else if (infoVO.getShot().equals("수두")) {
            spinner_shot.setSelection(2);
            shot = spinner_shot.getItemAtPosition(2);
        } else if (infoVO.getShot().equals("A형간염")) {
            spinner_shot.setSelection(3);
            shot = spinner_shot.getItemAtPosition(3);
        } else if (infoVO.getShot().equals("B형간염")) {
            spinner_shot.setSelection(4);
            shot = spinner_shot.getItemAtPosition(4);
        } else if (infoVO.getShot().equals("폴리오")) {
            spinner_shot.setSelection(5);
            shot = spinner_shot.getItemAtPosition(5);
        } else if (infoVO.getShot().equals("BCG")) {
            spinner_shot.setSelection(6);
            shot = spinner_shot.getItemAtPosition(6);
        } else if (infoVO.getShot().equals("DTaP")) {
            spinner_shot.setSelection(7);
            shot = spinner_shot.getItemAtPosition(7);
        } else if (infoVO.getShot().equals("Rotavirus")) {
            spinner_shot.setSelection(8);
            shot = spinner_shot.getItemAtPosition(8);
        } else if (infoVO.getShot().equals("MMR")) {
            spinner_shot.setSelection(9);
            shot = spinner_shot.getItemAtPosition(9);
        } else if (infoVO.getShot().equals("PCV")) {
            spinner_shot.setSelection(10);
            shot = spinner_shot.getItemAtPosition(10);
        } else if (infoVO.getShot().equals("Hib")) {
            spinner_shot.setSelection(11);
            shot = spinner_shot.getItemAtPosition(11);
        } else {

        }

    }

    public void writeDiary() throws ParseException, JSONException {
        JSONObject obj = new JSONObject();
        String monStr = (result_month<10)? "0"+result_month : ""+result_month;
        String dayStr = (result_day<10)? "0"+result_day : ""+result_day;
        diary_date_string = result_year + "-" + monStr + "-" + dayStr;
        if (next_year != 0) {
            next_date_string = next_year + "-" + monStr + "-" + dayStr;
            obj.put("next", next_date_string);
        } else {
            obj.put("next", 0);
        }
        diary_date = dateFormat.parse(diary_date_string);

        if (depart.toString().equals("선택")) {
            obj.put("depart", "");
        } else {
            obj.put("depart", depart.toString());
        }

        if (hospital_depart.toString().equals("선택")) {
            obj.put("hospital", "");
        } else {
            obj.put("hospital", hospital_depart.toString());
        }

        if (shot.toString().equals("선택")) {
            obj.put("shot", "");
        } else {
            obj.put("shot", shot.toString());
        }
        double weightNum = Double.parseDouble(weight.getText().toString());
        double heightNum = Double.parseDouble(height.getText().toString());
        if(weightNum < 1 || heightNum < 1) {
            Toast.makeText(getApplicationContext(), "키와 몸무게는 필수 입력입니다.", Toast.LENGTH_SHORT).show();
        }

        String userStr = previousIntent.getStringExtra("userId");
        String dateStr = dateFormat.format(diary_date);
        if(!fileName.contains(userStr+"_"+dateStr)) {
            fileName = userStr + "_" + dateStr + fileName;
        }
        obj.put("userId", userStr);
        obj.put("memo", memo.getText().toString());
        obj.put("weight", weightNum);
        obj.put("height", heightNum);
        obj.put("treat", printdisease());
        obj.put("fileName", fileName);

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userStr);
        params.put("wDate", dateStr);
        params.put("wDiary", obj.toString());

        try {
            if(photo_has_changed && !originFileName.equals(fileName)){
                //S3 Bucket에 이미지 업로드
                File file = createFileFromUri(mImageCaptureUri,fileName);
                uploadFile(file);
            }

            submit.setEnabled(false);
            aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-Diary")
                    .post(params)
                    .showLoading()
                    .response((response, error) -> {
                        if (response != null) {
                            Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
                            WriteDiaryActivity.this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "네트워크 연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                            submit.setEnabled(true);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "업로드에 실패 했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(File file) {

        Amplify.Storage.uploadFile(
                file.getName(),
                file,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    private Bitmap compressBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40, stream);
        byte[] byteArray = stream.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return compressedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            if (data != null) {
                addPhoto.setVisibility(addPhoto.GONE);
                mImageCaptureUri = data.getData();
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

    File createFileFromUri(Uri uri, String objectKey) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), objectKey);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 30, fos);
        fos.flush();
        fos.close();
        return file;
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
            String monStr = (result_month<10)? "0"+result_month : ""+result_month;
            result_day = start_day;
            String dayStr = (result_day<10)? "0"+result_day : ""+result_day;

            date = result_year + "-" + monStr + "-" + dayStr;
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
        if (cb_etc.isChecked()) {
            result += cb_etc.getText().toString() + ":" + etc.getText();
        }
        return result;
    }
}