package com.zoom.wise.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author 李阳
 * @创建时间   2017/3/20 - 上午8:20
 * @描述  不能滑动,懒加载
 * @ 当前版本:
 */
public class NoScrollViewPage extends ViewPager {

	public NoScrollViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 不让自己拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		return false;
	}

}
