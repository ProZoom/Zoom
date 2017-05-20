package com.bn.util.box2d;

import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
//生成物理形状的工具类
public class Box2DUtil 
{
	//创建矩形物体(颜色)
	public static Body createBox
	(
		float x,//x坐标
		float y,//y坐标
		World world,//世界
	    float halfWidth,//半宽
	    float halfHeight,//半高
        boolean isStatic,//是否为静止的
        int index
    ){
		BodyDef bd=new BodyDef();//创建刚体描述
		if(isStatic)//判断是否为可运动刚体
		{
			bd.type=BodyType.STATIC;
		}
		else
		{
			bd.type=BodyType.DYNAMIC;
		}
		bd.position.set(x, y);//设置位置
		Body bodyTemp= world.createBody(bd);//在世界中创建刚体 
		bodyTemp.setUserData(index);//设置数据
		PolygonShape ps=new PolygonShape();//创建刚体形状
		ps.setAsBox(halfWidth, halfHeight);//设定边框
		FixtureDef fd=new FixtureDef();//创建刚体物理描述
		fd.density = 1.0f;//设置密度 
		fd.friction = 0.05f;//设置摩擦系数
		fd.restitution = 0f;//设置恢复系数
		fd.shape=ps;//设置形状
		if(!isStatic)//将刚体物理描述与刚体结合
		{
			bodyTemp.createFixture(fd);
		}
		else
		{
			bodyTemp.createFixture(ps, 0);
		}
		return bodyTemp;
	}
	//创建圆形（颜色）
	public static Body createCircle
	(
			float x,//x坐标
			float y,//y坐标
			float radius,//半径
			World world,//世界
			float density,//密度
			float friction,//摩擦系数
			float restitution,//恢复系数
			int index
	)
	{
		//创建刚体描述
		BodyDef bd=new BodyDef();
		//设置是否为可运动刚体
		bd.type=BodyType.DYNAMIC;
		//设置位置
		bd.position.set(x,y);
		//在世界中创建刚体
		Body bodyTemp=null;
		while(bodyTemp==null)
		{
			bodyTemp= world.createBody(bd);
		}
		bodyTemp.setUserData(index);//设置数据
		//创建刚体形状
		CircleShape cs=new CircleShape();
		cs.m_radius=radius;
		//创建刚体物理描述
		FixtureDef fd=new  FixtureDef();
		//设置密度
		fd.density = density;
		//设置摩擦系数
		fd.friction = friction;
		//设置能量损失率（反弹）
		fd.restitution = restitution;
		//设置形状
		fd.shape=cs;
		//将刚体物理描述与刚体结合
		bodyTemp.createFixture(fd);
		//返回BNObject类对象
		return bodyTemp;
	}
	
	//创建直线
	public static void createEdge
	(
			float[] data,
			World world
	)
	{
		//创建刚体描述
		BodyDef bd=new BodyDef();
		//设置是否为可运动刚体
		bd.type=BodyType.STATIC;
		
		float positionX=(data[0]+data[2])/2;
		float positionY=(data[1]+data[3])/2;
		//设置位置						
		bd.position.set(positionX,positionY);
		//在世界中创建刚体
		Body bodyTemp = null;
		
		while(bodyTemp==null)
		{
			bodyTemp = world.createBody(bd);
		}
		//创建刚体形状
		EdgeShape ps=new EdgeShape();
		ps.set(new Vec2((data[0]-positionX),(data[1]-positionY)), new Vec2((data[2]-positionX),(data[3]-positionY)));
		//创建刚体物理描述
		FixtureDef fd=new FixtureDef();
		//设置密度
		fd.density = 8.0f; 
		//设置摩擦系数
		fd.friction = 0.0f;   
		//设置能量损失率（反弹）
		fd.restitution = 1.0f;
		//设置形状
		fd.shape=ps;
		//将刚体物理描述与刚体结合
		bodyTemp.createFixture(ps, 0);//创建密度为0的PolygonShape对象
	}
}
