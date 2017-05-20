package com.example.safe.utils;


import android.graphics.Paint;
import android.widget.TextView;

public class TextViewUtils {


    //添加删除线
    public static void AddUDeleteLine(TextView tv){
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }


    //加粗

    public static void AddStrike(TextView tv){
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }


    //添加下划线
    public static void AddUnderLine(TextView tv){
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

}
