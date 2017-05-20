package com.bn.util.action;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class ActionDown extends MyAction//动作为按下时...
{
	public ActionDown(String id,boolean collideConnected,Body poA,Body poB,
			Vec2 target,float maxForce,float frequencyHz,float dampingRatio,int index)
	{
		this.id=id;
		this.collideConnected=collideConnected;
		this.poA=poA;
		this.poB=poB;
		this.target=target;
		this.maxForce=maxForce;
		this.frequencyHz=frequencyHz;
		this.dampingRatio=dampingRatio;
		this.index=index;
	}
}
