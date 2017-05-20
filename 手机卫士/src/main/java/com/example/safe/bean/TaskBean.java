package com.example.safe.bean;

import android.graphics.drawable.Drawable;

/**
 * 作者：   李阳
 * 创建时间：  2017/2/4
 * 参数：
 * 描述：进程的数据封装类
 *
 */

public class TaskBean {
    private Drawable icon;//apk图标
    private String name;//apk的名字
    private String packname;//apk包名
    private long memSize;//apk占用的内存大小
    private boolean isSystem;//是否系统apk

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }




    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }
}
