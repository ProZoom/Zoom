package com.zoom.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zoom.android.R;
import com.zoom.android.db.DBManager;

/**
 * Created by Administrator on 2016/10/27.
 */

public class SplashActivity extends Activity {

    private final long delayMillis=4000;
    public DBManager dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //拷贝数据库到app文件下
        copyDbToApp();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },delayMillis);
    }

    private void copyDbToApp() {
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
        dbHelper.closeDatabase();
    }
}
