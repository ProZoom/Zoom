package com.bn.util.thread;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import com.bn.util.action.MyAction;
import com.bn.util.box2d.MyMouseJoint;
import com.bn.view.GameView;
import com.bn.view.OptionView;
import static com.bn.constant.Constant.*;

public class PhysicalThread extends Thread
{
	GameView gv;
	boolean flag=true;
	boolean isWorldStep=true;
	int count=0;
	long timeStart=System.nanoTime();//开始时间
	long lastTimeStamp=System.nanoTime();//起始时间
	final long spanMin=(long)((1/60.0)*1000*1000*1000);//最小速率
	
	MyMouseJoint mj=null;//创建鼠标关节
	public PhysicalThread(GameView gv)
	{
		this.gv=gv;
	}
	@Override
	public void run()
	{
		while(flag)
		{
			if(GameView.player_win||GameView.computer_win||gv.GameOver||gv.isPause||gv.switchView)
			{
				isWorldStep=false;
			}else
			{
				isWorldStep=true;
			}
			if(isWorldStep)//允许物理模拟
			{
				long currTimeStamp=System.nanoTime();//当前时间
				if((currTimeStamp-lastTimeStamp)<spanMin)//判断速率是否过快
				{
					try 
					{
						Thread.sleep(10);//休眠5毫秒
					} catch (InterruptedException e)
					{
						e.printStackTrace();//打印异常信息
					}
					continue;//继续循环
				}
				lastTimeStamp=currTimeStamp;//将当前时间赋值给起始时间
				
				gv.world.step(TIME_STEP, ITERA, ITERA);//物理模拟
				
				setLanBall();//对蓝色球进行计算
				setHongBall();
				updatePosition();
				Vec2 ballSpeed=gv.ball.gt.getLinearVelocity();
				float maxSpeed=20f;
				if(OptionView.difficultyIndex==0)
				{
					maxSpeed=20f;
				}else if(OptionView.difficultyIndex==1)
				{
					maxSpeed=25f;
				}else if(OptionView.difficultyIndex==2)
				{
					maxSpeed=35f;
				}
				limitBallSpeed(ballSpeed,maxSpeed);//限制黄球速度
				gv.lanjdz.gt.setLinearVelocity(new Vec2(0,0));
				gv.hongjdz.gt.setLinearVelocity(new Vec2(0,0));
			}
		}
	}
	public void limitBallSpeed(Vec2 ballSpeed,float maxSpeed)
	{
		//限制冰球速度
		if(Math.abs(ballSpeed.x)>=maxSpeed&&Math.abs(ballSpeed.y)>=maxSpeed)
		{
			if(ballSpeed.x<=0&&ballSpeed.y<=0)
			{
				gv.ball.gt.setLinearVelocity(new Vec2(-maxSpeed,-maxSpeed));
			}else if(ballSpeed.x>=0&&ballSpeed.y>=0)
			{
				gv.ball.gt.setLinearVelocity(new Vec2(maxSpeed,maxSpeed));
			}else if(ballSpeed.x<=0&&ballSpeed.y>=0)
			{
				gv.ball.gt.setLinearVelocity(new Vec2(-maxSpeed,maxSpeed));
			}else if(ballSpeed.x>=0&&ballSpeed.y<=0)
			{
				gv.ball.gt.setLinearVelocity(new Vec2(maxSpeed,-maxSpeed));
			}
		}else if(Math.abs(ballSpeed.x)>=maxSpeed&&Math.abs(ballSpeed.y)<=maxSpeed)
		{
			if(ballSpeed.x>0)
			{
				gv.ball.gt.setLinearVelocity(new Vec2(maxSpeed,ballSpeed.y));
			}else
			{
				gv.ball.gt.setLinearVelocity(new Vec2(-maxSpeed,ballSpeed.y));
			}
		}else if(Math.abs(ballSpeed.x)<=maxSpeed&&Math.abs(ballSpeed.y)>=maxSpeed)
		{
			if(ballSpeed.y>0)
			{
				gv.ball.gt.setLinearVelocity(new Vec2(ballSpeed.x,maxSpeed));
			}else
			{
				gv.ball.gt.setLinearVelocity(new Vec2(ballSpeed.x,-maxSpeed));
			}
		}
	}
	
