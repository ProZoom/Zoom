package com.zoom.wise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zoom.wise.R;
import com.zoom.wise.utils.DensityUtils;
import com.zoom.wise.utils.L;

import java.util.ArrayList;
import java.util.List;

import static com.zoom.wise.utils.Contants.pic_guide;

/**
 * @author 李阳
 * @创建时间 2017-02-27  下午12:25
 * @描述 TODO
 * @ 当前版本:
 */

public class GuideActivity extends Activity {


    ViewPager vp_guides;
    LinearLayout ll_point;
    View v_redpoint;
    Button btn_startExp;

    List<View> guide_pics;

    myAdapter adapter;
    int disPoints;//点与点之间的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();//初始化界面
        initData();//初始化数据
        initEvent();//初始化事件

    }

    private void initEvent() {

        //监听布局完成，并计算dispoint
        v_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //取消注册 界面变化而发生的回调结果
                v_redpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //计算点与点之间的距离
                disPoints = (ll_point.getChildAt(1).getLeft() - ll_point.getChildAt(0).getLeft());
            }
        });


        //btn_startExp按钮事件
        btn_startExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始体验
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //监听viewpager页面变化事件
        vp_guides.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                L.i("onPageScrolled");

                //positionOffset 移动的比例值
                //计算红点的左边距
                float leftMargin = disPoints * (position + positionOffset);

                //设置红点的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v_redpoint.getLayoutParams();
                layoutParams.leftMargin = Math.round(leftMargin);//对float类型四舍五入

                //重新设置布局
                v_redpoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                L.i("onPageSelected");
                //当前ViewPager显示的页码
                //如果ViewPager滑动到第三个页码(最后一页)，显示button
                if(position==guide_pics.size()-1){
                    btn_startExp.setVisibility(View.VISIBLE);
                }else {
                    btn_startExp.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                L.i("onPageScrollStateChanged");

            }
        });
    }

    private void initData() {

        //定义viewpager使用的容器
        guide_pics=new ArrayList<>();

        //初始化容器中的数据
        for(int i=0;i<pic_guide.length;i++){
            //将图片加入缓存

            //将布局加入缓存
            View v_temp=LayoutInflater.from(getApplicationContext()).inflate(pic_guide[i],null);

            guide_pics.add(v_temp);


            //给点容器LinearLayout初始化添加灰色点
            View v_point=new View(getApplicationContext());
            v_point.setBackgroundResource(R.drawable.point_gray);
            int dp=10;
            //设置灰色点大小
            LinearLayout.LayoutParams param=
                    new LinearLayout.LayoutParams(
                            DensityUtils.dip2px(getApplicationContext(),dp),
                            DensityUtils.dip2px(getApplicationContext(),dp));

            //设置点与点之间的距离
            if(i!=0){//过滤第一个点
                param.leftMargin=10;//10px
            }
            v_point.setLayoutParams(param);//无缝的拼接在一起

            ll_point.addView(v_point);
        }


        adapter=new myAdapter();

        vp_guides.setAdapter(adapter);

    }

    private void initView() {
        setContentView(R.layout.activity_guide);

        vp_guides= (ViewPager) findViewById(R.id.vp_guide_pages);
        ll_point= (LinearLayout) findViewById(R.id.ll_guide_points);
        v_redpoint=findViewById(R.id.v_guide_redpoint);
        btn_startExp= (Button) findViewById(R.id.bt_guide_startexp);

    }


    private class myAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return guide_pics.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;//过滤和缓存作用
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//从viewpager中移除
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //获取View
            View child=guide_pics.get(position);
            //添加view
            container.addView(child);
            return child;
        }
    }
}
