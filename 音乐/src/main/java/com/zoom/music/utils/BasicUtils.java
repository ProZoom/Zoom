package com.zoom.music.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class BasicUtils {


	public  void setTextMarquee(TextView textView_setTextMarquee) {
        if (textView_setTextMarquee != null) {
            textView_setTextMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView_setTextMarquee.setSingleLine(true);
            textView_setTextMarquee.setSelected(true);
            textView_setTextMarquee.setFocusable(true);
            textView_setTextMarquee.setFocusableInTouchMode(true);
        }
    }

	public static void mT(Activity activity, String MSG){
        boolean Toast_Enable=true;
        if(Toast_Enable){
            Toast.makeText(activity,activity.getClass().getSimpleName()+":"+MSG,Toast.LENGTH_SHORT).show();
        }
    }
}

