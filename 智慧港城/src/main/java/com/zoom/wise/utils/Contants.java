package com.zoom.wise.utils;

import com.zoom.wise.R;

/**
 * @author 李阳
 * @创建时间 2017-02-27  上午11:46
 * @描述 TODO
 * @ 当前版本:
 */
public class Contants {

    //apk发布修改该ip
    //绝对路径
    public static String Absolute_path="http://192.168.0.103:8080/zhbj";
    public static String NEWSCENTERURL=Absolute_path+"/categories.json";

    public static boolean netIsAccess;

    public static String CONFIGFILE="cachevalue";
    public static String ISSETUP="issetup";


    public static int[] pic_guide= new int[]{
                R.layout.guider_1,
                R.layout.guider_2,
                R.layout.guider_3};
}
