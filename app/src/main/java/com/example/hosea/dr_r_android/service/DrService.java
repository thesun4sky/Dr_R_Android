package com.example.hosea.dr_r_android.service;

import com.example.hosea.dr_r_android.dao.DiaryVO;
import com.example.hosea.dr_r_android.dao.ResultVO;

import java.util.ArrayList;

/**
 * Created by Hosea on 2016-11-09.
 */
interface DrService {

    ResultVO writeDiary(DiaryVO diary);
    ArrayList<DiaryVO> getDiaries (int num);
}
