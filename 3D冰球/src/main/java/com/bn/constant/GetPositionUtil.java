package com.bn.constant;

import static com.bn.constant.Constant.*;

public class GetPositionUtil
{
	public static float[] get3DPosition(float x,float y)//���㽻������
	{
		float[] result=new float[2];
		float[] AB=IntersectantUtil.calculateABPosition
				(
					x, //���ص�X����
					y, //���ص�Y����
					Constant.StandardScreenWidth, //��Ļ���
					Constant.StandardScreenHeight, //��Ļ����
					left,//�ӽ�left��topֵ
					top,
					near,//�ӽ�near��farֵ
					far
				);
	    float mt=(float)AB[1]/(float)(AB[1]-AB[4]);//����б��
	    result[0]=AB[0]+(AB[3]-AB[0])*mt;//����xλ��
	    result[1]=AB[2]+(AB[5]-AB[2])*mt;//����zλ��
	    return result;
	}
	public static float[] limitPositionResult(float x,float y)//����������ƺ�Ľ��
	{
		float[] result = get3DPosition(x , y);
		if(result[0]<=-3.45f)//��������߿�
		{
			result[0]=-3.45f;
		}else if(result[0]>=3.64f)//�������ұ߿�
		{
			result[0]=3.64f;
		}
		if(result[1]<=0.88f)//������������
		{
			result[1]=0.88f;
		}else if(result[1]>=8f)//�������ײ�Բ
		{
			result[1]=8f; 
		}
		return result;
	}
}
