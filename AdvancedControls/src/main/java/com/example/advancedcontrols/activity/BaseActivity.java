package com.example.advancedcontrols.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Stack;

/**
 * @author 李阳
 * @创建时间 2017-03-01  上午9:13
 * @描述    BaseActivity
 * @ 当前版本:
 */

public class BaseActivity extends FragmentActivity {

    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    /** 是否沉浸状态栏 **/
    private boolean isSetStatusBar = false;
    /** 是否允许全屏 **/
    private boolean mAllowFullScreen = false;
    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRoate = false;
    /** 用来保存所有已打开的Activity */
    private static Stack<Activity> listActivity = new Stack<Activity>();


    /*********** activity生命周期 *************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //避免虚拟按键对布局的覆盖
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        L("BaseActivity-->onCreate");
        //是否允许沉浸式状态栏
        if (isSetStatusBar) {
            L("允许沉浸式状态栏");
            steepStatusBar();
        }
        //是否允许全屏
        if(mAllowFullScreen){
            L("允许全屏");
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        initView();//初始化界面
        initData();//初始化数据
        initListenerEvent();//初始化监听事件
    }


    @Override
    protected void onResume() {
        super.onResume();
        L("BaseActivity-->onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        L("BaseActivity-->onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        L("BaseActivity-->onStop");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L("BaseActivity-->onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L("BaseActivity-->onDestroy");

    }

    /*************************************/

    /*初始化界面*/
    public void initView() {}

    /*初始化数据*/
    public void initData() {}

    /*初始化监听事件*/
    public void initListenerEvent() {}

    /***********沉浸状态栏***********/
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /********** activity跳转 ***********/
    public void openActivity(Class<?> targetActivityClass){
        openActivity(targetActivityClass,null);
    }
    public void openActivity(Class<?> targetActivityClass,Bundle bundle){

        Intent intent=new Intent(this,targetActivityClass);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    public void openActivityAndCloseThis(Class<?> targetActivityClass){
        openActivity(targetActivityClass,null);
        this.finish();
    }
    public void openActivityAndCloseThis(Class<?> targetActivityClass,Bundle bundle){
        openActivity(targetActivityClass,bundle);
        this.finish();
    }
    /**********************************/


    /********** 收起键盘 **************/
    public void closeInputKeyBoard(){
        View view=getWindow().peekDecorView();//用于判断虚拟键盘是否打开
        if(view!=null){
            InputMethodManager inputmanager= (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**********************************/


    /********** 双击退出程序 ************/
    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            //判断是否是在两秒内连续点击返回键，是则退出，不是不退出
            if(System.currentTimeMillis()-exitTime>2000){
                ToastShowShort("再按一次退出程序");
                exitTime=System.currentTimeMillis();
            }else {
                finishAll();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**********************************/

    /***关闭所有(前台、后台)Activity,注意：请已BaseActivity为父类***/
    private void finishAll() {
        int len=listActivity.size();
        for(int i=0;i<len;i++){
            Activity activity=listActivity.pop();
            activity.finish();
        }
    }

    /********** Toast简化 **************/
    public void ToastShowShort(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
    public void ToastShowShort(int i){
        Toast.makeText(this,i,Toast.LENGTH_SHORT).show();
    }
    public void ToastShowLong(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
    public void ToastShowLong(int i){
        Toast.makeText(this,i,Toast.LENGTH_LONG).show();
    }
    /**********************************/

    /*****[日志输出]*****/
    protected void L(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }
}
