package com.zoom.wise.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author 李阳
 * @创建时间 2017/3/28 - 下午4:02
 * @描述
 * @ 当前版本:
 */

public class InterceptScrollViewPager extends ViewPager {
    public InterceptScrollViewPager(Context context) {
        super(context);
    }

    public InterceptScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
