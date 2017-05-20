package com.bn.constant;

import static com.bn.constant.Constant.*;

public class GetPositionUtil
{
	public static float[] get3DPosition(float x,float y)//计算交点坐标
	{
		float[] result=new float[2];
		float[] AB=IntersectantUtil.calculateABPosition
				(
					x, //触控点X坐标
					y, //触控点Y坐标
					Constant.StandardScreenWidth, //屏幕宽度
					Constant.StandardScreenHeight, //屏幕长度
					left,//视角left、top值
					top,
					near,//视角near、far值
					far
				);
	    float mt=(float)AB[1]/(float)(AB[1]-AB[4]);//计算斜率
	    result[0]=AB[0]+(AB[3]-AB[0])*mt;//计算x位移
	    result[1]=AB[2]+(AB[5]-AB[2])*mt;//计算z位移
	    return result;
	}
	public static float[] limitPositionResult(float x,float y)//获得坐标限制后的结果
	{
		float[] result = get3DPosition(x , y);
		if(result[0]<=-3.45f)//不超过左边框
		{
			result[0]=-3.45f;
		}else if(result[0]>=3.64f)//不超过右边框
		{
			result[0]=3.64f;
		}
		if(result[1]<=0.88f)//不超过中心线
		{
			result[1]=0.88f;
		}else if(result[1]>=8f)//不超过底部圆
		{
			result[1]=8f; 
		}
		return result;
	}
}
