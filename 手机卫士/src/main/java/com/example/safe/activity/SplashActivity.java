package com.example.safe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.safe.R;
import com.example.safe.UpdateDialog;
import com.example.safe.bean.VersionBean;
import com.example.safe.engine.TastManagerEngine;
import com.example.safe.utils.L;
import com.example.safe.utils.T;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.safe.utils.Contents.VersionCode;
import static com.example.safe.utils.Contents.VersionName;


/**
 * 作者：   liyang
 * 创建时间：    下午9:41
 * 描述：  开发Splash界面
 *
 */
public class SplashActivity extends Activity {

    private static final int LOADMAIN = 0;
    private static final int SHOWUPDATEDIALOG =1 ;
    private RelativeLayout rl_splash;
    private TextView tv_versionName;
    private VersionBean versionBean;
    private UpdateDialog ud;
    private long startTime;//记录开始访问网络的事件
    private String newApkPath= Environment.getExternalStorageDirectory().getPath()+"/Download/222.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        L.i(newApkPath);
        TastManagerEngine.getToatleMemSize(this);

        TastManagerEngine.getAvailMemSize(this);

        //初始化界面
        initView();
        //初始化数据
        initdata();
        //初始化动画
        initAnimation();
        //检查版本
        checkVersion();

    }

    /**
     * 函数功能描述：初始化数据
     */
    private void initdata() {

        PackageManager pm =getPackageManager();

        try {
            PackageInfo packageInfo=pm.getPackageInfo(getPackageName(),0);

            //获取版本名
            VersionName=packageInfo.versionName;
            //获取版本号
            VersionCode=packageInfo.versionCode;

            tv_versionName.setText("加强版"+VersionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 函数功能描述：
    * 检查版本是否有更新
     */
    private void checkVersion() {

        new Thread(){

            @Override
            public void run() {

                try {

                    startTime = System.currentTimeMillis();

                    URL url=new URL("http://192.168.0.103:8080/version.json");

                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();
                    if(responseCode==200){//请求成功
                        InputStream inputStream = conn.getInputStream();//获取字节流

                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(inputStream));//把字节流转换成缓存字节流

                        String line = reader.readLine();//读取一行数据
                        StringBuilder jsonString=new StringBuilder();

                        while(line!=null){//有数据
                            jsonString.append(line);

                            line=reader.readLine();//继续读取下一行数据
                        }

                        L.i(jsonString+"");


                        //解析json数据
                        versionBean = parseVersionJson(jsonString);

                        L.i("version:"+versionBean.getVersion()+
                                "   url:"+versionBean.getUrl()+
                                "    desc:"+versionBean.getDesc());

                        //判断是否有新版本
                        isNewVersion(versionBean);
                        reader.close();
                        conn.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 函数功能描述：UI线程与线程通信
     */
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //处理消息
            switch(msg.what){
                case LOADMAIN://加载主界面
                    loadMain();
                    break;
                case SHOWUPDATEDIALOG:
                    showUpdateDialog();
                    break;
                default:break;
            }
        }
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("提醒")
                .setMessage("检查到新版本，是否更新新版本")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //更新事件
                        L.i("更新");
                        downloadNewApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消事件,进入主界面
                        L.i("取消");
                       loadMain();
                    }
                });
        builder.show();
    }

    private void downloadNewApk() {

        L.i("下载newApk");
        String urlPath=versionBean.getUrl();
        L.i(urlPath);
        RequestParams params=new RequestParams(urlPath);

        params.setSaveFilePath(newApkPath);
       // params.setSaveFilePath("/mnt/sdcard/222.apk");

        params.setAutoRename(true);//断点下载
        params.getConnectTimeout();


        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                L.i("onWaiting");
            }

            @Override
            public void onStarted() {
                L.i("onStart");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                L.i("onLoading");

                ud.show();

                ud.setMax((int) total);
                ud.setProgress((int) current);


            }

            @Override
            public void onSuccess(File result) {

                L.i("下载新版本成功"+result);
                T.showShort(getApplicationContext(),"下载新版本成功");

                installApk();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.i("下载新版本失败");
                T.showShort(getApplicationContext(),"下载新版本失败");
                loadMain();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                L.i("下载取消");
                T.showShort(getApplicationContext(),"下载取消");
                loadMain();
            }

            @Override
            public void onFinished() {
                L.i("下载完成");
                T.showShort(getApplicationContext(),"下载完成");

                loadMain();
            }
        });
    }

    private void installApk() {

        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type="application/vnd.android.package-archive";
        Uri Data=Uri.fromFile(new File(newApkPath));

        intent.setDataAndType(Data,type);

        startActivity(intent);
    }

    private void loadMain() {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }

    /**
     * 函数功能描述：判断是否有新版本,有的话更新
     */
    private void isNewVersion(VersionBean vb) {

        //获取服务器版本
        int serverCode=vb.getVersion();

        long endTime = System.currentTimeMillis() - startTime;
        if(endTime<3000){
            SystemClock.sleep(3000-endTime);
        }
        L.i("serverCode: "+serverCode+"");

        if(serverCode==VersionCode){//进入主界面
            //进入主界面
            Message msg=Message.obtain();
            msg.what=LOADMAIN;
            handler.sendMessage(msg);

        }else {//更新
            //弹出窗口进行更新
            Message msg=Message.obtain();
            msg.what=SHOWUPDATEDIALOG;
            handler.sendMessage(msg);
        }
    }


    /**
     * 函数功能描述解析json数据
     */

    private VersionBean parseVersionJson(StringBuilder jsonString) {


        VersionBean vb=new VersionBean();
        JSONObject jsonObject;

        try {
            //把json字符串封装成json数据
            jsonObject=new JSONObject(String.valueOf(jsonString));

            //获取数据
            int version=jsonObject.getInt("version");

            String apkPath = jsonObject.getString("url");

            String desc=jsonObject.getString("desc");

            //将数据封装对象中
            vb.setDesc(desc);
            vb.setUrl(apkPath);
            vb.setVersion(version);

            L.i("qversion:"+vb.getVersion()+
                    "   url:"+vb.getUrl()+
                    "    desc:"+vb.getDesc());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vb;
    }

    /**
     * 描述：初始化界面
     */
    private void initView() {
        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        tv_versionName= (TextView) findViewById(R.id.tv_splash_version_name);

        ud=new UpdateDialog(this);
        ud.setMessage("正在下载...");
        ud.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 描述：splash动画
     */
    private void initAnimation() {

        AnimationSet as=new AnimationSet(true);


        AlphaAnimation aa=new AlphaAnimation(0,1);


        ScaleAnimation sa=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);

        RotateAnimation ra=new RotateAnimation(0,1800,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);

        as.addAnimation(aa);
        as.addAnimation(sa);
        as.addAnimation(ra);
        //界面停留在动画结束界面
        as.setFillAfter(true);
        as.setDuration(3000);

        rl_splash.startAnimation(as);
    }
}
