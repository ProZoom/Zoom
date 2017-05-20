package com.example.safe.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safe.R;
import com.example.safe.bean.AppBean;
import com.example.safe.engine.AppManagerEngine;
import com.example.safe.utils.L;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class AppManagerActivity extends Activity implements View.OnClickListener {

    private static final int FINISH =1 ;
    private static final int LOADING = 0;
    private long sdAvail;
    private long romAvail;

    //
    private myAdapter adapter;

    //用户apk容器
    private List<AppBean> usersApks=new ArrayList<>();
    //系统apk容器
    private List<AppBean> sysApks=new ArrayList<>();

    TextView tv_Rom,tv_SD,tv_listview_lebel;
    ListView lv;
    ProgressBar pb;
    PackageManager pm;


    AppBean clickBean;
    PopupWindow pw;
    View popuView;
    ScaleAnimation sa;


    BroadcastReceiver receiver=null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case LOADING:
                    pb.setVisibility(View.VISIBLE);//显示加载数据进度
                    lv.setVisibility(View.GONE);//隐藏listview
                    break;

                case FINISH://数据加载完成
                    pb.setVisibility(View.GONE);//
                    lv.setVisibility(View.VISIBLE);
                    tv_Rom.setText("ROM可用空间: "+ Formatter.formatFileSize(getApplicationContext(),romAvail));
                    tv_SD.setText("SD可用空间: "+Formatter.formatFileSize(getApplicationContext(),sdAvail));
                    adapter.notifyDataSetChanged();//刷新画面
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);

        //初始化界面
        initView();
        //初始化数据
        initData();
        //初始化事件
        initEvent();
        //初始化弹出窗口
        initPopupWindow();
        //注册删除apk的广播接受者
        initRemoveApkReceiver();
    }

    @Override
    protected void onDestroy() {
        //取消注册删除apk的广播
        unregisterReceiver(receiver);

        super.onDestroy();
    }

    private void initRemoveApkReceiver() {
        receiver=new BroadcastReceiver() {
            //删除apk（包括系统的apk）的监听广告
            @Override
            public void onReceive(Context context, Intent intent) {
                L.i("Broadcast----onReceive");
                //更新数据
                initData();
                //数据删除
            }
        };


        //注册广播
        IntentFilter filter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        //注意配置数据模式
        filter.addDataScheme("package");
        registerReceiver(receiver,filter);
    }


    private void showPopupWindow(View parent,int x,int y){
        closePopupWindow();
        pw.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,x,y);
       popuView.startAnimation(sa);
    }
    private void closePopupWindow(){
        if(pw!=null&&pw.isShowing()){
            pw.dismiss();//关闭
        }
    }

    private void initPopupWindow() {
        //准备布局
        popuView=View.inflate(this,R.layout.popup_appmanager,null);

        LinearLayout ll_uninstall= (LinearLayout) popuView.findViewById(R.id.ll_uninstall);
        LinearLayout ll_setup= (LinearLayout) popuView.findViewById(R.id.ll_setup);
        LinearLayout ll_share= (LinearLayout) popuView.findViewById(R.id.ll_share);
        LinearLayout ll_setting= (LinearLayout) popuView.findViewById(R.id.ll_setting);

        ll_uninstall.setOnClickListener(this);
        ll_setup.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_share.setOnClickListener(this);



        //初始化弹出窗口
        pw=new PopupWindow(popuView,-2,-2);

        //弹出动画
        sa= new ScaleAnimation(0,1,0.5f,1,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0.5f);

        sa.setDuration(3000);

    }

    private void initEvent() {

        //Listview点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position: "+position);

                //判断是否是标签,是标签不做处理
                if(position==0||position==usersApks.size()+1){
                    return;
                }
                //
                clickBean = (AppBean) lv.getItemAtPosition(position);

                int[] location=new int[2];
                view.getLocationInWindow(location);
                showPopupWindow(view,location[0]+150,location[1]);
            }
        });

        //listview滑动事件处理
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            /**
             * 描述：滑动事件完成后，即禁止后的方法处理
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                L.i("onScrollstatechanged");
            }
            /**
             * 描述：滑动过程中的方法处理
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                L.i("onScroll");

                closePopupWindow();
                if(firstVisibleItem==0){
                    tv_listview_lebel.setVisibility(View.GONE);
                }else if(firstVisibleItem>0&&firstVisibleItem<usersApks.size()+1){
                        tv_listview_lebel.setVisibility(View.VISIBLE);
                        tv_listview_lebel.setText("个人软件("+usersApks.size()+")");
                    }else if(firstVisibleItem>usersApks.size()){
                        tv_listview_lebel.setText("系统软件("+sysApks.size()+")");
                        }
            }
        });
    }

    private void initData() {

        new Thread(){
            @Override
            public void run() {

                //通知正在加载数据
                handler.obtainMessage(LOADING).sendToTarget();

                SystemClock.sleep(1000);
                List<AppBean> datas=null;
                //取数据
                datas = AppManagerEngine.getAllApk(getApplicationContext());
                //添加新数据之前要清空数据
                sysApks.clear();
                usersApks.clear();
                for (AppBean appBean:datas){
                    if(appBean.isSystem()){
                        sysApks.add(appBean); //系统Apk
                    }else {
                        usersApks.add(appBean);//用户apks
                    }
                }

                sdAvail=AppManagerEngine.getSDAvail(getApplicationContext());
                romAvail=AppManagerEngine.getRomAvail(getApplicationContext());
                //数据处理完毕，发送完成信息
                handler.obtainMessage(FINISH).sendToTarget();
            }
        }.start();
    }

    private void initView() {

        tv_Rom= (TextView) findViewById(R.id.tv_app_rom);//Rom剩余空间的显示
        tv_SD= (TextView) findViewById(R.id.tv_app_sd);//sd卡剩余空间的显示
        tv_listview_lebel= (TextView) findViewById(R.id.tv_listview_lebel);
        lv= (ListView) findViewById(R.id.lv_app_datas);//显示所有apk的listview
        pb= (ProgressBar) findViewById(R.id.pb_app_manager);//


        pm=getPackageManager();

        //listview适配器
        adapter=new myAdapter();

        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        
        
        switch (v.getId()){
            
            case R.id.ll_setting:
                settingCenter();
                break;
            case R.id.ll_setup:
                setupApk();
                break;
            case R.id.ll_share:
                shareApk();
                break;
            case R.id.ll_uninstall:
                removeApk();
                break;
        }
    }

    private void shareApk() {
        L.i("shareApk");

        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(shareIntent.EXTRA_STREAM,"分享该软件的内容");
        shareIntent.setType("text/plain");
        //startActivity(shareIntent);
        closePopupWindow();
        startActivity(Intent.createChooser(shareIntent,"分享到"));

    }

    private void removeApk() {
        L.i("remove");
        /*
        *
        <activity android:name=".UninstallerActivity"
                android:configChanges="orientation|keyboardHidden|screenSize"
                android:excludeFromRecents="true"
                android:theme="@style/Theme.AlertDialogActivity">
            <intent-filter>
                <action android:name="android.intent.action.DELETE" />
                <action android:name="android.intent.action.UNINSTALL_PACKAGE" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="package" />
            </intent-filter>
        </activity>
        * */
        if(clickBean.isSystem()){
            //1.系统Apk，默认卸载不了，root之后可以卸载
            //命令写在代码中
            try {
                //判断是否root刷机
                if(!RootTools.isRootAvailable()){
                    Toast.makeText(this,"请root刷机，推荐使用306刷机工具",Toast.LENGTH_LONG).show();
                    return;
                }
                //是否root权限授给当前apk
                if(!RootTools.isAccessGiven()){
                    Toast.makeText(this,"请root工具给该app权限",Toast.LENGTH_LONG).show();
                }
                //直接使用命令删除apk
                try {
                    RootTools.sendShell("mount -o remount rw /system",8000);
                    L.i("apk安装路径： "+clickBean.getApkPath());
                    RootTools.sendShell("rm -r "+clickBean.getApkPath(),8000);
                    RootTools.sendShell("mount -o remount r /system",8000);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RootToolsException e) {
                    e.printStackTrace();
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }else {
            //2.用户apk,

            Intent uninstallIntent=new Intent("android.intent.action.DELETE");
            uninstallIntent.addCategory("android.intent.category.DEFAULT");
            uninstallIntent.setData(Uri.parse("package:"+clickBean.getPackName()));
            startActivity(uninstallIntent);
        }


        //刷新自己的数据，监听package remove   注册数据广播，通过广播来更新数据
        adapter.notifyDataSetChanged();//刷新画面
    }



    private void setupApk() {
        L.i("setup");
        String packName = clickBean.getPackName();
        //通过包名获取意图
        Intent lauchIntentForPackage=pm.getLaunchIntentForPackage(packName);
        startActivity(lauchIntentForPackage);
    }

    private void settingCenter() {
        L.i("setting");

        Intent settingIntent=new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        L.i("package:"+clickBean.getPackName());
        settingIntent.setData(Uri.parse("package:"+clickBean.getPackName()));
        startActivity(settingIntent);
    }


    private class myAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return usersApks.size()+1+sysApks.size()+1;
        }

        @Override
        public Object getItem(int position) {
            AppBean bean = null;
            if(position<=usersApks.size()-1){
                bean=usersApks.get(position-1);
            }else {
                bean=sysApks.get(position-1-1-usersApks.size());
            }
            return bean;
        }

        @Override
        public long getItemId(int position) {
            return usersApks.size()+1+sysApks.size()+1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;

            if(position==0){
                TextView tv_usertable=new TextView(getApplicationContext());
                tv_usertable.setText("个人软件("+usersApks.size()+")");
                tv_usertable.setBackgroundColor(Color.GRAY);
                tv_usertable.setTextColor(Color.WHITE);
                return tv_usertable;
            }else if(position==usersApks.size()+1){

                TextView tv_systable=new TextView(getApplicationContext());
                tv_systable.setText("系统软件("+sysApks.size()+")");
                tv_systable.setBackgroundColor(Color.GRAY);
                tv_systable.setTextColor(Color.WHITE);
               return tv_systable;
            }else {
                if (convertView != null&&convertView instanceof RelativeLayout) {
                    view = convertView;
                } else {
                    view = View.inflate(getApplicationContext(), R.layout.item_appmanager_listview, null);
                }
                ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_appmanager_item_icon);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_appmanager_name_icon);
                TextView tv_local = (TextView) view.findViewById(R.id.tv_appmanager_local_icon);
                TextView tv_size= (TextView) view.findViewById(R.id.tv_appsize);
                //获取数据
                AppBean bean=null;

                if(position<usersApks.size()+1){
                    bean=usersApks.get(position-1);
                }else{
                    bean=sysApks.get(position-1-1-usersApks.size());
                }
                //设置数据
                iv_icon.setImageDrawable(bean.getIcon());
                if(bean.isSd()){
                    tv_local.setText("SD卡存储");
                }else {
                    tv_local.setText("Rom存储");
                }

                tv_name.setText(bean.getAppName());
                tv_size.setText(Formatter.formatFileSize(getApplicationContext(),bean.getSize()));

                return view;
            }
        }
    }
}
