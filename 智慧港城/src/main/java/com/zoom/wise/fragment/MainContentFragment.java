package com.zoom.wise.fragment;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zoom.wise.R;
import com.zoom.wise.basepages.BaseTagPage;
import com.zoom.wise.basepages.GovAffairsBaseTagPager;
import com.zoom.wise.basepages.HomeBaseTagPager;
import com.zoom.wise.basepages.NewCenterBaseTagPager;
import com.zoom.wise.basepages.SmartServices;
import com.zoom.wise.utils.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 李阳
 * @创建时间 2017-02-27  下午7:35
 * @描述 TODO
 * @ 当前版本:
 */
@ContentView(R.layout.fragment_main_content)
public class MainContentFragment extends BaseFragment {


    @ViewInject(R.id.vp_main_content_pages)
    private ViewPager viewPager;

    int current_item=0;

    @ViewInject(R.id.rb_main_content_home)
    private RadioButton rb_home;

    @ViewInject(R.id.rb_main_content_newscenter)
    private RadioButton rb_news;

    @ViewInject(R.id.rb_main_content_smartservices)
    private RadioButton rb_smart;

    @ViewInject(R.id.rb_main_content_gov)
    private RadioButton rb_gov;



    private List<BaseTagPage> pages	= new ArrayList<BaseTagPage>();
    public void selectPage(int i){
        viewPager.setCurrentItem(i);

        if(i==0){
            //不让左侧菜单划出来
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }else {
            //可以让左侧菜单划出来
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        }
        current_item=i;
        switch (i){
            case 0:
                rb_home.setTextColor(Color.WHITE);
                rb_news.setTextColor(Color.BLACK);
                rb_smart.setTextColor(Color.BLACK);
                rb_gov.setTextColor(Color.BLACK);
                //rb_setting.setTextColor(Color.BLACK);
                break;
            case 1:
                rb_home.setTextColor(Color.BLACK);
                rb_news.setTextColor(Color.WHITE);
                rb_smart.setTextColor(Color.BLACK);
                rb_gov.setTextColor(Color.BLACK);
                //rb_setting.setTextColor(Color.BLACK);
                break;
            case 2:
                rb_home.setTextColor(Color.BLACK);
                rb_news.setTextColor(Color.BLACK);
                rb_smart.setTextColor(Color.WHITE);
                rb_gov.setTextColor(Color.BLACK);
                //rb_setting.setTextColor(Color.BLACK);
                break;
            case 3:
                rb_home.setTextColor(Color.BLACK);
                rb_news.setTextColor(Color.BLACK);
                rb_smart.setTextColor(Color.BLACK);
                rb_gov.setTextColor(Color.WHITE);
               // rb_setting.setTextColor(Color.BLACK);
                break;
            case 4:
                rb_home.setTextColor(Color.BLACK);
                rb_news.setTextColor(Color.BLACK);
                rb_smart.setTextColor(Color.BLACK);
                rb_gov.setTextColor(Color.BLACK);
                //rb_setting.setTextColor(Color.WHITE);
                break;
            default:break;
        }
    }

    @Override
    public void initEvent() {
        //viewpager滑动监听事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                L.i("onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                L.i("onPageSelected");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                L.i("onPageScrollStateChanged");
                selectPage(viewPager.getCurrentItem());
            }
        });

        //////////////////
        rb_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPage(0);
            }
        });
        rb_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           selectPage(1);
            }
        });
        rb_smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPage(2);
            }
        });
        rb_gov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPage(3);
            }
        });
      /*  rb_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPage(4);
            }
        });*/

    }


    @Override
    public void initData() {
        // 首页
        pages.add(new HomeBaseTagPager(mainActivity));
        // 新闻中心
        pages.add(new NewCenterBaseTagPager(mainActivity));
        // 智慧服务中心
        pages.add(new SmartServices(mainActivity));
        // 政务中心
        pages.add(new GovAffairsBaseTagPager(mainActivity));
        //
        //pages.add(new Setting(mainActivity));


        MyAdapter adapter = new MyAdapter();
        viewPager.setAdapter(adapter);
       // viewPager.setOffscreenPageLimit(2);//设置预加载为：前后各两个页面
        selectPage(0);
    }


    public void leftMenuClickSwitchPages(int subSelectonndex){
        BaseTagPage baseTagPage=pages.get(current_item);
        baseTagPage.switchPage(subSelectonndex);
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("lazy","instantiateItem: "+position);

            BaseTagPage baseTagPage = pages.get(position);
            View root = baseTagPage.getRoot();
            container.addView(root);
            return root;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("lazy","destroyItem: "+position);
            container.removeView((View) object);
        }

    }
}
