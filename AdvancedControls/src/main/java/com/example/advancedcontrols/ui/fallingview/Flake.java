package com.example.advancedcontrols.ui.fallingview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * @author 李阳
 * @创建时间 2017/5/3 - 下午7:59
 * @描述
 * @ 当前版本:
 */

public class Flake {

    private static Point mPoint;
    private static float mAngle;
    private static float mIncrement;
    private static float mFlakeSize;
    private Paint mPaint;

    private static Random mRandom=new Random();

    private static final float ANGE_RANGE = 0.1f;
    private static final float HALF_ANGLE_RANGE = ANGE_RANGE / 2f;
    private static final float HALF_PI = (float) Math.PI / 2f;
    private static final float ANGLE_SEED = 25f;
    private static final float ANGLE_DIVISOR = 10000f;
    private static final float INCREMENT_LOWER = 2f;
    private static final float INCREMENT_UPPER = 4f;

    /**
     * @描述
     * @Param
     * @Return 
     */
    public Flake(Point position,float angle, float increment, int flakeSize, Paint paint) {
        this.mPoint=position;
        this.mAngle=angle;
        this.mIncrement=increment;
        this.mFlakeSize=flakeSize;
        this.mPaint=paint;
    }

    public static Flake creat(int width,int height,Paint paint,int flakeSize){
        int x = mRandom.getRandom(width);//0--width随机数
        int y = mRandom.getRandom(height);
        Point positon = new Point(x,y);

        float angle = mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
        float increment = mRandom.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        return new Flake(positon,angle,increment,flakeSize,paint);
    }

    public void  draw(Canvas canvas, Bitmap flakeBitmap){
        int width=canvas.getWidth();
        int heght=canvas.getWidth();
        Falling(width,heght);
    }

    public static void Falling(int width, int height) {
        double x = mPoint.x + (mIncrement * Math.cos(mAngle));
        double y = mPoint.y + (mIncrement * Math.sin(mAngle));
        mAngle += mRandom.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR;
        mPoint.set((int) x, (int) y);

        if (!isInside(width, height)) {
            reset(width);
        }
    }

    private static boolean isInside(int width, int height) {
        int x = mPoint.x;
        int y = mPoint.y;
        return x >= -mFlakeSize - 1 && x + mFlakeSize <= width && y >= -mFlakeSize - 1 && y - mFlakeSize < height;
    }

    private static void reset(int width) {
        mPoint.x = mRandom.getRandom(width);
        mPoint.y = (int) (-mFlakeSize - 1);
        mAngle = mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
    }
}
