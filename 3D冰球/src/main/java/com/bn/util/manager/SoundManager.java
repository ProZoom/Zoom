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
	
	MainActivity activity;//����activity
	HashMap<String,PuSound> soundList=new HashMap<String,PuSound>();//������������HashMap
	boolean isCirculate=true;//�Ƿ�ѭ������
	boolean circulate=true;//�Ƿ�ѭ������
	AssetManager assetManager;//���assetsĿ¼
	AssetFileDescriptor temp;
	SoundPool soundPool;
	int count=0;//ѭ������������
	long min=0;//�Ƚϵó���С��ʱ��
	float volumn=0;//����
	String soundNameFinal=null;
	int loopFinal=0;
	public SoundManager(MainActivity activity)//������
	{
		this.activity=activity;
		assetManager= activity.getAssets();
		soundPool=new SoundPool(6,AudioManager.STREAM_MUSIC,100);//����
	}
	public int getSound(String soundName)//�������
	{
		int spTemp=-1;
		isCirculate=true;//ÿ�ε������������ѭ�����Ҽ�Ϊtrue
		while(isCirculate)
		{
			if(soundList.get(soundName)!=null)//���HashMap���д���Ч
			{
				spTemp=soundList.get(soundName).soundID;//ֱ�ӻ����ID
				soundList.get(soundName).date=System.nanoTime();
				isCirculate=false;
			}else//���HashMap��û�д���Ч
			{
				Loading(soundName);//������Ч
			}
		}
		return spTemp;
	}
	@SuppressWarnings("rawtypes")
	public void Loading(String soundName)//������Ч
	{
		PuSound soundTemp = new PuSound();
		if(soundList.size()<5)//���HashMap����С��5��ֱ�Ӽ�����Ч
		{
			try {
				String path="sound/"+soundName;
				temp=assetManager.openFd(path);//������Ч
				soundTemp.soundID=soundPool.load(temp, 1);//�������ID
				soundTemp.date=System.nanoTime();
				soundList.put(soundName,soundTemp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else//���HashMap���ȴ���5��ɾ��������Ч��������Ҫ����Ч
		{
			Iterator<Entry<String, PuSound>> iter = soundList.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry = (Map.Entry) iter.next();
				if(count==0)//��һ��ѭ��
				{
					min=((PuSound)entry.getValue()).date;//��¼��ǰ����ֵ
					count++;//��������1
				}else
				{
					if(min>((PuSound)entry.getValue()).date)//�����ǰ���ڴ��ڻ�õ�����
					{
						min=((PuSound)entry.getValue()).date;//��ֵ����С����
					}
				}
			}
			soundList.remove(min);//ɾ����Ч
			try {
				String path="sound/"+soundName;
				temp=assetManager.openFd(path);//������Ч
				soundTemp.soundID=soundPool.load(temp, 1);//�������ID
				soundTemp.date=System.nanoTime();//��õ�ǰʱ��
				soundList.put(soundName,soundTemp);//���������ӵ�HashMap��
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void LoadingSound(String soundName,int loop)//������Чǰ��׼������
	{
		soundNameFinal=soundName;//��ȡ��Ч����
		loopFinal=loop;//��ȡѭ������
		circulate=true;//�Ƿ�ѭ��
		AudioManager mgr = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//��õ�ǰ����
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//����������
		volumn=1.5f*(streamVolumeCurrent/streamVolumeMax);//�����������ŵ�����
	}
	public void playSound(String soundName,int loop)
	{
		LoadingSound(soundName,loop);
		while(circulate)//�Ƿ�ѭ��
		{
			if(getSound(soundName)!=-1)//�������˴���Ч
			{
				try
				{
					Thread.sleep(35);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				soundPool.play//������Ч
				(
						soundList.get(soundNameFinal).soundID,//������Դid
						volumn,					//����������
						volumn,					//����������
						1,						//���ȼ�				 
						loopFinal,				//ѭ������ -1������Զѭ��
						1f						//�ط��ٶ�0.5f��2.0f֮��
				);
				circulate=false;//����ѭ��
			}
		}
	}
}
class PuSound
{
	public int soundID;
	public long date;
}
