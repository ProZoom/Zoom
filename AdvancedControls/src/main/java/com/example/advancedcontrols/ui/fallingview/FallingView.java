package com.example.advancedcontrols.ui.fallingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.advancedcontrols.R;

/**
 * @author 李阳
 * @创建时间 2017/5/3 - 下午5:20
 * @描述
 * @ 当前版本:
 */

public class FallingView extends RelativeLayout {


    private static final int DEFAULT_FLAKES_DENSITY = 80;
    private static final int DEFAULT_DELAY = 10;
    private static final int DEFAULT_SCALE = 3;

    private Paint mPaint;
    private Bitmap mFlakeBitmap;
    private Flake[] mFlakes;


    private int mImgId;
    private int mScale;
    private int mWidth;
    private int mHeight;
    private int mRawWidth;
    private int mFlakesDensity = DEFAULT_FLAKES_DENSITY;
    private int mDelay = DEFAULT_DELAY;

    public FallingView(Context context) {
        this(context,null);
    }

    public FallingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FallingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setBackgroundColor(Color.TRANSPARENT);

        if(attrs!=null){  //获取布局里的属性值

            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.FallingView);//获取控件属性值
            
            mImgId = a.getResourceId(R.styleable.FallingView_flakeSrc,R.mipmap.snow_flake);
            
            mScale = a.getInt(R.styleable.FallingView_flakeScale,DEFAULT_SCALE);
            
            mFlakesDensity = a.getInt(R.styleable.FallingView_flakeDensity,DEFAULT_FLAKES_DENSITY);
            
            mDelay = a.getInt(R.styleable.FallingView_fallingDelay,DEFAULT_DELAY);

        }

        //设置画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * @描述
     * @Param
     * @Return 
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w!=oldw||h!=oldh){
            mWidth = w;
            mHeight = h;
            mRawWidth = initScale(mScale);
            initDenstity(w,h,mRawWidth);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (Flake flake : mFlakes){
            flake.draw(canvas,mFlakeBitmap);
        }
        getHandler().postDelayed(mRunnable,mDelay);
    }

    private  Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();//请求重新draw()，但只会绘制调用者本身。
        }
    };

    private void initDenstity(int w, int h, int mRawWidth) {
        mFlakes = new Flake[mFlakesDensity];
        for (int i = 0; i < mFlakesDensity; i++) {
            mFlakes[i] = Flake.creat(w,h,mPaint,mRawWidth/mScale);
        }
    }

    private int initScale(int mScale) {
        BitmapFactory.Options op= new BitmapFactory.Options();//获取参数控制器
        op.inJustDecodeBounds=true;//true只会返回这个bitmap的尺寸
        BitmapFactory.decodeResource(getResources(),mImgId,op);//
        int rawWidth=op.outWidth;//bitmap宽度
        mRawWidth=rawWidth;
        op.inSampleSize=mScale;
        //这个值是一个int，当它小于1的时候，将会被当做1处理，
        // 如果大于1，那么就会按照比例（1 / inSampleSize）缩小bitmap的宽和高、降低分辨率，
        // 大于1时这个值将会被处置为2的倍数。
        // 例如，width=100，height=100，inSampleSize=2，
        // 那么就会将bitmap处理为，width=50，height=50，宽高降为1 / 2，像素数降为1 / 4。
        op.inJustDecodeBounds=false;
        mFlakeBitmap=BitmapFactory.decodeResource(getResources(),mImgId,op);//
        return rawWidth;
    }


    public void setImageResource( int imgId){
        this.mImgId = imgId;
        initScale(mScale);
    }

    public void setScale(int scale){
        initScale(scale);
    }

    public void setDensity(int density){
        this.mFlakesDensity = density;
        initDenstity(mWidth,mHeight,mRawWidth);
    }

    public void setDelay(int delay){
        this.mDelay = delay;
    }
}
