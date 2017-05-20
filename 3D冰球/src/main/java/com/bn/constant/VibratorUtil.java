package com.bn.constant;

import android.app.Service;
import android.os.Vibrator;

import com.bn.happyhockey.MainActivity;
	  
/** 
 * �ֻ��𶯹����� 
 * @author Administrator 
 * 
 */  
public class VibratorUtil {  
	
	/** 
	 * final Activity activity  �����ø÷�����Activityʵ�� 
	 * long milliseconds ���𶯵�ʱ������λ�Ǻ��� 
	 * long[] pattern  ���Զ�����ģʽ �����������ֵĺ���������[��ֹʱ������ʱ������ֹʱ������ʱ��������]ʱ���ĵ�λ�Ǻ��� 
	 * boolean isRepeat �� �Ƿ񷴸��𶯣������true�������𶯣������false��ֻ��һ�� 
	 */  
	public static void Vibrate(final MainActivity activity, long milliseconds) 
	{
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);   
		vib.vibrate(milliseconds);   
	}
}
