package com.zoom.wise.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author 李阳
 * @创建时间 2017-02-27  上午11:24
 * @描述
 * @ 当前版本:
 */
public class SpTools {


    public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp=
                context.getSharedPreferences(Contants.CONFIGFILE,context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();//提交保存健值对
    }


    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sp=
                context.getSharedPreferences(Contants.CONFIGFILE,context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }


    public static void setString(Context context,String key,String value){
        SharedPreferences sp=
                context.getSharedPreferences(Contants.CONFIGFILE,context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();//提交保存健值对
    }


    public static String getString(Context context,String key,String defValue){
        SharedPreferences sp=
                context.getSharedPreferences(Contants.CONFIGFILE,context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }
}
