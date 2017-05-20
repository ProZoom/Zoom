package com.bn.view;

import java.util.ArrayList;
import java.util.List;

import com.bn.constant.Constant;
import com.bn.constant.MyHHData;
import com.bn.object.BN2DObject;
import com.bn.util.action.ActionGameWin;
import com.bn.util.action.MyAction;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

import android.view.MotionEvent;

public class PauseView
{
	GameView gv;
	List<BN2DObject> pauseView=new ArrayList<BN2DObject>();//暂停界面元素
	Object lock=new Object();
	public PauseView(GameView gv)
	{
		this.gv=gv;
		initView();
	}
	public void initView() 
	{
		pauseView=MyHHData.pauseView();
	}

	public boolean onTouchEvent(MotionEvent e) {
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch (e.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(x>Constant.PauseRestart_Left&&x<Constant.PauseRestart_Right&&
					y>Constant.PauseRestart_Top&&y<Constant.PauseRestart_Bottom)
			{//重新开始游戏
				BN2DObject bo=new BN2DObject(1080/2,950,600,280, TextureManager.getTextures("restart 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					pauseView.remove(1);
					pauseView.add(1,bo);
				}
				
			}else if(x>Constant.PauseResume_Left&&x<Constant.PauseResume_Right&&
					y>Constant.PauseResume_Top&&y<Constant.PauseResume_Bottom)
			{//返回游戏界面
				BN2DObject bo=new BN2DObject(1080/2,1250,600,280,TextureManager.getTextures("resume 4.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					pauseView.remove(2);
					pauseView.add(2, bo);
				}
			}else if(x>Constant.PauseBackMain_Left&&x<Constant.PauseBackMain_Right&&
					y>Constant.PauseBackMain_Top&&y<Constant.PauseBackMain_Bottom)
			{//返回主界面
				BN2DObject bo=new BN2DObject(1080/2,1550,600,280,TextureManager.getTextures("resume 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					pauseView.remove(3);
					pauseView.add(3,bo);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if(x>Constant.PauseRestart_Left&&x<Constant.PauseRestart_Right&&
					y>Constant.PauseRestart_Top&&y<Constant.PauseRestart_Bottom)
			{//重新开始游戏
				gv.isPause=false;
				MyAction ac=new ActionGameWin(true,4);
				synchronized(gv.lock)
				{
					gv.doActionQueue.add(ac);
				}
				gv.ReStartGame();
				//更换按钮
				BN2DObject bo=new BN2DObject(1080/2,950,600,280,TextureManager.getTextures("restart 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					pauseView.remove(1);
					pauseView.add(1,bo);
				}
			}else if(x>Constant.PauseResume_Left&&x<Constant.PauseResume_Right&&
					y>Constant.PauseResume_Top&&y<Constant.PauseResume_Bottom)
			{//返回游戏界面
				gv.isMove=true;
				gv.isPause=false;
				gv.pauseTime=((System.currentTimeMillis()-gv.pauseTime));//进球的时间间隔
				gv.changeUtil.startTime=(gv.changeUtil.startTime+gv.pauseTime);
				
				//更换按钮
				BN2DObject bo=new BN2DObject(1080/2,1250,600,280,TextureManager.getTextures("resume 3.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					pauseView.remove(2);
					pauseView.add(2,bo);
				}
			}else if(x>Constant.PauseBackMain_Left&&x<Constant.PauseBackMain_Right&&
					y>Constant.PauseBackMain_Top&&y<Constant.PauseBackMain_Bottom)
			{//返回主界面
				gv.isPause=false;
				MyAction ac=new ActionGameWin(true,5);
				synchronized(gv.lock)
				{
					gv.doActionQueue.add(ac);
				}
				gv.mv.currView=gv.mv.optionView;
				gv.ReStartGame();
				//更换按钮
				BN2DObject bo= new BN2DObject(1080/2,1550,600,280,TextureManager.getTextures("resume 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					pauseView.remove(3);
					pauseView.add(3,bo);
				}
			}
			break;
		}
		return true;
	}
	
	public void drawView() 
	{
		gv.isMove=false;
		synchronized(lock)
		{
			for(BN2DObject pause:pauseView)
			{
				pause.drawSelf(0);
			}
		}
	}
}
