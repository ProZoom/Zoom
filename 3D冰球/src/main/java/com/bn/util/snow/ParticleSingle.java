package com.bn.util.snow;

import com.bn.constant.MatrixState3D;
import com.bn.view.GameView;

public class ParticleSingle 
{    
    public float x;
    public float z;
    public float vx;
    public float vz;
    public float lifeSpan;
    
    ParticleForDraw fpfd;
    
    public ParticleSingle(float x,float z,float vx,float vz,ParticleForDraw fpfd)
    {
    	this.x=x;
    	this.z=z;
    	this.vx=vx;
    	this.vz=vz;
    	this.fpfd=fpfd;
    }
    
    public void go(float lifeSpanStep)
    {
    	//粒子进行移动的方法，同时岁数增大的方法
    	z=z+vz;
    	x=x+vx;
    	lifeSpan+=lifeSpanStep;
    }
    
    public void drawSelf(GameView gv,float[] startColor,float[] endColor,float maxLifeSpan)
    {
    	MatrixState3D.pushMatrix();//保护现场
    	MatrixState3D.translate(x,0,z);
    	float sj=(maxLifeSpan-lifeSpan)/maxLifeSpan;//衰减因子在逐渐的变小，最后变为0
    	if(sj==0)
    	{
    		gv.isSnow=false;
    	}
    	fpfd.drawSelf(sj,startColor,endColor);//绘制单个粒子 
    	MatrixState3D.popMatrix();//恢复现场
    }
}
