package com.bn.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.constant.MatrixState3D;
import com.bn.happyhockey.MySurfaceView;

import android.opengl.GLES20;

//���غ�����塪����Я��������Ϣ����ɫ���
public class LoadedObjectVertexNormalTexture
{
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int muMMatrixHandle;//λ�á���ת�任����
    int maPositionHandle; //����λ����������  
    int maNormalHandle; //���㷨������������  
    int maLightLocationHandle;//��Դλ����������  
    int maCameraHandle; //�����λ���������� 
    int maTexCoorHandle; //��������������������  
    int muIsShadow;//�Ƿ������Ӱ��������  
    int muProjCameraMatrixHandle;//ͶӰ��������
    int maShadowPosition;//����ͶӰ��λ��
    
    int muSjFactor;//˥����������id
    
    String mVertexShader;//������ɫ������ű�    	 
    String mFragmentShader;//ƬԪ��ɫ������ű�    
	
	FloatBuffer mVertexBuffer;//�����������ݻ���  
	FloatBuffer mNormalBuffer;//���㷨�������ݻ���
	FloatBuffer mTexCoorBuffer;//���������������ݻ���
    int vCount=0;  
    
    boolean initFlag=false;
    public LoadedObjectVertexNormalTexture(MySurfaceView mv,float[] vertices,float[] normals,float texCoors[],int mProgram)
    {    	
    	//��ʼ��������������ɫ����
    	initVertexData(vertices,normals,texCoors);
    	this.mProgram=mProgram;
    }
    //��ʼ��������������ɫ���ݵķ���
    public void initVertexData(float[] vertices,float[] normals,float texCoors[])
    {
    	//�����������ݵĳ�ʼ��================begin============================
    	vCount=vertices.length/3;   
		
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
        
        //���㷨�������ݵĳ�ʼ��================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ���ݵĳ�ʼ��================end============================
        
        //���������������ݵĳ�ʼ��================begin============================  
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoors);//�򻺳����з��붥��������������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }

    //��ʼ��shader
    public void initShader()
    {
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //��ȡ�����������λ������
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //��ȡ�������Ƿ������Ӱ��������
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
        //��ó����л�������Ӱ��λ�õ�����
        maShadowPosition=GLES20.glGetUniformLocation(mProgram, "shadowPosition"); 
        
        //��ȡ������ͶӰ���������Ͼ�������
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
    }
    
    public void initShader(boolean isDamping)
    {
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //��ȡ�����������λ������
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //��ȡ�������Ƿ������Ӱ��������
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow");
        //��ȡ������ͶӰ���������Ͼ�������
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
        //��ȡ������˥����������id
        muSjFactor=GLES20.glGetUniformLocation(mProgram, "sjFactor");
    }
    public void drawSelf(int texId,float sj)
    {   
    	if(!initFlag)
    	{
    		initShader(true);
    		initFlag=true;
    	}
    	//�ƶ�ʹ��ĳ����ɫ������
    	GLES20.glUseProgram(mProgram);
    	MatrixState3D.pushMatrix();
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
    	//��˥�����Ӵ���shader����
    	GLES20.glUniform1f(muSjFactor, sj);
    	//�����ձ任��������ɫ������
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState3D.getFinalMatrix(), 0); 
    	//��λ�á���ת�任��������ɫ������
    	GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState3D.getMMatrix(), 0);   
    	//����Դλ�ô�����ɫ������   
    	GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState3D.lightPositionFB);
    	//�������λ�ô�����ɫ������   
    	GLES20.glUniform3fv(maCameraHandle, 1, MatrixState3D.cameraFB);
        //��ͶӰ���������Ͼ�������ɫ������
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState3D.getViewProjMatrix(), 0);
    	// ������λ�����ݴ�����Ⱦ����
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,   
    			3, 
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mVertexBuffer
    			);       
    	//�����㷨�������ݴ�����Ⱦ����
    	GLES20.glVertexAttribPointer  
    	(
    			maNormalHandle, 
    			3,   
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mNormalBuffer
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
    	//���ö���λ�á���������������������
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maNormalHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
    	//������
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
    	//���Ƽ��ص�����
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND);
    	MatrixState3D.popMatrix();
    }
    public void drawSelf(int texId,int isShadow,float shadowPosition)
    {   
    	
    	if(!initFlag)
    	{
    		initShader();
    		initFlag=true;
    	}
    	//�ƶ�ʹ��ĳ����ɫ������
    	GLES20.glUseProgram(mProgram);
    	MatrixState3D.pushMatrix();
    	GLES20.glEnable(GLES20.GL_BLEND);//�򿪻��
    	//���û������
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	//�����ձ任��������ɫ������
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState3D.getFinalMatrix(), 0); 
    	//��λ�á���ת�任��������ɫ������
    	GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState3D.getMMatrix(), 0);   
    	//����Դλ�ô�����ɫ������   
    	GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState3D.lightPositionFB);
    	//�������λ�ô�����ɫ������   
    	GLES20.glUniform3fv(maCameraHandle, 1, MatrixState3D.cameraFB);
    	 //���Ƿ������Ӱ���Դ�����ɫ������ 
        GLES20.glUniform1i(muIsShadow, isShadow);
        GLES20.glUniform1f(maShadowPosition, shadowPosition);
        
        //��ͶӰ���������Ͼ�������ɫ������
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState3D.getViewProjMatrix(), 0);
    	// ������λ�����ݴ�����Ⱦ����
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,   
    			3, 
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mVertexBuffer
    			);       
    	//�����㷨�������ݴ�����Ⱦ����
    	GLES20.glVertexAttribPointer  
    	(
    			maNormalHandle, 
    			3,   
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mNormalBuffer
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
    	//���ö���λ�á���������������������
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maNormalHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
    	//������
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
    	//���Ƽ��ص�����
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    	//�رջ��
    	GLES20.glDisable(GLES20.GL_BLEND);
    	MatrixState3D.popMatrix();
    }
}
