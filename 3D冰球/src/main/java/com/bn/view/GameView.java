package com.bn.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import com.bn.constant.ChangeUtil;
import com.bn.constant.Constant;
import com.bn.constant.CrashUtil;
import com.bn.constant.GetPositionUtil;
import com.bn.constant.MatrixState2D;
import com.bn.constant.MatrixState3D;
import com.bn.constant.MyHHData;
import com.bn.constant.SnowUtil;
import com.bn.happyhockey.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.object.BN3DObject;
import com.bn.object.GameObject;
import com.bn.util.action.ActionDown;
import com.bn.util.action.ActionGameWin;
import com.bn.util.action.ActionMove;
import com.bn.util.action.ActionUp;
import com.bn.util.action.MyAction;
import com.bn.util.box2d.Box2DUtil;
import com.bn.util.box2d.MyContactListener;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.thread.BallGoThread;
import com.bn.util.thread.PhysicalThread;

import android.opengl.GLES20;
import android.view.MotionEvent;
import static com.bn.constant.Constant.*;

public class GameView extends BNAbstractView
{
	public MySurfaceView mv;
	
	public GameObject ball=null;
	public GameObject hongjdz=null;
	public GameObject lanjdz=null;
	
	public World world;
	
	int tableTexId;//����
	int lineTexId;//����
	int skyTexId;//��պ�
	int pillarTexId;//��������ͼID
	
	public static String player2Color="bg_red.png";//Ĭ��ѡ�еĵ��Ա�����ɫ
	public static String puckColor="bg_yellow.png";//Ĭ��ѡ�е�����ɫ
	public static String player1Color="bg_blue.png";//Ĭ��ѡ�е���ұ�����ɫ
	public static String tableColor="game 1.png";//Ĭ��ѡ�е���̨��ɫ
	public static String pillarColor="tablePic1.png";//Ĭ��ѡ�е���̨��ɫ
	public static int[] colorIndex=new int[]{0,1,2};//0--�����ɫ  1--��ҵ���ɫ  2--ϵͳ����ɫ
	
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���ƽ�����ʾ����--0Ϊһ������  1Ϊһ������
	public List<Body> boxBody=new ArrayList<Body>();
	List<BN2DObject> winView=new ArrayList<BN2DObject>();//ʤ����������Ƶ�Ԫ��
	
	BN2DObject[] Pause=new BN2DObject[2];//��ͣ��ť
	BN3DObject bnRound;//��Ȧ���ƶ���
	
	public int winCount=0;
	public static int Status=0;//�ж�Ϊ����ģʽ0--����ģʽ   1--��ʱģʽ
	
	public static boolean player_win=false;//���Ӯ
	public static boolean computer_win=false;//����Ӯ
	public boolean isPlayerScoreAdd=true;//����Ҽ�һ��
	public boolean isComputerScoreAdd=true;//�����Լ�һ��
	
	public boolean isTouch=false;//�Ƿ����������ؽ�
	public boolean Start_Game=false;//��ʼ��Ϸ
	public boolean GameOver=false;//��Ϸ�Ƿ����
	public boolean CWin=false;//��һ�ֵ���Ӯ
	public boolean isMove=true;//����Ƿ����ƶ�
	
	float roundScaleCount=1;//��Ҫ���ŵı���
	float roundDampingCount=1;//˥������
	public static boolean drawRound=false;//�Ƿ���ƹ�Ȧ
	public static boolean drawRoundOK=false;//�Ƿ���ƹ�Ȧ
	
	public static boolean drawLightning=false;//�Ƿ���ƹ�Ȧ
	public static boolean moveY=false;//���ƹ�Ȧʱ z�����Ƿ���Ҫ�ƶ�
	ArrayList<float[]> roundDrawList=new ArrayList<float[]>();//��Ҫ���ƹ�Ȧ���б�
	ArrayList<float[]> roundDeleteList=new ArrayList<float[]>();//��Ҫɾ����Ȧ���б�
	ArrayList<float[]> lightningDrawList=new ArrayList<float[]>();//��Ҫ���ƹ�Ȧ���б�
	ArrayList<float[]> lightningDeleteList=new ArrayList<float[]>();//��Ҫɾ����Ȧ���б�
	
