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
import static com.bn.constant.Constant.*;

import android.view.MotionEvent;

public class GameOverView
{
	GameView gv;
	List<BN2DObject> winView=new ArrayList<BN2DObject>();//暂停界面元素
	Object lock=new Object();
	boolean isDown=false;
	public GameOverView(GameView gv)
	{
		this.gv=gv;
		initView();
	}
	public void initView() 
	{
		winView=MyHHData.winView();
	}

	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch (e.getAction()){
		case MotionEvent.ACTION_DOWN:
			isDown=true;
			if(x>GameRestart_Left&&x<GameRestart_Right&&
					y>GameRestart_Top&&y<GameRestart_Bottom)
			{//重新开始游戏
				BN2DObject bn=new BN2DObject(1080/2,900,600,280, TextureManager.getTextures("restart 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					winView.remove(1);
					winView.add(1,bn);
				}
			}else if(x>GameBackMain_Left&&x<GameBackMain_Right&&
					y>GameBackMain_Top&&y<GameBackMain_Bottom)
			{//返回主界面
				BN2DObject bn=new BN2DObject(1080/2,1500,600,280,TextureManager.getTextures("resume 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					winView.remove(2);
					winView.add(2,bn);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isDown)
			{
				if(x>GameRestart_Left&&x<GameRestart_Right&&
						y>GameRestart_Top&&y<GameRestart_Bottom)
				{//重新开始游戏
					MyAction ac=new ActionGameWin(true,4);
					synchronized(gv.lock)
					{
						gv.doActionQueue.add(ac);
					}
					//更换按钮
					BN2DObject bn=new BN2DObject(1080/2,900,600,280, TextureManager.getTextures("restart 1.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						winView.remove(1);
						winView.add(1, bn);
					}
					gv.GameOver=false;
					gv.ReStartGame();
					isDown=false;
				}else if(x>GameBackMain_Left&&x<GameBackMain_Right&&
						y>GameBackMain_Top&&y<GameBackMain_Bottom)
				{//返回主界面
					
					MyAction ac=new ActionGameWin(true,5);
					synchronized(gv.lock)
					{
						gv.doActionQueue.add(ac);
					}
					gv.mv.currView=gv.mv.optionView;
					//更换按钮
					BN2DObject bn=new BN2DObject(1080/2,1500,600,280, TextureManager.getTextures("resume 1.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						winView.remove(2);
						winView.add(2, bn);
					}
					gv.GameOver=false;
					gv.ReStartGame();
					isDown=false;
				}
			}
			break;
		}
		return true;
	}
	
	public void isGameOver()
	{
		//游戏失败
		if(!gv.isTouch&&((gv.changeUtil.player_score<gv.changeUtil.computer_score&&gv.changeUtil.computer_score>=7)||
				(gv.changeUtil.countTime==0&&gv.changeUtil.player_score<gv.changeUtil.computer_score)
				||(gv.changeUtil.player_score==gv.changeUtil.computer_score&&gv.changeUtil.countTime==0)))
		{
			gv.GameOver=true;
			BN2DObject bn=new BN2DObject(1080/2,400,500,300,TextureManager.getTextures("failed.png"),ShaderManager.getShader(0));
			synchronized(lock)
			{
				winView.remove(0);
				winView.add(0,bn);
			}
		}else if(!gv.isTouch&&((gv.changeUtil.player_score>gv.changeUtil.computer_score&&gv.changeUtil.player_score>=7)||(
				gv.changeUtil.countTime==0&&gv.changeUtil.player_score>gv.changeUtil.computer_score)))
		{
			//游戏胜利
			gv.GameOver=true;
			BN2DObject bn=new BN2DObject(1080/2,400,500,300,TextureManager.getTextures("win.png"),ShaderManager.getShader(0));
			synchronized(lock)
			{
				winView.remove(0);
				winView.add(0,bn);
			}
		}
	}
	public void drawView() 
	{
		gv.isMove=false;
		gv.isTouch=false;
		synchronized(lock)
		{
			for(BN2DObject win:winView)
			{
				win.drawSelf(0);
			}
		}
	}
}
