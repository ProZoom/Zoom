package com.bn.view;

import static com.bn.constant.Constant.*;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.bn.constant.Constant;
import com.bn.happyhockey.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class ChooseColorView extends BNAbstractView{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	Object lock=new Object();
	int player2_SelectX=545;//选中颜色的黄框1的位置
	int player2_SelectY=430;
	int player1_SelectX=545;//选中颜色的黄框2的位置
	int player1_SelectY=1160;
	int puck_SelectX=545;//选中颜色的黄框3的位置
	int puck_SelectY=820;
	public ChooseColorView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		//背景
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("bg 2.png"),ShaderManager.getShader(0)));
		//白框背景
		al.add(new BN2DObject(540,750,850,1500,TextureManager.getTextures("bg 3.png"),ShaderManager.getShader(0)));
		//player2
		al.add(new BN2DObject(540,80,400,70,TextureManager.getTextures("player 2.png"),ShaderManager.getShader(0)));
		//player2颜色
		al.add(new BN2DObject(300,230,180,180,TextureManager.getTextures("p1.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(550,230,180,180,TextureManager.getTextures("p2.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(800,230,180,180,TextureManager.getTextures("p3.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(300,430,180,180,TextureManager.getTextures("p4.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(550,430,180,180,TextureManager.getTextures("p5.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(800,430,180,180,TextureManager.getTextures("p6.png"),ShaderManager.getShader(0)));
		//黄色选框
		al.add(new BN2DObject(545,430,210,210,TextureManager.getTextures("2.png"),ShaderManager.getShader(0)));
		//change puck
		al.add(new BN2DObject(540,670,400,90,TextureManager.getTextures("change puck.png"),ShaderManager.getShader(0)));
		//change puck颜色
		al.add(new BN2DObject(250,830,120,120,TextureManager.getTextures("s1.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(400,830,120,120,TextureManager.getTextures("s2.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(550,830,120,120,TextureManager.getTextures("s3.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(700,830,120,120,TextureManager.getTextures("s4.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(850,830,120,120,TextureManager.getTextures("s5.png"),ShaderManager.getShader(0)));
		//黄色选框
		al.add(new BN2DObject(545,820,150,150,TextureManager.getTextures("1.png"),ShaderManager.getShader(0)));
		//player1
		al.add(new BN2DObject(540,1015,400,70,TextureManager.getTextures("player 1.png"),ShaderManager.getShader(0)));
		//player1颜色
		al.add(new BN2DObject(300,1160,180,180,TextureManager.getTextures("p1.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(550,1160,180,180,TextureManager.getTextures("p2.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(800,1160,180,180,TextureManager.getTextures("p3.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(300,1360,180,180,TextureManager.getTextures("p4.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(550,1360,180,180,TextureManager.getTextures("p5.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(800,1360,180,180,TextureManager.getTextures("p6.png"),ShaderManager.getShader(0)));
		//黄色选框
		al.add(new BN2DObject(545,1160,210,210,TextureManager.getTextures("2.png"),ShaderManager.getShader(0)));
		//back
		al.add(new BN2DObject(800,1650,350,180,TextureManager.getTextures("back 1.png"),ShaderManager.getShader(0)));
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			//按下返回按钮
			if(x>ChooseColorBack_Left&&x<ChooseColorBack_Right&&y>ChooseColorBack_Top&&y<ChooseColorBack_Bottom)
			{
				BN2DObject bo=new BN2DObject(800,1650,350,180,TextureManager.getTextures("back 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(25);
					al.add(25,bo);
				}
			}
			//为电脑控制的球选择颜色
			if(y>=ChooseColorPlayer2_p1_Top&&y<=ChooseColorPlayer2_p1_Bottom)//第一列
			{
				player2_SelectY=230;
				if(x>ChooseColorPlayer2_p1_Left&&x<ChooseColorPlayer2_p1_Right)
				{
					GameView.player2Color=bg_Purple;//"bg_purple.png";
					GameView.colorIndex[2]=3;
					player2_SelectX=295;
				}else if(x>ChooseColorPlayer2_p2_Left&&x<ChooseColorPlayer2_p2_Right)
				{
					GameView.player2Color=bg_Blue;//"bg_blue.png";
					GameView.colorIndex[2]=1;
					player2_SelectX=545;
				}else if(x>ChooseColorPlayer2_p3_Left&&x<ChooseColorPlayer2_p3_Right)
				{
					GameView.player2Color=bg_Green;//"bg_green.png";
					GameView.colorIndex[2]=4;
					player2_SelectX=795;
				}
			}else if(y>=ChooseColorPlayer2_p4_Top&&y<=ChooseColorPlayer2_p4_Bottom)//第二列
			{
				player2_SelectY=430;
				if(x>ChooseColorPlayer2_p1_Left&&x<ChooseColorPlayer2_p1_Right)
				{
					GameView.player2Color=bg_Blue2;//"bg_blue2.png";
					GameView.colorIndex[2]=5;
					player2_SelectX=295;
				}else if(x>ChooseColorPlayer2_p2_Left&&x<ChooseColorPlayer2_p2_Right)
				{
					GameView.player2Color=bg_Red;//"bg_red.png";
					GameView.colorIndex[2]=2;
					player2_SelectX=545;
				}else if(x>ChooseColorPlayer2_p3_Left&&x<ChooseColorPlayer2_p3_Right)
				{
					GameView.player2Color=bg_Pink;//"bg_pink.png";
					GameView.colorIndex[2]=6;
					player2_SelectX=795;
				}
			//为冰球选择颜色
			}else if(y>=ChooseColorPuck_s1_Top&&y<=ChooseColorPuck_s1_Bottom)//第三列
			{
				puck_SelectY=820;
				if(x>ChooseColorPuck_s1_Left&&x<ChooseColorPuck_s1_Right)
				{
					GameView.puckColor=bg_Pink;//"bg_pink.png";
					puck_SelectX=245;
				}else if(x>ChooseColorPuck_s2_Left&&x<ChooseColorPuck_s2_Right)
				{
					GameView.puckColor=bg_Orange;//"bg_orange.png";
					puck_SelectX=395;
				}else if(x>ChooseColorPuck_s3_Left&&x<ChooseColorPuck_s3_Right)
				{
					GameView.puckColor=bg_Yellow;//"bg_yellow.png";
					puck_SelectX=545;
				}else if(x>ChooseColorPuck_s4_Left&&x<ChooseColorPuck_s4_Right)
				{
					GameView.puckColor=bg_Green;//"bg_green.png";
					puck_SelectX=695;
				}else if(x>ChooseColorPuck_s5_Left&&x<ChooseColorPuck_s5_Right)
				{
					GameView.puckColor=bg_Blue2;//"bg_blue2.png";
					puck_SelectX=845;
				}
			//为玩家控制的球选择颜色
			}else if(y>=ChooseColorPlayer1_p1_Top&&y<=ChooseColorPlayer1_p1_Bottom)
			{
				player1_SelectY=1160;
				if(x>ChooseColorPlayer2_p1_Left&&x<ChooseColorPlayer2_p1_Right)
				{
					GameView.player1Color=bg_Purple;//"bg_purple.png";
					GameView.colorIndex[1]=3;
					player1_SelectX=295;
				}else if(x>ChooseColorPlayer2_p2_Left&&x<ChooseColorPlayer2_p2_Right)
				{
					GameView.player1Color=bg_Blue;//"bg_blue.png";
					GameView.colorIndex[1]=1;
					player1_SelectX=545;
				}else if(x>ChooseColorPlayer2_p3_Left&&x<ChooseColorPlayer2_p3_Right)
				{
					GameView.player1Color=bg_Green;//"bg_green.png";
					GameView.colorIndex[1]=4;
					player1_SelectX=795;
				}
			}else if(y>=ChooseColorPlayer1_p4_Top&&y<=ChooseColorPlayer1_p4_Bottom)
			{
				System.out.println("y:"+y);
				player1_SelectY=1360;
				if(x>ChooseColorPlayer2_p1_Left&&x<ChooseColorPlayer2_p1_Right)
				{
					GameView.player1Color=bg_Blue2;//"bg_blue2.png";
					GameView.colorIndex[1]=5;
					player1_SelectX=295;
				}else if(x>ChooseColorPlayer2_p2_Left&&x<ChooseColorPlayer2_p2_Right)
				{
					GameView.player1Color=bg_Red;//"bg_red.png";
					GameView.colorIndex[1]=2;
					player1_SelectX=545;
				}else if(x>ChooseColorPlayer2_p3_Left&&x<ChooseColorPlayer2_p3_Right)
				{
					GameView.player1Color=bg_Pink;//"bg_pink.png";
					GameView.colorIndex[1]=6;
					player1_SelectX=795;
				}
			}
			//重新绘制黄框
			BN2DObject bo=new BN2DObject(player2_SelectX,player2_SelectY,210,210,TextureManager.getTextures("2.png"),ShaderManager.getShader(0));
			synchronized(lock)
			{
				al.remove(9);
				al.add(9,bo);
			}
			bo=new BN2DObject(puck_SelectX,puck_SelectY,150,150,TextureManager.getTextures("1.png"),ShaderManager.getShader(0));
			synchronized(lock)
			{
				al.remove(16);
				al.add(16,bo);
			}
			System.out.println("player1_SelectX:"+player1_SelectX+"　player1_SelectY："+player1_SelectY);
			bo=new BN2DObject(player1_SelectX,player1_SelectY,210,210,TextureManager.getTextures("2.png"),ShaderManager.getShader(0));
			synchronized(lock)
			{
				al.remove(24);
				al.add(24,bo);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(x>ChooseColorBack_Left&&x<ChooseColorBack_Right&&y>ChooseColorBack_Top&&y<ChooseColorBack_Bottom)
			{
				mv.currView=mv.chooseBgView;//回到选择背景界面
				bo=new BN2DObject(800,1650,350,180,TextureManager.getTextures("back 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(25);
					al.add(25,bo);
				}
			}
			break;
		}
		return true;
	}
	@Override
	public void drawView(GL10 gl) 
	{
		synchronized(lock)
		{
			for(BN2DObject bo:al)
			{
				bo.drawSelf(0);
			}
		}
	}
}
