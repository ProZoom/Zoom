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
	
	public int guardCount=1;//��¼��λ�õļ�����
	int CCount=0;
	boolean go=false;//�����ȷ���
	
	public AttackUtil au;
	GuardUtil gu;
	
	public Queue<MyAction> ActionMoveQueue=new LinkedList<MyAction>();//��������
	public Object ActionLock=new Object();
	
	public Queue<float[][]> GetPositionQueue=new LinkedList<float[][]>();//����������
	public Object ActionLockA=new Object();
	
	public Queue<float[]> GetBallPositionQueue=new LinkedList<float[]>();//����������
	public Object ActionLockB=new Object();
	boolean flag=true;//��־λ
	public boolean ballGO=false;
	public float[][] ballP=new float[2][2];//��Ż����λ��
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
				Vec2 ballPosition=new Vec2();//�����λ��
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
				
				ChangeMode(ballPosition,-1.5f);//�л�ģʽ
				
				//���� ���ؽ׶�=======================end================================
				if(gv.CWin)//ǰһ�ֵ���ʤ��ʱ����ǰ��ϵͳ�ȿ�ʼ��Ϸ
				{
					hongFirst();
				}
				if(go&&au.judgeIfTouch(gv.hongjdz.gt.getPosition()))//�����Ϸʤ�� ���Һ���������ײʱ
				{
					if(gv.hongjdz.gt.getPosition().x<0)
					{
						gv.ball.gt.applyLinearImpulse(new Vec2(0.2f,0.4f),ballPosition,true);//����һ���ĳ���
					}else if(gv.hongjdz.gt.getPosition().x>0)
					{
						gv.ball.gt.applyLinearImpulse(new Vec2(-0.2f,0.4f),ballPosition,true);
					}else if(gv.hongjdz.gt.getPosition().x==0)
					{
						gv.ball.gt.applyLinearImpulse(new Vec2(0,0.4f),ballPosition,true);
					}
					go=false;
				}
				if(OptionView.difficultyIndex==0)//��
				{
					sleepTime=12;
				}else if(OptionView.difficultyIndex==1)//һ��
				{
					sleepTime=11;
				}else if(OptionView.difficultyIndex==2)//����
				{
					sleepTime=10;
				}
				try
				{
					Thread.sleep(sleepTime);//����10����
				} catch (InterruptedException e)
				{
					e.printStackTrace();//��ӡ�쳣��Ϣ
				}
			}
			try
			{
				Thread.sleep(1000);//����10����
			} catch (InterruptedException e)
			{
				e.printStackTrace();//��ӡ�쳣��Ϣ
			}
		}
	}
	public void ChangeMode(Vec2 ballPosition,float distance)
	{
		if(ballPosition.y<=distance)//������z����С��-1.5
		{
			au.AttackMode();//����ģʽ
		}else
		{
			gu.GuardMode();//����ģʽ
			au.attackCount=1;//����ģʽ��ͷ��ʼ
			au.allowRecordC=true;//���¼�¼����
		}
	}
	public void hongFirst()//�����ȿ�ʼ
	{
		while(gv.CWin&&gv.Start_Game)
		{
			CCount++;
			if(CCount>160)
			{
				au.allowRecordC=true;//�����¼����
				au.attackCount=1;//������
				go=true;
				au.AttackMode();//����ģʽ
				gv.CWin=false;
				CCount=0;
			}else if(CCount>80&&CCount<150)
			{
				gv.isMove=true;
			}
		}
	}
}
