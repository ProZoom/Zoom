package com.bn.constant;

import com.bn.util.screenscale.ScreenScaleResult;

public class Constant 
{
	//透视投影设置参数
	public static float left=0.5625f;
	public static float right=0.5625f;
	public static float top=1;
	public static float bottom=1;
	public static float near=6f;
	public static float far=200;
	//=============首界面start================
	public static float PLAYER_Left=250;//开始游戏
	public static float PLAYER_Right=850;
	public static float PLAYER_Top=975;
	public static float PLAYER_Bottom=1125;
	
	public static float SELECTION_Left=250;//设置游戏
	public static float SELECTION_Right=850;
	public static float SELECTION_Top=1225;
	public static float SELECTION_Bottom=1575;
	
	public static float Sound_Left=50;//音效设置
	public static float Sound_Right=250;
	public static float Sound_Top=1640;
	public static float Sound_Bottom=1860;
	
	public static float Shock_Left=830;//震动设置
	public static float Shock_Right=1030;
	public static float Shock_Top=1640;
	public static float Shock_Bottom=1860;
	//================首界面end===============
	//=============选关界面start============== 
	public static float OptionClassic_Left=100;//计分模式
	public static float OptionClassic_Right=500;
	public static float OptionClassic_Top=400;
	public static float OptionClassic_Bottom=600;
	
	public static float OptionTimed_Left=600;//计时模式
	public static float OptionTimed_Right=1000;
	public static float OptionTimed_Top=400;
	public static float OptionTimed_Bottom=600;
	
	public static float OptionEasy_Left=275;//难度为简单
	public static float OptionEasy_Right=825;
	public static float OptionEasy_Top=675;
	public static float OptionEasy_Bottom=925;
	
	public static float OptionMedium_Left=275;//难度为一般
	public static float OptionMedium_Right=825;
	public static float OptionMedium_Top=975;
	public static float OptionMedium_Bottom=1125;
	
	public static float OptionHard_Left=275;//难度为复杂
	public static float OptionHard_Right=825;
	public static float OptionHard_Top=1275;
	public static float OptionHard_Bottom=1525;
	//=============选关界面end==============
	//=============设置背景界面start============
	public static float ChooseBGLeftButton_Left=60;
	public static float ChooseBGLeftButton_Right=160;
	public static float ChooseBGLeftButton_Top=625;
	public static float ChooseBGLeftButton_Bottom=775;
	
	public static float ChooseBGRightButton_Left=930;
	public static float ChooseBGRightButton_Right=1030;
	public static float ChooseBGRightButton_Top=625;
	public static float ChooseBGRightButton_Bottom=775;
	
	public static float ChooseBGNextView_Left=140;
	public static float ChooseBGNextView_Right=940;
	public static float ChooseBGNextView_Top=1175;
	public static float ChooseBGNextView_Bottom=1325;
	
	public static float ChooseBGBack_Left=625;
	public static float ChooseBGBack_Right=975;
	public static float ChooseBGBack_Top=1460;
	public static float ChooseBGBack_Bottom=1640;
	//=============设置背景界面end==============
	//=============设置颜色界面start==============
	public static float ChooseColorBack_Left=625;
	public static float ChooseColorBack_Right=975;
	public static float ChooseColorBack_Top=1560;
	public static float ChooseColorBack_Bottom=1740;
	
	public static float ChooseColorPlayer2_p1_Left=210;
	public static float ChooseColorPlayer2_p1_Right=390;
	public static float ChooseColorPlayer2_p1_Top=140;
	public static float ChooseColorPlayer2_p1_Bottom=320;
	
	public static float ChooseColorPlayer2_p2_Left=460;
	public static float ChooseColorPlayer2_p2_Right=640;
	
	public static float ChooseColorPlayer2_p3_Left=710;
	public static float ChooseColorPlayer2_p3_Right=890;
	
	public static float ChooseColorPlayer2_p4_Top=340;
	public static float ChooseColorPlayer2_p4_Bottom=520;
	
	public static float ChooseColorPuck_s1_Left=190;
	public static float ChooseColorPuck_s1_Right=310;
	public static float ChooseColorPuck_s1_Top=770;
	public static float ChooseColorPuck_s1_Bottom=890;
	
	public static float ChooseColorPuck_s2_Left=340;
	public static float ChooseColorPuck_s2_Right=460;
	
	public static float ChooseColorPuck_s3_Left=490;
	public static float ChooseColorPuck_s3_Right=610;
	
	public static float ChooseColorPuck_s4_Left=640;
	public static float ChooseColorPuck_s4_Right=760;
	
	public static float ChooseColorPuck_s5_Left=790;
	public static float ChooseColorPuck_s5_Right=910;
	
	public static float ChooseColorPlayer1_p1_Top=1070;
	public static float ChooseColorPlayer1_p1_Bottom=1250;
	
	public static float ChooseColorPlayer1_p4_Top=1270;
	public static float ChooseColorPlayer1_p4_Bottom=1450;
	//=============设置颜色界面end================
	
