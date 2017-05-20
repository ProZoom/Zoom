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
	
	int tableTexId;//����
	int skyTexId;//��պ�
	int pillarTexId;//��������ͼID
	
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���ƽ�����ʾ����--0Ϊһ������  1Ϊһ������
	
	public ArrayList<Body> body=new ArrayList<Body>();
	
	Vec2 tempLan=new Vec2(0,7f);//�����λ��
	Vec2 tempHong=new Vec2(0,-6f);//�����λ��
	public TransitionView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		TextureManager.loadingTexture(mv, 49, 37);//��ʼ��������Դ
		
		Vec2 gravity = new Vec2(0.0f,0.0f);//�����������ٶ�
		world = new World(gravity);//��������
		
		skyTexId=TextureManager.getTextures("skybox.png");
		
		Body tempBody=Box2DUtil.createCircle(0.0f, -6f, 0.9f, world, 1.0f, 0.5f, 0f, -1);
		body.add(tempBody);
		hongjdz=new GameObject//���Կ��Ƶ���
				(
						mv.hittingtool,
						TextureManager.getTextures("bg_red.png"),
						0.9f*2,
						tempBody
				);
		tempBody=Box2DUtil.createCircle(0.0f, 7.0f, 0.9f, world, 0.05f, 0.5f, 0f, -1);
		body.add(tempBody);
		lanjdz=new GameObject//��ҿ��Ƶ���
				(
						mv.hittingtool,
						TextureManager.getTextures("bg_blue.png"),
						0.9f*2,
						tempBody
				);
		
		tempBody=Box2DUtil.createCircle(0.0f, 0.0f,0.65f, world, 0.05f, 5f, 0.8f, 0);
		body.add(tempBody);
		ball=new GameObject//��
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
		//���ô˷����������͸��ͶӰ����
		MatrixState3D.setProjectFrustum(-left, right, -top, bottom, near, far);
		//���ô˷������������9����λ�þ���
		MatrixState3D.setCamera(cameraX,cameraY,cameraZ,0f,0f,targetZ,upX,upY,upZ);
		
		LookAroundCamera();//���ӷ��䣬���������
		
		MatrixState3D.pushMatrix();
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);//������ȼ��

		MatrixState3D.setLightLocation(0, 100, 0);//���õƹ�λ��
		
		//������պ�===========================
		MatrixState3D.pushMatrix();
		MatrixState3D.setLightLocation(0, 10, 10);//���õƹ�λ��
		MatrixState3D.translate(0, -6, 0);
		MatrixState3D.scale(30,30,30);
		mv.sky.drawSelf(skyTexId,0,0.0f);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		MatrixState3D.setLightLocation(0, 100, 0);//���õƹ�λ��
		//===========��������============
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.scale(10, 12, 20);
		mv.table.drawSelf(tableTexId,0,0.0f);//��̨
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		//===========��������============
		drawPillar(-4.5f,-1f,9.5f);
		drawPillar(4.5f,-1f,9.5f);
		drawPillar(-4.5f,-1f,-9.5f);
		drawPillar(4.5f,-1f,-9.5f);
		
		//===========����ʵ��============
		MatrixState3D.pushMatrix();//�����ֳ�
		drawObject(0,0.15f,0.0f);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		//=============�������ӵ�Ӱ��=============
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.setLightLocation(0, 5, 0);//���õƹ�λ��
		MatrixState3D.scale(10*0.5f, 12*0.5f, 20*0.5f);
		mv.table.drawSelf(tableTexId,1,-5.9f);//��̨
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		//===========����Ӱ��============
		MatrixState3D.pushMatrix();//�����ֳ�3
		drawObject(1,0.15f,0.1f);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);//�ر���ȼ��
		MatrixState3D.popMatrix();//�ָ��ֳ�
	}
	
	public void LookAroundCamera()//���ӷ���ʱ  �����������
	{
		//���㵱ǰ�۲�Ƕ����������λ��
		cameraX =(float)Math.sin(degree*3.1415926535898/180)*cameraLimit;
		cameraZ =(float)Math.cos(degree*3.1415926535898/180)*cameraLimit;
		
		tempx=(float)Math.sin(degree*3.1415926535898/180)*tempLimit;
		tempz=(float)Math.cos(degree*3.1415926535898/180)*tempLimit;
		//����up����ֵ
		upX=tempx-cameraX;
		upZ=tempz-cameraZ;
		degree+=0.3f;//�Ƕ��Լ�
	}
	
	public void drawPillar(float translateX,float translateY,float translateZ)//��������
	{
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.translate(translateX, translateY, translateZ);//ƽ��
		MatrixState3D.scale(1, 5, 1);//����
		mv.pillar.drawSelf(pillarTexId,0,0.0f);//��������
		MatrixState3D.popMatrix();//�ָ��ֳ�
	}
	
	public void drawObject(int index,float tlheight,float shadowPosition)//����Ӱ��
	{//=====index:����ʵ��(0)����Ӱ��(1)   lightHeight���ƹ�ĸ߶�     tlheight:ƽ�Ƶĸ߶�====
		
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.translate(0, tlheight, 0);
		hongjdz.drawSelf(index,tempHong.x,tempHong.y,shadowPosition);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.translate(0, tlheight, 0);
		lanjdz.drawSelf(index,tempLan.x,tempLan.y,shadowPosition);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.translate(0, tlheight, 0);
		ball.drawSelf(index,ball.gt.getPosition().x,ball.gt.getPosition().y,shadowPosition);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		//===========����Ӱ��end============
	}
}
