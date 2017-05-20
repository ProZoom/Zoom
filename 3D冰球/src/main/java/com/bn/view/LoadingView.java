package com.bn.view;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import com.bn.constant.LoadUtil;
import com.bn.happyhockey.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import android.view.MotionEvent;

public class LoadingView extends BNAbstractView//加载界面
{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	int count=0;
	boolean isLoading=false;//是否加载其他资源
	public LoadingView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		TextureManager.loadingTexture(mv, 0, 2);//初始化纹理资源
		al.add(//加载界面背景图
				new BN2DObject(
						1080/2,1920/2, 300,300, 
						TextureManager.getTextures("loading.png"),
						ShaderManager.getShader(0)
						)
				);
		al.add(//加载界面加载图
				new BN2DObject(
						540,600, 900,300, 
						TextureManager.getTextures("load.png"),
						ShaderManager.getShader(0)
						)
				);
		isLoading=true;
	}
	int step=0;
	public void initOtherView()
	{
		if(step<50)
		{
			TextureManager.loadingTexture(mv, 2*step+2, 2);//初始化纹理资源
		}else
		{
			if(step==50)
			{
				mv.mainView=new MainView(mv);
			}else if(step==51)
			{
				mv.optionView=new OptionView(mv);
			}else if(step==52)
			{
				mv.gameView=new GameView(mv);
			}else if(step==63)
			{
				mv.chooseBgView=new ChooseBgView(mv);
			}else if(step==64)
			{
				mv.chooseColorView=new ChooseColorView(mv);
			}else if(step==65)
			{
				mv.table=LoadUtil.loadFromFile("table.obj", mv.getResources(), mv);
			}else if(step==100)
			{
				mv.line=LoadUtil.loadFromFile("line.obj", mv.getResources(), mv);
			}else if(step==130)
			{
				mv.sky=LoadUtil.loadFromFile("skybox.obj", mv.getResources(), mv);
			}else if(step==170)
			{
				mv.pillar=LoadUtil.loadFromFile("zhuzi.obj", mv.getResources(), mv);
			}else if(step==200)
			{
				mv.puck=LoadUtil.loadFromFile("ball.obj", mv.getResources(), mv);
			}else if(step==230)
			{
				mv.hittingtool=LoadUtil.loadFromFile("hockey.obj", mv.getResources(), mv);
			}else if(step==260)
			{
				mv.transitionView=new TransitionView(mv);
			}
			else if(step==261)
			{
				mv.currView=mv.mainView;
				isLoading=false;
				return;
			}
		}
		step++;
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		return false;
	}
	@Override
	public void drawView(GL10 gl) 
	{
		for(int i=0;i<al.size();i++)
		{
			if(i==0)
			{
				al.get(i).drawSelf(1);
			}else
			{
				al.get(i).drawSelf(0);
			}
		}
		if(isLoading)
		{
			initOtherView();
		}
	}
}

