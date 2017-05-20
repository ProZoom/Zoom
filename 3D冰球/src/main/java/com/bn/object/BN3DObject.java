package com.bn.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.bn.constant.MatrixState3D;

public class BN3DObject {
	public FloatBuffer mVertexBuffer;//�����������ݻ���
	public FloatBuffer mTexCoorBuffer;//���������������ݻ���
	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ���������� 
    int maTexCoorHandle; //��������������������  
    int muSjFactor;//˥����������id
    
	int texId;//����ͼƬ��
	int vCount;//�������
    boolean initFlag=false;//�ж��Ƿ��ʼ����ɫ��
    public BN3DObject(float picWidth,float picHeight,int texId,int programId)
    {
    	this.texId=texId;
		this.mProgram=programId;
		initVertexData(picWidth,picHeight);//��ʼ����������
    }
    public void initVertexData(float width,float height)//��ʼ����������
	{
		vCount=4;//�������
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
		texCoor=new float[]{
				0,0,0,1,1,0,
				1,1,1,0,0,1
				};
		ByteBuffer cbb=ByteBuffer.allocateDirect(texCoor.length*4);//�������������������ݻ���
		cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mTexCoorBuffer=cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
		mTexCoorBuffer.position(0);//���û�������ʼλ��
	}
    public void initShader(boolean isDamping)
    {
    	//��ȡ�����ж���λ����������id  
  		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
  		//��ȡ�����ж�������������������id  
  		maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
  		//��ȡ�������ܱ任��������id
  		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
  		//��ȡ������˥����������id
        muSjFactor=GLES20.glGetUniformLocation(mProgram, "sjFactor");
  	}
	public void drawSelf(float sj)
    {        
    	if(!initFlag)
    	{
    		//��ʼ����ɫ��
    		initShader(true);
    		initFlag=true;
    	}
    	//�ƶ�ʹ��ĳ��shader����
    	GLES20.glUseProgram(mProgram);
    	
    	MatrixState3D.pushMatrix();//��������
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
    	//��˥�����Ӵ���shader����
    	GLES20.glUniform1f(muSjFactor, sj);
    	
    	//�����ձ任������shader����
    	GLES20.glUniformMatrix4fv
    	(
    			muMVPMatrixHandle, 
    			1, 
    			false, 
    			MatrixState3D.getFinalMatrix(), 
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
    	MatrixState3D.popMatrix();//�ָ�����
    }
}
