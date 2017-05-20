package com.bn.constant;

import com.bn.util.action.ActionMoveToTarget;
import com.bn.util.action.MyAction;
import com.bn.view.GameView;

import org.jbox2d.common.Vec2;

public class AttackUtil //����ģʽ
{
	GameView gv;
	
	float attackX1=0;//��ĵ�һ��λ��
	float attackZ1=0;
	float attackX2=0;//��ĵڶ���λ��
	float attackZ2=0;
	float attackX3=0;//����õ���Ŀ���
	float attackZ3=0;
	float attackX4=0;//����ʵʱ�ƶ���λ��
	float attackZ4=0;
	float attackX5=0;//����δ�ƶ�֮ǰ��λ��
	float attackZ5=0;
	
	int t=1;
	int tx=2;
	public int attackCount=1;//��¼�����λ�õı�־λ
	public boolean allowRecordC=true;//�����¼����
	float attackVX=0;//ÿ���ƶ���λ��
	float attackVY=0;
	
	boolean isUp=false;//�����Ƿ���������ƶ�
	float oneDistance = 0.2f;//ÿ���ƶ���λ��
	int integerStep=0;//��������
	float extraStep=0;//���ಽ��
	int stepCount=0;//��ǰ�ߵĲ���
	float extraX=0;//�����ߵ�xֵ
	float extraY=0;//�����ߵ�zֵ
	
	Vec2 destination=new Vec2();//����Ŀ��������
	public AttackUtil(GameView gv)
	{
		this.gv=gv;
	}
	
