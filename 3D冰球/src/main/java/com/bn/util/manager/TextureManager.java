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
		"loading.png","load.png",//���ؽ���0-1
		//�׽���2-7
		"bg.png","1-player 1.png","1-player 2.png","settings 1.png","settings 2.png","round.png",
		//�ѶȽ���8-19
		"difficulty.png","classic 1.png","classic 2.png","timed 1.png","timed 2.png",
		"bg 2.png","easy 1.png","easy 2.png","medium 1.png","medium 2.png","hard 1.png","hard 2.png",
		//���ñ�������20-32
		"setting.png","table-hockey.png","bai_03.png","d-b-1.png","d-b-2.png","d-b-3.png","d-b-4.png",
		"jt l 2.png","jt r 2.png","paddles and puck 1.png","paddles and puck 2.png","back 1.png","back 2.png",
		//���ñ�����ɫ����33-49
		"player 1.png","player 2.png","change puck.png","p1.png","p2.png","p3.png","p4.png","p5.png","p6.png",
		"s1.png","s2.png","s3.png","s4.png","s5.png","1.png","2.png","bg 3.png",
		//��Ϸ����50-88
		"game 1.png","game 2.png","game 3.png","game 4.png",
		"bg_red.png","bg_blue.png","bg_yellow.png","bg_blue2.png",
		"bg_green.png","bg_pink.png","bg_orange.png","bg_purple.png",
		"nb1.png","nb5.png","pause 1.png","pause 2.png",
		"computerwin.png","start 1.png",
		"snow.png","particle_red.png","particle_green.png","particle_purple.png","particle_blue2.png",
		"particle_blue1.png","particle_yellow.png","particle_pink.png","eye.png","eye1.png",
		"light.png","skybox.png","tablePic1.png","tablePic2.png","tablePic3.png","tablePic4.png",
		"time 1.png","time 2.png","time.png","menu 1.png","screenshot1.png",
		//��ͣ����89-95
		"pause.png","restart 2.png","restart 1.png","resume 1.png","resume 2.png","resume 3.png","resume 4.png",
		//ʤ������96-97
		"failed.png","win.png",
		//����ͼƬ����ͼƬ98-101
		"sound 1.png","no sound 1.png","shock 1.png","shock 3.png"
		};//����ͼ������
	
	static HashMap<String,Integer> texList=new HashMap<String,Integer>();//������ͼ���б�
	public static int initTexture(MySurfaceView mv,String texName,boolean isRepeat)//��������id
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,//����������id������
				textures,//����id������
				0//ƫ����
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);//������id
		//����MAGʱΪ���Բ���
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR
		);
		//����MINʱΪ��������
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, 
				GLES20.GL_NEAREST
		);
		if(isRepeat)
		{
			//����S������췽ʽΪ�ظ�����
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_REPEAT
			);
			//����T������췽ʽΪ�ظ�����
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_REPEAT
			);
		}else
		{
			//����S������췽ʽΪ��ȡ
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_CLAMP_TO_EDGE
			);
			//����T������췽ʽΪ��ȡ
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_CLAMP_TO_EDGE
			);
		}
		
		String path="pic/"+texName;//����ͼƬ·��
		InputStream in = null;
		try {
			in = mv.getResources().getAssets().open(path);
		}catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap=BitmapFactory.decodeStream(in);//�����м���ͼƬ����
		GLUtils.texImage2D
		(
				GLES20.GL_TEXTURE_2D,//�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
				0,//����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
				bitmap,//����ͼ��
				0//����߿�ߴ�
		);
		bitmap.recycle();//������سɹ����ͷ��ڴ��е�����ͼ
		return textures[0];
	}
	public static void loadingTexture(MySurfaceView mv,int start,int picNum)//������������ͼ
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
			texList.put(texturesName[i],texture);//�����ݼ��뵽�б���
		}
	}
	public static int getTextures(String texName)//�������ͼ
	{
		int result=0;
		if(texList.get(texName)!=null)//����б����д�����ͼ
		{
			result=texList.get(texName);//��ȡ����ͼ
		}else
		{
			result=-1;
		}
		return result;
	}
}
