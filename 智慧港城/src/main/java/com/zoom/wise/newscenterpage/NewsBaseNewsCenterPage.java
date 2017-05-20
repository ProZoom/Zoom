package com.zoom.wise.newscenterpage;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.zoom.wise.R;
import com.zoom.wise.activity.MainActivity;
import com.zoom.wise.bean.NewsCenterData;
import com.zoom.wise.newstpipage.TPINewsCenterPage;
import com.zoom.wise.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李阳
 * @创建时间 2017/3/21 - 上午11:07
 * @描述
 * @ 当前版本:
 */


public class NewsBaseNewsCenterPage extends BaseNewsCenterPage {

    private ViewPager vp_newcenter;

    private TabPageIndicator tpi_newscenter;

    List<NewsCenterData.NewsData.ViewTagData> viewTagDatas=new ArrayList<>();//页签的数据

    public NewsBaseNewsCenterPage(MainActivity mainActivity, List<NewsCenterData.NewsData.ViewTagData> children) {
        super(mainActivity);
        this.viewTagDatas=children;
    }

    @Override
    public View initView() {

        View rootView=View.inflate(mainActivity,R.layout.newscenterpage_content,null);

        tpi_newscenter= (TabPageIndicator) rootView.findViewById(R.id.tpi_newscenter);
        vp_newcenter= (ViewPager) rootView.findViewById(R.id.vp_newscenter);

        return rootView;
    }

    @Override
    public void initData() {
        myAdapter adapter=new myAdapter();
        // 设置ViewPager的适配器
        vp_newcenter.setAdapter(adapter);
        // 把ViewPager和Tabpagerindicator关联
        tpi_newscenter.setViewPager(vp_newcenter);
        super.initData();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        tpi_newscenter.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                L.i("tpi_newscenter--onPageScrolled--position = "+position);
            }

            @Override
            public void onPageSelected(int position) {
                L.i("tpi_newscenter--onPageSelected--postion = "+position);
                if(position!=0){//在第二个标签页以后禁止左滑
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }else {
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                L.i("tpi_newscenter--onPageScrollStateChanged--state = "+state);

            }
        });
    }

    private class myAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return viewTagDatas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //要展示的内容
            TPINewsCenterPage tpiPager=new TPINewsCenterPage(mainActivity,viewTagDatas.get(position));
            View rootView = tpiPager.getRoot();
            container.addView(rootView);
            return rootView;
        }

        /**
         * @描述
         * @Param 页签显示数据，调用该方法
         * @Return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return viewTagDatas.get(position).title;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
