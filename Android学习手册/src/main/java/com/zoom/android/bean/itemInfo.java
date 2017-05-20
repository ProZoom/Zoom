package com.zoom.android.bean;

/**
 * Created by Administrator on 2016/10/28.
 */

public class itemInfo {

    private static String id;
    private static String title;
    private static String content;

    public itemInfo(String id,String title,String content) {
        itemInfo.id = id;
        itemInfo.title = title;
        itemInfo.content = content;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        itemInfo.id = id;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        itemInfo.title = title;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        itemInfo.content = content;
    }
}