	public ArrayList<Body> body=new ArrayList<Body>();
	
	public boolean isSnow=false;//�Ƿ����ѩ�� 
	public int index=0;//0--���ư�ɫ  1--��ɫ  2--��ɫ  3--��ɫ
	
	public boolean switchView=false;//�Ƿ��л��ӽ�
	
	public SnowUtil su;//����ѩ��������                                                                                                                                                        
	public ChangeUtil changeUtil;//������ʱ�������
	public CrashUtil cu;
	
	public PhysicalThread pt;//�����߳���
	public BallGoThread bt;
	
	//��������ʱ��ƽ�ơ���ת�����ű���ֵ=====================
	float translateX=0;
	float translateZ=0;
	float scaleX=0;
	float scaleZ=0;
	float angle=0;
	boolean drawRotateLine=false;
	boolean drawTranslateLine=false;
	float scaleLineCount=1;
	
	Vec2 tempLan=new Vec2(0,7f);//�����λ��
	Vec2 tempHong=new Vec2(0,-6f);//�����λ��
	Vec2 tempBall=new Vec2(0,1.5f);//�����λ��
	public Queue<MyAction> doActionQueue=new LinkedList<MyAction>();//��������
	public Queue<float[][]> positionQueue=new LinkedList<float[][]>();//���ݶ���
	public Object lock=new Object();
	public Object lockA=new Object();
	
	int rotateCount=0;//�Ƿ���ת
	int threadCount=0;//��ʼ��Ϸʱ �����߳� ֻ��һ��
	public boolean isPause=false;//�Ƿ���ͣ
	PauseView pv;//��ͣ����
	GameOverView gov;//��Ϸ��������
	public int changeCount=0;
	public int downCount=0;
	BN2DObject eyeView;//�л��ӽǵİ�ť
	public long winTime=0;//�������ͣʱ��
	public long pauseTime=0;//������ͣ��ťʱ��
	
	int cameraCount=1;//�ƶ�������ļ�����
	float playerY1=0;//��һ������
	float playerY2=0;//�ڶ�������
	boolean resetCamera=false;
	
