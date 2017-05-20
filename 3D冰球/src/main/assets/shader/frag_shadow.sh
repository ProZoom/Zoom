precision mediump float;				//����Ĭ�ϵĸ��㾫��
uniform highp int isShadow;			//��Ӱ���Ʊ�־
uniform sampler2D sTexture;//������������
varying vec4 ambient;  				//�Ӷ�����ɫ�����ݹ����Ļ���������ǿ��
varying vec4 diffuse;					//�Ӷ�����ɫ�����ݹ�����ɢ�������ǿ��
varying vec4 specular;				//�Ӷ�����ɫ�����ݹ����ľ��������ǿ��
varying vec2 vTextureCoord;
void main() { 
   	if(isShadow==0){						//�������屾��
	   vec4 finalColor=texture2D(sTexture, vTextureCoord);		//���屾�����ɫ
	    //�ۺ�����ͨ���������ǿ�ȼ�ƬԪ����ɫ���������ƬԪ����ɫ�����ݸ�����
	    gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
   	}else{								//������Ӱ
	   gl_FragColor = vec4(0.2,0.2,0.2,0.5);//ƬԪ������ɫΪ��Ӱ����ɫ
   	}
}     
