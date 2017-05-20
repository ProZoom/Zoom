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
	long timeStart=System.nanoTime();//��ʼʱ��
	long lastTimeStamp=System.nanoTime();//��ʼʱ��
	final long spanMin=(long)((1/60.0)*1000*1000*1000);//��С����
	
	MyMouseJoint mj=null;//�������ؽ�
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
			if(isWorldStep)//��������ģ��
			{
				long currTimeStamp=System.nanoTime();//��ǰʱ��
				if((currTimeStamp-lastTimeStamp)<spanMin)//�ж������Ƿ����
				{
					try 
					{
						Thread.sleep(10);//����5����
					} catch (InterruptedException e)
					{
						e.printStackTrace();//��ӡ�쳣��Ϣ
					}
					continue;//����ѭ��
				}
				lastTimeStamp=currTimeStamp;//����ǰʱ�丳ֵ����ʼʱ��
				
				gv.world.step(TIME_STEP, ITERA, ITERA);//����ģ��
				
				setLanBall();//����ɫ����м���
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
				limitBallSpeed(ballSpeed,maxSpeed);//���ƻ����ٶ�
				gv.lanjdz.gt.setLinearVelocity(new Vec2(0,0));
				gv.hongjdz.gt.setLinearVelocity(new Vec2(0,0));
			}
		}
	}
	public void limitBallSpeed(Vec2 ballSpeed,float maxSpeed)
	{
		//���Ʊ����ٶ�
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
	
	
	public void setLanBall()//������ҿ��Ƶ���
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
				if(mc.index==0)//�������ֵΪ0
				{
					mj=new MyMouseJoint(mc.id,gv.world,mc.collideConnected,mc.poA,mc.poB,
							mc.target,mc.maxForce,mc.frequencyHz,mc.dampingRatio);//�������ؽ�
				}else if(mc.index==1)//�������ֵΪ1
				{
					if(mj!=null)
					{
						if(mj.mJoint !=null)
						{
							Vec2 target=transformPosition(mc.target,gv.ball.gt.getPosition());
							target.x=gv.cu.limitC(target.x,target.y,0.75f,8f)[0];
							target.y=gv.cu.limitC(target.x,target.y,0.75f,8f)[1];
							mj.mJoint.setTarget(target);//�������ؽڵ�����Ŀ���
						}
					}
				}else if(mc.index==2)//�������ֵΪ2
				{
					if(mj!=null)//�ж����ؽ��Ƿ�Ϊ��
					{
						if(mj.mJoint!=null)
						{
							gv.world.destroyJoint(mj.mJoint);//ɾ�����ؽڶ���
							mj.mJoint=null;
						}
						mj=null;//�����ؽڸ�ֵΪNULL
					}
				}else if(mc.index==3)//��Ϸʤ��
				{
					if(mc.computer_win)//����Ӯ
					{
						setTransform(-1.5f);
					}else//���Ӯ
					{
						setTransform(1.5f);
					}
				}else if(mc.index==4)//���¿�ʼ��Ϸ
				{
					setTransform(0);//��ʼ�����λ��
				}
				else if(mc.index==5)//���½�����Ϸ����
				{
					setTransform(1.5f);//��ʼ�����λ��
					destroyBody();
				}
			}
		}
		Vec2 target=transformPosition(gv.lanjdz.gt.getPosition(),gv.ball.gt.getPosition());
		target.x=gv.cu.limitC(target.x,target.y,0.75f,8f)[0];
		target.y=gv.cu.limitC(target.x,target.y,0.75f,8f)[1];
		gv.lanjdz.gt.setTransform(target,0);
	}
	public void updatePosition()//��������
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
		//��û����λ��
		synchronized(gv.bt.ActionLockB)
		{
			gv.bt.GetBallPositionQueue.offer(position[2]);
		}
		synchronized(gv.lockA)
		{
			gv.positionQueue.offer(position);//������ͺ���λ����ӽ�������
		}
		float[][] result=new float[1][2];
		result[0][0]=position[1][0];
		result[0][1]=position[1][1];
		//��ú����λ��
		synchronized(gv.bt.ActionLockA)
		{
			gv.bt.GetPositionQueue.offer(result);
		}
	}
	public Vec2 transformPosition(Vec2 position,Vec2 ballPosition)//���ƺ�����������ƶ�ǰ������
	{
		Vec2 result=new Vec2();
		//��������֮��ľ���
		float distance = (float) Math.sqrt(Math.abs((position.x - ballPosition.x)*(position.x - ballPosition.x)+
				(position.y - ballPosition.y)* (position.y - ballPosition.y)));
		if(distance>=1.54)//����������������İ뾶֮�ͣ�������ֱ���ƶ�
		{
			result.x=position.x;
			result.y=position.y;
		}else //��С�ڰ뾶֮��
		{
			if(ballPosition.x-position.x!=0)//б�ʴ���
			{
				float k=(ballPosition.y-position.y)/(ballPosition.x-position.x);//����б��
				float b=ballPosition.y-k*ballPosition.x;
				float x1=0;
				if(position.x<ballPosition.x)//������ڱ������ߣ���Ӧ���ʵ�����Ų
				{
					x1=ballPosition.x-(float)Math.sqrt((1.54*1.54)/(1+k*k));
					float y1=x1*k+b;
					result.x=x1;
					result.y=y1;
				}else//������ڱ�����ұߣ���Ӧ���ʵ�����Ų
				{
					x1=ballPosition.x+(float)Math.sqrt((1.54*1.54)/(1+k*k));
					float y1=x1*k+b;
					result.x=x1;
					result.y=y1;
				}
			}else//б�ʲ�����
			{
				if(position.x<ballPosition.x)//������ڱ������ߣ���Ӧ���ʵ�����Ų
				{
					float x1=ballPosition.x-1.54f;
					float y1=position.y;
					result.x=x1;
					result.y=y1;
				}else//������ڱ�����ұߣ���Ӧ���ʵ�����Ų
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
	{//��ʼ�����λ��
		gv.ball.gt.setTransform(new Vec2(0.0f, sy), 0);
		gv.hongjdz.gt.setTransform(new Vec2(0.0f, -6f),0);
		gv.lanjdz.gt.setTransform(new Vec2(0.0f, 7.0f), 0);
		gv.ball.gt.setLinearVelocity(new Vec2(0,0));//��ҿ��Ƶı����ٶ���ԶΪ0
		gv.bt.ballGO=true;
	}
	public void destroyBody()//�������
	{
		for(Body body:gv.body)
		{
			gv.world.destroyBody(body);
		}
		gv.body.clear();
	}
}