	public GameView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	public void initPhy()//��ʼ������
	{
		Body tempBody=Box2DUtil.createCircle(0.0f, -6f, 0.9f, world, 0.05f, 0f, 0.5f, -1);
		body.add(tempBody);
		hongjdz=new GameObject//���Կ��Ƶ���
				(
						mv.hittingtool,
						TextureManager.getTextures(player2Color),
						0.9f*2,
						tempBody
				);
		hongjdz.gt.setLinearVelocity(new Vec2(0,0));
		tempBody=Box2DUtil.createCircle(0.0f, 7.0f, 0.9f, world, 0.05f, 0f, 0.5f, -1);
		body.add(tempBody);
		lanjdz=new GameObject//��ҿ��Ƶ���
				(
						mv.hittingtool,
						TextureManager.getTextures(player1Color),
						0.9f*2,
						tempBody
				);
		lanjdz.gt.setLinearVelocity(new Vec2(0,0));
		
		tempBody=Box2DUtil.createCircle(0.0f, 0.0f,0.6f, world, 0.05f, 0f, 0.8f, 0);
		body.add(tempBody);
		ball=new GameObject//��
				(
						mv.puck,
						TextureManager.getTextures(puckColor),
						0.6f*2,
						tempBody
				);
		ball.gt.setLinearVelocity(new Vec2(0,0));
		tableTexId=TextureManager.getTextures(tableColor);
		pillarTexId=TextureManager.getTextures(pillarColor);
	}
	@Override
	public void initView()
	{
		Vec2 gravity = new Vec2(0.0f,0.0f);//�����������ٶ�
		world = new World(gravity);//��������
		MyContactListener mcl=new MyContactListener(GameView.this);
		world.setContactListener(mcl);
		
		lineTexId=TextureManager.getTextures("light.png");
		skyTexId=TextureManager.getTextures("skybox.png");
		
		bnRound=new BN3DObject(1,1,TextureManager.getTextures("round.png"),ShaderManager.getShader(3));
		
		Pause[1]=new BN2DObject(520, 100, 120, 120, TextureManager.getTextures("pause 1.png"), 
				ShaderManager.getShader(0));//��ʱ��ͣ��ť
		Pause[0]=new BN2DObject(730, 100, 150, 150, TextureManager.getTextures("pause 1.png"), 
				ShaderManager.getShader(0));//����ģʽ��ͣ��ť
		
		MyHHData.createEdage(world);//�����߿�
		boxBody.add(Box2DUtil.createBox(0.0f,9.3f, world, 1.6f, 0.5f, true, 0));//��ҿ���ʤ������
		boxBody.add(Box2DUtil.createBox(0.0f,-9.3f, world, 1.6f, 0.5f, true, 0));//���Կ���ʤ������
		
		al.add(new BN2DObject(
				540,1100,500,200,
				TextureManager.getTextures("computerwin.png"),
				ShaderManager.getShader(0)
				));
		al.add(new BN2DObject(
				540,800,500,200,
				TextureManager.getTextures("computerwin.png"),
				ShaderManager.getShader(0)
				));
		al.add(new BN2DObject(
				540,1000,400,180,
				TextureManager.getTextures("start 1.png"),
				ShaderManager.getShader(0)
				));
		al.add(new BN2DObject(
				100,280,100,120,
				TextureManager.getTextures("screenshot1.png"),
				ShaderManager.getShader(0)
				));
		changeUtil=new ChangeUtil(this);//�����ı������ʱ�Ķ���
		eyeView=new BN2DObject(
				100,100,135,135,
				TextureManager.getTextures("eye.png"),
				ShaderManager.getShader(0)
				);//�л��ӽǵİ�ť
		//===========ʤ�������Ԫ��start==================
		winView=MyHHData.winView();
		//===========ʤ�������Ԫ��end==================
		pv=new PauseView(this);//��ͣ����
		cu=new CrashUtil(this);
		gov=new GameOverView(this);//��Ϸ��������
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if(isMove&&!isPause&&!GameOver)
		{
			float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
			float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
			switch (e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				if(!Start_Game&&x>=StartGame_Left&&x<=StartGame_Right&&y>=StartGame_Top
				&&y<=StartGame_Buttom&&threadCount==0)
				{
					pt=new PhysicalThread(this);
					pt.start();//�����߳̿�ʼ
					bt=new BallGoThread(this);
					bt.ballGO=true;//�����ߵ��߳̿���
					bt.start();
					su=new SnowUtil(this);//����������Ч���ƹ��������
					su.initSnow(index,ball);//��ʼ��ѩ��
					threadCount=1;
				}
				if(!Start_Game&&x>=StartGame_Left&&x<=StartGame_Right&&
						y>=StartGame_Top&&y<=StartGame_Buttom)
				{
					Start_Game=true;
					ball.gt.setTransform(new Vec2(0.0f, 1.5f), 0);
					changeUtil.startTime=System.currentTimeMillis();//��ʼʱ��
					resetCamera(downCount);
					x=0;y=0;
				}
				if(mv.screenShot.isAllowed&&!mv.screenShot.saveFlag&&Start_Game&&x>=ScreenShot_Left&&x<=ScreenShot_Right&&y>=ScreenShot_Top
						&&y<=ScreenShot_Buttom)
				{//����
					mv.screenShot.setFlag(true);
				}
				float[] result=GetPositionUtil.limitPositionResult(x,y);
				Vec2 locationWorld0=new Vec2(result[0],result[1]); //��������ص��ڱ�������ϵ�ϵ�����
				if(Start_Game&&lanjdz.gt.getFixtureList().testPoint(locationWorld0))
				{
					isTouch=true;//���������ؽ�
					MyAction ac=new ActionDown(1+"",true,lanjdz.gt,lanjdz.gt,locationWorld0,
							2200.0f*lanjdz.gt.getMass(),500.0f,0.0f,0);
					synchronized(lock)
					{
						doActionQueue.offer(ac);
					}
				}
				if(Start_Game&&x>=ChangeEye_Left&&x<=ChangeEye_Right&&y>=ChangeEye_Top&&y<=ChangeEye_Bottom)//����л��ӽ�
				{
					switchView=true;
					resetCamera=true;
					downCount++;
					if(downCount%2==1)//����
					{
						cameraCount=1;
						eyeView=new BN2DObject(
								100,100,135,135,
								TextureManager.getTextures("eye1.png"),
								ShaderManager.getShader(0)
								);//�л��ӽǵİ�ť
					}else
					{
						eyeView=new BN2DObject(
								100,100,135,135,
								TextureManager.getTextures("eye.png"),
								ShaderManager.getShader(0)
								);//�л��ӽǵİ�ť
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float mx=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				float my=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(Start_Game&&isTouch)
				{
					result=GetPositionUtil.limitPositionResult(mx,my);//�������
					Vec2 locationWorld1=new Vec2(result[0],result[1]);//��������ص��ڱ�������ϵ�ϵ�����
					MyAction ac=new ActionMove(locationWorld1,1);
					synchronized(lock)
					{
						doActionQueue.offer(ac);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				isTouch=false;//������Ҫ���ؽ�
				MyAction ac=new ActionUp(2);
				synchronized(lock)
				{
					doActionQueue.offer(ac);
				}
				switch(Status)
				{
				case 1:if(x>PauseTimer_Left&&x<PauseTimer_Right&&
						y>PauseTimer_Top&&y<PauseTimer_Bottom&&Start_Game&&winCount==0)
				{
					pauseTime=System.currentTimeMillis();
					isPause=true;
				}
				default :if(x>PauseClassical_Left&&x<PauseClassical_Right&&
						y>PauseClassical_Top&&y<PauseClassical_Bottom&&Start_Game&&winCount==0)
				{
					pauseTime=System.currentTimeMillis();
					isPause=true;
				}
				}
				break;
			}
		}else if(isPause)
		{
			return pv.onTouchEvent(e);
		}else if(GameOver)
		{
			return gov.onTouchEvent(e);
		}
		return true;
	}
	
	@Override
	public void drawView(GL10 gl)
	{
		//���ô˷����������͸��ͶӰ����
		MatrixState3D.setProjectFrustum(-left, right, -top, bottom, near, far);
		//���ô˷������������9����λ�þ���
		MatrixState3D.setCamera(cameraX,cameraY,cameraZ,0f,0f,targetZ,upX,upY,upZ);
		MoveCamera();//�ƶ������
		LookAroundCamera();//���ӷ��䣬���������
		ChangeViewCamera();//ת���Ƕ�
		
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
		
		updateData();//��������
		
		//===========����Ӱ��============
		MatrixState3D.pushMatrix();//�����ֳ�3
		drawObject(1,0.15f,0.1f);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		//===============�ж�����������Ƿ���ײ==============
		if(Start_Game&&!computer_win&&!player_win)
		{
			if(cu.judgeIfTouch(tempLan,tempBall))
			{
				cu.lanApplyBall(tempBall,tempLan);//������һ������
				cu.doCrashAction(1,"puckBeaterSound.mp3");//����������Ч�ȡ���
			}
		}
		//===========��������=============
		drawLine();
		if(drawTranslateLine)
		{
			drawTranslateLine(translateX,translateZ,scaleX,scaleZ);
		}
		if(drawRotateLine)
		{
			drawRotateLine(translateX,translateZ,scaleX,scaleZ,angle);
		}
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);//�ر���ȼ��
		//===========���ƹ�Ȧ=============
		drawRound();
		//===========����ѩ��=============
		if(Start_Game)
		{
			su.drawSnow(index);
		}
		MatrixState3D.popMatrix();//�ָ��ֳ�
		
		draw2DImage();//����2DԪ
		drawWinView();//������Ϸ��������
		if(Start_Game)
		{
			JudgeGameWin();//�ж���Ϸ�Ƿ�ʤ��
		}
		drawPauseView();//������ͣ����
	}
	public void MoveCamera()//�ƶ������
	{
		if(downCount%2==0)//б��ʱ�����ƶ������
		{
			if(cameraCount%3==1)
			{
				playerY1=lanjdz.gt.getPosition().y;
				cameraCount++;
			}else if(cameraCount%3==2)
			{
				playerY2=lanjdz.gt.getPosition().y;
				cameraCount++;
			}else if(cameraCount%3==0)
			{
				if(downCount%2==0)
				{
					float distance=playerY1-playerY2;
					cameraZ-=distance;//���������z����ֵ
					targetZ-=distance;//���������Ŀ���zֵ
					cameraCount++;
				}
			}
		}
	}
	public void LookAroundCamera()//���ӷ���ʱ  �����������
	{
		if(Start_Game&&rotateCount==0)
		{
			rotateCount=1;
		}
		if(rotateCount==0)
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
		}else if(rotateCount==1)//��ʼ��Ϸ������
		{
			resetCamera(downCount);
			rotateCount++;
		}
	}
	public void ChangeViewCamera()//ת���Ƕ�ʱ�����������
	{
		if(resetCamera)
		{
			if(downCount%2==1)
			{
				resetCamera(0);
			}else
			{
				resetCamera(1);
			}
			resetCamera=false;
		}
		if(switchView)//����ת���ӽ�
		{
			if(downCount%2==1)//����
			{
				if(changeCount<20)
				{
					cameraY+=1.9f;
					cameraZ-=3.15f;
					upY-=0.02775f;
					upZ-=0.008375f;
					changeCount++;
				}else
				{
					changeCount=0;
					switchView=false;
				}
			}else//б��
			{
				if(changeCount<20)
				{
					cameraY-=1.9f;
					cameraZ+=3.15f;
					upY+=0.02775f;
					upZ+=0.008375f;
					changeCount++;
				}else
				{
					changeCount=0;
					switchView=false;
				}
			}
		}
	}
	public void draw2DImage()//����2DԪ��
	{
		changeUtil.changeGrade();//���Ʒ���
		eyeView.drawSelf(0);//�����л��ӽǵİ�ť
		if(Status==0)
		{
			MatrixState2D.pushMatrix();//�����ֳ�
			if(!Start_Game)
			{
				al.get(2).drawSelf(0);
			}else
			{
				al.get(3).drawSelf(0);
			}
			changeUtil.currGrade.get(0).drawSelf(480, 100);//��ҷ���
			changeUtil.currGrade.get(1).drawSelf(580, 100);
			
			changeUtil.currGrade.get(2).drawSelf(880, 100);//���Է���
			changeUtil.currGrade.get(3).drawSelf(980, 100);
			Pause[0].drawSelf(0);//������ͣ��ť
			MatrixState2D.popMatrix();//�ָ��ֳ�
		}else
		{
			changeUtil.changeTimer();//����ʱģʽ
			
			MatrixState2D.pushMatrix();//�����ֳ�
			if(!Start_Game)
			{
				al.get(2).drawSelf(0);
			}else
			{
				al.get(3).drawSelf(0);
			}
			changeUtil.TimerBackGround.drawSelf(0);//���Ƽ�ʱ����ͼ
			if(changeUtil.currTimer.size()>0)
			{
				changeUtil.currTimer.get(0).drawSelf(850, 100);//��ҷ���
				changeUtil.currTimer.get(1).drawSelf(930, 100);
			}
			changeUtil.currGrade.get(0).drawSelf(330, 100);//��ҷ���
			changeUtil.currGrade.get(1).drawSelf(410, 100);
			
			changeUtil.currGrade.get(2).drawSelf(630, 100);//���Է���
			changeUtil.currGrade.get(3).drawSelf(710,100);
			Pause[1].drawSelf(0);//������ͣ��ť
			MatrixState2D.popMatrix();//�ָ��ֳ�
		}
	}
	public void drawPillar(float translateX,float translateY,float translateZ)//��������
	{
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.translate(translateX, translateY, translateZ);//ƽ��
		MatrixState3D.scale(1, 5, 1);//����
		mv.pillar.drawSelf(pillarTexId,0,0.0f);//��������
		MatrixState3D.popMatrix();//�ָ��ֳ�
	}
	public void updateData()//��������
	{
		synchronized(lockA)
		{
			while(positionQueue.size()>0)//������еĳ��ȴ���0
			{
				float[][] result=positionQueue.poll();
				tempLan=new Vec2(result[0][0],result[0][1]);
				tempHong=new Vec2(result[1][0],result[1][1]);
				tempBall=new Vec2(result[2][0],result[2][1]);
			}
		}
	}
	public void drawLine()//��������
	{
		float currentBallX=ball.gt.getPosition().x;//��¼���xλ��
		float currentBallY=ball.gt.getPosition().y;//��¼���yλ��
		//����
		if(currentBallY<=-7.8f)
		{
			judgeDirection(currentBallX,1);
		}
		//�ײ�
		else if(currentBallY>=7.8f)
		{
			judgeDirection(currentBallX,4);
	    }
		//�Ϸ�
		if(currentBallY<0&&currentBallY>=-7.95f)
		{
			judgeDirection(currentBallX,2);
		}
		//�·�
		else if(currentBallY>0&&currentBallY<=7.95f)
		{
			judgeDirection(currentBallX,3);
		}
	}
	//���ƿ�����ת������
	public void drawRotateLine(float translateX,float translateZ,float scaleX,float scaleZ,float angle)
	{
		if(scaleLineCount>=0)
		{
			MatrixState3D.pushMatrix();//�����ֳ�
			MatrixState3D.translate(translateX,0f,translateZ);//ƽ��
			MatrixState3D.scale(scaleX,1.2f,scaleZ);//����
			MatrixState3D.rotate(angle, 0, 1, 0);//��ת
			mv.line.drawSelf(lineTexId,scaleLineCount);//����
			MatrixState3D.popMatrix();//�ָ��ֳ�
			scaleLineCount-=0.05f;
		}else
		{
			scaleLineCount=1;
			drawRotateLine=false;
		}
	}
	//���Ʋ���ת������
	public void drawTranslateLine(float translateX,float translateZ,float scaleX,float scaleZ)
	{
		if(scaleLineCount>=0)
		{
			MatrixState3D.pushMatrix();//�����ֳ�
			MatrixState3D.translate(translateX,0f,translateZ);//ƽ��
			MatrixState3D.scale(scaleX,1.2f,scaleZ);//����
			mv.line.drawSelf(lineTexId,scaleLineCount);//����
			MatrixState3D.popMatrix();//�ָ��ֳ�
			scaleLineCount-=0.05f;
		}else
		{
			scaleLineCount=1;
			drawTranslateLine=false;
		}
	}
	//�жϻ������ߵľ���λ��
	public void judgeDirection(float currentBallX,int index)
	{
		if(currentBallX<=-3.5f)//��
		{
			if(index==2)//���ϱ�
			{
				translateX=-4.6f;
				translateZ=-4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=90;
				drawRotateLine=true;
			}
			else if(index==3)//���±�
			{
				translateX=-4.6f;
				translateZ=4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=90;
				drawRotateLine=true;
			}
		}
		else if(currentBallX>=3.5f)//��
		{
			if(index==2)//���ϱ�
			{
				translateX=4.6f;
				translateZ=-4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=-90;
				drawRotateLine=true;
			}
			else if(index==3)//���±�
			{
				translateX=4.6f;
				translateZ=4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=-90;
				drawRotateLine=true;
			}
		}
		if(currentBallX<=-1.92f)//���
		{
			if(index==1)//���ϱ�--left
			{
				translateX=-3.17f;
				translateZ=-8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				drawTranslateLine=true;
			}
			else if(index==4)//���±�--left
			{
				translateX=-3.17f;
				translateZ=8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				angle=180;
				drawRotateLine=true;
			}
		}
		else if(currentBallX>=1.92f)//�ұ�
		{
			if(index==1)//���ϱ�--right
			{
				translateX=3.17f;
				translateZ=-8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				drawTranslateLine=true;
			}
			else if(index==4)//���±�--right
			{
				translateX=3.17f;
				translateZ=8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				angle=180;
				drawRotateLine=true;
			}
		}
	}
	//���ƹ�Ȧ�ķ���
	public void drawRound()
	{
		if(drawRound)//������Ȧ
		{
			float currentBallX=ball.gt.getPosition().x;//��¼���xλ��
			float currentBallY=ball.gt.getPosition().y;//��¼���yλ��
			if(currentBallX<=0)
			{
				currentBallX-=1.2f;
			}else
			{
				currentBallX+=1.2f;
			}
			if(moveY)
			{
				if(currentBallY<=0)
				{
					currentBallY-=1.2f;
				}else
				{
					currentBallY+=2.5f;
				}
				moveY=false;
			}
			float[] result=new float[2];
			result[0]=currentBallX;
			result[1]=currentBallY;
			roundDrawList.add(result);//��x��y������ӽ�ArrayList
			
			drawRound=false;
		}
		if(drawRoundOK)
		{
			for(float[] result:roundDrawList)//����ArrayList
			{
				if(roundScaleCount<=2)
				{
					MatrixState3D.pushMatrix();//�����ֳ�
					MatrixState3D.translate(result[0], 1.5f, result[1]);//ƽ��
					if(downCount%2==1)//���ӽǵ��л��ı�
			    	{
			    		MatrixState3D.rotate(-120, 1, 0,0);
			    	}
					MatrixState3D.scale(roundScaleCount, roundScaleCount, roundScaleCount);//����
					bnRound.drawSelf(roundDampingCount);//����
					MatrixState3D.popMatrix();//�ָ��ֳ�
					roundDampingCount-=0.05f;
					roundScaleCount+=0.05f;
				}else
				{
					roundScaleCount=1;//���ű�����Ϊ1
					roundDampingCount=1;//˥������Ϊ1
					roundDeleteList.add(result);
				}
			}
			for(float[] result:roundDeleteList)
			{
				roundDrawList.remove(result);//��������Ĺ�Ȧɾ��
			}
		}
	}
	public void drawPauseView()//������ͣ����
	{
		if(isPause)
		{
			pv.drawView();
		}
	}
	public void drawWinView()//������Ϸ��������
	{
		gov.isGameOver();//�ж���Ϸ�Ƿ����
		if(GameOver)
		{
			gov.drawView();
		}
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
		ball.drawSelf(index,tempBall.x,tempBall.y,shadowPosition);
		MatrixState3D.popMatrix();//�ָ��ֳ�
		//===========����Ӱ��end============
	}
	public void JudgeGameWin()//�ж���Ϸ�Ƿ�ʤ��
	{
		if(player_win)//���Ӯ
		{
			if(isPlayerScoreAdd)//�ӷ�
			{
				bt.ballGO=false;
				bt.ActionMoveQueue.clear();
				bt.GetPositionQueue.clear();
				winTime=System.currentTimeMillis();//�������ͣʱ��
				changeUtil.player_score++;//��Ҽӷ�
				isPlayerScoreAdd=false;
			}
			if(!GameOver)
			{
				if(winCount<=100)//���ƽ�����
				{
					al.get(0).drawSelf(0);
					winCount++;
				}else//����������ʾ��Ϣ
				{
					winTime=((System.currentTimeMillis()-winTime));//�����ʱ����
					changeUtil.startTime=(changeUtil.startTime+winTime);
					MyAction ac=new ActionGameWin(false,3);
					synchronized(lock)
					{
						doActionQueue.add(ac);
					}
					initGameView();
				}
			}
		}
		if(computer_win)//����Ӯ
		{
			if(isComputerScoreAdd)//�ӷ�
			{
				bt.ballGO=false;
				bt.ActionMoveQueue.clear();
				bt.GetPositionQueue.clear();
				winTime=System.currentTimeMillis();//�������ͣʱ��
				changeUtil.computer_score++;//���Լӷ�
				isComputerScoreAdd=false;
			}
			if(!GameOver)
			{
				if(winCount<=100)
				{
					al.get(1).drawSelf(0);
					winCount++;
				}else//����������ʾ��Ϣ
				{
					winTime=((System.currentTimeMillis()-winTime));//�����ʱ����
					changeUtil.startTime=(changeUtil.startTime+winTime);
					MyAction ac=new ActionGameWin(true,3);
					synchronized(lock)
					{
						doActionQueue.add(ac);
					}
					CWin=true;
					isMove=false;
					initGameView();
				}
			}
		}
	}
	public void resetCamera(int count)//���������
	{
		if(count%2==1)//����
		{
			cameraX=0;
			cameraY=80;
			cameraZ=0;
			targetZ=0;
			upX=0;
			upY=0;
			upZ=-1;
		}else//б��
		{
			cameraX=0;
			cameraY=42;
			cameraZ=63;
			targetZ=0;
			upX=0;
			upY=0.555f;
			upZ=-0.8325f;
		}
	}
	public void initGameView()
	{
		cameraCount=1;
		resetCamera(downCount);
		bt.au.attackCount=1;
		bt.au.allowRecordC=true;
		isPlayerScoreAdd=true;//�������ӷ�
		isComputerScoreAdd=true;//��������ӷ�
		GameView.player_win=false;//���Ӯ�ı�־λ��Ϊfalse
		GameView.computer_win=false;//����Ӯ�ı�־λ��Ϊfalse
		winCount=0;//����ʤ��ͼƬ�ļ�������Ϊ0
		isMove=true;
		isSnow=false;//ֹͣ����������Ч
		drawRound=false;
		drawRoundOK=false;
		roundDrawList.clear();
		roundDeleteList.clear();
		drawLightning=false;
		drawRotateLine=false;
		drawTranslateLine=false;
		isTouch=false;
	}
	public void ReStartGame()
	{
		cameraCount=1;
		bt.au.attackCount=1;
		bt.au.allowRecordC=true;
		isTouch=false;
		isPlayerScoreAdd=true;//�������ӷ�
		isComputerScoreAdd=true;//��������ӷ�
		player_win=false;//���Ӯ�ı�־λ��Ϊfalse
		computer_win=false;//����Ӯ�ı�־λ��Ϊfalse
		winCount=0;//����ʤ��ͼƬ�ļ�������Ϊ0
		isSnow=false;//ֹͣ����������Ч
		drawRound=false;
		drawRoundOK=false;
		roundDrawList.clear();
		roundDeleteList.clear();
		drawLightning=false;
		drawRotateLine=false;
		drawTranslateLine=false;
		isPause=false;//��������ͣ
		GameOver=false;//��Ϸ����ֹͣ����
		changeUtil.countTime=90;//����ʱ�ָ���ʼ��
		changeUtil.startTime=0;
		Start_Game=false;
		changeUtil.computer_score=0;
		changeUtil.player_score=0;
		isMove=true;
		downCount=0;
		eyeView=new BN2DObject(100,100,135,135,TextureManager.getTextures("eye.png"),ShaderManager.getShader(0));//�л��ӽǵİ�ť
		resetCamera(downCount);
	}
}