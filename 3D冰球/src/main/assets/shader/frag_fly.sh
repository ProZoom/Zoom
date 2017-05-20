precision mediump float;
uniform float sjFactor;//衰减因子
uniform sampler2D sTexture;//纹理内容数据
varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
void main()                         
{
    gl_FragColor=texture2D(sTexture, vTextureCoord)*sjFactor;
}