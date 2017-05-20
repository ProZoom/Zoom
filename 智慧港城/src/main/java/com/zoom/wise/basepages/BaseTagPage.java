package com.zoom.wise.basepages;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zoom.wise.R;
import com.zoom.wise.activity.MainActivity;
import com.zoom.wise.utils.L;

/**
 * @author 李阳
 * @创建时间 2017-02-28  上午9:09
 * @描述 TODO
 *@ 当前版本:
 */
public class BaseTagPage {


    MainActivity mainActivity;
    View rootView;
    ImageButton iv_menu;
    TextView tv_title;
    FrameLayout fl_content;
    public BaseTagPage(Context context) {
        this.mainActivity= (MainActivity) context;
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("左上角按钮");
                mainActivity.getSlidingMenu().toggle();//左侧菜单的开关
            }
        });
    }


    public void initData() {

    }

    private void initView() {
        //界面的根布局
        rootView=View.inflate(mainActivity, R.layout.fragment_content_base,null);
        iv_menu= (ImageButton) rootView.findViewById(R.id.ib_base_content_menu);
        tv_title= (TextView) rootView.findViewById(R.id.tv_base_content_title);
        fl_content= (FrameLayout) rootView.findViewById(R.id.fl_base_content_tag);
    }


    public void switchPage(int position){
    }


    public View getRoot() {
        return rootView;
    }
}
