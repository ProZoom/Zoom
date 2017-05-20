package com.bn.util.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

public class MyMouseJoint //���ؽ���
{
	public MouseJoint mJoint;//�������ؽڶ���
	public World mWorld;//�����������������
	public MyMouseJoint
	(
		String id,//�ؽ�id 
		World world,//�����������
		boolean collideConnected,//�Ƿ���������������ײ
		Body poA,//ָ�����������A
		Body poB,//ָ�����������B
		Vec2 target,//���������Ŀ���
		float maxForce,//Լ������ʩ�Ӹ��ƶ���ѡ��������
		float frequencyHz,//�������Ӧ���ٶ�
		float dampingRatio//����ϵ��
	)
	{
		this.mWorld=world;//���������������ֵ
		MouseJointDef mjd = new MouseJointDef();//�������ؽ�����
		mjd.userData=id;//�����û�����
		mjd.collideConnected=collideConnected;//���Ƿ�������ײ��־��ֵ
		mjd.bodyA=poA;//���ùؽڹ����ĸ���bodyA
		mjd.bodyB=poB;//���ùؽڹ����ĸ���bodyB
		mjd.target=target;//���ø�������Ŀ���
		mjd.maxForce=maxForce;//�����϶�����ʱ����������
		mjd.frequencyHz=frequencyHz;//���ø������Ӧ�ٶ�
		mjd.dampingRatio=dampingRatio;//��������ϵ��
		mJoint=(MouseJoint)world.createJoint(mjd);//������������������ؽ�
	}
}
