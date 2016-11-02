package com.example.hosea.dr_r_android.api;

/**
 * Created by Hosea on 2016-11-03.
 */
import com.example.hosea.dr_r_android.dao.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by terry on 2015. 8. 27..
 * 목표
 * 1. AsyncTask와 HTTPURLConnection을 이용한 간단한 HTTP 호출 만들기
 * 2. 리턴된 JSON을 파싱하는 방법을 통하여, JSON 객체 다루는 법 습득하기
 * 3. Phone Location (GPS) API 사용 방법 파악하기
 *
 * 참고 자료 : http://developer.android.com/training/basics/network-ops/connecting.html
 * */
public class Api {

    final static String RestURL = "http://192.168.1.3:8080/get/userData";

    public ArrayList<UserVO> getUserData(){
        ArrayList<UserVO> userData = new ArrayList<UserVO>();
        UserVO userVO = new UserVO();

        try {
            // call API by using HTTPURLConnection
            URL url = new URL(RestURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = new JSONObject(getStringFromInputStream(in));

            // parse JSON
            userVO = parseJSON(json);
            userVO.setU_name("");
            userVO.setU_id(1);
            userVO.setLogin_id("");

            userData.add(userVO);


        }catch(MalformedURLException e){
            System.err.println("Malformed URL");
            e.printStackTrace();
            return null;

        }catch(JSONException e) {
            System.err.println("JSON parsing error");
            e.printStackTrace();
            return null;
        }catch(IOException e){
            System.err.println("URL Connection failed");
            e.printStackTrace();
            return null;
        }

        return userData;
    }

    private UserVO parseJSON(JSONObject json) throws JSONException {
        UserVO userVO = new UserVO();
        userVO.setU_id(json.getInt("temp"));
        userVO.setLogin_id(json.getString("temp"));
        userVO.setU_name(json.getString("name"));
//        userVO.setU_id(json.getJSONObject("main").getInt("temp"));

        return userVO;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}