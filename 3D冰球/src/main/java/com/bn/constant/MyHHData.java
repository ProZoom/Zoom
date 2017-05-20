package com.bn.constant;

import java.util.ArrayList;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import com.bn.object.BN2DObject;
import com.bn.util.box2d.Box2DUtil;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class MyHHData {
	public static ArrayList<Body> boxList=new ArrayList<Body>();
	public static void createEdage(World world)//�����߿����
	{
		//��̨�ϱ�Ե--left
		boxList.add(Box2DUtil.createBox(-3.46f,9.3f, world, 1.54f, 0.7f, true,-1));
		//��̨�ϱ�Ե--right
		boxList.add(Box2DUtil.createBox(3.46f,9.3f, world, 1.54f, 0.7f, true,-1));
		//��̨�±�Ե--left
		boxList.add(Box2DUtil.createBox(-3.46f,-9.3f, world, 1.54f, 0.7f, true,-1));
		//��̨�±�Ե--right
		boxList.add(Box2DUtil.createBox(3.46f,-9.3f, world, 1.54f, 0.7f, true,-1));
		//��̨���Ե
		boxList.add(Box2DUtil.createBox(-4.65f,0.0f, world, 0.35f, 8.6f, true,-1));
		//��̨�ұ�Ե
		boxList.add(Box2DUtil.createBox(4.65f,0.0f, world, 0.35f, 8.6f, true,-1));
	}
	public static ArrayList<BN2DObject[]> createGrade()//���Ƶ÷����
	{
		ArrayList<BN2DObject[]> gradeData=new ArrayList<BN2DObject[]>();//���Ʒ�������--0Ϊһ������  1Ϊһ������
		BN2DObject data1[][]=new BN2DObject[2][10];
		
		for(int i=0;i<10;i++)
		{
			data1[0][i]=new BN2DObject(i, TextureManager.getTextures("nb1.png"), 
					ShaderManager.getShader(0)
					);
			data1[1][i]=new BN2DObject(i, TextureManager.getTextures("nb5.png"), 
					ShaderManager.getShader(0)
					);
		}
		gradeData.add(data1[0]);
		gradeData.add(data1[1]);
		return gradeData;
	}
	public static ArrayList<BN2DObject[]> createTimer()//���Ƽ�ʱ���
	{
		ArrayList<BN2DObject[]> TimerData=new ArrayList<BN2DObject[]>();//���Ƽ�ʱ����--0Ϊһ������  1Ϊһ������
		BN2DObject data1[][]=new BN2DObject[2][10];
		
		for(int i=0;i<10;i++)
		{
			data1[0][i]=new BN2DObject(i, TextureManager.getTextures("time 1.png"), 
					ShaderManager.getShader(0)
					);
			data1[1][i]=new BN2DObject(i, TextureManager.getTextures("time 2.png"), 
					ShaderManager.getShader(0)
					);
		}
		TimerData.add(data1[0]);
		TimerData.add(data1[1]);
		return TimerData;
	}
	public static ArrayList<BN2DObject> winView()//����ʤ�������Ԫ��
	{
		ArrayList<BN2DObject> winData=new ArrayList<BN2DObject>();//���ʤ�����������Ԫ��
		winData.add(
				new BN2DObject(
						1080/2,400,500,300, 
						TextureManager.getTextures("win.png"),
						ShaderManager.getShader(0)
						));
		winData.add(
				new BN2DObject(
						1080/2,900,600,280, 
						TextureManager.getTextures("restart 1.png"),
						ShaderManager.getShader(0)
						));
		winData.add(
				new BN2DObject(
						1080/2,1500,600,280, 
						TextureManager.getTextures("resume 1.png"),
						ShaderManager.getShader(0)
						));
		return winData;
	}
	public static ArrayList<BN2DObject> pauseView()//������ͣ�����Ԫ��
	{
		ArrayList<BN2DObject> pauseData=new ArrayList<BN2DObject>();//���ʤ�����������Ԫ��
		pauseData.add(
				new BN2DObject(
						1080/2,1920/2-400,500,300, 
						TextureManager.getTextures("pause.png"),
						ShaderManager.getShader(0)
						));
		pauseData.add(
				new BN2DObject(
						1080/2,950,600,280, 
						TextureManager.getTextures("restart 1.png"),
						ShaderManager.getShader(0)
						));
		pauseData.add(
				new BN2DObject(
						1080/2,1250,600,280, 
						TextureManager.getTextures("resume 3.png"),
						ShaderManager.getShader(0)
						));
		pauseData.add(
				new BN2DObject(
						1080/2,1550,600,280, 
						TextureManager.getTextures("resume 1.png"),
						ShaderManager.getShader(0)
						));
		return pauseData;
	}
}