	public void AttackMode()//����ģʽ �����ƶ�
	{
		if(attackCount%4==1)//��¼��һ�������λ��
		{
			Vec2 ballP=new Vec2(0,0);
			synchronized(gv.bt.ActionLockB)
			{
				ballP=new Vec2(gv.bt.ballP[0][0],gv.bt.ballP[0][1]);
			}
			attackX1=ballP.x;
			attackZ1=ballP.y;
			attackCount++;
		}else if(attackCount%4==2)//��¼�ڶ��������λ��
		{
			Vec2 ballP=new Vec2(0,0);
			synchronized(gv.bt.ActionLockB)
			{
				ballP=new Vec2(gv.bt.ballP[1][0],gv.bt.ballP[1][1]);
			}
			attackX2=ballP.x;
			attackZ2=ballP.y;
			attackCount++;
		}else if(attackCount%4==3)//����ǰ����ʱ�̵�λ�� ȷ��Ŀ��� ��¼����
		{
			RecordCoordinate();//��¼λ��
			GoToTarget();//����Ŀ���
		}else if(attackCount%4==0)
		{
			GoBackToOrigon();//����ԭ��
		}
	}
	public void GoToTarget()//����Ŀ���
	{
		if(stepCount<integerStep)//������������
		{
			if(attackX5>=attackX3&&attackZ5>=attackZ3)//������
			{
				ToLeftUp(attackVX,attackVY);
			}else if(attackX5<=attackX3&&attackZ5<=attackZ3)//������
			{
				ToRightDown(attackVX,attackVY);
			}else if(attackX5>attackX3&&attackZ5<attackZ3)//������
			{
				ToLeftDown(attackVX,attackVY);
			}else if(attackX5<attackX3&&attackZ5>attackZ3)//������
			{
				ToRightUp(attackVX,attackVY);
			}
			stepCount++;
		}else if(extraStep!=0)//��ʣ��ľ���
		{
			if(attackX5>=attackX3&&attackZ5>=attackZ3)//������
			{
				ToLeftUp(extraX,extraY);
			}else if(attackX5<=attackX3&&attackZ5<=attackZ3)//������
			{
				ToRightDown(extraX,extraY);
			}else if(attackX5>attackX3&&attackZ5<attackZ3)//������
			{
				ToLeftDown(extraX,extraY);
			}else if(attackX5<attackX3&&attackZ5>attackZ3)//������
			{
				ToRightUp(extraX,extraY);
			}
		}
	}
	public void RecordCoordinate()//��¼��ǰ������ֵ
	{
		if(allowRecordC)//�����¼����
		{
			//����Ŀ���
			attackX3=(float)((tx/t)*(float)(attackX2-attackX1)+(float)attackX2);
			attackZ3=(float)((tx/t)*(float)(attackZ2-attackZ1)+(float)attackZ2);
			//�ҵ��ܴ���Է����ڵ����λ��
			attackX3=getPlayerPosition(attackX3,attackZ3).x;
			attackZ3=getPlayerPosition(attackX3,attackZ3).y;
			//��ȡ�����λ��
			synchronized(gv.bt.ActionLockA)
			{
				while(gv.bt.GetPositionQueue.size()>0)//������еĳ��ȴ���0
				{
					float[][] result=gv.bt.GetPositionQueue.poll();
					attackX4=result[0][0];
					attackZ4=result[0][1];
					attackX5=result[0][0];
					attackZ5=result[0][1];
				}
			}
			//��������֮��ľ���
			float distance=(float)Math.sqrt(
					(attackX4-attackX3)*(attackX4-attackX3)+(attackZ4-attackZ3)*(attackZ4-attackZ3));
			float cos=Math.abs(attackX4-attackX3)/distance;//��������ֵ
			float sin=Math.abs(attackZ4-attackZ3)/distance;//��������ֵ
			float step=distance/oneDistance;//���㲽��
			integerStep=(int)step;//��������
			extraStep=step-integerStep;//����Ĳ���1�Ĳ���
			extraX=(extraStep*oneDistance)*cos;//�����ߵ�xֵ
			extraY=(extraStep*oneDistance)*sin;//�����ߵ�yֵ
			if(integerStep!=0)
			{
				attackVX=Math.abs(attackX4-attackX3-extraX)/integerStep;//ÿ��xҪ�ߵ�λ��
				attackVY=Math.abs(attackZ4-attackZ3-extraY)/integerStep;//ÿ��yҪ�ߵ�λ��
			}
			allowRecordC=false;
		}
	}
	public void GoBackToOrigon()//���ص�ԭ��
	{
		if(allowRecordC)//�����¼����
		{
			//��ȡ��ǰ�����λ��
			synchronized(gv.bt.ActionLockA)
			{
				while(gv.bt.GetPositionQueue.size()>0)//������еĳ��ȴ���0
				{
					float[][] result=gv.bt.GetPositionQueue.poll();
					attackX4=result[0][0];
					attackZ4=result[0][1];
					attackX5=result[0][0];
					attackZ5=result[0][1];
				}
			}
			attackX3=0;//Ŀ���λ��
			attackZ3=-6f;
			//����֮�����
			float distance=(float)Math.sqrt(
				(attackX4-attackX3)*(attackX4-attackX3)+(attackZ4-attackZ3)*(attackZ4-attackZ3));
			
			float cos=Math.abs(attackX4-attackX3)/distance;//�Ƕȵ�����ֵ
			float sin=Math.abs(attackZ4-attackZ3)/distance;//�Ƕȵ�����ֵ
			float step=distance/oneDistance;//���㲽��
			integerStep=(int)step;//��������
			extraStep=step-integerStep;//����Ĳ���1�Ĳ���
			extraX=(extraStep*oneDistance)*cos;//�����ߵ�xֵ
			extraY=(extraStep*oneDistance)*sin;//�����ߵ�yֵ
			if(integerStep!=0)
			{
				attackVX=Math.abs(attackX4-attackX3-extraX)/integerStep;//ÿ��xҪ�ߵ�λ��
				attackVY=Math.abs(attackZ4-attackZ3-extraY)/integerStep;//ÿ��yҪ�ߵ�λ��
			}
			allowRecordC=false;
		}
		if(stepCount<integerStep)
		{
			if(attackX5<attackX3)//����෵�ص�ԭ��
			{
				LeftBackToOrigon(attackVX,attackVY);
			}else//���Ҳ෵�ص�ԭ��
			{
				RightBackToOrigon(attackVX,attackVY);
			}
			stepCount++;
		}else if(extraStep!=0)
		{
			if(attackX5<attackX3)//����෵�ص�ԭ��
			{
				LeftBackToOrigon(extraX,extraY);
			}else//���Ҳ෵�ص�ԭ��
			{
				RightBackToOrigon(extraX,extraY);
			}
		}
	}
	public void RightBackToOrigon(float mx,float my)//���Ҳ෵�ص�ԭ��
	{
		if(attackX4<=attackX3)
		{
			stepCount=0;
			attackCount++;//�����Լ�
			allowRecordC=true;//�����¼��һʱ�̵��λ��
		}else
		{
			attackX4-=mx;
			attackZ4-=my;
			attackX4=gv.cu.limitC(attackX4,attackZ4,-6f,-0.75f)[0];//�������������
			attackZ4=gv.cu.limitC(attackX4,attackZ4,-6f,-0.75f)[1];
			MyAction ma=new ActionMoveToTarget(attackX4,attackZ4,6);
			synchronized(gv.bt.ActionLock)
			{
				gv.bt.ActionMoveQueue.offer(ma);
			}
		}
	}
	public void LeftBackToOrigon(float mx,float my)//����෵�ص�ԭ��
	{
		if(attackX4>=attackX3)
		{
			stepCount=0;
			attackCount++;//�����Լ�
			allowRecordC=true;//�����¼��һʱ�̵��λ��
		}else
		{
			attackX4+=mx;
			attackZ4-=my;
			attackX4=gv.cu.limitC(attackX4,attackZ4,-6f,-0.75f)[0];//�������������
			attackZ4=gv.cu.limitC(attackX4,attackZ4,-6f,-0.75f)[1];
			MyAction ma=new ActionMoveToTarget(attackX4,attackZ4,6);
			synchronized(gv.bt.ActionLock)
			{
				gv.bt.ActionMoveQueue.offer(ma);
			}
		}
	}
	public void arriveAtTarget()//������򵽴�Ŀ��� 
	{
		if(isUp)
		{
			SpeedUp();//�������
			isUp=false;
		}else
		{
			apply();//�������Է��Ķ���
		}
		stepCount=0;
		attackCount++;//�����Լ�
		allowRecordC=true;//�����¼��һʱ�̵��λ��
		gv.cu.doCrashAction(2,"puckBeaterSound.mp3");//����������Ч��
	}
	