	public void setHongBall()
	{
		ArrayList<MyAction> ac=new ArrayList<MyAction>();
		MyAction ma=null;
		
		if(GameView.player_win||GameView.computer_win||gv.GameOver)
		{
			gv.bt.ActionMoveQueue.clear();
			gv.bt.GetPositionQueue.clear();
			gv.bt.GetBallPositionQueue.clear();
		} 
		synchronized(gv.bt.ActionLock)
		{
			while(gv.bt.ActionMoveQueue.size()>0)
			{
				ma=gv.bt.ActionMoveQueue.poll();
				ac.add(ma);
			}
		}
		if(ac.size()>0)
		{
			for(MyAction mc:ac)
			{
				if(mc.index==6)
				{
					Vec2 target=transformPosition(new Vec2(mc.targetX,mc.targetY),gv.ball.gt.getPosition());
					target.x=gv.cu.limitC(target.x,target.y,-7.5f,-0.75f)[0];
					target.y=gv.cu.limitC(target.x,target.y,-7.5f,-0.75f)[1];
					gv.hongjdz.gt.setTransform(target,0);
				}
			}
		}
		Vec2 target=transformPosition(gv.hongjdz.gt.getPosition(),gv.ball.gt.getPosition());
		target.x=gv.cu.limitC(target.x,target.y,-7.5f,-0.75f)[0];
		target.y=gv.cu.limitC(target.x,target.y,-7.5f,-0.75f)[1];
		gv.hongjdz.gt.setTransform(target,0);
	}
	
	
	public void setLanBall()//设置玩家控制的球
	{
		ArrayList<MyAction> ac=new ArrayList<MyAction>();
		MyAction ma=null;
		if(gv.GameOver)
		{
			gv.doActionQueue.clear();
		}
		synchronized(gv.lock)
		{
			while(gv.doActionQueue.size()>0)
			{
				ma=gv.doActionQueue.poll();
				ac.add(ma);
			}
		}
		if(ac.size()>0)
		{
			for(MyAction mc:ac)
			{
				if(mc.index==0)//如果索引值为0
				{
					mj=new MyMouseJoint(mc.id,gv.world,mc.collideConnected,mc.poA,mc.poB,
							mc.target,mc.maxForce,mc.frequencyHz,mc.dampingRatio);//创建鼠标关节
				}else if(mc.index==1)//如果索引值为1
				{
					if(mj!=null)
					{
						if(mj.mJoint !=null)
						{
							Vec2 target=transformPosition(mc.target,gv.ball.gt.getPosition());
							target.x=gv.cu.limitC(target.x,target.y,0.75f,8f)[0];
							target.y=gv.cu.limitC(target.x,target.y,0.75f,8f)[1];
							mj.mJoint.setTarget(target);//设置鼠标关节的世界目标点
						}
					}
				}else if(mc.index==2)//如果索引值为2
				{
					if(mj!=null)//判断鼠标关节是否为空
					{
						if(mj.mJoint!=null)
						{
							gv.world.destroyJoint(mj.mJoint);//删除鼠标关节对象
							mj.mJoint=null;
						}
						mj=null;//给鼠标关节赋值为NULL
					}
				}else if(mc.index==3)//游戏胜利
				{
					if(mc.computer_win)//电脑赢
					{
						setTransform(-1.5f);
					}else//玩家赢
					{
						setTransform(1.5f);
					}
				}else if(mc.index==4)//重新开始游戏
				{
					setTransform(0);//初始化球的位置
				}
				else if(mc.index==5)//重新进入游戏界面
				{
					setTransform(1.5f);//初始化球的位置
					destroyBody();
				}
			}
		}
		Vec2 target=transformPosition(gv.lanjdz.gt.getPosition(),gv.ball.gt.getPosition());
		target.x=gv.cu.limitC(target.x,target.y,0.75f,8f)[0];
		target.y=gv.cu.limitC(target.x,target.y,0.75f,8f)[1];
		gv.lanjdz.gt.setTransform(target,0);
	}
	public void updatePosition()//更新数据
	{
		float[][] position=new float[3][2];
		Vec2 lanPosition=gv.lanjdz.gt.getPosition();
		Vec2 hongPosition=gv.hongjdz.gt.getPosition();
		Vec2 ballPosition=gv.ball.gt.getPosition();
		position[0][0]=lanPosition.x;
		position[0][1]=lanPosition.y;
		position[1][0]=hongPosition.x;
		position[1][1]=hongPosition.y;
		position[2][0]=ballPosition.x;
		position[2][1]=ballPosition.y;
		//获得黄球的位置
		synchronized(gv.bt.ActionLockB)
		{
			gv.bt.GetBallPositionQueue.offer(position[2]);
		}
		synchronized(gv.lockA)
		{
			gv.positionQueue.offer(position);//将蓝球和红球位置添加进对列中
		}
		float[][] result=new float[1][2];
		result[0][0]=position[1][0];
		result[0][1]=position[1][1];
		//获得红球的位置
		synchronized(gv.bt.ActionLockA)
		{
			gv.bt.GetPositionQueue.offer(result);
		}
	}
	public Vec2 transformPosition(Vec2 position,Vec2 ballPosition)//限制红球或者蓝球移动前的坐标
	{
		Vec2 result=new Vec2();
		//计算两球之间的距离
		float distance = (float) Math.sqrt(Math.abs((position.x - ballPosition.x)*(position.x - ballPosition.x)+
				(position.y - ballPosition.y)* (position.y - ballPosition.y)));
		if(distance>=1.54)//如果距离大于两个球的半径之和，则允许直接移动
		{
			result.x=position.x;
			result.y=position.y;
		}else //若小于半径之和
		{
			if(ballPosition.x-position.x!=0)//斜率存在
			{
				float k=(ballPosition.y-position.y)/(ballPosition.x-position.x);//计算斜率
				float b=ballPosition.y-k*ballPosition.x;
				float x1=0;
				if(position.x<ballPosition.x)//如果球在冰球的左边，则应该适当向左挪
				{
					x1=ballPosition.x-(float)Math.sqrt((1.54*1.54)/(1+k*k));
					float y1=x1*k+b;
					result.x=x1;
					result.y=y1;
				}else//如果球在冰球的右边，则应该适当向右挪
				{
					x1=ballPosition.x+(float)Math.sqrt((1.54*1.54)/(1+k*k));
					float y1=x1*k+b;
					result.x=x1;
					result.y=y1;
				}
			}else//斜率不存在
			{
				if(position.x<ballPosition.x)//如果球在冰球的左边，则应该适当向左挪
				{
					float x1=ballPosition.x-1.54f;
					float y1=position.y;
					result.x=x1;
					result.y=y1;
				}else//如果球在冰球的右边，则应该适当向右挪
				{
					float x1=ballPosition.x+1.54f;
					float y1=position.y;
					result.x=x1;
					result.y=y1;
				}
			}
		}
		return result;
	}
	public void setTransform(float sy)
	{//初始化球的位置
		gv.ball.gt.setTransform(new Vec2(0.0f, sy), 0);
		gv.hongjdz.gt.setTransform(new Vec2(0.0f, -6f),0);
		gv.lanjdz.gt.setTransform(new Vec2(0.0f, 7.0f), 0);
		gv.ball.gt.setLinearVelocity(new Vec2(0,0));//玩家控制的冰球速度永远为0
		gv.bt.ballGO=true;
	}
	public void destroyBody()//清除刚体
	{
		for(Body body:gv.body)
		{
			gv.world.destroyBody(body);
		}
		gv.body.clear();
	}
}
