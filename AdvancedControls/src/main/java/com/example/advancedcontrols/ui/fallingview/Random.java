package com.example.advancedcontrols.ui.fallingview;

/**
 * @author 李阳
 * @创建时间 2017/5/3 - 下午5:40
 * @描述
 * @ 当前版本:
 */

public class Random {

    private static final java.util.Random RANDOM = new java.util.Random();

    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;      //nextFloat()--->[0.0，1.0）
    }

    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}
