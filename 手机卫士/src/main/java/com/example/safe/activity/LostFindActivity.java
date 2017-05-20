package com.example.safe.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.safe.R;


public class LostFindActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }



    private void initView() {
        setContentView(R.layout.activity_lostfind);
    }
}
