package com.bn.util.thread;

import java.util.LinkedList;
import java.util.Queue;

import org.jbox2d.common.Vec2;

import com.bn.constant.AttackUtil;
import com.bn.constant.GuardUtil;
import com.bn.util.action.MyAction;
import com.bn.view.GameView;
import com.bn.view.OptionView;

public class BallGoThread extends Thread
{
	GameView gv;
	
	public int guardCount=1;//记录球位置的计数器
	int CCount=0;
	boolean go=false;//红球先发球
	
	public AttackUtil au;
	GuardUtil gu;
	
	public Queue<MyAction> ActionMoveQueue=new LinkedList<MyAction>();//动作对列
	public Object ActionLock=new Object();
	
	public Queue<float[][]> GetPositionQueue=new LinkedList<float[][]>();//红球动作对列
	public Object ActionLockA=new Object();
	
	public Queue<float[]> GetBallPositionQueue=new LinkedList<float[]>();//黄球动作对列
	public Object ActionLockB=new Object();
	boolean flag=true;//标志位
	public boolean ballGO=false;
	public float[][] ballP=new float[2][2];//存放黄球的位置
	int sleepTime=10;
	public BallGoThread(GameView gv)
	{
		this.gv=gv;
		au=new AttackUtil(gv);
		gu=new GuardUtil(gv);
	}
	public void run()
	{
		while(flag)
		{
			while(ballGO)
			{
				Vec2 ballPosition=new Vec2();//黄球的位置
				synchronized(ActionLockB)
				{
					while(GetBallPositionQueue.size()>1)
					{
						ballP[0]=GetBallPositionQueue.poll();
						ballP[1]=GetBallPositionQueue.poll();
					}
				}
				ballPosition.x=ballP[0][0];
				ballPosition.y=ballP[0][1];
				
				ChangeMode(ballPosition,-1.5f);//切换模式
				
				//进攻 防守阶段=======================end================================
				if(gv.CWin)//前一局电脑胜利时，当前局系统先开始游戏
				{
					hongFirst();
				}
				if(go&&au.judgeIfTouch(gv.hongjdz.gt.getPosition()))//如果游戏胜利 并且红球与球碰撞时
				{
					if(gv.hongjdz.gt.getPosition().x<0)
					{
						gv.ball.gt.applyLinearImpulse(new Vec2(0.2f,0.4f),ballPosition,true);//给予一定的冲量
					}else if(gv.hongjdz.gt.getPosition().x>0)
					{
						gv.ball.gt.applyLinearImpulse(new Vec2(-0.2f,0.4f),ballPosition,true);
					}else if(gv.hongjdz.gt.getPosition().x==0)
					{
						gv.ball.gt.applyLinearImpulse(new Vec2(0,0.4f),ballPosition,true);
					}
					go=false;
				}
				if(OptionView.difficultyIndex==0)//简单
				{
					sleepTime=12;
				}else if(OptionView.difficultyIndex==1)//一般
				{
					sleepTime=11;
				}else if(OptionView.difficultyIndex==2)//复杂
				{
					sleepTime=10;
				}
				try
				{
					Thread.sleep(sleepTime);//休眠10毫秒
				} catch (InterruptedException e)
				{
					e.printStackTrace();//打印异常信息
				}
			}
			try
			{
				Thread.sleep(1000);//休眠10毫秒
			} catch (InterruptedException e)
			{
				e.printStackTrace();//打印异常信息
			}
		}
	}
	public void ChangeMode(Vec2 ballPosition,float distance)
	{
		if(ballPosition.y<=distance)//如果球的z坐标小于-1.5
		{
			au.AttackMode();//进攻模式
		}else
		{
			gu.GuardMode();//防守模式
			au.attackCount=1;//攻击模式从头开始
			au.allowRecordC=true;//重新记录坐标
		}
	}
	public void hongFirst()//红球先开始
	{
		while(gv.CWin&&gv.Start_Game)
		{
			CCount++;
			if(CCount>160)
			{
				au.allowRecordC=true;//允许记录坐标
				au.attackCount=1;//计数器
				go=true;
				au.AttackMode();//进攻模式
				gv.CWin=false;
				CCount=0;
			}else if(CCount>80&&CCount<150)
			{
				gv.isMove=true;
			}
		}
	}
}
