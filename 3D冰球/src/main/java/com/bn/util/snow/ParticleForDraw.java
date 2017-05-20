package com.bn.util.snow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.constant.MatrixState3D;

import android.opengl.GLES20;
//����������
public class ParticleForDraw
{	
	int muMVPMatrixHandle;//�ܱ任��������id
	int muSjFactor;//˥����������id
	int muBj;//�뾶����id
	int muStartColor;//��ʼ��ɫ����id
	int muEndColor;//��ֹ��ɫ����id
	int maPositionHandle; //����λ����������id
	int maTexCoorHandle; //��������������������id
	String mVertexShader;//������ɫ��
	String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
	int vCount=0;   
	float halfSize;
	boolean initFlag=false;//�ж��Ƿ��ʼ����ɫ��  
	int programId;//�Զ�����Ⱦ���߳���id
	int textureId;//����ͼƬId
	public ParticleForDraw(float halfSize,int programId,int textureId)
	{
		this.halfSize=halfSize;
		this.programId=programId;
		this.textureId=textureId;
		//��ʼ��������������ɫ����
		initVertexData(halfSize);
	}
	
	//��ʼ��������������ɫ���ݵķ���
	public void initVertexData(float halfSize)
	{
		//�����������ݵĳ�ʼ��================begin============================
		vCount=6;
		float vertices[]=new float[]
				{
				-halfSize,halfSize,0,
				-halfSize,-halfSize,0,
				halfSize,halfSize,0,
				
				-halfSize,-halfSize,0,
				halfSize,-halfSize,0,
				halfSize,halfSize,0
				};
		
		//���������������ݻ���
		//vertices.length*4����Ϊһ�������ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);//�򻺳����з��붥����������
		mVertexBuffer.position(0);//���û�������ʼλ��
		//�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		//ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		//�����������ݵĳ�ʼ��================end============================
		//���������������ݵĳ�ʼ��================begin============================
		float texCoor[]=new float[]//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
				{
				0,0, 0,1, 1,0,
				0,1, 1,1, 1,0
				};        
		//�������������������ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
		cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
		mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
		mTexCoorBuffer.position(0);//���û�������ʼλ��
		//�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		//ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		//���������������ݵĳ�ʼ��================end============================
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
		//��ȡ������˥����������id
		muSjFactor=GLES20.glGetUniformLocation(programId, "sjFactor");
		//��ȡ�����а뾶����id
		muBj=GLES20.glGetUniformLocation(programId, "bj");
		//��ȡ��ʼ��ɫ����id
		muStartColor=GLES20.glGetUniformLocation(programId, "startColor");
		//��ȡ��ֹ��ɫ����id
		muEndColor=GLES20.glGetUniformLocation(programId, "endColor");
	}
	
	public void drawSelf(float sj,float[] startColor,float[] endColor)
	{
		if(!initFlag)
		{
			//��ʼ����ɫ��
			initShader();
			initFlag=true;
		}
		//�ƶ�ʹ��ĳ��shader����
		GLES20.glUseProgram(programId);  
		//�����ձ任������shader����
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState3D.getFinalMatrix(), 0); 
		//��˥�����Ӵ���shader����
		GLES20.glUniform1f(muSjFactor, sj);
		//���뾶����shader����
		GLES20.glUniform1f(muBj, halfSize);
		//����ʼ��ɫ������Ⱦ����
		GLES20.glUniform4fv(muStartColor, 1, startColor, 0);
		//����ֹ��ɫ������Ⱦ����
		GLES20.glUniform4fv(muEndColor, 1, endColor, 0);
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
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		
		//�����������
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
	}
}
