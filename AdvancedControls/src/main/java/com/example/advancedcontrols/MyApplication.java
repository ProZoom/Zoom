package com.example.advancedcontrols;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;

/**
 * @author 李阳
 * @创建时间 2017/4/21 - 上午11:43
 * @描述
 * @ 当前版本:
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this,"1d343b257f0a0");
    }
}
