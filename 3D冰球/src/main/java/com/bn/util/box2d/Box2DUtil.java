package com.bn.util.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
//����������״�Ĺ�����
public class Box2DUtil 
{
	//������������(��ɫ)
	public static Body createBox
	(
		float x,//x����
		float y,//y����
		World world,//����
	    float halfWidth,//���
	    float halfHeight,//���
        boolean isStatic,//�Ƿ�Ϊ��ֹ��
        int index
    ){
		BodyDef bd=new BodyDef();//������������
		if(isStatic)//�ж��Ƿ�Ϊ���˶�����
		{
			bd.type=BodyType.STATIC;
		}
		else
		{
			bd.type=BodyType.DYNAMIC;
		}
		bd.position.set(x, y);//����λ��
		Body bodyTemp= world.createBody(bd);//�������д������� 
		bodyTemp.setUserData(index);//��������
		PolygonShape ps=new PolygonShape();//����������״
		ps.setAsBox(halfWidth, halfHeight);//�趨�߿�
		FixtureDef fd=new FixtureDef();//����������������
		fd.density = 1.0f;//�����ܶ� 
		fd.friction = 0.05f;//����Ħ��ϵ��
		fd.restitution = 0f;//���ûָ�ϵ��
		fd.shape=ps;//������״
		if(!isStatic)//���������������������
		{
			bodyTemp.createFixture(fd);
		}
		else
		{
			bodyTemp.createFixture(ps, 0);
		}
		return bodyTemp;
	}
	//����Բ�Σ���ɫ��
	public static Body createCircle
	(
			float x,//x����
			float y,//y����
			float radius,//�뾶
			World world,//����
			float density,//�ܶ�
			float friction,//Ħ��ϵ��
			float restitution,//�ָ�ϵ��
			int index
	)
	{
		//������������
		BodyDef bd=new BodyDef();
		//�����Ƿ�Ϊ���˶�����
		bd.type=BodyType.DYNAMIC;
		//����λ��
		bd.position.set(x,y);
		//�������д�������
		Body bodyTemp=null;
		while(bodyTemp==null)
		{
			bodyTemp= world.createBody(bd);
		}
		bodyTemp.setUserData(index);//��������
		//����������״
		CircleShape cs=new CircleShape();
		cs.m_radius=radius;
		//����������������
		FixtureDef fd=new  FixtureDef();
		//�����ܶ�
		fd.density = density;
		//����Ħ��ϵ��
		fd.friction = friction;
		//����������ʧ�ʣ�������
		fd.restitution = restitution;
		//������״
		fd.shape=cs;
		//���������������������
		bodyTemp.createFixture(fd);
		//����BNObject�����
		return bodyTemp;
	}
	
	//����ֱ��
	public static void createEdge
	(
			float[] data,
			World world
	)
	{
		//������������
		BodyDef bd=new BodyDef();
		//�����Ƿ�Ϊ���˶�����
		bd.type=BodyType.STATIC;
		
		float positionX=(data[0]+data[2])/2;
		float positionY=(data[1]+data[3])/2;
		//����λ��						
		bd.position.set(positionX,positionY);
		//�������д�������
		Body bodyTemp = null;
		
		while(bodyTemp==null)
		{
			bodyTemp = world.createBody(bd);
		}
		//����������״
		EdgeShape ps=new EdgeShape();
		ps.set(new Vec2((data[0]-positionX),(data[1]-positionY)), new Vec2((data[2]-positionX),(data[3]-positionY)));
		//����������������
		FixtureDef fd=new FixtureDef();
		//�����ܶ�
		fd.density = 8.0f; 
		//����Ħ��ϵ��
		fd.friction = 0.0f;   
		//����������ʧ�ʣ�������
		fd.restitution = 1.0f;
		//������״
		fd.shape=ps;
		//���������������������
		bodyTemp.createFixture(ps, 0);//�����ܶ�Ϊ0��PolygonShape����
	}
}
