package com.coawesome.hosea.dr_r.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.aquery.AQuery;
import com.coawesome.hosea.dr_r.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeeMoonSeong on 2016-11-10.
 */
public class LoginActivity extends AppCompatActivity {
    private AQuery aq;
    private Intent previousIntent;
    private EditText id, password;
    private Button join, login, help, findPass;
    private String u_name;
    private int u_id;
    private AlertDialog newPassDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        previousIntent = getIntent();
        aq = new AQuery(this);

        id = (EditText) findViewById(R.id.login_login_id);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.request_login);
        join = (Button) findViewById(R.id.request_join);

        //help 버튼
        help = (Button)findViewById(R.id.btn_help_login);
        //비밀번호 찾기 버튼
        findPass = (Button)findViewById(R.id.btn_find_pass);

        help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent helpIntent = new Intent(getApplicationContext(),SlideActivity.class);
                startActivity(helpIntent);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater=getLayoutInflater();
                final View dialogView= inflater.inflate(R.layout.dialog_agreement, null);
                AlertDialog.Builder buider= new AlertDialog.Builder(LoginActivity.this); //AlertDialog.Builder 객체 생성
                buider.setTitle("개인정보 활용 동의서"); //Dialog 제목
                buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox checkBox1 = (CheckBox) dialogView.findViewById(R.id.dialog_agree1);
                        CheckBox checkBox2 = (CheckBox) dialogView.findViewById(R.id.dialog_agree2);

                        if (checkBox1.isChecked() && checkBox2.isChecked()) {
                            Toast.makeText(getApplicationContext(), "약관에 동의했습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), JoinActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "약관에 동의하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                buider.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "약관을 동의 하셔야 가입이 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog=buider.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id.getText().toString().length() < 5 || password.getText().toString().length() < 5){
                    Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Amplify.Auth.signIn(
                        id.getText().toString(),
                        password.getText().toString(),
                        result -> {
                            Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                            if (Looper.myLooper() == null) Looper.prepare();
                            Toast.makeText(getApplicationContext(), id.getText().toString() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.putExtra("userId", id.getText().toString());
                            startActivity(mainIntent);
                            LoginActivity.this.finish();
                            Looper.loop();
                        },
                        error -> {
                            Log.e("AuthQuickstart", error.toString());
                            if (Looper.myLooper() == null) Looper.prepare();
                            Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                            password.requestFocus();
                            Looper.loop();
                        }
                );

            }
        });


        findPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater=getLayoutInflater();
                final View dialogView= inflater.inflate(R.layout.dialog_find_pass, null);
                EditText dialog_find_pass_id = (EditText) dialogView.findViewById(R.id.dialog_find_pass_id);
                Button send_code = (Button) dialogView.findViewById(R.id.send_code);
                LinearLayout layout0 = (LinearLayout) dialogView.findViewById(R.id.dialog_find_pass0);
                LinearLayout layout1 = (LinearLayout) dialogView.findViewById(R.id.dialog_find_pass1);
                LinearLayout layout2 = (LinearLayout) dialogView.findViewById(R.id.dialog_find_pass2);
                LinearLayout layout3 = (LinearLayout) dialogView.findViewById(R.id.dialog_find_pass3);
                LinearLayout layout4 = (LinearLayout) dialogView.findViewById(R.id.dialog_find_pass4);
                EditText dialog_find_pass_code = (EditText) dialogView.findViewById(R.id.dialog_find_pass_code);
                EditText dialog_new_pass = (EditText) dialogView.findViewById(R.id.dialog_new_pass);
                EditText dialog_new_pass_check = (EditText) dialogView.findViewById(R.id.dialog_new_pass_check);
                TextView guide_msg = (TextView) dialogView.findViewById(R.id.guide_msg);
                send_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = dialog_find_pass_id;

                        if (!editText.getText().toString().equals("")) {
                            Amplify.Auth.resetPassword(
                                    editText.getText().toString(),
                                    result -> {
                                        Log.i("AuthQuickstart", result.toString());
                                        //String email = result.getNextStep().getCodeDeliveryDetails().getDestination();
                                        Toast.makeText(getApplicationContext(), editText.getText().toString() + " 이메일로 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                                        layout0.setVisibility(View.GONE);
                                        layout1.setVisibility(View.VISIBLE);
                                        layout2.setVisibility(View.VISIBLE);
                                        layout3.setVisibility(View.VISIBLE);
                                        layout4.setVisibility(View.VISIBLE);
                                        dialog_find_pass_code.requestFocus();
                                        guide_msg.setText(editText.getText().toString() + " 이메일로 코드가 발송되었습니다.");
                                    },
                                    error -> {
                                        Log.e("AuthQuickstart", error.toString());
                                        Toast.makeText(getApplicationContext(), "존재하지 않는 이메일 또는 1일 비밀번호 찾기 횟수 5회를 초과하였습니다.", Toast.LENGTH_SHORT).show();
                                        dialog_find_pass_id.requestFocus();
                                    }
                            );
                        } else {
                            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button dialog_set_pass = (Button) dialogView.findViewById(R.id.dialog_set_pass);
                dialog_set_pass.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String pass_code = dialog_find_pass_code.getText().toString();
                       String new_pass = dialog_new_pass.getText().toString();
                       String new_pass_check = dialog_new_pass_check.getText().toString();
                       if (pass_code.equals("") || new_pass.equals("")) {
                           Toast.makeText(getApplicationContext(), "CODE 와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                       }else if(!new_pass.equals(new_pass_check)){
                           Toast.makeText(getApplicationContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                       }else {
                           Amplify.Auth.confirmResetPassword(
                                   new_pass,
                                   pass_code,
                                   () -> {
                                       Log.i("AuthQuickstart", "New password confirmed");
                                       Toast.makeText(getApplicationContext(), "새로운 비밀번호로 설정되었습니다.", Toast.LENGTH_SHORT).show();
                                       layout0.setVisibility(View.VISIBLE);
                                       layout1.setVisibility(View.GONE);
                                       layout2.setVisibility(View.GONE);
                                       layout3.setVisibility(View.GONE);
                                       layout4.setVisibility(View.GONE);
                                       dialog_find_pass_id.setText("");
                                       dialog_new_pass.setText("");
                                       dialog_new_pass_check.setText("");
                                       dialog_find_pass_code.setText("");
                                       guide_msg.setText("이메일로 인증코드가 발송됩니다.");
                                       newPassDialog.dismiss();
                                   },
                                   error -> {
                                       Log.e("AuthQuickstart", error.getCause().getMessage());
                                       Toast.makeText(getApplicationContext(), error.getCause().getMessage().split(":")[1].split("Service")[0], Toast.LENGTH_SHORT).show();
                                       Toast.makeText(getApplicationContext(), "CODE 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                       password.requestFocus();
                                   }
                           );
                       }
                   }
                });
                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this); //AlertDialog.Builder 객체 생성
                builder.setTitle("비밀번호 찾기"); //Dialog 제목
                builder.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        layout0.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        dialog_find_pass_id.setText("");
                        dialog_new_pass.setText("");
                        dialog_new_pass_check.setText("");
                        dialog_find_pass_code.setText("");
                        guide_msg.setText("이메일로 인증코드가 발송됩니다.");
                    }
                });
                newPassDialog=builder.create();
                newPassDialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                newPassDialog.show();
            }
        });
    }


}
