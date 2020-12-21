package com.coawesome.hosea.dr_r.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private AQuery aq;
    private EditText login_id, name, password1, password2;
    private Date born_date;
    private Date ex_date;
    private EditText a_week, a_date;
    private EditText b_weight, b_height;
    private EditText email, confrimCode;
    int year = 0, month = 0, day = 0;
    int year2 = 0, month2 = 0, day2 = 0;
    int result_year = 0, result_month = 0, result_day = 0;   // 출생일
    int ex_year = 0, ex_month = 0, ex_day = 0;   //예정일
    private TextView u_born;
    private TextView u_expected;
    private String born_string = "";
    private String ex_string = "";
    private static final int MY_READ_PHONE_STATE = 0;
    private Button checkId, submit, sendCode;
    private String array, deviceId;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        aq = new AQuery(this);
        GregorianCalendar calendar = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year2 = calendar2.get(Calendar.YEAR);
        month2 = calendar2.get(Calendar.MONTH);
        day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        /* API 29이상 버전에서는 READ_PRIVILEGED_PHONE_STATE 권한 미지원...
        try {
            getUUID();
        } catch (Exception e) {
            checkPermission();
        }*/

        a_week = (EditText) findViewById(R.id.a_week);
        a_date = (EditText) findViewById(R.id.a_date);
        b_weight = (EditText) findViewById(R.id.b_weight);
        b_height = (EditText) findViewById(R.id.b_height);
        login_id = (EditText) findViewById(R.id.login_id);
        name = (EditText) findViewById(R.id.user_name);
        u_born = (TextView) findViewById(R.id.u_born);
        u_expected = (TextView)findViewById(R.id.u_expected);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        submit = (Button) findViewById(R.id.joinSubmit);

        email = (EditText) findViewById(R.id.email);
        sendCode = (Button) findViewById(R.id.sendCode);
        confrimCode = (EditText) findViewById(R.id.confrimCode);

        final RadioGroup rg = (RadioGroup) findViewById(R.id.radioForSex);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValidEmail(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이메일을 체크하세요", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                } else if (!password1.getText().toString().equals(password2.getText().toString()) || password1.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요. (5자 이상)", Toast.LENGTH_SHORT).show();
                    password1.requestFocus();
                } else {
                    Amplify.Auth.signUp(
                            login_id.getText().toString(),
                            password1.getText().toString(),
                            AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email.getText().toString()).build(),
                            result -> {
                                Log.i("AuthQuickStart", "Result: " + result.toString());
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), email.getText().toString()+"로 인증코드가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            },
                            error -> {
                                Log.e("AuthQuickStart", "Sign up failed", error);
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), getAWSErrorMessage(error), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                    );
                }
            }
        });

        u_born.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(JoinActivity.this, dateSetListener, year, month, day);
                datePickerDialog1.show();
            }
        });

        u_expected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(JoinActivity.this, dateSetListener2, year2, month2, day2);
                datePickerDialog2.show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                born_date = null;
                ex_date = null;
                try {
                    born_date = dateFormat.parse(born_string);
                    ex_date = dateFormat.parse(ex_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //유효성 테스트
                if ((login_id.getText().toString().equals("")) || login_id.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "id를 다시 입력하세요. (5자 이상)", Toast.LENGTH_SHORT).show();
                    login_id.setText("");
                    login_id.requestFocus();
                } else if (!password1.getText().toString().equals(password2.getText().toString()) || password1.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요. (5자 이상)", Toast.LENGTH_SHORT).show();
                    password1.requestFocus();
                } else if ((name.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "아기 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    name.requestFocus();
                } else if ((a_week.getText().toString().equals("")) || (a_date.getText().toString()==null)) {
                    Toast.makeText(getApplicationContext(), "출생 주 수를 입력하세요.", Toast.LENGTH_SHORT).show();
                    a_week.requestFocus();
                } else if (ex_string.equals("")) {
                    Toast.makeText(getApplicationContext(), "출생 예정일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    u_expected.requestFocus();
                } else if (born_string.equals("")) {
                    Toast.makeText(getApplicationContext(), "출생일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    u_born.requestFocus();
                } else if (rb.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "성별을 입력하세요.", Toast.LENGTH_SHORT).show();
                    rb.requestFocus();
                } else if ((b_weight.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "출생 몸무게를 입력하세요.", Toast.LENGTH_SHORT).show();
                    b_weight.setText("");
                    b_weight.requestFocus();
                } else if ((b_height.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), "출생 키를 입력하세요.", Toast.LENGTH_SHORT).show();
                    b_height.setText("");
                    b_height.requestFocus();
                } else if (!checkConfrimCode(login_id.getText().toString(),confrimCode.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이메일 인증코드를 확인하세요.", Toast.LENGTH_SHORT).show();
                    confrimCode.setText("");
                    confrimCode.requestFocus();
                } else {
                    submit.setEnabled(false);
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("httpMethod", "POST");

                    Map<String, Object> body = new HashMap<String, Object>();
                    body.put("user", login_id.getText().toString());
                    body.put("u_name", name.getText().toString());
                    body.put("u_a_week", a_week.getText().toString());
                    body.put("u_a_date", a_date.getText().toString());
                    body.put("u_expected" , dateFormat.format(ex_date));
                    body.put("u_born", dateFormat.format(born_date));
                    body.put("u_w", b_weight.getText().toString());
                    body.put("u_h", b_height.getText().toString());
                    body.put("u_sex", rb.getText().toString());
                    params.put("body", body);


                    //회원가입정보 DynamoDB 저장
                   aq.ajax("https://em0gmx2oj5.execute-api.us-east-1.amazonaws.com/dev/dynamodbCRUD-dev-User", params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject html, AjaxStatus status) {
                            if (html != null) {
                                try {
                                    Toast.makeText(getApplicationContext(), html.getString("msg"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(intent);
                                JoinActivity.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "연결 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
                                submit.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * check email confirm code
     * @param username
     * @param code
     * @return check result
     */
    private boolean checkConfrimCode(String username, String code) {
        AtomicBoolean ret = new AtomicBoolean(false);

        // - 회원가입 확정
        Amplify.Auth.confirmSignUp(
                username,
                code,
                result -> {
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    ret.set(result.isSignUpComplete());
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );

        return ret.get();
    }

    /** * Comment : 정상적인 이메일 인지 검증. */
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }

    private String getAWSErrorMessage(AuthException error){

        if(error.getCause().getMessage().contains("Password not long enough")){
            return "패스워드 길이가 짧습니다.(8자 이상)";
        }else if(error.getCause().getMessage().contains("User already exists")){
            return "이미 존재하는 아이디 입니다.";
        }

        return error.getMessage();
    }

    private void getUUID() {   //디바이스 정보 받아오기
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "";// + tm.getDeviceId();
        tmSerial = "";// + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub

            result_year = year;
            result_month = monthOfYear+1;
            result_day = dayOfMonth;

            u_born.setText(result_year+"년 "+ result_month+"월 "+result_day+"일");
            born_string = result_year + "-" + result_month + "-" + result_day + " " + "00:00:00";
        }
    };
    private DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub

            ex_year = year;
            ex_month = monthOfYear+1;
            ex_day = dayOfMonth;

            u_expected.setText(ex_year+"년 "+ ex_month+"월 "+ex_day+"일");
            ex_string = ex_year + "-" + ex_month + "-" + ex_day + " " + "00:00:00";
        }
    };


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {    //사용자에게 디바이스 정보 받아오는거 확인
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_READ_PHONE_STATE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            getUUID();
        }

    }

}