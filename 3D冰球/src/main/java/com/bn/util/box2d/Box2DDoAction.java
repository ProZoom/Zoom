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
		for(int i=0;i<boxBody.size();i++)//��������ʤ������ĸ���
		{
			if((bodyA.equals(ball.gt)||bodyB.equals(ball.gt))&&(bodyA.equals(boxBody.get(i))||bodyB.equals(boxBody.get(i))))//�������ʤ����������ײ
			{
				ball.gt.setLinearVelocity(new Vec2(0,0));//����ٶ���Ϊ0
				if(i==0)
				{
					GameView.computer_win=true;//����Ӯ
					if(MainView.isShock)
					{
						VibratorUtil.Vibrate(gv.mv.activity, 500);//��500ms  
					}
					gv.cu.doCrashAction(1,"jq.ogg");
				}else if(i==1)
				{
					GameView.player_win=true;//���Ӯ
					if(MainView.isShock)
					{
						VibratorUtil.Vibrate(gv.mv.activity, 500);//��500ms 
					}
					gv.cu.doCrashAction(2,"jq.ogg");
				}
			}
		}
	}
	public static void doCrashAction(Body bodyA,Body bodyB,List<Body> boxBody,GameObject ball,GameView gv)
	{
		//������Ӧ����
		for(int i=0;i<boxBody.size();i++)//������Χ�����
		{
			if((bodyA.equals(ball.gt)||bodyB.equals(ball.gt))&&(bodyA.equals(boxBody.get(i))||bodyB.equals(boxBody.get(i))))//�������ʤ����������ײ
			{
				if(i==0||i==1||i==2||i==3)
				{
					GameView.moveY=true;
				}
				GameView.drawRound=true;//���ƹ�Ȧ�ı�־λ��Ϊtrue
				GameView.drawRoundOK=true;
				GameView.drawLightning=true;
				gv.cu.doCrashAction(0, "puckWallSound.mp3");
			}
		}
	}
}
