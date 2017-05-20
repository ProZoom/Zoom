package com.bn.util.manager;

import java.util.HashMap;

import com.bn.constant.ShaderUtil;
import com.bn.happyhockey.MySurfaceView;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class ShaderManager 
{
	static String[][] programs={{"vertex.sh","frag.sh"},{"vertex_shadow.sh","frag_shadow.sh"},
		{"vertex_snow.sh","frag_snow.sh"},{"vertex_fly.sh","frag_fly.sh"},{"vertex_line.sh","frag_line.sh"}};//所有着色器的名称
	static HashMap<Integer,Integer> list=new HashMap<Integer,Integer>();
	public static void loadingShader(MySurfaceView mv)//加载着色器
	{
		for(int i=0;i<programs.length;i++)
		{
			//加载顶点着色器的脚本内容
			String mVertexShader=ShaderUtil.loadFromAssetsFile(programs[i][0], mv.getResources());
			//加载片元着色器的脚本内容
			String mFragmentShader=ShaderUtil.loadFromAssetsFile(programs[i][1],mv.getResources());
			//基于顶点着色器与片元着色器创建程序
			int mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
			list.put(i, mProgram);
		}  
	}
	public static int getShader(int index)//获得某套程序
	{
		int result=0;
		if(list.get(index)!=null)//如果列表中有此套程序
		{
			result=list.get(index);
		}
		return result;
	}
}
