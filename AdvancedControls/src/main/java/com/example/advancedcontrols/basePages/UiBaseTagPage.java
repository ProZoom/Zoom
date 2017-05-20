package com.example.advancedcontrols.basePages;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.activity.FallingView.FlakeFallingView;
import com.example.advancedcontrols.activity.Image3CacheActivity;
import com.example.advancedcontrols.activity.Splash.SplashDemoActivity;
import com.example.advancedcontrols.activity.SurperTextView.SurperTextViewActivity;
import com.example.advancedcontrols.utils.L;

import static com.example.advancedcontrols.utils.Contants.uiItemTitle;

/**
 * @author 李阳
 * @创建时间 2017/4/22 - 上午10:41
 * @描述
 * @ 当前版本:
 */

public class UiBaseTagPage extends baseTagPage implements AdapterView.OnItemClickListener {


    private ListView lv_ui;


    public UiBaseTagPage(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("Ui");
        View view=View.inflate(mainActivity, R.layout.fragment_main_ui, null);
        fl_main.addView(view);
        lv_ui= (ListView) view.findViewById(R.id.lv_ui);

        //装备数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mainActivity,
                R.layout.list_item,
                uiItemTitle);

        //lv_ui.getDivider();
        lv_ui.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        lv_ui.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        L.i("Ui--position-->"+position);
        switch (position){
            case 0:
                break;
            case 1://雪花下落效果
                mainActivity.startActivity( new Intent(mainActivity, FlakeFallingView.class));

                break;
            case 2: //SurperTextView
                mainActivity.startActivity( new Intent(mainActivity, SurperTextViewActivity.class));
                break;
            case 3:
                mainActivity.startActivity( new Intent(mainActivity, SplashDemoActivity.class));
                break;

            case 5: //图片三级缓存
                mainActivity.startActivity( new Intent(mainActivity, Image3CacheActivity.class));
                break;
            case 6: //图片三级缓存
                mainActivity.startActivity( new Intent(mainActivity, Image3CacheActivity.class));
                break;
        }
    }
}
