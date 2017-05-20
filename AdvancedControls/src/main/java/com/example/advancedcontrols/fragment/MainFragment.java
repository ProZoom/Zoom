package com.example.advancedcontrols.fragment;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.basePages.FunctionBaseTagPage;
import com.example.advancedcontrols.basePages.SettingBaseTagPage;
import com.example.advancedcontrols.basePages.UiBaseTagPage;
import com.example.advancedcontrols.basePages.baseTagPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李阳
 * @创建时间 2017/4/21 - 下午5:16
 * @描述
 * @ 当前版本:
 */

public class MainFragment extends BaseFragment implements View.OnClickListener{



    private ViewPager vp_main;

    private RadioButton rb_main_ui;

    private RadioButton rb_main_function;

    private RadioButton rb_main_setting;


    private List<baseTagPage> page=new ArrayList();
    private MyAdapter adapter;




    @Override
    public int bindView() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        super.initView();
        vp_main= (ViewPager) getRootView().findViewById(R.id.vp_main);
        rb_main_ui= (RadioButton) getRootView().findViewById(R.id.rb_main_ui);
        rb_main_function= (RadioButton) getRootView().findViewById(R.id.rb_main_function);
        rb_main_setting= (RadioButton) getRootView().findViewById(R.id.rb_main_setting);
    }

    @Override
    public void initData() {
        super.initData();

        page.add(new UiBaseTagPage(mainActivity));
        page.add(new FunctionBaseTagPage(mainActivity));
        page.add(new SettingBaseTagPage(mainActivity));

        adapter=new MyAdapter();
        vp_main.setAdapter(adapter);
        selectPages(0);

    }


    @Override
    public void initEvent() {
        super.initEvent();
        vp_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                selectPages(vp_main.getCurrentItem());
            }
        });

        rb_main_ui.setOnClickListener(this);
        rb_main_function.setOnClickListener(this);
        rb_main_setting.setOnClickListener(this);
    }

    /**
     * @描述 同步ViewPager和地步RadioButton
     * @Param
     * @Return
     */
    private void selectPages(int currentItem) {
        vp_main.setCurrentItem(currentItem);
        switch (currentItem){
            case 0:
                rb_main_ui.setTextColor(Color.WHITE);
                rb_main_function.setTextColor(Color.BLACK);
                rb_main_setting.setTextColor(Color.BLACK);
                break;
            case 1:
                rb_main_ui.setTextColor(Color.BLACK);
                rb_main_function.setTextColor(Color.WHITE);
                rb_main_setting.setTextColor(Color.BLACK);
                break;
            case 2:
                rb_main_ui.setTextColor(Color.BLACK);
                rb_main_function.setTextColor(Color.BLACK);
                rb_main_setting.setTextColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_main_ui:
                selectPages(0);
                break;
            case R.id.rb_main_function:
                selectPages(1);
                break;
            case R.id.rb_main_setting:
                selectPages(2);
                break;
        }
    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return page.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            baseTagPage baseTagpage=page.get(position);
            View root=baseTagpage.getRoot();
            container.addView(root);
            return root;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
