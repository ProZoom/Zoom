package com.zoom.wise.newscenterpage;

import android.view.View;

import com.zoom.wise.activity.MainActivity;

/**
 * @author 李阳
 * @创建时间 2017/3/21 - 上午10:53
 * @描述
 * @ 当前版本:
 */

public abstract class BaseNewsCenterPage {

     MainActivity mainActivity;
    private View rootView;//根布局

    public BaseNewsCenterPage(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        rootView= initView();
        initEvent();
    }



    public void initEvent() {
    }

    public void initData(){}


    public abstract View initView();

    public View getRoot(){
        return rootView;
    }
}
