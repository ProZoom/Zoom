package com.example.safe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.example.safe.bean.TaskBean;
import com.example.safe.utils.L;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyang on 2017/1/24.
 * 进程管理，获取所有进程的信息
 *
 */

public class TastManagerEngine {

    /**
     * 描述：获取运行中的进程
     */
    public static List getAllRunningTaskInfos(Context context){

        List<TaskBean> datas=new ArrayList<TaskBean>();

        ActivityManager am=
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //获取包管理器
        PackageManager pm=context.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses =
                am.getRunningAppProcesses();

        for(ActivityManager.RunningAppProcessInfo runningAppProcessesInfos:runningAppProcesses){

            TaskBean bean=new TaskBean();

            String processName=runningAppProcessesInfos.processName;//apk包名
            bean.setPackname(processName);//设置apk包名

            //有些进程是无名进程
            PackageInfo packageInfo;
            //获取apk的图标和名字
            try {
                packageInfo= pm.getPackageInfo(processName, 0);

            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }

            //获取apk图标
            bean.setIcon(packageInfo.applicationInfo.loadIcon(pm));
            //获取apk名字
            bean.setName(packageInfo.applicationInfo.loadLabel(pm)+"");

            //判断是否是系统apk
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)!=0){
                bean.setSystem(true);
            }else {
                bean.setSystem(false);
            }

            //获取占用内存的大小
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{runningAppProcessesInfos.pid});
            int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty();
            bean.setMemSize(totalPrivateDirty);


            datas.add(bean);
        }


        return datas;
    }


    /**
     * 描述：获取可用内存大小
     */
    public static long getAvailMemSize(Context context){

        long size = 0;

        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);


        //memoryInfo 存放内存的信息
        ActivityManager.MemoryInfo outInfo=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        size=outInfo.availMem;

        L.i("可用内存:  "+size+" byte");


        return size;
    }



    /**
     * 描述：获取内存大小
     */
    public static long getToatleMemSize(Context context){

        long size = 0;

        /*//1.actvity管理器，获取运行相关信息
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);


        //memoryInfo 存放内存的信息
        ActivityManager.MemoryInfo outInfo=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        size=outInfo.totalMem;*/

        //2.读取配置文件来获取内存大小
        File file=new File("/proc/meminfo");

        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String memInfo=reader.readLine();
            int startIndex=memInfo.indexOf(':');
            int endIndex=memInfo.indexOf('k');

            memInfo=memInfo.substring(startIndex+1,endIndex).trim();
            size=Long.parseLong(memInfo);
            size=size*1024;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        L.i("总内存:  "+size+" byte");
        return size;
    }
}
