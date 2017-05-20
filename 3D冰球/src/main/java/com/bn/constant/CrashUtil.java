package com.bn.constant;

import com.bn.view.GameView;
import com.bn.view.MainView;

import org.jbox2d.common.Vec2;

public class CrashUtil //碰撞工具类
{
	GameView gv;
	public CrashUtil(GameView gv)
	{
		this.gv=gv;
	}
	public boolean judgeIfTouch(Vec2 p1,Vec2 p2)//判断两个球是否相撞
	{
		float distance = (float) Math.sqrt(Math.abs((p1.x - p2.x)*(p1.x - p2.x)+(p1.y - p2.y)* (p1.y - p2.y)));
		if(distance<=1.55f)
		{
			return true;
		}else
		{
			return false;
		}
	}
	public void doCrashAction(int number,String musicName)//碰撞处理
	{
		gv.su.setColor(GameView.colorIndex[number]);//设置粒子颜色
		gv.su.ReSet(gv.index,gv.ball);//重置
		gv.isSnow=true;//播放雪花
		if(MainView.isSound)//允许播放音效
		{
			gv.mv.activity.sound.playSound(musicName, 0);
		}
	}
	public void lanApplyBall(Vec2 tempBall,Vec2 tempLan)//蓝球与冰球碰撞后 给予冰球一定冲量
	{
		float x1=0;
		float y1=0;
		if(tempBall.x!=tempLan.x)//存在斜率
		{
			float k=(tempBall.y-tempLan.y)/(tempBall.x-tempLan.x);//计算斜率
			if(tempBall.x<tempLan.x)//如果冰球在蓝球的左边
			{
				x1=-(float)Math.random();//随机给出x负方向冲量
			}else if(tempBall.x>tempLan.x)//如果冰球在蓝球的右边
			{
				x1=(float)Math.random();//随机给出x正方向冲量
			}else
			{
				x1=0;
			}
			y1=k*x1;//求y方向的冲量
			Vec2 speed=new Vec2(x1,y1);
			gv.ball.gt.applyLinearImpulse(speed, tempBall, true);//给球一定冲量
		}else//不存在斜率
		{
			if(tempBall.x<tempLan.x)//如果冰球在蓝球的左边
			{
				x1=-(float)Math.random();
			}else if(tempBall.x>tempLan.x)//如果冰球在蓝球的右边
			{
				x1=(float)Math.random();
			}else
			{
				x1=0;
			}
			y1=0;//求y方向的冲量
			Vec2 speed=new Vec2(x1,y1);
			gv.ball.gt.applyLinearImpulse(speed, tempBall, true);//给球一定冲量
		}
	}
	public float[] limitC(float x,float y,float minY,float maxY)//坐标限制
	{
		float[] result=new float[2];
		if(x<=-3.42f)//球能到达的最左边
		{
			x=-3.42f;
		}else if(x>=3.6f)//球能到达的最右边
		{
			x=3.6f;
		}
		if(y<=minY)//球能到达的最上边
		{
			y=minY;
		}else if(y>=maxY)//球能到达的最下边
		{
			y=maxY;
		}
		result[0]=x;
		result[1]=y;
		return result;
	}
}
