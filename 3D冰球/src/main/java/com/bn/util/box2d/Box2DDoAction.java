package com.bn.util.box2d;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.bn.constant.VibratorUtil;
import com.bn.object.GameObject;
import com.bn.view.GameView;
import com.bn.view.MainView;

public class Box2DDoAction 
{
	public static void doAction(GameView gv,Body bodyA,Body bodyB,List<Body> boxBody,GameObject ball)
	{
		for(int i=0;i<boxBody.size();i++)//遍历两个胜利区域的刚体
		{
			if((bodyA.equals(ball.gt)||bodyB.equals(ball.gt))&&(bodyA.equals(boxBody.get(i))||bodyB.equals(boxBody.get(i))))//如果球与胜利区域发生碰撞
			{
				ball.gt.setLinearVelocity(new Vec2(0,0));//球的速度设为0
				if(i==0)
				{
					GameView.computer_win=true;//电脑赢
					if(MainView.isShock)
					{
						VibratorUtil.Vibrate(gv.mv.activity, 500);//震动500ms  
					}
					gv.cu.doCrashAction(1,"jq.ogg");
				}else if(i==1)
				{
					GameView.player_win=true;//玩家赢
					if(MainView.isShock)
					{
						VibratorUtil.Vibrate(gv.mv.activity, 500);//震动500ms 
					}
					gv.cu.doCrashAction(2,"jq.ogg");
				}
			}
		}
	}
	public static void doCrashAction(Body bodyA,Body bodyB,List<Body> boxBody,GameObject ball,GameView gv)
	{
		//进行相应动作
		for(int i=0;i<boxBody.size();i++)//遍历包围框刚体
		{
			if((bodyA.equals(ball.gt)||bodyB.equals(ball.gt))&&(bodyA.equals(boxBody.get(i))||bodyB.equals(boxBody.get(i))))//如果球与胜利区域发生碰撞
			{
				if(i==0||i==1||i==2||i==3)
				{
					GameView.moveY=true;
				}
				GameView.drawRound=true;//绘制光圈的标志位设为true
				GameView.drawRoundOK=true;
				GameView.drawLightning=true;
				gv.cu.doCrashAction(0, "puckWallSound.mp3");
			}
		}
	}
}
