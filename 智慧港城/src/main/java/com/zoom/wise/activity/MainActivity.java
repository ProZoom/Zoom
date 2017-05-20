package com.zoom.wise.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zoom.wise.R;
import com.zoom.wise.fragment.LeftMenuFragment;
import com.zoom.wise.fragment.MainContentFragment;

import static com.zoom.wise.utils.DensityUtils.dip2px;

/**
 * 作者：   ${USER}
 * 创建时间：    ${DATE}
 * 描述：
 * 提交者：liyang
 * 提交时间：
 * 当前版本：
 */
public class MainActivity extends SlidingFragmentActivity{


    private static final String	MAIN_MUNE_TAG	= "MAIN_MUNE_TAG";
    private static final String LEFT_MUNE_TAG = "LEFT_MUNE_TAG";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initData();

    }


    public void initData() {
        FragmentManager fragmentmanager=getSupportFragmentManager();
        //获取事物

        FragmentTransaction transaction=fragmentmanager.beginTransaction();
        //完成替换

        //完成主菜单的替换
        transaction.replace(R.id.fragment_main_menu,
                new MainContentFragment(), MAIN_MUNE_TAG);
        transaction.replace(R.id.fragment_left_menu,
                new LeftMenuFragment(),LEFT_MUNE_TAG);
        transaction.commit();
    }

    private void initView() {
        //设置主界面
        setContentView(R.layout.fragment_content);
        //设置左滑菜单
        setBehindContentView(R.layout.fragment_left);

        SlidingMenu sm=getSlidingMenu();

        //设置滑动模式
        sm.setMode(SlidingMenu.LEFT);
        //设置滑动位置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置剩余空间
        sm.setBehindOffset(dip2px(this,180));
    }

    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fragmentmanager=getSupportFragmentManager();
        LeftMenuFragment leftFragment= (LeftMenuFragment) fragmentmanager.findFragmentByTag(LEFT_MUNE_TAG);
        return leftFragment;
    }

    public MainContentFragment getMainMenuFragment() {
        FragmentManager fragmentmanager=getSupportFragmentManager();
        MainContentFragment mainFragment= (MainContentFragment) fragmentmanager.findFragmentByTag(MAIN_MUNE_TAG);
        return mainFragment;
    }
}