	//=============游戏界面start==============
	public static float GameStart_Left=405;
	public static float GameStart_Right=675;
	public static float GameStart_Top=1040;
	public static float GameStart_Bottom=1160;
	//计时模式
	public static float PauseTimer_Left=460;
	public static float PauseTimer_Right=580;
	public static float PauseTimer_Top=40;
	public static float PauseTimer_Bottom=160;
	//经典模式
	public static float PauseClassical_Left=665;
	public static float PauseClassical_Right=805;
	public static float PauseClassical_Top=25;
	public static float PauseClassical_Bottom=175;
	
	//改变视角
	public static float ChangeEye_Left=22.5f;
	public static float ChangeEye_Right=167.5f;
	public static float ChangeEye_Top=22.5f;
	public static float ChangeEye_Bottom=167.5f;
	
	//开始游戏的按钮位置
	public static float StartGame_Left=390;
	public static float StartGame_Right=690;
	public static float StartGame_Top=910;
	public static float StartGame_Buttom=1090;
	
	//==============游戏界面截屏的按钮位置  start==============
	public static float ScreenShot_Left=50;
	public static float ScreenShot_Right=150;
	public static float ScreenShot_Top=220;
	public static float ScreenShot_Buttom=340;
	//==============游戏界面截屏的按钮位置  end================
	
	//==================游戏界面 end================
	
	public static float cameraX=0;//摄像机位置x值
	public static float cameraY=42;//摄像机y位置
	public static float cameraZ=63;//摄像机z位置
	public static float cameraLimit=cameraZ;
	public static float targetZ=0;//摄像机目标点z位置
	public static float upX=0;//摄像机up向量x值
	public static float upY=0.555f;//摄像机up向量y值
	public static float upZ=-0.8325f;//摄像机up向量z值
	public static float tempx=upX+cameraX;//中间值x
	public static float tempz=upZ+cameraZ;//中间值z
	public static float tempLimit=tempz;
	
	public static float degree=0;//旋转角度
	
	//=============游戏界面end==============
	
	//=============暂停界面start==============
	public static float PauseRestart_Left=240;
	public static float PauseRestart_Right=840;
	public static float PauseRestart_Top=810;
	public static float PauseRestart_Bottom=1090;
	
	public static float PauseResume_Left=240;
	public static float PauseResume_Right=840;
	public static float PauseResume_Top=1110;
	public static float PauseResume_Bottom=1390;
	
	public static float PauseBackMain_Left=240;
	public static float PauseBackMain_Right=840;
	public static float PauseBackMain_Top=1410;
	public static float PauseBackMain_Bottom=1690;
	
	//=============暂停界面end==============
	
	//============游戏胜利界面start===============
	public static float GameRestart_Left=240;
	public static float GameRestart_Right=840;
	public static float GameRestart_Top=760;
	public static float GameRestart_Bottom=1040;
	
	public static float GameBackMain_Left=240;
	public static float GameBackMain_Right=840;
	public static float GameBackMain_Top=1360;
	public static float GameBackMain_Bottom=1640;
	//============游戏胜利界面end===============
	
	//================物体颜色图片  start==================
	public static String bg_Green="bg_green.png";
	public static String bg_Blue="bg_blue.png";
	public static String bg_Purple="bg_purple.png";
	public static String bg_Blue2="bg_blue2.png";
	public static String bg_Pink="bg_pink.png";
	public static String bg_Red="bg_red.png";
	
	public static String bg_Orange="bg_orange.png";
	public static String bg_Yellow="bg_yellow.png";
	//================物体颜色图片  end==================
	
	public static final float TIME_STEP = 1.0f/60.0f;//模拟的的频率   
	public static final int ITERA = 5;//迭代越大，模拟约精确，但性能越低   

	//标准屏幕的宽度
	public static float StandardScreenWidth=1080;
	//标准屏幕的高度
	public static float StandardScreenHeight=1920;
	
	//标准屏幕宽高比
	public static float ratio=StandardScreenWidth/StandardScreenHeight;
	//缩放计算结果
	public static ScreenScaleResult ssr;

	public static float fromPixSizeToNearSize(float size)
	{
		return size*2/StandardScreenHeight;
	}
	//屏幕x坐标到视口x坐标
	public static float fromScreenXToNearX(float x)
	{
		return (x-StandardScreenWidth/2)/(StandardScreenHeight/2);
	}
	//屏幕y坐标到视口y坐标
	public static float fromScreenYToNearY(float y)
	{
		return -(y-StandardScreenHeight/2)/(StandardScreenHeight/2);
	}
	//实际屏幕x坐标到标准屏幕x坐标
	public static float fromRealScreenXToStandardScreenX(float rx)
	{
		return (rx-ssr.lucX)/ssr.ratio;
	}
	//实际屏幕y坐标到标准屏幕y坐标
	public static float fromRealScreenYToStandardScreenY(float ry)
	{
		return (ry-ssr.lucY)/ssr.ratio;
	}
}
