package com.bn.util.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

public class MyMouseJoint //鼠标关节类
{
	public MouseJoint mJoint;//声明鼠标关节对象
	public World mWorld;//声明物理世界类对象
	public MyMouseJoint
	(
		String id,//关节id 
		World world,//物理世界对象
		boolean collideConnected,//是否允许两个刚体碰撞
		Body poA,//指向物体类对象A
		Body poB,//指向物体类对象B
		Vec2 target,//刚体的世界目标点
		float maxForce,//约束可以施加给移动候选体的最大力
		float frequencyHz,//刚体的响应的速度
		float dampingRatio//阻尼系数
	)
	{
		this.mWorld=world;//给物理世界类对象赋值
		MouseJointDef mjd = new MouseJointDef();//创建鼠标关节描述
		mjd.userData=id;//设置用户数据
		mjd.collideConnected=collideConnected;//给是否允许碰撞标志赋值
		mjd.bodyA=poA;//设置关节关联的刚体bodyA
		mjd.bodyB=poB;//设置关节关联的刚体bodyB
		mjd.target=target;//设置刚体世界目标点
		mjd.maxForce=maxForce;//设置拖动刚体时允许的最大力
		mjd.frequencyHz=frequencyHz;//设置刚体的响应速度
		mjd.dampingRatio=dampingRatio;//设置阻尼系数
		mJoint=(MouseJoint)world.createJoint(mjd);//物理世界里增添这个关节
	}
}
