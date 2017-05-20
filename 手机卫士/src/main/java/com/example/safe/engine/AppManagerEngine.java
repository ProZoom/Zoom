package com.example.safe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.example.safe.bean.AppBean;
import com.example.safe.utils.L;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyang on 2017/1/14.
 *
 * 获取所有安装apk详细信息
 */

public class AppManagerEngine {


    /**
     * 作者：   李阳
     * 创建时间：  2017/1/14
     * 参数：
     * 描述：返回SD卡可用空间
     *
     */
    public static long getSDAvail(Context context){

        long sdAvail = 0;
        //获取SD卡的目录
        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        sdAvail=externalStorageDirectory.getFreeSpace();
        L.i("SdAvail:  "+sdAvail);
        return sdAvail;
    }

    /**
     * 作者：   李阳
     * 创建时间：  2017/1/14
     * 参数：
     * 描述：获取Rom可用空间
     *
     */
    public static long getRomAvail(Context context){
        long romAvail = 0;
        File dataDirection = Environment.getDataDirectory();
        romAvail = dataDirection.getFreeSpace();
        L.i("RomAvail:  "+romAvail);
        return romAvail;
    }


    public static List<AppBean> getAllApk(Context context){

        //获取所有安装的apk数据
        List<AppBean> apks=new ArrayList<AppBean>();


        //获取所有安装的APk数据
        PackageManager pm=context.getPackageManager();

        //所有安装的apk
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);

        for(PackageInfo packageInfo:installedPackages){
            AppBean bean=new AppBean();
            //设置apk的名字
            bean.setAppName(packageInfo.applicationInfo.loadLabel(pm)+"");
            //设置apk的图标
            bean.setIcon(packageInfo.applicationInfo.loadIcon(pm));
            //设置apk的包名
            bean.setPackName(packageInfo.applicationInfo.packageName);
            //设置apk的大小
            String sourceDir = packageInfo.applicationInfo.sourceDir;
            File file=new File(sourceDir);
            bean.setSize(file.length());
            //设置apk路径
            //bean.setApkPath(context.getPackageResourcePath());
            bean.setApkPath(packageInfo.applicationInfo.sourceDir);
            //设置位置标志,获取当前apk的flag标志
            int flag=packageInfo.applicationInfo.flags;
            //判断是否是系统apk
            if((flag & ApplicationInfo.FLAG_SYSTEM)!=0){
                //系统apk
                bean.setSystem(true);//是系统apk
            }else {
                bean.setSystem(false);//用户apk
            }
            //安装的位置
            if((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                //安装在sd卡的apk
                bean.setSd(true);//sd卡的apk
            }else {
                bean.setSd(false);//rom的apk
            }
            apks.add(bean);
        }

        L.i(apks.toString());
        return apks;
    }
}
