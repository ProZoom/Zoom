package com.bn.view;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.bn.constant.Constant;
import com.bn.happyhockey.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import static com.bn.constant.Constant.*;

import android.view.MotionEvent;

public class OptionView extends BNAbstractView{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	Object lock=new Object();
	public static int difficultyIndex=0;//�Ѷ�ѡ��
	public OptionView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		//ѡ�ؽ������
		al.add(new BN2DObject(550,200,900,200,TextureManager.getTextures("difficulty.png"),ShaderManager.getShader(0)));
		//ѡ�ؽ��澭��ģʽ����
		al.add(new BN2DObject(300,500,400,200,TextureManager.getTextures("classic 1.png"),ShaderManager.getShader(0)));
		//ѡ�ؽ����ʱģʽ����
		al.add(new BN2DObject(800,500, 400, 200, TextureManager.getTextures("timed 2.png"),ShaderManager.getShader(0)));
		//�򵥹ؿ���ť
		al.add(new BN2DObject(550,800,550,250,TextureManager.getTextures("easy 1.png"),ShaderManager.getShader(0)));
		//һ���Ѷȹؿ���ť
		al.add(new BN2DObject(550,1100, 550,250,TextureManager.getTextures("medium 1.png"),ShaderManager.getShader(0)));
		//�����Ѷȹؿ���ť
		al.add(new BN2DObject(550,1400, 550,250, TextureManager.getTextures("hard 1.png"),ShaderManager.getShader(0)));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			//ѡ��÷�ģʽ
			if(x>OptionClassic_Left&&x<OptionClassic_Right&&y>OptionClassic_Top&&y<OptionClassic_Bottom)
			{
				BN2DObject bo=new BN2DObject(300,500,400,200,TextureManager.getTextures("classic 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(1);
					al.add(1,bo);
				}
				bo=new BN2DObject(800,500, 400, 200,TextureManager.getTextures("timed 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(2);
					al.add(2,bo);
				}
				GameView.Status=0;//ѡ��÷�ģʽ
			}
			//ѡ��ʱ������ģʽ
			else if(x>OptionTimed_Left&&x<OptionTimed_Right&&y>OptionTimed_Top&&y<OptionTimed_Bottom)
			{
				BN2DObject bo=new BN2DObject(300,500,400,200,TextureManager.getTextures("classic 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(1);
					al.add(1,bo);
				}
				bo=new BN2DObject(800,500, 400, 200,TextureManager.getTextures("timed 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(2);
					al.add(2,bo);
				}
				GameView.Status=1;//ѡ���ʱģʽ
			}
			//ѡ�����Ϸģʽ
			else if(x>OptionEasy_Left&&x<OptionEasy_Right&&y>OptionEasy_Top&&y<OptionEasy_Bottom)
			{
				BN2DObject bo=new BN2DObject(550,800,550,250,TextureManager.getTextures("easy 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(3);
					al.add(3,bo);
				}
				difficultyIndex=0;
			}
			//ѡ��һ����Ϸģʽ
			else if(x>OptionMedium_Left&&x<OptionMedium_Right&&y>OptionMedium_Top&&y<OptionMedium_Bottom)
			{
				BN2DObject bo=new BN2DObject(550,1100, 550,250,TextureManager.getTextures("medium 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(4);
					al.add(4,bo);
				}
				difficultyIndex=1;
			}
			//ѡ������Ϸģʽ
			else if(x>OptionHard_Left&&x<OptionHard_Right&&y>OptionHard_Top&&y<OptionHard_Bottom)
			{
				BN2DObject bo=new BN2DObject(550,1400, 550,250,TextureManager.getTextures("hard 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(5);
					al.add(5,bo);
				}
				difficultyIndex=2;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(x>OptionEasy_Left&&x<OptionEasy_Right&&y>OptionEasy_Top&&y<OptionEasy_Bottom)
			{
				mv.gameView.initPhy();
				mv.gameView.rotateCount=0;
				mv.currView=mv.gameView;
				BN2DObject bo=new BN2DObject(550,800,550,250,TextureManager.getTextures("easy 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(3);
					al.add(3,bo);
				}
			}
			//ѡ��һ����Ϸģʽ
			else if(x>OptionMedium_Left&&x<OptionMedium_Right&&y>OptionMedium_Top&&y<OptionMedium_Bottom)
			{
				mv.gameView.initPhy();
				mv.gameView.rotateCount=0;
				mv.currView=mv.gameView;
				BN2DObject bo=new BN2DObject(550,1100, 550,250,TextureManager.getTextures("medium 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(4);
					al.add(4,bo);
				}
			}
			//ѡ������Ϸģʽ
			else if(x>OptionHard_Left&&x<OptionHard_Right&&y>OptionHard_Top&&y<OptionHard_Bottom)
			{
				mv.gameView.initPhy();
				mv.gameView.rotateCount=0;
				mv.currView=mv.gameView;
				BN2DObject bo=new BN2DObject(550,1400, 550,250,TextureManager.getTextures("hard 1.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(5);
					al.add(5,bo);
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void drawView(GL10 gl)
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
