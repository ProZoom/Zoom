package com.example.advancedcontrols.activity;

import android.os.Handler;

import com.example.advancedcontrols.R;

/**
 * @author 李阳
 * @创建时间 2017/4/21 - 上午11:48
 * @描述
 * @ 当前版本:
 */

public class SplashActivity extends BaseActivity {

    private static final long DELAY_MS = 3000;
    Handler mHandler=new Handler();

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initListenerEvent() {
        super.initListenerEvent();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(MainActivity.class);
                finish();
            }
        },DELAY_MS);
    }
}
