package com.bn.util.action;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class MyAction
{
	public String id;//关节id 
	public boolean collideConnected;//是否允许两个刚体碰撞
	public Body poA;//物体类对象A
	public Body poB;//物体类对象B
	public Vec2 target;//刚体的世界目标点
	public float maxForce;//约束可以施加给移动候选体的最大力
	public float frequencyHz;//刚体的响应的速度
	public float dampingRatio;//阻尼系数
	
	public float targetX;//目标点X
	public float targetY;//目标点Y
	public int index=-1;//索引值 默认为-1
	public boolean computer_win=false;//电脑赢 
}
