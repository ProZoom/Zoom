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
	
	int tableTexId;//桌子
	int lineTexId;//亮线
	int skyTexId;//天空盒
	int pillarTexId;//柱子纹理图ID
	
	public static String player2Color="bg_red.png";//默认选中的电脑冰球颜色
	public static String puckColor="bg_yellow.png";//默认选中的球颜色
	public static String player1Color="bg_blue.png";//默认选中的玩家冰球颜色
	public static String tableColor="game 1.png";//默认选中的桌台颜色
	public static String pillarColor="tablePic1.png";//默认选中的桌台颜色
	public static int[] colorIndex=new int[]{0,1,2};//0--球的颜色  1--玩家的颜色  2--系统的颜色
	
	List<BN2DObject> al=new ArrayList<BN2DObject>();//绘制进球提示对象--0为一个对象  1为一个对象
	public List<Body> boxBody=new ArrayList<Body>();
	List<BN2DObject> winView=new ArrayList<BN2DObject>();//胜利界面需绘制的元素
	
	BN2DObject[] Pause=new BN2DObject[2];//暂停按钮
	BN3DObject bnRound;//光圈绘制对象
	
	public int winCount=0;
	public static int Status=0;//判断为哪种模式0--经典模式   1--计时模式
	
	public static boolean player_win=false;//玩家赢
	public static boolean computer_win=false;//电脑赢
	public boolean isPlayerScoreAdd=true;//给玩家加一分
	public boolean isComputerScoreAdd=true;//给电脑加一分
	
	public boolean isTouch=false;//是否允许创建鼠标关节
	public boolean Start_Game=false;//开始游戏
	public boolean GameOver=false;//游戏是否结束
	public boolean CWin=false;//上一局电脑赢
	public boolean isMove=true;//玩家是否能移动
	
	float roundScaleCount=1;//需要缩放的比例
	float roundDampingCount=1;//衰减因子
	public static boolean drawRound=false;//是否绘制光圈
	public static boolean drawRoundOK=false;//是否绘制光圈
	
	public static boolean drawLightning=false;//是否绘制光圈
	public static boolean moveY=false;//绘制光圈时 z坐标是否需要移动
	ArrayList<float[]> roundDrawList=new ArrayList<float[]>();//需要绘制光圈的列表
	ArrayList<float[]> roundDeleteList=new ArrayList<float[]>();//需要删除光圈的列表
	ArrayList<float[]> lightningDrawList=new ArrayList<float[]>();//需要绘制光圈的列表
	ArrayList<float[]> lightningDeleteList=new ArrayList<float[]>();//需要删除光圈的列表
	
	public ArrayList<Body> body=new ArrayList<Body>();
	
	public boolean isSnow=false;//是否绘制雪花 
	public int index=0;//0--淡黄白色  1--蓝色  2--红色  3--橙色
	
	public boolean switchView=false;//是否切换视角
	
	public SnowUtil su;//绘制雪花工具类                                                                                                                                                        
	public ChangeUtil changeUtil;//分数计时对象绘制
	public CrashUtil cu;
	
	public PhysicalThread pt;//物理线程类
	public BallGoThread bt;
	
	//绘制亮线时的平移、旋转、缩放变量值=====================
	float translateX=0;
	float translateZ=0;
	float scaleX=0;
	float scaleZ=0;
	float angle=0;
	boolean drawRotateLine=false;
	boolean drawTranslateLine=false;
	float scaleLineCount=1;
	
	Vec2 tempLan=new Vec2(0,7f);//蓝球的位置
	Vec2 tempHong=new Vec2(0,-6f);//红球的位置
	Vec2 tempBall=new Vec2(0,1.5f);//冰球的位置
	public Queue<MyAction> doActionQueue=new LinkedList<MyAction>();//动作对列
	public Queue<float[][]> positionQueue=new LinkedList<float[][]>();//数据对列
	public Object lock=new Object();
	public Object lockA=new Object();
	
	int rotateCount=0;//是否旋转
	int threadCount=0;//开始游戏时 开启线程 只有一次
	public boolean isPause=false;//是否暂停
	PauseView pv;//暂停界面
	GameOverView gov;//游戏结束界面
	public int changeCount=0;
	public int downCount=0;
	BN2DObject eyeView;//切换视角的按钮
	public long winTime=0;//进球的暂停时间
	public long pauseTime=0;//按下暂停按钮时间
	
	int cameraCount=1;//移动摄像机的计数器
	float playerY1=0;//第一次坐标
	float playerY2=0;//第二次坐标
	boolean resetCamera=false;
	
	public GameView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	public void initPhy()//初始化数据
	{
		Body tempBody=Box2DUtil.createCircle(0.0f, -6f, 0.9f, world, 0.05f, 0f, 0.5f, -1);
		body.add(tempBody);
		hongjdz=new GameObject//电脑控制的球
				(
						mv.hittingtool,
						TextureManager.getTextures(player2Color),
						0.9f*2,
						tempBody
				);
		hongjdz.gt.setLinearVelocity(new Vec2(0,0));
		tempBody=Box2DUtil.createCircle(0.0f, 7.0f, 0.9f, world, 0.05f, 0f, 0.5f, -1);
		body.add(tempBody);
		lanjdz=new GameObject//玩家控制的球
				(
						mv.hittingtool,
						TextureManager.getTextures(player1Color),
						0.9f*2,
						tempBody
				);
		lanjdz.gt.setLinearVelocity(new Vec2(0,0));
		
		tempBody=Box2DUtil.createCircle(0.0f, 0.0f,0.6f, world, 0.05f, 0f, 0.8f, 0);
		body.add(tempBody);
		ball=new GameObject//球
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
		Vec2 gravity = new Vec2(0.0f,0.0f);//设置重力加速度
		world = new World(gravity);//创建世界
		MyContactListener mcl=new MyContactListener(GameView.this);
		world.setContactListener(mcl);
		
		lineTexId=TextureManager.getTextures("light.png");
		skyTexId=TextureManager.getTextures("skybox.png");
		
		bnRound=new BN3DObject(1,1,TextureManager.getTextures("round.png"),ShaderManager.getShader(3));
		
		Pause[1]=new BN2DObject(520, 100, 120, 120, TextureManager.getTextures("pause 1.png"), 
				ShaderManager.getShader(0));//计时暂停按钮
		Pause[0]=new BN2DObject(730, 100, 150, 150, TextureManager.getTextures("pause 1.png"), 
				ShaderManager.getShader(0));//经典模式暂停按钮
		
		MyHHData.createEdage(world);//创建边框
		boxBody.add(Box2DUtil.createBox(0.0f,9.3f, world, 1.6f, 0.5f, true, 0));//玩家控制胜利区域
		boxBody.add(Box2DUtil.createBox(0.0f,-9.3f, world, 1.6f, 0.5f, true, 0));//电脑控制胜利区域
		
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
		changeUtil=new ChangeUtil(this);//创建改变分数计时的对象
		eyeView=new BN2DObject(
				100,100,135,135,
				TextureManager.getTextures("eye.png"),
				ShaderManager.getShader(0)
				);//切换视角的按钮
		//===========胜利界面的元素start==================
		winView=MyHHData.winView();
		//===========胜利界面的元素end==================
		pv=new PauseView(this);//暂停界面
		cu=new CrashUtil(this);
		gov=new GameOverView(this);//游戏结束界面
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if(isMove&&!isPause&&!GameOver)
		{
			float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
			float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
			switch (e.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				if(!Start_Game&&x>=StartGame_Left&&x<=StartGame_Right&&y>=StartGame_Top
				&&y<=StartGame_Buttom&&threadCount==0)
				{
					pt=new PhysicalThread(this);
					pt.start();//物理线程开始
					bt=new BallGoThread(this);
					bt.ballGO=true;//红球走的线程开启
					bt.start();
					su=new SnowUtil(this);//创建星星特效绘制工具类对象
					su.initSnow(index,ball);//初始化雪花
					threadCount=1;
				}
				if(!Start_Game&&x>=StartGame_Left&&x<=StartGame_Right&&
						y>=StartGame_Top&&y<=StartGame_Buttom)
				{
					Start_Game=true;
					ball.gt.setTransform(new Vec2(0.0f, 1.5f), 0);
					changeUtil.startTime=System.currentTimeMillis();//开始时间
					resetCamera(downCount);
					x=0;y=0;
				}
				if(mv.screenShot.isAllowed&&!mv.screenShot.saveFlag&&Start_Game&&x>=ScreenShot_Left&&x<=ScreenShot_Right&&y>=ScreenShot_Top
						&&y<=ScreenShot_Buttom)
				{//截屏
					mv.screenShot.setFlag(true);
				}
				float[] result=GetPositionUtil.limitPositionResult(x,y);
				Vec2 locationWorld0=new Vec2(result[0],result[1]); //计算出触控点在本地坐标系上的坐标
				if(Start_Game&&lanjdz.gt.getFixtureList().testPoint(locationWorld0))
				{
					isTouch=true;//允许创建鼠标关节
					MyAction ac=new ActionDown(1+"",true,lanjdz.gt,lanjdz.gt,locationWorld0,
							2200.0f*lanjdz.gt.getMass(),500.0f,0.0f,0);
					synchronized(lock)
					{
						doActionQueue.offer(ac);
					}
				}
				if(Start_Game&&x>=ChangeEye_Left&&x<=ChangeEye_Right&&y>=ChangeEye_Top&&y<=ChangeEye_Bottom)//点击切换视角
				{
					switchView=true;
					resetCamera=true;
					downCount++;
					if(downCount%2==1)//俯视
					{
						cameraCount=1;
						eyeView=new BN2DObject(
								100,100,135,135,
								TextureManager.getTextures("eye1.png"),
								ShaderManager.getShader(0)
								);//切换视角的按钮
					}else
					{
						eyeView=new BN2DObject(
								100,100,135,135,
								TextureManager.getTextures("eye.png"),
								ShaderManager.getShader(0)
								);//切换视角的按钮
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float mx=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
				float my=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(Start_Game&&isTouch)
				{
					result=GetPositionUtil.limitPositionResult(mx,my);//算出交点
					Vec2 locationWorld1=new Vec2(result[0],result[1]);//计算出触控点在本地坐标系上的坐标
					MyAction ac=new ActionMove(locationWorld1,1);
					synchronized(lock)
					{
						doActionQueue.offer(ac);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				isTouch=false;//不再需要鼠标关节
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
		//调用此方法计算产生透视投影矩阵
		MatrixState3D.setProjectFrustum(-left, right, -top, bottom, near, far);
		//调用此方法产生摄像机9参数位置矩阵
		MatrixState3D.setCamera(cameraX,cameraY,cameraZ,0f,0f,targetZ,upX,upY,upZ);
		MoveCamera();//移动摄像机
		LookAroundCamera();//环视房间，设置摄像机
		ChangeViewCamera();//转换角度
		
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
		
		updateData();//更新数据
		
		//===========绘制影子============
		MatrixState3D.pushMatrix();//保护现场3
		drawObject(1,0.15f,0.1f);
		MatrixState3D.popMatrix();//恢复现场
		//===============判断蓝球与黄球是否碰撞==============
		if(Start_Game&&!computer_win&&!player_win)
		{
			if(cu.judgeIfTouch(tempLan,tempBall))
			{
				cu.lanApplyBall(tempBall,tempLan);//给冰球一定冲量
				cu.doCrashAction(1,"puckBeaterSound.mp3");//播放粒子特效等。。
			}
		}
		//===========绘制亮线=============
		drawLine();
		if(drawTranslateLine)
		{
			drawTranslateLine(translateX,translateZ,scaleX,scaleZ);
		}
		if(drawRotateLine)
		{
			drawRotateLine(translateX,translateZ,scaleX,scaleZ,angle);
		}
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);//关闭深度检测
		//===========绘制光圈=============
		drawRound();
		//===========绘制雪花=============
		if(Start_Game)
		{
			su.drawSnow(index);
		}
		MatrixState3D.popMatrix();//恢复现场
		
		draw2DImage();//绘制2D元
		drawWinView();//绘制游戏结束界面
		if(Start_Game)
		{
			JudgeGameWin();//判断游戏是否胜利
		}
		drawPauseView();//绘制暂停界面
	}
	public void MoveCamera()//移动摄像机
	{
		if(downCount%2==0)//斜视时允许移动摄像机
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
					cameraZ-=distance;//计算摄像机z坐标值
					targetZ-=distance;//计算摄像机目标点z值
					cameraCount++;
				}
			}
		}
	}
	public void LookAroundCamera()//环视房间时  摄像机的设置
	{
		if(Start_Game&&rotateCount==0)
		{
			rotateCount=1;
		}
		if(rotateCount==0)
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
		}else if(rotateCount==1)//开始游戏，重置
		{
			resetCamera(downCount);
			rotateCount++;
		}
	}
	public void ChangeViewCamera()//转换角度时，设置摄像机
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
		if(switchView)//允许转换视角
		{
			if(downCount%2==1)//俯视
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
			}else//斜视
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
	public void draw2DImage()//绘制2D元素
	{
		changeUtil.changeGrade();//绘制分数
		eyeView.drawSelf(0);//绘制切换视角的按钮
		if(Status==0)
		{
			MatrixState2D.pushMatrix();//保护现场
			if(!Start_Game)
			{
				al.get(2).drawSelf(0);
			}else
			{
				al.get(3).drawSelf(0);
			}
			changeUtil.currGrade.get(0).drawSelf(480, 100);//玩家分数
			changeUtil.currGrade.get(1).drawSelf(580, 100);
			
			changeUtil.currGrade.get(2).drawSelf(880, 100);//电脑分数
			changeUtil.currGrade.get(3).drawSelf(980, 100);
			Pause[0].drawSelf(0);//绘制暂停按钮
			MatrixState2D.popMatrix();//恢复现场
		}else
		{
			changeUtil.changeTimer();//倒计时模式
			
			MatrixState2D.pushMatrix();//保护现场
			if(!Start_Game)
			{
				al.get(2).drawSelf(0);
			}else
			{
				al.get(3).drawSelf(0);
			}
			changeUtil.TimerBackGround.drawSelf(0);//绘制计时背景图
			if(changeUtil.currTimer.size()>0)
			{
				changeUtil.currTimer.get(0).drawSelf(850, 100);//玩家分数
				changeUtil.currTimer.get(1).drawSelf(930, 100);
			}
			changeUtil.currGrade.get(0).drawSelf(330, 100);//玩家分数
			changeUtil.currGrade.get(1).drawSelf(410, 100);
			
			changeUtil.currGrade.get(2).drawSelf(630, 100);//电脑分数
			changeUtil.currGrade.get(3).drawSelf(710,100);
			Pause[1].drawSelf(0);//绘制暂停按钮
			MatrixState2D.popMatrix();//恢复现场
		}
	}
	public void drawPillar(float translateX,float translateY,float translateZ)//绘制柱子
	{
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.translate(translateX, translateY, translateZ);//平移
		MatrixState3D.scale(1, 5, 1);//缩放
		mv.pillar.drawSelf(pillarTexId,0,0.0f);//绘制柱子
		MatrixState3D.popMatrix();//恢复现场
	}
	public void updateData()//更新数据
	{
		synchronized(lockA)
		{
			while(positionQueue.size()>0)//如果对列的长度大于0
			{
				float[][] result=positionQueue.poll();
				tempLan=new Vec2(result[0][0],result[0][1]);
				tempHong=new Vec2(result[1][0],result[1][1]);
				tempBall=new Vec2(result[2][0],result[2][1]);
			}
		}
	}
	public void drawLine()//绘制亮线
	{
		float currentBallX=ball.gt.getPosition().x;//记录球的x位置
		float currentBallY=ball.gt.getPosition().y;//记录球的y位置
		//顶部
		if(currentBallY<=-7.8f)
		{
			judgeDirection(currentBallX,1);
		}
		//底部
		else if(currentBallY>=7.8f)
		{
			judgeDirection(currentBallX,4);
	    }
		//上方
		if(currentBallY<0&&currentBallY>=-7.95f)
		{
			judgeDirection(currentBallX,2);
		}
		//下方
		else if(currentBallY>0&&currentBallY<=7.95f)
		{
			judgeDirection(currentBallX,3);
		}
	}
	//绘制可以旋转的亮线
	public void drawRotateLine(float translateX,float translateZ,float scaleX,float scaleZ,float angle)
	{
		if(scaleLineCount>=0)
		{
			MatrixState3D.pushMatrix();//保护现场
			MatrixState3D.translate(translateX,0f,translateZ);//平移
			MatrixState3D.scale(scaleX,1.2f,scaleZ);//缩放
			MatrixState3D.rotate(angle, 0, 1, 0);//旋转
			mv.line.drawSelf(lineTexId,scaleLineCount);//绘制
			MatrixState3D.popMatrix();//恢复现场
			scaleLineCount-=0.05f;
		}else
		{
			scaleLineCount=1;
			drawRotateLine=false;
		}
	}
	//绘制不旋转的亮线
	public void drawTranslateLine(float translateX,float translateZ,float scaleX,float scaleZ)
	{
		if(scaleLineCount>=0)
		{
			MatrixState3D.pushMatrix();//保护现场
			MatrixState3D.translate(translateX,0f,translateZ);//平移
			MatrixState3D.scale(scaleX,1.2f,scaleZ);//缩放
			mv.line.drawSelf(lineTexId,scaleLineCount);//绘制
			MatrixState3D.popMatrix();//恢复现场
			scaleLineCount-=0.05f;
		}else
		{
			scaleLineCount=1;
			drawTranslateLine=false;
		}
	}
	//判断绘制亮线的具体位置
	public void judgeDirection(float currentBallX,int index)
	{
		if(currentBallX<=-3.5f)//左
		{
			if(index==2)//左上边
			{
				translateX=-4.6f;
				translateZ=-4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=90;
				drawRotateLine=true;
			}
			else if(index==3)//左下边
			{
				translateX=-4.6f;
				translateZ=4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=90;
				drawRotateLine=true;
			}
		}
		else if(currentBallX>=3.5f)//右
		{
			if(index==2)//右上边
			{
				translateX=4.6f;
				translateZ=-4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=-90;
				drawRotateLine=true;
			}
			else if(index==3)//右下边
			{
				translateX=4.6f;
				translateZ=4.5f;
				scaleX=0.6f;
				scaleZ=9f;
				angle=-90;
				drawRotateLine=true;
			}
		}
		if(currentBallX<=-1.92f)//左边
		{
			if(index==1)//横上边--left
			{
				translateX=-3.17f;
				translateZ=-8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				drawTranslateLine=true;
			}
			else if(index==4)//横下边--left
			{
				translateX=-3.17f;
				translateZ=8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				angle=180;
				drawRotateLine=true;
			}
		}
		else if(currentBallX>=1.92f)//右边
		{
			if(index==1)//横上边--right
			{
				translateX=3.17f;
				translateZ=-8.9f;
				scaleX=2.5f;
				scaleZ=0.6f;
				drawTranslateLine=true;
			}
			else if(index==4)//横下边--right
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
	//绘制光圈的方法
	public void drawRound()
	{
		if(drawRound)//允许画光圈
		{
			float currentBallX=ball.gt.getPosition().x;//记录球的x位置
			float currentBallY=ball.gt.getPosition().y;//记录球的y位置
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
			roundDrawList.add(result);//将x、y坐标添加进ArrayList
			
			drawRound=false;
		}
		if(drawRoundOK)
		{
			for(float[] result:roundDrawList)//遍历ArrayList
			{
				if(roundScaleCount<=2)
				{
					MatrixState3D.pushMatrix();//保护现场
					MatrixState3D.translate(result[0], 1.5f, result[1]);//平移
					if(downCount%2==1)//随视角的切换改变
			    	{
			    		MatrixState3D.rotate(-120, 1, 0,0);
			    	}
					MatrixState3D.scale(roundScaleCount, roundScaleCount, roundScaleCount);//缩放
					bnRound.drawSelf(roundDampingCount);//绘制
					MatrixState3D.popMatrix();//恢复现场
					roundDampingCount-=0.05f;
					roundScaleCount+=0.05f;
				}else
				{
					roundScaleCount=1;//缩放比例设为1
					roundDampingCount=1;//衰减因子为1
					roundDeleteList.add(result);
				}
			}
			for(float[] result:roundDeleteList)
			{
				roundDrawList.remove(result);//将绘制完的光圈删除
			}
		}
	}
	public void drawPauseView()//绘制暂停界面
	{
		if(isPause)
		{
			pv.drawView();
		}
	}
	public void drawWinView()//绘制游戏结束界面
	{
		gov.isGameOver();//判断游戏是否结束
		if(GameOver)
		{
			gov.drawView();
		}
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
		ball.drawSelf(index,tempBall.x,tempBall.y,shadowPosition);
		MatrixState3D.popMatrix();//恢复现场
		//===========绘制影子end============
	}
	public void JudgeGameWin()//判断游戏是否胜利
	{
		if(player_win)//玩家赢
		{
			if(isPlayerScoreAdd)//加分
			{
				bt.ballGO=false;
				bt.ActionMoveQueue.clear();
				bt.GetPositionQueue.clear();
				winTime=System.currentTimeMillis();//进球的暂停时间
				changeUtil.player_score++;//玩家加分
				isPlayerScoreAdd=false;
			}
			if(!GameOver)
			{
				if(winCount<=100)//绘制进球了
				{
					al.get(0).drawSelf(0);
					winCount++;
				}else//结束绘制提示信息
				{
					winTime=((System.currentTimeMillis()-winTime));//进球的时间间隔
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
		if(computer_win)//电脑赢
		{
			if(isComputerScoreAdd)//加分
			{
				bt.ballGO=false;
				bt.ActionMoveQueue.clear();
				bt.GetPositionQueue.clear();
				winTime=System.currentTimeMillis();//进球的暂停时间
				changeUtil.computer_score++;//电脑加分
				isComputerScoreAdd=false;
			}
			if(!GameOver)
			{
				if(winCount<=100)
				{
					al.get(1).drawSelf(0);
					winCount++;
				}else//结束绘制提示信息
				{
					winTime=((System.currentTimeMillis()-winTime));//进球的时间间隔
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
	public void resetCamera(int count)//重置摄像机
	{
		if(count%2==1)//俯视
		{
			cameraX=0;
			cameraY=80;
			cameraZ=0;
			targetZ=0;
			upX=0;
			upY=0;
			upZ=-1;
		}else//斜视
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
		isPlayerScoreAdd=true;//玩家允许加分
		isComputerScoreAdd=true;//电脑允许加分
		GameView.player_win=false;//玩家赢的标志位设为false
		GameView.computer_win=false;//电脑赢的标志位设为false
		winCount=0;//绘制胜利图片的计数器归为0
		isMove=true;
		isSnow=false;//停止绘制星星特效
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
		isPlayerScoreAdd=true;//玩家允许加分
		isComputerScoreAdd=true;//电脑允许加分
		player_win=false;//玩家赢的标志位设为false
		computer_win=false;//电脑赢的标志位设为false
		winCount=0;//绘制胜利图片的计数器归为0
		isSnow=false;//停止绘制星星特效
		drawRound=false;
		drawRoundOK=false;
		roundDrawList.clear();
		roundDeleteList.clear();
		drawLightning=false;
		drawRotateLine=false;
		drawTranslateLine=false;
		isPause=false;//不绘制暂停
		GameOver=false;//游戏界面停止绘制
		changeUtil.countTime=90;//倒计时恢复初始化
		changeUtil.startTime=0;
		Start_Game=false;
		changeUtil.computer_score=0;
		changeUtil.player_score=0;
		isMove=true;
		downCount=0;
		eyeView=new BN2DObject(100,100,135,135,TextureManager.getTextures("eye.png"),ShaderManager.getShader(0));//切换视角的按钮
		resetCamera(downCount);
	}
}