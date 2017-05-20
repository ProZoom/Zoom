package com.bn.util.manager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.bn.happyhockey.MySurfaceView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureManager 
{
	static String[] texturesName=
		{
		"loading.png","load.png",//加载界面0-1
		//首界面2-7
		"bg.png","1-player 1.png","1-player 2.png","settings 1.png","settings 2.png","round.png",
		//难度界面8-19
		"difficulty.png","classic 1.png","classic 2.png","timed 1.png","timed 2.png",
		"bg 2.png","easy 1.png","easy 2.png","medium 1.png","medium 2.png","hard 1.png","hard 2.png",
		//设置背景界面20-32
		"setting.png","table-hockey.png","bai_03.png","d-b-1.png","d-b-2.png","d-b-3.png","d-b-4.png",
		"jt l 2.png","jt r 2.png","paddles and puck 1.png","paddles and puck 2.png","back 1.png","back 2.png",
		//设置冰球颜色界面33-49
		"player 1.png","player 2.png","change puck.png","p1.png","p2.png","p3.png","p4.png","p5.png","p6.png",
		"s1.png","s2.png","s3.png","s4.png","s5.png","1.png","2.png","bg 3.png",
		//游戏界面50-88
		"game 1.png","game 2.png","game 3.png","game 4.png",
		"bg_red.png","bg_blue.png","bg_yellow.png","bg_blue2.png",
		"bg_green.png","bg_pink.png","bg_orange.png","bg_purple.png",
		"nb1.png","nb5.png","pause 1.png","pause 2.png",
		"computerwin.png","start 1.png",
		"snow.png","particle_red.png","particle_green.png","particle_purple.png","particle_blue2.png",
		"particle_blue1.png","particle_yellow.png","particle_pink.png","eye.png","eye1.png",
		"light.png","skybox.png","tablePic1.png","tablePic2.png","tablePic3.png","tablePic4.png",
		"time 1.png","time 2.png","time.png","menu 1.png","screenshot1.png",
		//暂停界面89-95
		"pause.png","restart 2.png","restart 1.png","resume 1.png","resume 2.png","resume 3.png","resume 4.png",
		//胜利界面96-97
		"failed.png","win.png",
		//声音图片、震动图片98-101
		"sound 1.png","no sound 1.png","shock 1.png","shock 3.png"
		};//纹理图的名称
	
	static HashMap<String,Integer> texList=new HashMap<String,Integer>();//放纹理图的列表
	public static int initTexture(MySurfaceView mv,String texName,boolean isRepeat)//生成纹理id
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,//产生的纹理id的数量
				textures,//纹理id的数组
				0//偏移量
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);//绑定纹理id
		//设置MAG时为线性采样
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR
		);
		//设置MIN时为最近点采样
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, 
				GLES20.GL_NEAREST
		);
		if(isRepeat)
		{
			//设置S轴的拉伸方式为重复拉伸
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_REPEAT
			);
			//设置T轴的拉伸方式为重复拉伸
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_REPEAT
			);
		}else
		{
			//设置S轴的拉伸方式为截取
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_CLAMP_TO_EDGE
			);
			//设置T轴的拉伸方式为截取
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_CLAMP_TO_EDGE
			);
		}
		
		String path="pic/"+texName;//定义图片路径
		InputStream in = null;
		try {
			in = mv.getResources().getAssets().open(path);
		}catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap=BitmapFactory.decodeStream(in);//从流中加载图片内容
		GLUtils.texImage2D
		(
				GLES20.GL_TEXTURE_2D,//纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
				0,//纹理的层次，0表示基本图像层，可以理解为直接贴图
				bitmap,//纹理图像
				0//纹理边框尺寸
		);
		bitmap.recycle();//纹理加载成功后释放内存中的纹理图
		return textures[0];
	}
	public static void loadingTexture(MySurfaceView mv,int start,int picNum)//加载所有纹理图
	{
		for(int i=start;i<start+picNum;i++)
		{
			int texture=0;
			if(texturesName[i].equals("game 1.png")||texturesName[i].equals("game 2.png")||
					texturesName[i].equals("game 3.png")||texturesName[i].equals("game 4.png"))
			{
				texture=initTexture(mv,texturesName[i],true);
			}else
			{
				texture=initTexture(mv,texturesName[i],false);
			}
			texList.put(texturesName[i],texture);//将数据加入到列表中
		}
	}
	public static int getTextures(String texName)//获得纹理图
	{
		int result=0;
		if(texList.get(texName)!=null)//如果列表中有此纹理图
		{
			result=texList.get(texName);//获取纹理图
		}else
		{
			result=-1;
		}
		return result;
	}
}
