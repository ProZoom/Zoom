package com.bn.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.bn.constant.MatrixState3D;

public class BN3DObject {
	public FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
	public FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
	
	int mProgram;//自定义渲染管线着色器程序id  
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用 
    int maTexCoorHandle; //顶点纹理坐标属性引用  
    int muSjFactor;//衰减因子引用id
    
	int texId;//纹理图片名
	int vCount;//顶点个数
    boolean initFlag=false;//判断是否初始化着色器
    public BN3DObject(float picWidth,float picHeight,int texId,int programId)
    {
    	this.texId=texId;
		this.mProgram=programId;
		initVertexData(picWidth,picHeight);//初始化顶点数据
    }
    public void initVertexData(float width,float height)//初始化顶点数据
	{
		vCount=4;//顶点个数
		//初始化顶点坐标数据
		float vertices[]=new float[]
		{
				-width/2,height/2,0,
				-width/2,-height/2,0,
				width/2,height/2,0,
				width/2,-height/2,0
		};
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);//创建顶点坐标数据缓冲
		vbb.order(ByteOrder.nativeOrder());//设置字节顺序
		mVertexBuffer=vbb.asFloatBuffer();//转换为Float型缓冲
		mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
		mVertexBuffer.position(0);//设置缓冲区起始位置
		float[] texCoor=new float[12];//初始化纹理坐标数据
		texCoor=new float[]{
				0,0,0,1,1,0,
				1,1,1,0,0,1
				};
		ByteBuffer cbb=ByteBuffer.allocateDirect(texCoor.length*4);//创建顶点纹理坐标数据缓冲
		cbb.order(ByteOrder.nativeOrder());//设置字节顺序
		mTexCoorBuffer=cbb.asFloatBuffer();//转换为Float型缓冲
		mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
		mTexCoorBuffer.position(0);//设置缓冲区起始位置
	}
    public void initShader(boolean isDamping)
    {
    	//获取程序中顶点位置属性引用id  
  		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
  		//获取程序中顶点纹理坐标属性引用id  
  		maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
  		//获取程序中总变换矩阵引用id
  		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
  		//获取程序中衰减因子引用id
        muSjFactor=GLES20.glGetUniformLocation(mProgram, "sjFactor");
  	}
	public void drawSelf(float sj)
    {        
    	if(!initFlag)
    	{
    		//初始化着色器
    		initShader(true);
    		initFlag=true;
    	}
    	//制定使用某套shader程序
    	GLES20.glUseProgram(mProgram);
    	
    	MatrixState3D.pushMatrix();//保护场景
    	GLES20.glEnable(GLES20.GL_BLEND);//打开混合
    	//设置混合因子
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
    	//将衰减因子传入shader程序
    	GLES20.glUniform1f(muSjFactor, sj);
    	
    	//将最终变换矩阵传入shader程序
    	GLES20.glUniformMatrix4fv
    	(
    			muMVPMatrixHandle, 
    			1, 
    			false, 
    			MatrixState3D.getFinalMatrix(), 
    			0
    	); 
    	//为画笔指定顶点位置数据
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,
    			3, 
    			GLES20.GL_FLOAT,
    			false,
    			3*4,
    			mVertexBuffer
    			);
    	//为画笔指定顶点纹理坐标数据
    	GLES20.glVertexAttribPointer
    	(
    			maTexCoorHandle,
    			2,
    			GLES20.GL_FLOAT,
    			false,
    			2*4,
    			mTexCoorBuffer
    			);   
    	//允许顶点位置数据数组
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
    	
    	//绑定纹理
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texId);
    	
    	//绘制纹理矩形--条带法
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount); 
    	//关闭混合
    	GLES20.glDisable(GLES20.GL_BLEND);
    	MatrixState3D.popMatrix();//恢复场景
    }
}
