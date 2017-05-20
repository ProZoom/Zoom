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

public class ChooseBgView extends BNAbstractView{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	List<BN2DObject> backgroundAL=new ArrayList<BN2DObject>();//存放背景图对象
	int backgroundIndex=0;//选中背景的索引值
	Object lock=new Object();
	Object lockA=new Object();
	public ChooseBgView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView() {
		//选择游戏背景图界面
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("bg 2.png"),ShaderManager.getShader(0)));
		//setting
		al.add(new BN2DObject(540,100,600,150,TextureManager.getTextures("setting.png"),ShaderManager.getShader(0)));
		//选择游戏背景图界面的小背景
		al.add(new BN2DObject(540,800,1000, 1200,TextureManager.getTextures("bai_03.png"),ShaderManager.getShader(0)));
		//选择游戏背景图界面的小背景的标题
		al.add(new BN2DObject(540,300,600, 100,TextureManager.getTextures("table-hockey.png"),ShaderManager.getShader(0)));
		//背景图
		backgroundAL.add(new BN2DObject(540,700,300, 600,TextureManager.getTextures("d-b-1.png"),ShaderManager.getShader(0)));
		backgroundAL.add(new BN2DObject(260,700,180, 400,TextureManager.getTextures("d-b-2.png"),ShaderManager.getShader(0)));
		backgroundAL.add(new BN2DObject(820,700,180, 400,TextureManager.getTextures("d-b-3.png"),ShaderManager.getShader(0)));
		//向左的按钮
		al.add(new BN2DObject(110,700,100, 150,TextureManager.getTextures("jt l 2.png"),ShaderManager.getShader(0)));
		//向右的按钮
		al.add(new BN2DObject(980,700,100, 150,TextureManager.getTextures("jt r 2.png"),ShaderManager.getShader(0)));
		//选择冰球颜色按钮
		al.add(new BN2DObject(540,1250,800, 150,TextureManager.getTextures("paddles and puck 1.png"),ShaderManager.getShader(0)));
		//返回按钮
		al.add(new BN2DObject(800,1550,350, 180,TextureManager.getTextures("back 1.png"),ShaderManager.getShader(0)));
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			//点击向左按钮
			if(x>ChooseBGLeftButton_Left&&x<ChooseBGLeftButton_Right&&y>ChooseBGLeftButton_Top&&y<ChooseBGLeftButton_Bottom)
			{
				backgroundIndex=(backgroundIndex+4-1)%4;
			}else if(x>ChooseBGRightButton_Left&&x<ChooseBGRightButton_Right&&y>ChooseBGRightButton_Top&&y<ChooseBGRightButton_Bottom)
			{
				backgroundIndex=(backgroundIndex+1)%4;
			}
			//选择返回按钮
			else if(x>ChooseBGBack_Left&&x<ChooseBGBack_Right&&y>ChooseBGBack_Top&&y<ChooseBGBack_Bottom)
			{
				BN2DObject bo=new BN2DObject(800,1550,400, 200,TextureManager.getTextures("back 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(7);
					al.add(7,bo);
				}
			}
			//选择 设置冰球颜色按钮
			else if(x>ChooseBGNextView_Left&&x<ChooseBGNextView_Right&&y>ChooseBGNextView_Top&&y<ChooseBGNextView_Bottom)
			{
				BN2DObject bo=new BN2DObject(540,1250,800, 150,TextureManager.getTextures("paddles and puck 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(6);
					al.add(6,bo);
				}
			}
			changeBackGroundAL(backgroundIndex);
			break;
		case MotionEvent.ACTION_UP:
			if(x>ChooseBGBack_Left&&x<ChooseBGBack_Right&&y>ChooseBGBack_Top&&y<ChooseBGBack_Bottom)
			{
				mv.currView=mv.mainView;//回到主界面
				BN2DObject bo=new BN2DObject(800,1550,400, 200,TextureManager.getTextures("back 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(7);
					al.add(7,bo);
				}
			}
			else if(x>ChooseBGNextView_Left&&x<ChooseBGNextView_Right&&y>ChooseBGNextView_Top&&y<ChooseBGNextView_Bottom)
			{
				mv.currView=mv.chooseColorView;
				BN2DObject bo=new BN2DObject(540,1250,800, 150,TextureManager.getTextures("paddles and puck 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(6);
					al.add(6,bo);
				}
			}
			break;
		}
		return true;
	}
	
	public void changeBackGroundAL(int index)//改变背景图
	{
		if(index==0)//当前索引值为0 则绘制0、1、2
		{
			GameView.tableColor="game 1.png";
			GameView.pillarColor="tablePic1.png";
			BN2DObject bo=new BN2DObject(260,700,180, 400,TextureManager.getTextures("d-b-2.png"),ShaderManager.getShader(0));
			BN2DObject bo1=new BN2DObject(540,700,300, 600,TextureManager.getTextures("d-b-1.png"),ShaderManager.getShader(0));
			BN2DObject bo2=new BN2DObject(820,700,180, 400,TextureManager.getTextures("d-b-3.png"),ShaderManager.getShader(0));
			synchronized(lockA)
			{
				backgroundAL.clear();
				backgroundAL.add(bo);
				backgroundAL.add(bo1);
				backgroundAL.add(bo2);
			}
		}else if(index==1)//当前索引值为1 则绘制1、2、3
		{
			GameView.tableColor="game 3.png";
			GameView.pillarColor="tablePic3.png";
			BN2DObject bo=new BN2DObject(260,700,180, 400,TextureManager.getTextures("d-b-1.png"),ShaderManager.getShader(0));
			BN2DObject bo1=new BN2DObject(540,700,300, 600,TextureManager.getTextures("d-b-3.png"),ShaderManager.getShader(0));
			BN2DObject bo2=new BN2DObject(820,700,180, 400,TextureManager.getTextures("d-b-4.png"),ShaderManager.getShader(0));
			synchronized(lockA)
			{
				backgroundAL.clear();
				backgroundAL.add(bo);
				backgroundAL.add(bo1);
				backgroundAL.add(bo2);
			}
		}else if(index==2)//当前索引值为2 则绘制2、3、0
		{
			GameView.tableColor="game 4.png";
			GameView.pillarColor="tablePic4.png";
			BN2DObject bo=new BN2DObject(260,700,180, 400,TextureManager.getTextures("d-b-3.png"),ShaderManager.getShader(0));
			BN2DObject bo1=new BN2DObject(540,700,300, 600,TextureManager.getTextures("d-b-4.png"),ShaderManager.getShader(0));
			BN2DObject bo2=new BN2DObject(820,700,180, 400,TextureManager.getTextures("d-b-2.png"),ShaderManager.getShader(0));
			synchronized(lockA)
			{
				backgroundAL.clear();
				backgroundAL.add(bo);
				backgroundAL.add(bo1);
				backgroundAL.add(bo2);
			}
		}else if(index==3)//当前索引值为3 则绘制3、0、1
		{
			GameView.tableColor="game 2.png";
			GameView.pillarColor="tablePic2.png";
			BN2DObject bo=new BN2DObject(260,700,180, 400,TextureManager.getTextures("d-b-4.png"),ShaderManager.getShader(0));
			BN2DObject bo1=new BN2DObject(540,700,300, 600,TextureManager.getTextures("d-b-2.png"),ShaderManager.getShader(0));
			BN2DObject bo2=new BN2DObject(820,700,180, 400,TextureManager.getTextures("d-b-1.png"),ShaderManager.getShader(0));
			synchronized(lockA)
			{
				backgroundAL.clear();
				backgroundAL.add(bo);
				backgroundAL.add(bo1);
				backgroundAL.add(bo2);
			}
		}
	}
	
	@Override
	public void drawView(GL10 gl)
	{
		synchronized(lock)
		{
			for(BN2DObject object:al)
			{
				object.drawSelf(0);
			}
		}
		synchronized(lockA)//绘制背景图
		{
			for(BN2DObject bo:backgroundAL)
			{
				bo.drawSelf(0);
			}
		}
	}
}
