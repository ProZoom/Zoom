package com.zoom.wise.newscenterpage;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zoom.wise.activity.MainActivity;

/**
 * @author 李阳
 * @创建时间 2017/3/22 - 下午10:55
 * @描述
 * @ 当前版本:
 */

public class TopicBaseNewsCenterPage extends BaseNewsCenterPage {
    public TopicBaseNewsCenterPage(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public View initView() {
        //要展示的内容

        TextView tv=new TextView(mainActivity);
        tv.setText("专题的内容");
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLUE);
        return tv;
    }
}
