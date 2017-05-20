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
    	//���ӽ����ƶ��ķ�����ͬʱ��������ķ���
    	z=z+vz;
    	x=x+vx;
    	lifeSpan+=lifeSpanStep;
    }
    
    public void drawSelf(GameView gv,float[] startColor,float[] endColor,float maxLifeSpan)
    {
    	MatrixState3D.pushMatrix();//�����ֳ�
    	MatrixState3D.translate(x,0,z);
    	float sj=(maxLifeSpan-lifeSpan)/maxLifeSpan;//˥���������𽥵ı�С������Ϊ0
    	if(sj==0)
    	{
    		gv.isSnow=false;
    	}
    	fpfd.drawSelf(sj,startColor,endColor);//���Ƶ������� 
    	MatrixState3D.popMatrix();//�ָ��ֳ�
    }
}