	//�����������ײʱ���������һ�����ٶ� 
	public void SpeedUp()
	{
		Vec2 ballSpeed=gv.ball.gt.getLinearVelocity();//��ȡ�����ٶ�
		Vec2 ballPosition=new Vec2();//��ȡ����λ��
		synchronized(gv.bt.ActionLockB)
		{
			ballPosition.x=gv.bt.ballP[1][0];
			ballPosition.y=gv.bt.ballP[1][1];
		}
		if(ballSpeed.x!=0&&ballSpeed.y!=0)
		{
			float k=(float)ballSpeed.y/(float)ballSpeed.x;//�����ֵ
			float ballSpeedX=0;
			if(ballSpeed.x<0)
			{
				ballSpeedX=-(float)Math.random()/5;//����õ�һ��xֵ
			}else
			{
				ballSpeedX=(float)Math.random()/5;
			}
			float ballSpeedY=(float)ballSpeedX*k;//���ݱ��� �õ�yֵ
			while(Math.abs(ballSpeedY)>1.5)
			{
				ballSpeedY=ballSpeedY/5;
			}
			//���������ó��� ����һ���ٶ�
			gv.ball.gt.applyLinearImpulse(new Vec2(ballSpeedX,ballSpeedY),ballPosition,true);
		}
	}
	public void ToLeftUp(float mx,float my)//������
	{
		attackX4-=mx;
		attackZ4-=my;
		attackX4=limitCoordinate(attackX4,attackZ4)[0];
		attackZ4=limitCoordinate(attackX4,attackZ4)[1];
		MyAction ma=new ActionMoveToTarget(attackX4,attackZ4,6);
		synchronized(gv.bt.ActionLock)
		{
			gv.bt.ActionMoveQueue.offer(ma);
		}
		
		if(attackX4<=attackX3&&attackZ4<=attackZ3)//����Ŀ���
		{
			isUp=true;
			arriveAtTarget();
		}else if(judgeIfTouch(gv.hongjdz.gt.getPosition()))//�����Ѿ���ײ
		{
			isUp=true;
			arriveAtTarget();
		}
	}
	public void ToRightDown(float mx,float my)//������
	{
		attackX4+=mx;
		attackZ4+=my;
		attackX4=limitCoordinate(attackX4,attackZ4)[0];
		attackZ4=limitCoordinate(attackX4,attackZ4)[1];
		MyAction ma=new ActionMoveToTarget(attackX4,attackZ4,6);
		synchronized(gv.bt.ActionLock)
		{
			gv.bt.ActionMoveQueue.offer(ma);
		}
		if(attackX4>=attackX3&&attackZ4>=attackZ3)//����Ŀ���
		{
			arriveAtTarget();
		}else if(judgeIfTouch(gv.hongjdz.gt.getPosition()))//�����Ѿ���ײ
		{
			arriveAtTarget();
		}
	}
	public void ToLeftDown(float mx,float my)//������
	{
		attackX4-=mx;
		attackZ4+=my;
		attackX4=limitCoordinate(attackX4,attackZ4)[0];
		attackZ4=limitCoordinate(attackX4,attackZ4)[1];
		MyAction ma=new ActionMoveToTarget(attackX4,attackZ4,6);
		synchronized(gv.bt.ActionLock)
		{
			gv.bt.ActionMoveQueue.offer(ma);
		}
		if(attackX4<=attackX3&&attackZ4>=attackZ3)//����Ŀ���
		{
			arriveAtTarget();
		}else if(judgeIfTouch(gv.hongjdz.gt.getPosition()))//�����Ѿ���ײ
		{
			arriveAtTarget();
		}
	}
	public void ToRightUp(float mx,float my)//������
	{
		attackX4+=mx;
		attackZ4-=my;
		attackX4=limitCoordinate(attackX4,attackZ4)[0];
		attackZ4=limitCoordinate(attackX4,attackZ4)[1];
		MyAction ma=new ActionMoveToTarget(attackX4,attackZ4,6);
		synchronized(gv.bt.ActionLock)
		{
			gv.bt.ActionMoveQueue.offer(ma);
		}
		if(attackX4>=attackX3&&attackZ4<=attackZ3)//����Ŀ���
		{
			isUp=true;
			arriveAtTarget();
		}else if(judgeIfTouch(gv.hongjdz.gt.getPosition()))//�����Ѿ���ײ
		{
			isUp=true;
			arriveAtTarget();
		}
	}
	//�жϸ����Ƿ���ײ
	public boolean judgeIfTouch(Vec2 p)
	{
		Vec2 p1=p;
		Vec2 p2=new Vec2();
		synchronized(gv.bt.ActionLockB)
		{
			p2=new Vec2(gv.bt.ballP[1][0],gv.bt.ballP[1][1]);
		}
		return gv.cu.judgeIfTouch(p1, p2);
	}
	public float[] limitCoordinate(float mx,float my)//���ƺ�������
	{
		float[] result=new float[2];
		boolean isCountAdd=false;
		if(mx<=-3.42f)//�����ܵ���������
		{
			isCountAdd=true;
			mx=-3.42f;
		}else if(mx>=3.6f)//�����ܵ�������ұ�
		{
			isCountAdd=true;
			mx=3.6f;
		}
		if(my<=-7f)//�����ܵ�������ϱ�
		{
			isCountAdd=true;
			my=-7f;
		}else if(my>=-0.75)//�����ܵ�������±�
		{
			isCountAdd=true;
			my=-0.75f;
		}
		if(isCountAdd)
		{
			attackCount++;
			allowRecordC=true;
			isCountAdd=false;
		}
		result[0]=mx;
		result[1]=my;
		return result;
	}
	public Vec2 getPlayerPosition(float mx,float my)
	{
		Vec2 ballL=new Vec2(mx,my);
		Vec2 player=gv.lanjdz.gt.getPosition();//��������λ��
		float rake=0;
		
		float xr1;
		do//�ж�ȷ����ֱ���������Ƿ����
		{
			xr1=(float)(Math.pow(-1, (int)(Math.random()*2))*Math.random()*0.9f);//������������xֵ
			destination=new Vec2((float) (Math.random()*4-1.6f),(float) (Math.random()+8.5f));
			
			if(ballL.x<xr1)//ֱ�߷���\
			{
				rake=-(ballL.y-destination.y)/(ballL.x-destination.x);
			}else//ֱ�߷���/
			{
				rake=(ballL.y-destination.y)/(ballL.x-destination.x);
			}
		}while(Math.abs((player.y-rake*player.x))<0.01f&&Math.abs(ballL.x-player.x)>1.6f);
		
		float k=(ballL.y-destination.y)/(ballL.x-destination.x);
		float b=ballL.y-k*ballL.x;
		float x1=0;
		x1=ballL.x+(float)Math.sqrt((1.55*1.55)/(1+k*k));
		float y1=x1*k+b;
		
		Vec2 result=new Vec2(x1,y1);
		
		return result;
	}
	public void apply()
	{
		Vec2 ballL=new Vec2();
		synchronized(gv.bt.ActionLockB)
		{
			ballL.x=gv.bt.ballP[1][0];
			ballL.y=gv.bt.ballP[1][1];
		}
		Vec2 attckV=Attack(ballL.x,ballL.y,destination.x,destination.y);
		
		float m=gv.ball.gt.getMass()*5;//������������
		attckV=new Vec2(m*attckV.x*10,m*attckV.y*10);
		gv.ball.gt.applyLinearImpulse(attckV,ballL, true);
	}
	public Vec2 Attack(float sx,float sy,float ex,float ey)//��ý������ٶ�
	{
		float a=3;//����
		Vec2 ballSV=new Vec2();//��ʼ�˶����ٶ�
		ballSV.x=(float)Math.sqrt(2*a*(Math.abs(sx-ex)))*0.4f;
		ballSV.y=(float)Math.sqrt(2*a*(Math.abs(sy-ey)))*0.4f;
		return ballSV;
	}
}
