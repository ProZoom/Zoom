package com.bn.view;

import static com.bn.constant.Constant.*;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import android.opengl.GLES20;
import android.view.MotionEvent;

import com.bn.constant.MatrixState3D;
import com.bn.happyhockey.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.object.GameObject;
import com.bn.util.box2d.Box2DUtil;
import com.bn.util.manager.TextureManager;

public class TransitionView extends BNAbstractView{

	public MySurfaceView mv;
	
	public GameObject ball=null;
	public GameObject hongjdz=null;
	public GameObject lanjdz=null;
	
	public World world;
	
	int tableTexId;//桌子
	int skyTexId;//天空盒
	int pillarTexId;//柱子纹理图ID
	
	List<BN2DObject> al=new ArrayList<BN2DObject>();//绘制进球提示对象--0为一个对象  1为一个对象
	
	public ArrayList<Body> body=new ArrayList<Body>();
	
	Vec2 tempLan=new Vec2(0,7f);//蓝球的位置
	Vec2 tempHong=new Vec2(0,-6f);//红球的位置
	public TransitionView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		TextureManager.loadingTexture(mv, 49, 37);//初始化纹理资源
		
		Vec2 gravity = new Vec2(0.0f,0.0f);//设置重力加速度
		world = new World(gravity);//创建世界
		
		skyTexId=TextureManager.getTextures("skybox.png");
		
		Body tempBody=Box2DUtil.createCircle(0.0f, -6f, 0.9f, world, 1.0f, 0.5f, 0f, -1);
		body.add(tempBody);
		hongjdz=new GameObject//电脑控制的球
				(
						mv.hittingtool,
						TextureManager.getTextures("bg_red.png"),
						0.9f*2,
						tempBody
				);
		tempBody=Box2DUtil.createCircle(0.0f, 7.0f, 0.9f, world, 0.05f, 0.5f, 0f, -1);
		body.add(tempBody);
		lanjdz=new GameObject//玩家控制的球
				(
						mv.hittingtool,
						TextureManager.getTextures("bg_blue.png"),
						0.9f*2,
						tempBody
				);
		
		tempBody=Box2DUtil.createCircle(0.0f, 0.0f,0.65f, world, 0.05f, 5f, 0.8f, 0);
		body.add(tempBody);
		ball=new GameObject//球
				(
						mv.puck,
						TextureManager.getTextures("bg_yellow.png"),
						0.65f*2,
						tempBody
				);
		tableTexId=TextureManager.getTextures("game 1.png");
		pillarTexId=TextureManager.getTextures("tablePic1.png");
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{return false;}
	
	@Override
	public void drawView(GL10 gl)
	{
		//调用此方法计算产生透视投影矩阵
		MatrixState3D.setProjectFrustum(-left, right, -top, bottom, near, far);
		//调用此方法产生摄像机9参数位置矩阵
		MatrixState3D.setCamera(cameraX,cameraY,cameraZ,0f,0f,targetZ,upX,upY,upZ);
		
		LookAroundCamera();//环视房间，设置摄像机
		
		MatrixState3D.pushMatrix();
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);//开启深度检测

		MatrixState3D.setLightLocation(0, 100, 0);//设置灯光位置
		
		//绘制天空盒===========================
		MatrixState3D.pushMatrix();
		MatrixState3D.setLightLocation(0, 10, 10);//设置灯光位置
		MatrixState3D.translate(0, -6, 0);
		MatrixState3D.scale(30,30,30);
		mv.sky.drawSelf(skyTexId,0,0.0f);
		MatrixState3D.popMatrix();//恢复现场
		
		MatrixState3D.setLightLocation(0, 100, 0);//设置灯光位置
		//===========绘制桌子============
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.scale(10, 12, 20);
		mv.table.drawSelf(tableTexId,0,0.0f);//桌台
		MatrixState3D.popMatrix();//恢复现场
		
		//===========绘制柱子============
		drawPillar(-4.5f,-1f,9.5f);
		drawPillar(4.5f,-1f,9.5f);
		drawPillar(-4.5f,-1f,-9.5f);
		drawPillar(4.5f,-1f,-9.5f);
		
		//===========绘制实物============
		MatrixState3D.pushMatrix();//保护现场
		drawObject(0,0.15f,0.0f);
		MatrixState3D.popMatrix();//恢复现场
		
		//=============绘制桌子的影子=============
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.setLightLocation(0, 5, 0);//设置灯光位置
		MatrixState3D.scale(10*0.5f, 12*0.5f, 20*0.5f);
		mv.table.drawSelf(tableTexId,1,-5.9f);//桌台
		MatrixState3D.popMatrix();//恢复现场
		
		//===========绘制影子============
		MatrixState3D.pushMatrix();//保护现场3
		drawObject(1,0.15f,0.1f);
		MatrixState3D.popMatrix();//恢复现场
		
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);//关闭深度检测
		MatrixState3D.popMatrix();//恢复现场
	}
	
	public void LookAroundCamera()//环视房间时  摄像机的设置
	{
		//计算当前观察角度下摄像机的位置
		cameraX =(float)Math.sin(degree*3.1415926535898/180)*cameraLimit;
		cameraZ =(float)Math.cos(degree*3.1415926535898/180)*cameraLimit;
		
		tempx=(float)Math.sin(degree*3.1415926535898/180)*tempLimit;
		tempz=(float)Math.cos(degree*3.1415926535898/180)*tempLimit;
		//计算up向量值
		upX=tempx-cameraX;
		upZ=tempz-cameraZ;
		degree+=0.3f;//角度自加
	}
	
	public void drawPillar(float translateX,float translateY,float translateZ)//绘制柱子
	{
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.translate(translateX, translateY, translateZ);//平移
		MatrixState3D.scale(1, 5, 1);//缩放
		mv.pillar.drawSelf(pillarTexId,0,0.0f);//绘制柱子
		MatrixState3D.popMatrix();//恢复现场
	}
	
	public void drawObject(int index,float tlheight,float shadowPosition)//绘制影子
	{//=====index:绘制实物(0)还是影子(1)   lightHeight：灯光的高度     tlheight:平移的高度====
		
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.translate(0, tlheight, 0);
		hongjdz.drawSelf(index,tempHong.x,tempHong.y,shadowPosition);
		MatrixState3D.popMatrix();//恢复现场
		
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.translate(0, tlheight, 0);
		lanjdz.drawSelf(index,tempLan.x,tempLan.y,shadowPosition);
		MatrixState3D.popMatrix();//恢复现场
		
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.translate(0, tlheight, 0);
		ball.drawSelf(index,ball.gt.getPosition().x,ball.gt.getPosition().y,shadowPosition);
		MatrixState3D.popMatrix();//恢复现场
		//===========绘制影子end============
	}
}
