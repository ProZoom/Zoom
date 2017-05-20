package com.bn.view;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import com.bn.constant.Constant;
import com.bn.happyhockey.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import android.view.MotionEvent;
import static com.bn.constant.Constant.*;

public class MainView extends BNAbstractView
{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	Object lock=new Object();
	public static boolean isSound=true;//播放声音
	public static boolean isShock=true;//有震动
	int soundCount=0;
	int shockCount=0;
	public MainView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		//首界面背景图
		al.add(new BN2DObject(550,300,800,300,TextureManager.getTextures("bg.png"),ShaderManager.getShader(0)));
		//首界面开始游戏第一个模式按钮
		al.add(new BN2DObject(550,1100, 600,250,TextureManager.getTextures("1-player 1.png"),ShaderManager.getShader(0)));
		//从首界面跳转到选择游戏背景的界面
		al.add(new BN2DObject(550,1450, 600,250,TextureManager.getTextures("settings 1.png"),ShaderManager.getShader(0)));
		//声音
		al.add(new BN2DObject(150,1750, 200,220,TextureManager.getTextures("sound 1.png"),ShaderManager.getShader(0)));
		//震动
		al.add(new BN2DObject(930,1750, 200,220,TextureManager.getTextures("shock 1.png"),ShaderManager.getShader(0)));
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	//选中单人游戏按钮
				if(x>PLAYER_Left&&x<PLAYER_Right&&y>PLAYER_Top&&y<PLAYER_Bottom)
				{
					//将显示图片换成选中图片
					BN2DObject bo=new BN2DObject(550,1100, 600,250,TextureManager.getTextures("1-player 2.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						al.remove(1);
						al.add(1,bo);
					}
				//选中设置按钮
				}else if(x>SELECTION_Left&&x<SELECTION_Right&&y>SELECTION_Top&&y<SELECTION_Bottom)
				{
					//将显示图片换成选中图片
					BN2DObject bo=new BN2DObject(550,1450,600,250,TextureManager.getTextures("settings 2.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						al.remove(2);
						al.add(2,bo);
					}
				}
				else if(x>Sound_Left&&x<Sound_Right&&y>Sound_Top&&y<Sound_Bottom)
				{
					soundCount++;
					if(soundCount%2==1)
					{
						BN2DObject bo=new BN2DObject(150,1750,200,220,TextureManager.getTextures("no sound 1.png"),ShaderManager.getShader(0));
						synchronized(lock)
						{
							al.remove(3);
							al.add(3,bo);
						}
						isSound=false;
					}else
					{
						BN2DObject bo=new BN2DObject(150,1750,200,220,TextureManager.getTextures("sound 1.png"),ShaderManager.getShader(0));
						synchronized(lock)
						{
							al.remove(3);
							al.add(3,bo);
						}
						isSound=true;
					}
				}else if(x>Shock_Left&&x<Shock_Right&&y>Shock_Top&&y<Shock_Bottom)
				{
					shockCount++;
					if(shockCount%2==1)
					{
						BN2DObject bo=new BN2DObject(930,1750,200,220,TextureManager.getTextures("shock 3.png"),ShaderManager.getShader(0));
						synchronized(lock)
						{
							al.remove(4);
							al.add(4,bo);
						}
						isShock=false;
					}else
					{
						BN2DObject bo=new BN2DObject(930,1750,200,220,TextureManager.getTextures("shock 1.png"),ShaderManager.getShader(0));
						synchronized(lock)
						{
							al.remove(4);
							al.add(4,bo);
						}
						isShock=true;
					}
				}
			break;
		    case MotionEvent.ACTION_UP:
		    	if(x>PLAYER_Left&&x<PLAYER_Right&&y>PLAYER_Top&&y<PLAYER_Bottom)
				{
					mv.currView=mv.optionView;//跳到选关界面
					//将选中图片改成显示图片
					BN2DObject bo=new BN2DObject(550,1100, 600,250,TextureManager.getTextures("1-player 1.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						al.remove(1);
						al.add(1,bo);
					}
				}else if(x>SELECTION_Left&&x<SELECTION_Right&&y>SELECTION_Top&&y<SELECTION_Bottom)
				{
					mv.currView=mv.chooseBgView;//跳到选择游戏背景界面
					//将选中图片改成显示图片
					BN2DObject bo=new BN2DObject(550,1450,600,250,TextureManager.getTextures("settings 1.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						al.remove(2);
						al.add(2,bo);
					}
				}
			break;
		}
		return true;
	}
	@Override
	public void drawView(GL10 gl)//绘制
	{
		mv.transitionView.drawView(gl);
		synchronized(lock)
		{
			for(BN2DObject bo:al)
			{
				bo.drawSelf(0);
			}
		}
	}
}
