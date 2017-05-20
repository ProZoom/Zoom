package com.bn.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.constant.MatrixState3D;
import com.bn.happyhockey.MySurfaceView;

import android.opengl.GLES20;

//加载后的物体——仅携带顶点信息，颜色随机
public class LoadedObjectVertexNormalTexture
{
	int mProgram;//自定义渲染管线着色器程序id  
    int muMVPMatrixHandle;//总变换矩阵引用
    int muMMatrixHandle;//位置、旋转变换矩阵
    int maPositionHandle; //顶点位置属性引用  
    int maNormalHandle; //顶点法向量属性引用  
    int maLightLocationHandle;//光源位置属性引用  
    int maCameraHandle; //摄像机位置属性引用 
    int maTexCoorHandle; //顶点纹理坐标属性引用  
    int muIsShadow;//是否绘制阴影属性引用  
    int muProjCameraMatrixHandle;//投影矩阵引用
    int maShadowPosition;//物体投影的位置
    
    int muSjFactor;//衰减因子引用id
    
    String mVertexShader;//顶点着色器代码脚本    	 
    String mFragmentShader;//片元着色器代码脚本    
	
	FloatBuffer mVertexBuffer;//顶点坐标数据缓冲  
	FloatBuffer mNormalBuffer;//顶点法向量数据缓冲
	FloatBuffer mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount=0;  
    
    boolean initFlag=false;
    public LoadedObjectVertexNormalTexture(MySurfaceView mv,float[] vertices,float[] normals,float texCoors[],int mProgram)
    {    	
    	//初始化顶点坐标与着色数据
    	initVertexData(vertices,normals,texCoors);
    	this.mProgram=mProgram;
    }
    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices,float[] normals,float texCoors[])
    {
    	//顶点坐标数据的初始化================begin============================
    	vCount=vertices.length/3;   
		
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
        
        //顶点法向量数据的初始化================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mNormalBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        mNormalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================
        
        //顶点纹理坐标数据的初始化================begin============================  
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoors);//向缓冲区中放入顶点纹理坐标数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================
    }

    //初始化shader
    public void initShader()
    {
        //获取程序中顶点位置属性引用  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //获取程序中光源位置引用
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中顶点纹理坐标属性引用  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //获取程序中摄像机位置引用
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //获取程序中是否绘制阴影属性引用
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
        //获得程序中绘制物体影子位置的引用
        maShadowPosition=GLES20.glGetUniformLocation(mProgram, "shadowPosition"); 
        
        //获取程序中投影、摄像机组合矩阵引用
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
    }
    
    public void initShader(boolean isDamping)
    {
        //获取程序中顶点位置属性引用  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //获取程序中光源位置引用
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中顶点纹理坐标属性引用  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor"); 
        //获取程序中摄像机位置引用
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //获取程序中是否绘制阴影属性引用
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow");
        //获取程序中投影、摄像机组合矩阵引用
        muProjCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjCameraMatrix"); 
        //获取程序中衰减因子引用id
        muSjFactor=GLES20.glGetUniformLocation(mProgram, "sjFactor");
    }
    public void drawSelf(int texId,float sj)
    {   
    	if(!initFlag)
    	{
    		initShader(true);
    		initFlag=true;
    	}
    	//制定使用某套着色器程序
    	GLES20.glUseProgram(mProgram);
    	MatrixState3D.pushMatrix();
    	GLES20.glEnable(GLES20.GL_BLEND);//打开混合
    	//设置混合因子
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
    	//将衰减因子传入shader程序
    	GLES20.glUniform1f(muSjFactor, sj);
    	//将最终变换矩阵传入着色器程序
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState3D.getFinalMatrix(), 0); 
    	//将位置、旋转变换矩阵传入着色器程序
    	GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState3D.getMMatrix(), 0);   
    	//将光源位置传入着色器程序   
    	GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState3D.lightPositionFB);
    	//将摄像机位置传入着色器程序   
    	GLES20.glUniform3fv(maCameraHandle, 1, MatrixState3D.cameraFB);
        //将投影、摄像机组合矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState3D.getViewProjMatrix(), 0);
    	// 将顶点位置数据传入渲染管线
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,   
    			3, 
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mVertexBuffer
    			);       
    	//将顶点法向量数据传入渲染管线
    	GLES20.glVertexAttribPointer  
    	(
    			maNormalHandle, 
    			3,   
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mNormalBuffer
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
    	//启用顶点位置、法向量、纹理坐标数据
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maNormalHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
    	//绑定纹理
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
    	//绘制加载的物体
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    	//关闭混合
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
    	//制定使用某套着色器程序
    	GLES20.glUseProgram(mProgram);
    	MatrixState3D.pushMatrix();
    	GLES20.glEnable(GLES20.GL_BLEND);//打开混合
    	//设置混合因子
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	//将最终变换矩阵传入着色器程序
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState3D.getFinalMatrix(), 0); 
    	//将位置、旋转变换矩阵传入着色器程序
    	GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState3D.getMMatrix(), 0);   
    	//将光源位置传入着色器程序   
    	GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState3D.lightPositionFB);
    	//将摄像机位置传入着色器程序   
    	GLES20.glUniform3fv(maCameraHandle, 1, MatrixState3D.cameraFB);
    	 //将是否绘制阴影属性传入着色器程序 
        GLES20.glUniform1i(muIsShadow, isShadow);
        GLES20.glUniform1f(maShadowPosition, shadowPosition);
        
        //将投影、摄像机组合矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muProjCameraMatrixHandle, 1, false, MatrixState3D.getViewProjMatrix(), 0);
    	// 将顶点位置数据传入渲染管线
    	GLES20.glVertexAttribPointer  
    	(
    			maPositionHandle,   
    			3, 
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mVertexBuffer
    			);       
    	//将顶点法向量数据传入渲染管线
    	GLES20.glVertexAttribPointer  
    	(
    			maNormalHandle, 
    			3,   
    			GLES20.GL_FLOAT, 
    			false,
    			3*4,   
    			mNormalBuffer
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
    	//启用顶点位置、法向量、纹理坐标数据
    	GLES20.glEnableVertexAttribArray(maPositionHandle);  
    	GLES20.glEnableVertexAttribArray(maNormalHandle);  
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle); 
    	//绑定纹理
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
    	//绘制加载的物体
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    	//关闭混合
    	GLES20.glDisable(GLES20.GL_BLEND);
    	MatrixState3D.popMatrix();
    }
}
