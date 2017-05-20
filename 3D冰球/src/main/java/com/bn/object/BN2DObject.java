package com.bn.object;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import com.bn.constant.Constant;
import com.bn.constant.MatrixState2D;

import android.opengl.GLES20;

public class BN2DObject
{
	public FloatBuffer mVertexBuffer;//�����������ݻ���
	public FloatBuffer mTexCoorBuffer;//���������������ݻ���
    int muMVPMatrixHandle;//�ܱ任��������id
    int maPositionHandle;//����λ����������id  
    int maTexCoorHandle;//��������������������id
    
    int programId;//�Զ�����Ⱦ���߳���id
	int texId;//����ͼƬ��
	int vCount;//�������
    boolean initFlag=false;//�ж��Ƿ��ʼ����ɫ��  
    float x;//��Ҫƽ�Ƶ�x����
	float y;//��Ҫƽ�Ƶ�y����
	boolean isGrade=false;//���Ʒ���
	int num=0;
	int muSjFactor;//˥����������id
	int count=0;
	
	public BN2DObject(float x,float y,float picWidth,float picHeight,int texId,int programId)
	{
		this.x=Constant.fromScreenXToNearX(x);//����Ļxת�����ӿ�x����
		this.y=Constant.fromScreenYToNearY(y);//����Ļyת�����ӿ�y����
		this.texId=texId;
		this.programId=programId;
		initVertexData(picWidth,picHeight);//��ʼ����������
	}
	public BN2DObject(int num,int texId,int programId)
	{
		isGrade=true;//���Ƶ÷�
		this.num=num;
		this.texId=texId;
		this.programId=programId;
		initVertexData(80,100);//��ʼ����������
	}
	public BN2DObject(int texId,int programId)
	{
		this.texId=texId;
		this.programId=programId;
		initVertexData(150,150);//��ʼ����������
	}
	
	public void initVertexData(float width,float height)//��ʼ����������
	{
		vCount=4;//�������
		width=Constant.fromPixSizeToNearSize(width);//��Ļ���ת�����ӿڿ��
		height=Constant.fromPixSizeToNearSize(height);//��Ļ�߶�ת�����ӿڸ߶�
		//��ʼ��������������
		float vertices[]=new float[]
		{
				-width/2,height/2,0,
				-width/2,-height/2,0,
				width/2,height/2,0,
				width/2,-height/2,0
		};
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
		vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mVertexBuffer=vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);//�򻺳����з��붥����������
		mVertexBuffer.position(0);//���û�������ʼλ��
		float[] texCoor=new float[12];//��ʼ��������������
		//����ͼ�ε���������
		if(!isGrade)
		{
			texCoor=new float[]{
					0,0,0,1,1,0,
					1,1,1,0,0,1};
		}else
		{
			float rate=0.1f*num;
			texCoor=new float[]
					{
					0+rate,0,
					0+rate,1,
					1*0.1f+rate,0,
					1*0.1f+rate,1,
					1*0.1f+rate,0,
					0+rate,1
					};
		}
		ByteBuffer cbb=ByteBuffer.allocateDirect(texCoor.length*4);//�������������������ݻ���
		cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mTexCoorBuffer=cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
		mTexCoorBuffer.position(0);//���û�������ʼλ��
	}
	//��ʼ����ɫ��
	public void initShader()
	{
		//��ȡ�����ж���λ����������id  
		maPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
		//��ȡ�����ж�������������������id  
		maTexCoorHandle= GLES20.glGetAttribLocation(programId, "aTexCoor");
		//��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(programId, "uMVPMatrix");  
	}
	//����ͼ��
	public void drawSelf(int index)
	{        
		if(!initFlag)
		{
			//��ʼ����ɫ��        
    		initShader();
    		initFlag=true;
    	}
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	//�ƶ�ʹ��ĳ��shader����
    	GLES20.glUseProgram(programId);
    	
    	MatrixState2D.pushMatrix();//��������
		MatrixState2D.translate(x,y, 0);//ƽ��
		if(index==1)
		{
			count++;
			MatrixState2D.rotate(-count*8,0, 0, 1);//��ת
		}
    	//�����ձ任������shader����
    	GLES20.glUniformMatrix4fv
    	(
    			muMVPMatrixHandle, 
    			1, 
    			false, 
    			MatrixState2D.getFinalMatrix(), 
    			0
    	); 
    	//Ϊ����ָ������λ������
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,
    			3, 
    			GLES20.GL_FLOAT,
    			false,
    			3*4,
    			mVertexBuffer
    			);
    	//Ϊ����ָ������������������
    	GLES20.glVertexAttribPointer
    	(
    			maTexCoorHandle,
    			2,
    			GLES20.GL_FLOAT,
    			false,
    			2*4,
    			mTexCoorBuffer
    			);   
    	//������λ����������
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
    	
    	//������
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texId);
    	
    	//�����������--������
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount); 
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND);
    	MatrixState2D.popMatrix();//�ָ�����
	}
	
	public void drawSelf(float lx,float ly)
	{        
		if(!initFlag)
		{
			//��ʼ����ɫ��        
			initShader();
			initFlag=true;
		}
		GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
		//���û������
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//�ƶ�ʹ��ĳ��shader����
		GLES20.glUseProgram(programId);
		
		MatrixState2D.pushMatrix();//��������
		lx=Constant.fromScreenXToNearX(lx);
		ly=Constant.fromScreenYToNearY(ly);
		MatrixState2D.translate(lx,ly, 0);//ƽ��
		//�����ձ任������shader����
		GLES20.glUniformMatrix4fv
		(
				muMVPMatrixHandle, 
				1, 
				false, 
				MatrixState2D.getFinalMatrix(), 
				0
				); 
		//Ϊ����ָ������λ������
		GLES20.glVertexAttribPointer  
		(
				maPositionHandle,
				3, 
				GLES20.GL_FLOAT,
				false,
				3*4,
				mVertexBuffer
				);
		//Ϊ����ָ������������������
		GLES20.glVertexAttribPointer
		(
				maTexCoorHandle,
				2,
				GLES20.GL_FLOAT,
				false,
				2*4,
				mTexCoorBuffer
				);   
		//������λ����������
		GLES20.glEnableVertexAttribArray(maPositionHandle);  
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
		
		//������
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texId);
		
		//�����������--������
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount); 
		//�رջ��
		GLES20.glDisable(GLES20.GL_BLEND);
		MatrixState2D.popMatrix();//�ָ�����
	}
}
