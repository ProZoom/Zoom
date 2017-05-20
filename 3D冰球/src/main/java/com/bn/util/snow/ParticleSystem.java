package com.bn.util.snow;

import java.util.*;

import com.bn.constant.MatrixState3D;
import com.bn.view.GameView;

import android.opengl.GLES20;
import static com.bn.util.snow.ParticleConstant.*;

public class ParticleSystem implements Comparable<ParticleSystem> 
{
	//���ڴ�����е�����
	public ArrayList<ParticleSingle> alFsp=new ArrayList<ParticleSingle>();
	//-���ڴ����Ҫɾ��������
	ArrayList<ParticleSingle> alFspForDel=new ArrayList<ParticleSingle>();
	//����ת�����е����ӣ�ÿ�ζ�Ҫ���/
	public ArrayList<ParticleSingle> alFspForDraw=new ArrayList<ParticleSingle>();
	//���ڻ��Ƶ���������
	public ArrayList<ParticleSingle> alFspForDrawTemp=new ArrayList<ParticleSingle>();
	//��Դ��
	Object lock=new Object();
	//Դ�������
	public int srcBlend;
	//Ŀ��������
	public int dstBlend;
	//��Ϸ�ʽ
	public int blendFunc;
	//�������������
	public float maxLifeSpan;
	//���������ڲ���
	public float lifeSpanStep;
	//���Ӹ����߳�����ʱ����
	public int sleepSpan;
	//ÿ���緢����������
	public int groupCount;
	//���������
	public float sx;
	public float sy;
	//����λ��
	public float positionX;
	public float positionZ;
	//�����仯��Χ
	public float xRange;
	//���ӷ�����ٶ�
	public float vx;
	public float vy;
	//������
	ParticleForDraw fpfd;
	//������־λ
	boolean flag=true;
	
	public boolean isOne=true;
	GameView gv;
	float vp[]=new float[2];
    public ParticleSystem(GameView gv,float positionx,float positionz,ParticleForDraw fpfd)
    {
    	this.gv=gv;
    	this.positionX=positionx;
    	this.positionZ=positionz;
    	this.srcBlend=SRC_BLEND[CURR_INDEX]; 
    	this.dstBlend=DST_BLEND[CURR_INDEX];
    	this.blendFunc=BLEND_FUNC[CURR_INDEX];
    	this.maxLifeSpan=MAX_LIFE_SPAN[CURR_INDEX];
    	this.lifeSpanStep=LIFE_SPAN_STEP[CURR_INDEX];
    	this.groupCount=GROUP_COUNT[CURR_INDEX];
    	this.sleepSpan=THREAD_SLEEP[CURR_INDEX];
    	this.sx=positionx;
    	this.sy=positionz;
    	this.xRange=X_RANGE[CURR_INDEX];
    	this.vx=0;
    	this.vy=VY[CURR_INDEX];
    	this.fpfd=fpfd;
    	
    	new Thread()//�����߳�
    	{
    		public void run()
    		{
    			while(flag)
    			{
    				update();
    				try 
    				{
						Thread.sleep(sleepSpan);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    }
    public float[] getSpecialEfficiency()//�����Ч���ֵ��ٶ�
	{
		float[] v=new float[2];
		v[0]=gv.ball.gt.getLinearVelocity().x;
		v[1]=gv.ball.gt.getPosition().y;
		return v;
	}
    public void drawSelf(float[] startColor,float[] endColor)
    {
        //��Ϊÿ�ν��л������ӵĸ����Ѿ������ǲ��ϱ仯�ģ�������Ҫ���ϵظ���
    	alFspForDrawTemp.clear();
    	synchronized(lock)
    	{
    		//������Ŀ����Ϊ�˱�֤��Ӻ������ͬʱ���У�Ҳ���Ǳ�֤��ÿ��add�����ж���
    		for(int i=0;i<alFspForDraw.size();i++)
    		{
    			alFspForDrawTemp.add(alFspForDraw.get(i));
    		}
    	}
    	MatrixState3D.pushMatrix();
    	
    	//�������
        GLES20.glEnable(GLES20.GL_BLEND);
        //���û�Ϸ�ʽ
         GLES20.glBlendEquation(blendFunc);
        //���û������
        GLES20.glBlendFunc(srcBlend,dstBlend);
        
    	MatrixState3D.translate(positionX, 0, positionZ);
    	if(gv.downCount%2==1)//���ӽǵ��л��ı�
    	{
    		MatrixState3D.rotate(-120, 1, 0,0);
    	}
    
    	for(ParticleSingle fsp:alFspForDrawTemp)
    	{
    		fsp.drawSelf(gv,startColor,endColor,maxLifeSpan);
    	}
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND); 
    	
    	MatrixState3D.popMatrix();
    }
    
    public void update()
    {
    	if(isOne)
    	{
    		alFsp.clear();
    		//�緢������
        	for(int i=0;i<groupCount;i++)
        	{
        		//�����ĸ��������������ӵ�λ��------**/
        		float px=(float) (sx+xRange*(Math.random()*2-1.0f));
        		float py=(float) (sy+xRange*(Math.random()*2-1.0f));
        		vp=getSpecialEfficiency();
        		vx=(sx-px)/10;
        		if(vp[0]<0)
        		{
        			vx=-vx;
        		}
        		if(vp[1]<0)
        		{
        			vy=-vy;
        		}
                //x������ٶȺ�С,���ԾͲ����������Ļ�������
                ParticleSingle fsp=new ParticleSingle(px,py,vx,vy,fpfd);
                alFsp.add(fsp);
        	}   
        	isOne=false;
    	}
    	//��ջ���������б����б���Ҫ�洢��Ҫɾ��������
    	alFspForDel.clear();
    	for(ParticleSingle fsp:alFsp)
    	{
    		//��ÿ������ִ���˶�����
    		fsp.go(lifeSpanStep);
    		//��������Ѿ����ڵ�ʱ���Ѿ��㹻�ˣ��Ͱ�����ӵ���Ҫɾ���������б�
    		if(fsp.lifeSpan>this.maxLifeSpan)
    		{
    			alFspForDel.add(fsp);
    		}
    	}
    	//ɾ����������
    	for(ParticleSingle fsp:alFspForDel)
    	{
    		alFsp.remove(fsp);
    	}
    	if(alFsp.size()<1)
    	{
    		gv.isSnow=false;
    	}
    	//alFsp�б��д�������е����Ӷ����������б�Ϊ�����������������ӣ�ͬʱҲ����ɾ��ĳЩ���ڵ����Ӷ���
    	//���»����б� 
    	synchronized(lock)
    	{
    		alFspForDraw.clear();
    		for(int i=0;i<alFsp.size();i++)
    		{
    			alFspForDraw.add(alFsp.get(i));
    		}
    	}
    }
	@Override
	public int compareTo(ParticleSystem another) 
	{
		return 0;
	}
}
