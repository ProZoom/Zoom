package com.example.safe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by liyang on 2017/1/14.
 * apk信息封装类
 */

public class AppBean {
    private String apkPath;
    private Drawable icon;//apk图标
    private String appName;//apk的名字
    private long size;//占用的大小
    private boolean isSd;//是否存在SD上
    private boolean isSystem;//是否是系统apk
    private String packName;//apk包名

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSd() {
        return isSd;
    }

    public void setSd(boolean sd) {
        isSd = sd;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean syatem) {
        isSystem = syatem;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }


    @Override
    public String toString() {
        return "icon: "+icon+"  appName: "+appName+"  size: "+size+"  isSD: "+isSd+"  isSys: "+isSystem+"  packName: "+packName+"  apkPath: "+apkPath+"\n";
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
