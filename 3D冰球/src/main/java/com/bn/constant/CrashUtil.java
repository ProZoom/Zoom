package com.bn.constant;

import com.bn.view.GameView;
import com.bn.view.MainView;

import org.jbox2d.common.Vec2;

public class CrashUtil //��ײ������
{
	GameView gv;
	public CrashUtil(GameView gv)
	{
		this.gv=gv;
	}
	public boolean judgeIfTouch(Vec2 p1,Vec2 p2)//�ж��������Ƿ���ײ
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
	public void doCrashAction(int number,String musicName)//��ײ����
	{
		gv.su.setColor(GameView.colorIndex[number]);//����������ɫ
		gv.su.ReSet(gv.index,gv.ball);//����
		gv.isSnow=true;//����ѩ��
		if(MainView.isSound)//��������Ч
		{
			gv.mv.activity.sound.playSound(musicName, 0);
		}
	}
	public void lanApplyBall(Vec2 tempBall,Vec2 tempLan)//�����������ײ�� �������һ������
	{
		float x1=0;
		float y1=0;
		if(tempBall.x!=tempLan.x)//����б��
		{
			float k=(tempBall.y-tempLan.y)/(tempBall.x-tempLan.x);//����б��
			if(tempBall.x<tempLan.x)//�����������������
			{
				x1=-(float)Math.random();//�������x���������
			}else if(tempBall.x>tempLan.x)//���������������ұ�
			{
				x1=(float)Math.random();//�������x���������
			}else
			{
				x1=0;
			}
			y1=k*x1;//��y����ĳ���
			Vec2 speed=new Vec2(x1,y1);
			gv.ball.gt.applyLinearImpulse(speed, tempBall, true);//����һ������
		}else//������б��
		{
			if(tempBall.x<tempLan.x)//�����������������
			{
				x1=-(float)Math.random();
			}else if(tempBall.x>tempLan.x)//���������������ұ�
			{
				x1=(float)Math.random();
			}else
			{
				x1=0;
			}
			y1=0;//��y����ĳ���
			Vec2 speed=new Vec2(x1,y1);
			gv.ball.gt.applyLinearImpulse(speed, tempBall, true);//����һ������
		}
	}
	public float[] limitC(float x,float y,float minY,float maxY)//��������
	{
		float[] result=new float[2];
		if(x<=-3.42f)//���ܵ���������
		{
			x=-3.42f;
		}else if(x>=3.6f)//���ܵ�������ұ�
		{
			x=3.6f;
		}
		if(y<=minY)//���ܵ�������ϱ�
		{
			y=minY;
		}else if(y>=maxY)//���ܵ�������±�
		{
			y=maxY;
		}
		result[0]=x;
		result[1]=y;
		return result;
	}
}
