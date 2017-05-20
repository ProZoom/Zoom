package com.bn.object;

import org.jbox2d.dynamics.Body;

import com.bn.constant.MatrixState3D;

public class GameObject 
{
	LoadedObjectVertexNormalTexture lovnt;
	int texId;
	public Body gt;
	float scale;
	
	public GameObject(LoadedObjectVertexNormalTexture lovnt,int texId,float scale,Body gt)
	{
		this.lovnt=lovnt;
		this.texId=texId;
		this.scale=scale;
		this.gt=gt;
	}
    public void drawSelf(int isShadow,float x,float y,float shadowPosition)
    {
    	MatrixState3D.pushMatrix();//保护现场 
    	MatrixState3D.translate(x, 0, y);
    	MatrixState3D.scale(scale,scale,scale);
    	lovnt.drawSelf(texId,isShadow,shadowPosition);
		MatrixState3D.popMatrix();//恢复现场
    }
}
