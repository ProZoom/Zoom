package com.zoom.music.cviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zoom.music.R;
import com.zoom.music.utils.BitmapTools;


/**
 * Created by abiguime on 2016/9/13.
 */

public class TurningView extends View {


    private static final int PERCENTAGE = 8;
    private static final long SPEED = 50;
    int album_default = R.mipmap.prof;

    // 视图高宽
    int width, height;

    // 画出透明圆形的画笔
    Paint mExtPaint;
    private int mCenterY;

    Bitmap mAlbumPic; /*专辑图*/


    Matrix rMatrix = new Matrix();

    int degree = 0; /* 角度 */

    Handler mHandler = new Handler();


    private boolean playing = true;
    Runnable run = new Runnable() {
        @Override
        public void run() {

            rMatrix.postRotate(degree++, getScreenWidth()/2, mCenterY);
            invalidate(); /*重绘*/

            /*让本方法通过递归实现不停的旋转的操作*/
            if (playing)
            mHandler.postDelayed(run, SPEED);
        }
    };

    public TurningView(Context context) {
        super(context, null);
    }

    public TurningView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /*获取高宽*/
        height = 6*getScreenHeight()/11;
        height = 5*height/6; /*把高度减小点儿*/
        width = height;

        /*计算专辑图的中心点*/
        Log.d("xxx", "\n------");
        Log.d("xxx", (2/11)*getScreenHeight() +"");
        Log.d("xxx", (2)*getScreenHeight()/11 +"");

        mCenterY = (2/11)*getScreenHeight() + height/2;
        initPaint(); /* 初始化画笔*/
        initBitmap(); /* 初始化专辑图片*/
    }

    private void initBitmap() {

        mAlbumPic = BitmapFactory.decodeResource(getResources(), album_default);
        /*压缩图片*/
        mAlbumPic = Bitmap.createScaledBitmap(mAlbumPic, height, height, false);
        /*把压缩图片转成圆形图*/
        mAlbumPic = BitmapTools.getRoundedRectBitmap(mAlbumPic, height, PERCENTAGE);
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private void initPaint() {

        mExtPaint=  new Paint(Paint.ANTI_ALIAS_FLAG);
        mExtPaint.setColor(Color.parseColor("#33000000"));
        mExtPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.concat(rMatrix);
        /*
        *cx，cy -》圆圈中心点坐标
         * radius-》圆圆半径
          * paint-》画笔
        * */
        /*在view上画内容的方法。*/
        canvas.drawCircle(getScreenWidth()/2, mCenterY, height/2 /*+ (height*PERCENTAGE/100)*/, mExtPaint);


        /*圆形图片。*/
        if (mAlbumPic != null)
            canvas.drawBitmap(mAlbumPic, getScreenWidth()/2 - mAlbumPic.getWidth()/2, mCenterY - mAlbumPic.getHeight()/2, null);

        rMatrix.reset();/*重设矩阵*/
    }


    public void play(){
        playing = true;
        mHandler.post(run);
    }

    public void pause () {
        playing = false;
    }
}
