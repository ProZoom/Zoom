package com.example.advancedcontrols.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.fragment.MainFragment;

/**
 * @author 李阳
 * @创建时间 2017/4/21 - 下午12:47
 * @描述
 * @ 当前版本:
 */

public class MainActivity extends BaseActivity {

    private static final String MAIN_FRAGMENT_TAG ="MAIN_TAG" ;

    @Override
    public void initView() {
        //设置fragment容器
        setContentView(R.layout.fragment_main_content);
        super.initView();
    }

    @Override
    public void initData() {
        //获取Fragment管理器
        FragmentManager fragmentManager=getSupportFragmentManager();
        //打开事务
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        //把内容显示至帧布局
        fragmentTransaction.replace(R.id.fl_main_content, new MainFragment(), MAIN_FRAGMENT_TAG);

        //提交事物
        fragmentTransaction.commit();
    }
}
