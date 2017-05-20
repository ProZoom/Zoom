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
	private SceneRenderer mRenderer;//������Ⱦ��
	public BNAbstractView currView;//��ǰ����
	public BNAbstractView mainView;//������
	public BNAbstractView optionView;//ѡ�ؽ���
	public GameView gameView;//��Ϸ����
	public BNAbstractView chooseBgView;//ѡ�񱳾�����
	public BNAbstractView chooseColorView;//ѡ����ɫ����
	public BNAbstractView transitionView;//ת��
//	public boolean isStartGame=false;//�Ƿ�ʼ��Ϸ��־λ
	
	public LoadedObjectVertexNormalTexture table;//��̨
	public LoadedObjectVertexNormalTexture line;//����
	public LoadedObjectVertexNormalTexture sky;//��պ�
	public LoadedObjectVertexNormalTexture pillar;//����
	public LoadedObjectVertexNormalTexture hittingtool;//���򹤾�
	public LoadedObjectVertexNormalTexture puck;//����
	
	private static boolean isExit = false;
	public ScreenShot screenShot;
	public MySurfaceView(MainActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.setEGLContextClientVersion(2);//����ʹ��OPENGL ES2.0
		mRenderer = new SceneRenderer();//����������Ⱦ��
		setRenderer(mRenderer);//������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ
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
	//���ؼ� ������һ����
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(currView==optionView)//��ѡ����淵��������
			{
				currView=mainView;
			}else if(currView==gameView&&gameView.isPause)
			{
				gameView.pauseTime=((System.currentTimeMillis()-gameView.pauseTime));//�����ʱ����
				gameView.changeUtil.startTime=(gameView.changeUtil.startTime+gameView.pauseTime);
				gameView.isMove=true;
				gameView.isPause=false;
			}else if(currView==gameView&&gameView.Start_Game&&!gameView.GameOver)//����Ϸ���淵����ͣ����
			{
				gameView.pauseTime=System.currentTimeMillis();
				gameView.isPause=true;
			}else if(currView==chooseColorView)//��ѡ����ɫ���淵�ص��������汳������
			{
				currView=chooseBgView;
			}else if(currView==chooseBgView)//��ѡ����ɫ���淵�ص��������汳������
			{
				currView=mainView;
			}
			else if(currView==mainView)//ֻ�д���������ʱ�ſ��԰����ؼ���������
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
			isExit = true; // ׼���˳�  
			Toast.makeText(this.getContext(),"�ٰ�һ���˳���Ϸ", Toast.LENGTH_SHORT).show();
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
	public Handler handler = new Handler()//����ͼƬ�Ƿ�ɹ� ��ʾ��
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what==0)//ͼƬ����ɹ�
			{
				Toast.makeText(MySurfaceView.this.getContext(),"ͼƬ����ɹ�������", Toast.LENGTH_SHORT).show();
			}else//ͼƬ����ʧ��
			{
				Toast.makeText(MySurfaceView.this.getContext(),"ͼƬû�б���ɹ�������", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			if(screenShot.saveFlag)//����Ѿ����������ť
			{
				screenShot.saveScreen((int)(Constant.StandardScreenWidth*Constant.ssr.ratio),//width
						(int)(Constant.StandardScreenHeight*Constant.ssr.ratio));//����ͼƬ
				screenShot.setFlag(false);
			}
			//�����Ȼ�������ɫ����
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			currView.drawView(gl);
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {//�����Ӵ���С��λ��
			GLES20.glViewport
			(
					(int)Constant.ssr.lucX,//x
					(int)Constant.ssr.lucY,//y
					(int)(Constant.StandardScreenWidth*Constant.ssr.ratio),//width
					(int)(Constant.StandardScreenHeight*Constant.ssr.ratio)//height
			);
			//����GLSurfaceView�Ŀ�߱�
			float ratio = (float) width / height;
			
			//���ô˷����������ƽ��ͶӰ����
			MatrixState2D.setProjectOrtho(-ratio, ratio, -1, 1, 1, 100);
			//���ô˷������������9����λ�þ���
			MatrixState2D.setCamera(0,0,5,0f,0f,0f,0f,1f,0f);
			//��ʼ���任����
			MatrixState2D.setInitStack();
			MatrixState3D.setInitStack();
			MatrixState2D.setLightLocation(0,50,0);
			currView=new LoadingView(MySurfaceView.this);
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			//��ʼ����ɫ��
			ShaderManager.loadingShader(MySurfaceView.this);
			//������Ļ����ɫRGBA
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			//�򿪱������
			GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
	}
}