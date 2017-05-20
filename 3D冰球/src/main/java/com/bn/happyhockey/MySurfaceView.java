package com.bn.happyhockey;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.constant.Constant;
import com.bn.constant.MatrixState2D;
import com.bn.constant.MatrixState3D;
import com.bn.constant.ScreenShot;
import com.bn.object.LoadedObjectVertexNormalTexture;
import com.bn.util.manager.ShaderManager;
import com.bn.view.BNAbstractView;
import com.bn.view.GameView;
import com.bn.view.LoadingView;
import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MySurfaceView extends GLSurfaceView
{
	public MainActivity activity;
	private SceneRenderer mRenderer;//场景渲染器
	public BNAbstractView currView;//当前界面
	public BNAbstractView mainView;//主界面
	public BNAbstractView optionView;//选关界面
	public GameView gameView;//游戏界面
	public BNAbstractView chooseBgView;//选择背景界面
	public BNAbstractView chooseColorView;//选择颜色界面
	public BNAbstractView transitionView;//转场
//	public boolean isStartGame=false;//是否开始游戏标志位
	
	public LoadedObjectVertexNormalTexture table;//桌台
	public LoadedObjectVertexNormalTexture line;//亮线
	public LoadedObjectVertexNormalTexture sky;//天空盒
	public LoadedObjectVertexNormalTexture pillar;//柱子
	public LoadedObjectVertexNormalTexture hittingtool;//击球工具
	public LoadedObjectVertexNormalTexture puck;//冰球
	
	private static boolean isExit = false;
	public ScreenShot screenShot;
	public MySurfaceView(MainActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.setEGLContextClientVersion(2);//设置使用OPENGL ES2.0
		mRenderer = new SceneRenderer();//创建场景渲染器
		setRenderer(mRenderer);//设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
		screenShot=new ScreenShot(this);
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent e)
	{
		if(currView==null)
		{
			return false;
		}
		return currView.onTouchEvent(e);
	}
	//返回键 返回上一界面
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(currView==optionView)//从选项界面返回主界面
			{
				currView=mainView;
			}else if(currView==gameView&&gameView.isPause)
			{
				gameView.pauseTime=((System.currentTimeMillis()-gameView.pauseTime));//进球的时间间隔
				gameView.changeUtil.startTime=(gameView.changeUtil.startTime+gameView.pauseTime);
				gameView.isMove=true;
				gameView.isPause=false;
			}else if(currView==gameView&&gameView.Start_Game&&!gameView.GameOver)//从游戏界面返回暂停界面
			{
				gameView.pauseTime=System.currentTimeMillis();
				gameView.isPause=true;
			}else if(currView==chooseColorView)//从选择颜色界面返回到设置桌面背景界面
			{
				currView=chooseBgView;
			}else if(currView==chooseBgView)//从选择颜色界面返回到设置桌面背景界面
			{
				currView=mainView;
			}
			else if(currView==mainView)//只有处于主界面时才可以按返回键返回桌面
			{
				exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void exit()
	{
		if (isExit == false) 
		{
			isExit = true; // 准备退出  
			Toast.makeText(this.getContext(),"再按一次退出游戏", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable()
			{
				@Override  
				public void run()
				{
					isExit = false;  
				}
			}, 2500);
		} else 
		{
			android.os.Process.killProcess(android.os.Process.myPid()); 
		}  
	}
	public Handler handler = new Handler()//保存图片是否成功 提示框
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what==0)//图片保存成功
			{
				Toast.makeText(MySurfaceView.this.getContext(),"图片保存成功！！！", Toast.LENGTH_SHORT).show();
			}else//图片保存失败
			{
				Toast.makeText(MySurfaceView.this.getContext(),"图片没有保存成功！！！", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			if(screenShot.saveFlag)//如果已经点击截屏按钮
			{
				screenShot.saveScreen((int)(Constant.StandardScreenWidth*Constant.ssr.ratio),//width
						(int)(Constant.StandardScreenHeight*Constant.ssr.ratio));//保存图片
				screenShot.setFlag(false);
			}
			//清除深度缓冲与颜色缓冲
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			currView.drawView(gl);
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {//设置视窗大小及位置
			GLES20.glViewport
			(
					(int)Constant.ssr.lucX,//x
					(int)Constant.ssr.lucY,//y
					(int)(Constant.StandardScreenWidth*Constant.ssr.ratio),//width
					(int)(Constant.StandardScreenHeight*Constant.ssr.ratio)//height
			);
			//计算GLSurfaceView的宽高比
			float ratio = (float) width / height;
			
			//调用此方法计算产生平行投影矩阵
			MatrixState2D.setProjectOrtho(-ratio, ratio, -1, 1, 1, 100);
			//调用此方法产生摄像机9参数位置矩阵
			MatrixState2D.setCamera(0,0,5,0f,0f,0f,0f,1f,0f);
			//初始化变换矩阵
			MatrixState2D.setInitStack();
			MatrixState3D.setInitStack();
			MatrixState2D.setLightLocation(0,50,0);
			currView=new LoadingView(MySurfaceView.this);
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			//初始化着色器
			ShaderManager.loadingShader(MySurfaceView.this);
			//设置屏幕背景色RGBA
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			//打开背面剪裁
			GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
	}
}