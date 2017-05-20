package com.bn.util.snow;

import android.opengl.GLES20;

public class ParticleConstant {
	public static int CURR_INDEX=0;
	//��ʼ��ɫ
	public static final float[][] START_COLOR=
		{
		{0.9f,0.9f,0.9f,1.0f},
		{0.9f,0.9f,0.9f,1.0f},//===���ư�ɫ ==
		{0.9f,0.9f,0.9f,1.0f},
		
		{0.22f,0.47f,1f,1.0f},
		{0.22f,0.47f,1f,1.0f},//===����ɫ==
		{0.22f,0.47f,1f,1.0f},
		
		
		{0.92f,0.14f,0.18f,1.0f},
		{0.92f,0.14f,0.18f,1.0f},//===��ɫ==
		{0.92f,0.14f,0.18f,1.0f},
		
		{0.69f,0.024f,0.73f,1.0f},
		{0.69f,0.024f,0.73f,1.0f},//===��ɫ==
		{0.69f,0.024f,0.73f,1.0f},
		
		{0.01f,0.71f,0,1.0f},
		{0.01f,0.71f,0,1.0f},//===��ɫ==
		{0.01f,0.71f,0,1.0f},
		
		{0.1f,0.95f,0.92f,1f},
		{0.1f,0.95f,0.92f,1f},//===ǳ��ɫ==
		{0.1f,0.95f,0.92f,1f},
		
		{0.97f,0.024f,0.91f,1f},
		{0.97f,0.024f,0.91f,1f},//===ǳ��ɫ==
		{0.97f,0.024f,0.91f,1f}
		};
	//��ֹ��ɫ
	public static final float[][] END_COLOR=
		{
		{1.0f,1.0f,1.0f,0.0f},
		{1.0f,1.0f,1.0f,0.0f},
		{1.0f,1.0f,1.0f,0.0f}
		};
	
	//Դ�������
	public static final int[] SRC_BLEND=
		{
		GLES20.GL_SRC_ALPHA,
		GLES20.GL_SRC_ALPHA,
		GLES20.GL_SRC_ALPHA
		};
	//Ŀ��������
	public static final int[] DST_BLEND=
		{
		GLES20.GL_ONE_MINUS_SRC_ALPHA,
		GLES20.GL_ONE_MINUS_SRC_ALPHA,
		GLES20.GL_ONE_MINUS_SRC_ALPHA
		};
	//��Ϸ�ʽ
	public static final int[] BLEND_FUNC=
		{
		GLES20.GL_FUNC_ADD,
		GLES20.GL_FUNC_ADD,
		GLES20.GL_FUNC_ADD
		};
	//�������Ӱ뾶
	public static final float[] RADIS=
		{
		0.22f,
		0.18f,
		0.15f
		};
	
	//�������������
	public static final float[] MAX_LIFE_SPAN=
		{
		4f,
		3.5f,
		3.8f
		};
	
	//�����������ڲ���
	public static final float[] LIFE_SPAN_STEP=
		{
		0.1f,
		0.05f,
		0.08f
		};
	
	//���ӷ����X���ҷ�Χ
	public static final float[] X_RANGE=
		{
		1f,
		1.3f,
		0.7f
		};
	
	//ÿ���緢���������
	public static final int[] GROUP_COUNT=
		{
		7,
		5,
		8
		};
	
	//����Y�������ڵ��ٶ�
	public static final float[] VY=
		{
		-0.1f,
		-0.12f,
		-0.08f
		};
	//���Ӹ��������߳���Ϣʱ��
	public static final int[] THREAD_SLEEP=
		{
		15,
		15,
		15
		};
}
