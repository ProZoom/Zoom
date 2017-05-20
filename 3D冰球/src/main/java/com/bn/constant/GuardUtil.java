package com.bn.constant;

import com.bn.util.action.ActionMoveToTarget;
import com.bn.util.action.MyAction;
import com.bn.view.GameView;

public class GuardUtil //防守模式
{
	GameView gv;
	public GuardUtil(GameView gv)
	{
		this.gv=gv;
	}
	public void GuardMode()//防守模式
	{
		float currentX=gv.hongjdz.gt.getPosition().x;//当前红球位置
		float currentY=gv.hongjdz.gt.getPosition().y;
		float moveX=0;//需要移动的坐标点
		float moveY=0;
		float targetX;
		synchronized(gv.bt.ActionLockB)
		{
			targetX=gv.bt.ballP[1][0];
		}
		moveX=Math.abs(currentX-targetX)/10;
		moveY=Math.abs(currentY+7.5f)/10;
		if(currentX<targetX)//向右挪
		{
			if(currentX+moveX<targetX)
			{
				if(gv.hongjdz.gt!=null)
				{
					float mx=currentX+moveX;
					float my=currentY-moveY;
					mx=gv.cu.limitC(currentX+moveX,currentY-moveY,-6f,-0.75f)[0];
					my=gv.cu.limitC(currentX+moveX,currentY-moveY,-6f,-0.75f)[1];
					
					MyAction ma=new ActionMoveToTarget(mx,my,6);
					synchronized(gv.bt.ActionLock)
					{
						gv.bt.ActionMoveQueue.offer(ma);
					}
				}
			}
		}else//向左挪
		{
			if(currentX-moveX>targetX)
			{
				if(gv.hongjdz.gt!=null)
				{
					float mx=currentX-moveX;
					float my=currentY-moveY;
					mx=gv.cu.limitC(currentX-moveX,currentY-moveY,-6f,-0.75f)[0];
					my=gv.cu.limitC(currentX-moveX,currentY-moveY,-6f,-0.75f)[1];
					
					MyAction ma=new ActionMoveToTarget(mx,my,6);
					synchronized(gv.bt.ActionLock)
					{
						gv.bt.ActionMoveQueue.offer(ma);
					}
				}
			}
		}
	}
}
