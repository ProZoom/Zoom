package com.zoom.wise.utils;

import android.content.Context;

/**
 * @author 李阳
 * @创建时间 2017-02-27  下午3:00
 * @描述 TODO
 * @ 当前版本:
 */
public class DensityUtils {

    // 根据手机的分辨率从 dip 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
