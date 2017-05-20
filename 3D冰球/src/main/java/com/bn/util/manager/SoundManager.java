package com.bn.util.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bn.happyhockey.MainActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	MainActivity activity;//定义activity
	HashMap<String,PuSound> soundList=new HashMap<String,PuSound>();//定义存放声音的HashMap
	boolean isCirculate=true;//是否循环查找
	boolean circulate=true;//是否循环查找
	AssetManager assetManager;//获得assets目录
	AssetFileDescriptor temp;
	SoundPool soundPool;
	int count=0;//循环次数计数器
	long min=0;//比较得出最小的时间
	float volumn=0;//音量
	String soundNameFinal=null;
	int loopFinal=0;
	public SoundManager(MainActivity activity)//构造器
	{
		this.activity=activity;
		assetManager= activity.getAssets();
		soundPool=new SoundPool(6,AudioManager.STREAM_MUSIC,100);//创建
	}
	public int getSound(String soundName)//获得声音
	{
		int spTemp=-1;
		isCirculate=true;//每次调用这个方法，循环查找即为true
		while(isCirculate)
		{
			if(soundList.get(soundName)!=null)//如果HashMap中有此音效
			{
				spTemp=soundList.get(soundName).soundID;//直接获得其ID
				soundList.get(soundName).date=System.nanoTime();
				isCirculate=false;
			}else//如果HashMap中没有此音效
			{
				Loading(soundName);//加载音效
			}
		}
		return spTemp;
	}
	@SuppressWarnings("rawtypes")
	public void Loading(String soundName)//加载音效
	{
		PuSound soundTemp = new PuSound();
		if(soundList.size()<5)//如果HashMap长度小于5，直接加载音效
		{
			try {
				String path="sound/"+soundName;
				temp=assetManager.openFd(path);//加载音效
				soundTemp.soundID=soundPool.load(temp, 1);//获得声音ID
				soundTemp.date=System.nanoTime();
				soundList.put(soundName,soundTemp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else//如果HashMap长度大于5，删除部分音效，加载需要的音效
		{
			Iterator<Entry<String, PuSound>> iter = soundList.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry = (Map.Entry) iter.next();
				if(count==0)//第一次循环
				{
					min=((PuSound)entry.getValue()).date;//记录当前日期值
					count++;//计数器加1
				}else
				{
					if(min>((PuSound)entry.getValue()).date)//如果当前日期大于获得的日期
					{
						min=((PuSound)entry.getValue()).date;//赋值给最小日期
					}
				}
			}
			soundList.remove(min);//删除音效
			try {
				String path="sound/"+soundName;
				temp=assetManager.openFd(path);//加载音效
				soundTemp.soundID=soundPool.load(temp, 1);//获得声音ID
				soundTemp.date=System.nanoTime();//获得当前时间
				soundList.put(soundName,soundTemp);//将此声音加到HashMap中
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void LoadingSound(String soundName,int loop)//播放音效前的准备工作
	{
		soundNameFinal=soundName;//获取音效名称
		loopFinal=loop;//获取循环次数
		circulate=true;//是否循环
		AudioManager mgr = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//获得当前音量
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获得最大音量
		volumn=1.5f*(streamVolumeCurrent/streamVolumeMax);//计算声音播放的音量
	}
	public void playSound(String soundName,int loop)
	{
		LoadingSound(soundName,loop);
		while(circulate)//是否循环
		{
			if(getSound(soundName)!=-1)//如果获得了此音效
			{
				try
				{
					Thread.sleep(35);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				soundPool.play//开启音效
				(
						soundList.get(soundNameFinal).soundID,//声音资源id
						volumn,					//左声道音量
						volumn,					//右声道音量
						1,						//优先级				 
						loopFinal,				//循环次数 -1代表永远循环
						1f						//回放速度0.5f～2.0f之间
				);
				circulate=false;//不再循环
			}
		}
	}
}
class PuSound
{
	public int soundID;
	public long date;
}
