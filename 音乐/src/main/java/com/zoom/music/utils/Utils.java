package com.zoom.music.utils;

/**
 * Created by abiguime on 2016/9/19.
 */
public class Utils {

    public static String fromMilliToSecond(int duration) {

        // 60000 ==> 60s
        // 1分钟 -》 60毫秒
        int minute = duration/60000;
        int sec = (duration - minute*60000)/1000;
        return (minute<10?"0":"")+minute+":"+(sec<10?"0":"")+sec;
    }
}
