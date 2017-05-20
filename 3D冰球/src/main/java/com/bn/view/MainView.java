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
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	Object lock=new Object();
	public static boolean isSound=true;//��������
	public static boolean isShock=true;//����
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
		//�׽��汳��ͼ
		al.add(new BN2DObject(550,300,800,300,TextureManager.getTextures("bg.png"),ShaderManager.getShader(0)));
		//�׽��濪ʼ��Ϸ��һ��ģʽ��ť
		al.add(new BN2DObject(550,1100, 600,250,TextureManager.getTextures("1-player 1.png"),ShaderManager.getShader(0)));
		//���׽�����ת��ѡ����Ϸ�����Ľ���
		al.add(new BN2DObject(550,1450, 600,250,TextureManager.getTextures("settings 1.png"),ShaderManager.getShader(0)));
		//����
		al.add(new BN2DObject(150,1750, 200,220,TextureManager.getTextures("sound 1.png"),ShaderManager.getShader(0)));
		//��
		al.add(new BN2DObject(930,1750, 200,220,TextureManager.getTextures("shock 1.png"),ShaderManager.getShader(0)));
	}
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	//ѡ�е�����Ϸ��ť
				if(x>PLAYER_Left&&x<PLAYER_Right&&y>PLAYER_Top&&y<PLAYER_Bottom)
				{
					//����ʾͼƬ����ѡ��ͼƬ
					BN2DObject bo=new BN2DObject(550,1100, 600,250,TextureManager.getTextures("1-player 2.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						al.remove(1);
						al.add(1,bo);
					}
				//ѡ�����ð�ť
				}else if(x>SELECTION_Left&&x<SELECTION_Right&&y>SELECTION_Top&&y<SELECTION_Bottom)
				{
					//����ʾͼƬ����ѡ��ͼƬ
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
					mv.currView=mv.optionView;//����ѡ�ؽ���
					//��ѡ��ͼƬ�ĳ���ʾͼƬ
					BN2DObject bo=new BN2DObject(550,1100, 600,250,TextureManager.getTextures("1-player 1.png"),ShaderManager.getShader(0));
					synchronized(lock)
					{
						al.remove(1);
						al.add(1,bo);
					}
				}else if(x>SELECTION_Left&&x<SELECTION_Right&&y>SELECTION_Top&&y<SELECTION_Bottom)
				{
					mv.currView=mv.chooseBgView;//����ѡ����Ϸ��������
					//��ѡ��ͼƬ�ĳ���ʾͼƬ
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
	public void drawView(GL10 gl)//����
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
