package com.example.safe.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contents.SPFILE, context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,value).commit();//提交数据
    }


    public static String getString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contents.SPFILE, context.MODE_PRIVATE);

        return sharedPreferences.getString(key,value);
    }




    public static void putBoolean(Context context, String key, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contents.SPFILE, context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,value).commit();//提交数据
    }


    public static boolean getBoolean(Context context,String key,boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contents.SPFILE, context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(key,value);
    }
}
