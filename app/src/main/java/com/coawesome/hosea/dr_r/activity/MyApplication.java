package com.coawesome.hosea.dr_r.activity;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class MyApplication extends Application {

    private int u_id;
    private String deviceId;
    @Override
    public void onCreate() {
        //전역 변수 초기화
        u_id = 0;
        deviceId = "";
        super.onCreate();

        try {

            //AWS Cognito 연동
            Amplify.addPlugin(new AWSCognitoAuthPlugin());

            //AWS Lambda REST 연동
            Amplify.addPlugin(new AWSApiPlugin());

            //AWS S3 Storage 연동
            Amplify.addPlugin(new AWSS3StoragePlugin());

            //AWS IAM등 기본 설정값 연동
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");

        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setU_id(int u_id){
       this.u_id = u_id;
    }

    public int getU_id(){
        return u_id;
    }

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceId(){
        return deviceId;
    }

}