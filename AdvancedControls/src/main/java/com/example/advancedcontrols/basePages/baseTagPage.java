package com.example.advancedcontrols.basePages;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.activity.MainActivity;

/**
 * @author 李阳
 * @创建时间 2017/4/22 - 上午9:21
 * @描述
 * @ 当前版本:
 */

public class baseTagPage {

    public MainActivity mainActivity;
    public TextView tv_title;
    public FrameLayout fl_main;
    private View root;

    public baseTagPage(Context context) {
        this.mainActivity= (MainActivity) context;

        initView();
        initData();
        initEvent();
    }


    public void initView() {
        root=View.inflate(mainActivity, R.layout.fragment_main_basepage,null);

        tv_title = (TextView) root.findViewById(R.id.tv_main_basepage_title);
        fl_main= (FrameLayout) root.findViewById(R.id.fl_main_basepage_frame);
    }
    public void initData() {
    }

    public void initEvent() {
    }


    public View getRoot() {
        return root;
    }
}
