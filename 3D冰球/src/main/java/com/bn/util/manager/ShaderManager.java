package com.bn.util.manager;

import java.util.HashMap;

import com.bn.constant.ShaderUtil;
import com.bn.happyhockey.MySurfaceView;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class ShaderManager 
{
	static String[][] programs={{"vertex.sh","frag.sh"},{"vertex_shadow.sh","frag_shadow.sh"},
		{"vertex_snow.sh","frag_snow.sh"},{"vertex_fly.sh","frag_fly.sh"},{"vertex_line.sh","frag_line.sh"}};//������ɫ��������
	static HashMap<Integer,Integer> list=new HashMap<Integer,Integer>();
	public static void loadingShader(MySurfaceView mv)//������ɫ��
	{
		for(int i=0;i<programs.length;i++)
		{
			//���ض�����ɫ���Ľű�����
			String mVertexShader=ShaderUtil.loadFromAssetsFile(programs[i][0], mv.getResources());
			//����ƬԪ��ɫ���Ľű�����
			String mFragmentShader=ShaderUtil.loadFromAssetsFile(programs[i][1],mv.getResources());
			//���ڶ�����ɫ����ƬԪ��ɫ����������
			int mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
			list.put(i, mProgram);
		}  
	}
	public static int getShader(int index)//���ĳ�׳���
	{
		int result=0;
		if(list.get(index)!=null)//����б����д��׳���
		{
			result=list.get(index);
		}
		return result;
	}
}
