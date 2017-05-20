package com.bn.util.action;

import org.jbox2d.common.Vec2;

public class ActionMove extends MyAction
{
	public ActionMove(Vec2 target,int index)
	{
		this.target=target;
		this.index=index;
	}
}
