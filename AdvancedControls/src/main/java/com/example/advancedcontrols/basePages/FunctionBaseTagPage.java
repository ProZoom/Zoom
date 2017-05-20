package com.example.advancedcontrols.basePages;


import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.advancedcontrols.R;

/**
 * @author 李阳
 * @创建时间 2017/4/22 - 上午10:41
 * @描述
 * @ 当前版本:
 */

public class FunctionBaseTagPage extends baseTagPage {

    private WebView wb_function;

    public FunctionBaseTagPage(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        super.initView();
        View view=View.inflate(mainActivity, R.layout.fragment_main_function, null);
        fl_main.addView(view);
        wb_function= (WebView) view.findViewById(R.id.wb_function);

        wb_function.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wb_function.loadUrl("http://professorzoom.top/categories/Architect/");
    }


    

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("架构漫谈");

    }

    @Override
    public void initEvent() {
        super.initEvent();

    }
}
