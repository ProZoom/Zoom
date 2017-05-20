package com.bn.util.snow;

import android.opengl.GLES20;

public class ParticleConstant {
	public static int CURR_INDEX=0;
	//起始颜色
	public static final float[][] START_COLOR=
		{
		{0.9f,0.9f,0.9f,1.0f},
		{0.9f,0.9f,0.9f,1.0f},//===淡黄白色 ==
		{0.9f,0.9f,0.9f,1.0f},
		
		{0.22f,0.47f,1f,1.0f},
		{0.22f,0.47f,1f,1.0f},//===深蓝色==
		{0.22f,0.47f,1f,1.0f},
		
		
		{0.92f,0.14f,0.18f,1.0f},
		{0.92f,0.14f,0.18f,1.0f},//===红色==
		{0.92f,0.14f,0.18f,1.0f},
		
		{0.69f,0.024f,0.73f,1.0f},
		{0.69f,0.024f,0.73f,1.0f},//===紫色==
		{0.69f,0.024f,0.73f,1.0f},
		
		{0.01f,0.71f,0,1.0f},
		{0.01f,0.71f,0,1.0f},//===绿色==
		{0.01f,0.71f,0,1.0f},
		
		{0.1f,0.95f,0.92f,1f},
		{0.1f,0.95f,0.92f,1f},//===浅蓝色==
		{0.1f,0.95f,0.92f,1f},
		
		{0.97f,0.024f,0.91f,1f},
		{0.97f,0.024f,0.91f,1f},//===浅红色==
		{0.97f,0.024f,0.91f,1f}
		};
	//终止颜色
	public static final float[][] END_COLOR=
		{
		{1.0f,1.0f,1.0f,0.0f},
		{1.0f,1.0f,1.0f,0.0f},
		{1.0f,1.0f,1.0f,0.0f}
		};
	
	//源混合因子
	public static final int[] SRC_BLEND=
		{
		GLES20.GL_SRC_ALPHA,
		GLES20.GL_SRC_ALPHA,
		GLES20.GL_SRC_ALPHA
		};
	//目标混合因子
	public static final int[] DST_BLEND=
		{
		GLES20.GL_ONE_MINUS_SRC_ALPHA,
		GLES20.GL_ONE_MINUS_SRC_ALPHA,
		GLES20.GL_ONE_MINUS_SRC_ALPHA
		};
	//混合方式
	public static final int[] BLEND_FUNC=
		{
		GLES20.GL_FUNC_ADD,
		GLES20.GL_FUNC_ADD,
		GLES20.GL_FUNC_ADD
		};
	//单个粒子半径
	public static final float[] RADIS=
		{
		0.22f,
		0.18f,
		0.15f
		};
	
	//粒子最大生命期
	public static final float[] MAX_LIFE_SPAN=
		{
		4f,
		3.5f,
		3.8f
		};
	
	//粒子生命周期步进
	public static final float[] LIFE_SPAN_STEP=
		{
		0.1f,
		0.05f,
		0.08f
		};
	
	//粒子发射的X左右范围
	public static final float[] X_RANGE=
		{
		1f,
		1.3f,
		0.7f
		};
	
	//每次喷发发射的数量
	public static final int[] GROUP_COUNT=
		{
		7,
		5,
		8
		};
	
	//粒子Y方向升腾的速度
	public static final float[] VY=
		{
		-0.1f,
		-0.12f,
		-0.08f
		};
	//粒子更新物理线程休息时间
	public static final int[] THREAD_SLEEP=
		{
		15,
		15,
		15
		};
}
