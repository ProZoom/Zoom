package com.bn.util.action;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class MyAction
{
	public String id;//�ؽ�id 
	public boolean collideConnected;//�Ƿ���������������ײ
	public Body poA;//���������A
	public Body poB;//���������B
	public Vec2 target;//���������Ŀ���
	public float maxForce;//Լ������ʩ�Ӹ��ƶ���ѡ��������
	public float frequencyHz;//�������Ӧ���ٶ�
	public float dampingRatio;//����ϵ��
	
	public float targetX;//Ŀ���X
	public float targetY;//Ŀ���Y
	public int index=-1;//����ֵ Ĭ��Ϊ-1
	public boolean computer_win=false;//����Ӯ 
}
