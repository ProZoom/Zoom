precision mediump float;
uniform float sjFactor;//˥������
uniform sampler2D sTexture;//������������
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
void main()                         
{
    gl_FragColor=texture2D(sTexture, vTextureCoord)*sjFactor;
}