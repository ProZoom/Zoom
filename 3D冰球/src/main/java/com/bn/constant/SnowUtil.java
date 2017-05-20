package com.bn.constant;

import static com.bn.util.snow.ParticleConstant.CURR_INDEX;
import static com.bn.util.snow.ParticleConstant.END_COLOR;
import static com.bn.util.snow.ParticleConstant.START_COLOR;

import java.util.ArrayList;
import java.util.List;

import com.bn.object.GameObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.snow.ParticleConstant;
import com.bn.util.snow.ParticleForDraw;
import com.bn.util.snow.ParticleSystem;
import com.bn.view.GameView;
/*
 * 0--С��ײ������ɫ
 * 1--����ɫ
 * 2--��ɫ
 * 3--��ɫ
 * 4--��ɫ
 * 5--ǳ��ɫ
 * 6--����ɫ
 * */
public class SnowUtil {
	List<ParticleSystem> fps=new ArrayList<ParticleSystem>();//��ѩ������ϵͳ���б�
	ParticleForDraw[] fpfd;//ѩ�����ƵĶ���
	ParticleSystem[] ps=new ParticleSystem[3];
	GameView gv;
	Object lock=new Object();//��֤��Ч����ʱ����ȡ����ӷֿ�
	public SnowUtil(GameView gv)
	{
		this.gv=gv;
	} 
	public void setColor(int colorIndex)
	{//Ϊ��ײ����������ɫ
		switch(colorIndex)
		{
		case 0:gv.index=0;break;
		case 1:gv.index=1;break;
		case 2:gv.index=2;break;
		case 3:gv.index=3;break;
		case 4:gv.index=4;break;
		case 5:gv.index=5;break;
		case 6:gv.index=6;break;
		}
	}
	
	//��ʼ��������Ч
 	public void initSnow(int index,GameObject ball)
	{
		float p[]={ball.gt.getPosition().x,ball.gt.getPosition().y};
		fps.clear();//���ѩ���б�
		int count=ParticleConstant.END_COLOR.length;//ѩ������ĸ���
		fpfd=new ParticleForDraw[count];//4������ţ�4����ɫ
		//��������ϵͳ
		for(int i=0;i<count;i++)
		{
			ParticleConstant.CURR_INDEX=i;
			fpfd[i]=new ParticleForDraw(ParticleConstant.RADIS[ParticleConstant.CURR_INDEX],
					ShaderManager.getShader(2),TextureManager.getTextures("particle_yellow.png")); 
			ps[i]=new ParticleSystem(gv,p[0],p[1],fpfd[i]);
			//��������,��ѩ���ĳ�ʼλ�ô���������
			fps.add(ps[i]);
		}
	}
	//���¿�ʼ������Ч�Ļ���
	public void ReSet(int index,GameObject ball)
	{
		synchronized(lock)
		{
			float p[]={ball.gt.getPosition().x,ball.gt.getPosition().y};
			fps.clear();//���ѩ���б�
			int count=ParticleConstant.END_COLOR.length;//ѩ������ĸ���
			//��������ϵͳ
			for(int i=0;i<count;i++)
			{
				ParticleConstant.CURR_INDEX=i;
				//��������,��ѩ���ĳ�ʼλ�ô���������
				ps[i].positionX=p[0];
				ps[i].positionZ=p[1];
				fps.add(ps[i]);
				ps[i].isOne=true;
			}
		}
	}
	public void drawSnow(int index)//����ѩ��
	{
		if(gv.isSnow)//����ѩ������ϵͳ
		{
			if(index!=-1)
			{
				synchronized(lock)
				{
					for(int i=0;i<fps.size();i++)
					{
						fps.get(i).drawSelf(START_COLOR[3*index+CURR_INDEX],END_COLOR[CURR_INDEX]);
					}
				}
			}
		}
	}
}
