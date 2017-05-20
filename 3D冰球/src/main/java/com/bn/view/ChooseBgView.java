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
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	List<BN2DObject> backgroundAL=new ArrayList<BN2DObject>();//��ű���ͼ����
	int backgroundIndex=0;//ѡ�б���������ֵ
	Object lock=new Object();
	Object lockA=new Object();
	public ChooseBgView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView() {
		//ѡ����Ϸ����ͼ����
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("bg 2.png"),ShaderManager.getShader(0)));
		//setting
		al.add(new BN2DObject(540,100,600,150,TextureManager.getTextures("setting.png"),ShaderManager.getShader(0)));
		//ѡ����Ϸ����ͼ�����С����
		al.add(new BN2DObject(540,800,1000, 1200,TextureManager.getTextures("bai_03.png"),ShaderManager.getShader(0)));
		//ѡ����Ϸ����ͼ�����С�����ı���
		al.add(new BN2DObject(540,300,600, 100,TextureManager.getTextures("table-hockey.png"),ShaderManager.getShader(0)));
		//����ͼ
		backgroundAL.add(new BN2DObject(540,700,300, 600,TextureManager.getTextures("d-b-1.png"),ShaderManager.getShader(0)));
		backgroundAL.add(new BN2DObject(260,700,180, 400,TextureManager.getTextures("d-b-2.png"),ShaderManager.getShader(0)));
		backgroundAL.add(new BN2DObject(820,700,180, 400,TextureManager.getTextures("d-b-3.png"),ShaderManager.getShader(0)));
		//����İ�ť
		al.add(new BN2DObject(110,700,100, 150,TextureManager.getTextures("jt l 2.png"),ShaderManager.getShader(0)));
		//���ҵİ�ť
		al.add(new BN2DObject(980,700,100, 150,TextureManager.getTextures("jt r 2.png"),ShaderManager.getShader(0)));
		//ѡ�������ɫ��ť
		al.add(new BN2DObject(540,1250,800, 150,TextureManager.getTextures("paddles and puck 1.png"),ShaderManager.getShader(0)));
		//���ذ�ť
		al.add(new BN2DObject(800,1550,350, 180,TextureManager.getTextures("back 1.png"),ShaderManager.getShader(0)));
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			//�������ť
			if(x>ChooseBGLeftButton_Left&&x<ChooseBGLeftButton_Right&&y>ChooseBGLeftButton_Top&&y<ChooseBGLeftButton_Bottom)
			{
				backgroundIndex=(backgroundIndex+4-1)%4;
			}else if(x>ChooseBGRightButton_Left&&x<ChooseBGRightButton_Right&&y>ChooseBGRightButton_Top&&y<ChooseBGRightButton_Bottom)
			{
				backgroundIndex=(backgroundIndex+1)%4;
			}
			//ѡ�񷵻ذ�ť
			else if(x>ChooseBGBack_Left&&x<ChooseBGBack_Right&&y>ChooseBGBack_Top&&y<ChooseBGBack_Bottom)
			{
				BN2DObject bo=new BN2DObject(800,1550,400, 200,TextureManager.getTextures("back 2.png"),ShaderManager.getShader(0));
				synchronized(lock)
				{
					al.remove(7);
					al.add(7,bo);
				}
			}
			//ѡ�� ���ñ�����ɫ��ť
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
				mv.currView=mv.mainView;//�ص�������
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
	
	public void changeBackGroundAL(int index)//�ı䱳��ͼ
	{
		if(index==0)//��ǰ����ֵΪ0 �����0��1��2
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
		}else if(index==1)//��ǰ����ֵΪ1 �����1��2��3
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
		}else if(index==2)//��ǰ����ֵΪ2 �����2��3��0
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
		}else if(index==3)//��ǰ����ֵΪ3 �����3��0��1
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
		synchronized(lockA)//���Ʊ���ͼ
		{
			for(BN2DObject bo:backgroundAL)
			{
				bo.drawSelf(0);
			}
		}
	}
}
