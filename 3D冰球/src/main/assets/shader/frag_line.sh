precision mediump float;				//给出默认的浮点精度
uniform sampler2D sTexture;//纹理内容数据
varying vec4 ambient;  				//从顶点着色器传递过来的环境光最终强度
varying vec4 diffuse;					//从顶点着色器传递过来的散射光最终强度
varying vec4 specular;				//从顶点着色器传递过来的镜面光最终强度
varying vec2 vTextureCoord;
uniform float sjFactor;//衰减因子
void main() {						//绘制物体本身
	vec4 finalColor=texture2D(sTexture, vTextureCoord)*sjFactor;		//物体本身的颜色
	//综合三个通道光的最终强度及片元的颜色计算出最终片元的颜色并传递给管线
	gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
}     