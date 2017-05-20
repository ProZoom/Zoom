package com.bn.constant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import com.bn.happyhockey.MySurfaceView;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.os.Environment;

public class ScreenShot//������
{
	int count=0;
	MySurfaceView mv;
	Matrix matrix = new Matrix();
	public boolean saveFlag=false;
	public boolean isAllowed=true;//�Ƿ��������
	public ScreenShot(MySurfaceView mv)
	{
		this.mv=mv;
	}
	public synchronized void setFlag(boolean flag)//���ý�����־λ
	{
		saveFlag=flag;
	}
	public void saveScreen(final int screenWidth,final int screenHeight)
    {
		isAllowed=false;//���������
        matrix.reset();  
        matrix.setRotate(180); //��ת180��
        matrix.postScale(-1, 1);
        
    	final ByteBuffer cbbTemp = ByteBuffer.allocateDirect(screenWidth*screenHeight*4);
    	GLES20.glReadPixels(0, 0, screenWidth, screenHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, cbbTemp);
    	
    	new Thread()
    	{
    		public void run()
    		{
    			Bitmap bm =Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
    	    	bm.copyPixelsFromBuffer(cbbTemp);
    	    	bm=Bitmap.createBitmap(bm, 0, 0, screenWidth, screenHeight, matrix, true);
    	    	try
    			{
    	    		File sd=Environment.getExternalStorageDirectory();
    	    		String path=sd.getPath()+"/HappyHockey";
    	    		File file=new File(path);
    	    		if(!file.exists())
    	    		{
    	    			file.mkdirs();
    	    		}
    	    		File myFile = File.createTempFile
    	    				(
    	    						"ScreenShot"+count,  //�����ļ���
    	    						".png",     //��׺
    	    						file//Ŀ¼·��
    	    				);
    	    		
    				 FileOutputStream fout=new FileOutputStream(myFile);
    				 BufferedOutputStream bos = new BufferedOutputStream(fout);  
    				 bm.compress
    				 (
    						 Bitmap.CompressFormat.PNG,   //ͼƬ��ʽ
    						 100, 						   //Ʒ��0-100
    						 bos						   //ʹ�õ������
    				  );
    				 bos.flush();
    				 bos.close();
    				 System.out.println("����ɹ����ļ�����"+myFile.getName());
    				 mv.handler.sendEmptyMessage(0);
    				 isAllowed=true;//�������
    				 count++;
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    				System.out.println("����ʧ�ܣ�");
    				mv.handler.sendEmptyMessage(1);
    			}
    		}
    	}.start();
    }
}
