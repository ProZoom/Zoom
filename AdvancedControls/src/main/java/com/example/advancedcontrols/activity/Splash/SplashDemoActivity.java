package com.example.advancedcontrols.activity.Splash;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.activity.BaseActivity;
import com.example.advancedcontrols.adapter.AnimationAdapter;
import com.example.advancedcontrols.utils.Contants;
import com.example.advancedcontrols.utils.L;

/**
 * @author 李阳
 * @创建时间 2017/5/3 - 上午8:39
 * @描述
 * @ 当前版本:
 */

public class SplashDemoActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    ListView lv_splash_demo;
    private AnimationAdapter adapter;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_splashdemo);
        lv_splash_demo= (ListView) findViewById(R.id.lv_splash_demo);

    }

    @Override
    public void initData() {
        super.initData();
        adapter = new AnimationAdapter(this, Contants.splashName);
        lv_splash_demo.setAdapter(adapter);
    }

    @Override
    public void initListenerEvent() {
        super.initListenerEvent();
        lv_splash_demo.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        L.i("splash-->position-->"+position);
        switch (position){
            case 0://Zaker风格
                startActivity( new Intent(this, ZakerActivity.class));
                break;
            case 1://镜头风格_由远至近
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
