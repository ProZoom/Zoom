package com.zoom.wise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.zoom.wise.R;
import com.zoom.wise.utils.Contants;
import com.zoom.wise.utils.L;
import com.zoom.wise.utils.SpTools;
import com.zoom.wise.utils.T;

/**
 * Created by liyang on 2017/2/27.
 */

public class SplashActivity extends Activity {

    ImageView iv_spalsh;
    AnimationSet as;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();     //初始化界面
        startAnimation();//开始播放动画
        initEvent();//初始化事件

    }

    private void initEvent() {

        //监听动画状态
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                L.i("onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                L.i("onAnimationEnd");
                //监画播放完，判断是否进入向导界面
                if(SpTools.getBoolean(getApplicationContext(), Contants.ISSETUP,false)){
                    //进入主界面
                    T.showShort(getApplicationContext(),"主界面");
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);

                }else {
                    //进入设置向导
                    T.showShort(getApplicationContext(),"设 置向导");
                    Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                L.i("onAnimationRepeat");
            }
        });
    }

    private void startAnimation() {
        // false 代表动画集中每种动画都采用各自的动画插入器(数学函数)
        as=new AnimationSet(false);

        //旋转动画，设置锚点
        RotateAnimation ra=new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);//设置中间为锚点
        ra.setDuration(1500);//设置过渡时间
        ra.setFillAfter(true);//播放完动画后，停留在当前动画

        as.addAnimation(ra);//添加到动画集

        //渐变动画
        AlphaAnimation aa=new AlphaAnimation(0,1);
        aa.setDuration(1500);
        aa.setFillAfter(true);

        as.addAnimation(aa);

        //缩放动画
        ScaleAnimation sa=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(1500);
        sa.setFillAfter(true);

        as.addAnimation(sa);

        //播放动画
        iv_spalsh.startAnimation(as);
        //进入向导界面或者主界面
    }

    private void initView() {

        //设置主界面
        setContentView(R.layout.activity_splash);

        //获取背景图片
        iv_spalsh= (ImageView) findViewById(R.id.iv_splash);

    }
}
