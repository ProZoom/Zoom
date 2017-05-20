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
 * 0--小球撞桌的颜色
 * 1--深蓝色
 * 2--红色
 * 3--紫色
 * 4--绿色
 * 5--浅蓝色
 * 6--淡红色
 * */
public class SnowUtil {
	List<ParticleSystem> fps=new ArrayList<ParticleSystem>();//放雪花粒子系统的列表
	ParticleForDraw[] fpfd;//雪花绘制的对象
	ParticleSystem[] ps=new ParticleSystem[3];
	GameView gv;
	Object lock=new Object();//保证特效绘制时粒子取与添加分开
	public SnowUtil(GameView gv)
	{
		this.gv=gv;
	} 
	public void setColor(int colorIndex)
	{//为碰撞粒子设置颜色
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
	
	//初始化星星特效
 	public void initSnow(int index,GameObject ball)
	{
		float p[]={ball.gt.getPosition().x,ball.gt.getPosition().y};
		fps.clear();//清空雪花列表
		int count=ParticleConstant.END_COLOR.length;//雪花种类的个数
		fpfd=new ParticleForDraw[count];//4组绘制着，4种颜色
		//创建粒子系统
		for(int i=0;i<count;i++)
		{
			ParticleConstant.CURR_INDEX=i;
			fpfd[i]=new ParticleForDraw(ParticleConstant.RADIS[ParticleConstant.CURR_INDEX],
					ShaderManager.getShader(2),TextureManager.getTextures("particle_yellow.png")); 
			ps[i]=new ParticleSystem(gv,p[0],p[1],fpfd[i]);
			//创建对象,将雪花的初始位置传给构造器
			fps.add(ps[i]);
		}
	}
	//重新开始星星特效的绘制
	public void ReSet(int index,GameObject ball)
	{
		synchronized(lock)
		{
			float p[]={ball.gt.getPosition().x,ball.gt.getPosition().y};
			fps.clear();//清空雪花列表
			int count=ParticleConstant.END_COLOR.length;//雪花种类的个数
			//创建粒子系统
			for(int i=0;i<count;i++)
			{
				ParticleConstant.CURR_INDEX=i;
				//创建对象,将雪花的初始位置传给构造器
				ps[i].positionX=p[0];
				ps[i].positionZ=p[1];
				fps.add(ps[i]);
				ps[i].isOne=true;
			}
		}
	}
	public void drawSnow(int index)//绘制雪花
	{
		if(gv.isSnow)//绘制雪花粒子系统
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
