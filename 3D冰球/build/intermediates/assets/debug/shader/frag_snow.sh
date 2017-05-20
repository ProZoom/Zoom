precision mediump float;
uniform vec4 startColor;//��ʼ��ɫ
uniform vec4 endColor;//��ֹ��ɫ
uniform float sjFactor;//˥������
uniform float bj;//�뾶
uniform sampler2D sTexture;//������������
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
varying vec3 vPosition;
void main()                         
{               
    vec4 colorTL = texture2D(sTexture, vTextureCoord); 
    vec4 colorT;
    float disT=distance(vPosition,vec3(0.0,0.0,0.0));
    float tampFactor=(1.0-disT/bj)*sjFactor;
    vec4 factor4=vec4(tampFactor,tampFactor,tampFactor,tampFactor);
    colorT=clamp(factor4,endColor,startColor);
    colorT=colorT*colorTL.a;  
    gl_FragColor=colorT;
}