package com.dream.NiuFaNet.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.RequiresPermission;

import com.dream.NiuFaNet.Other.MyApplication;

import java.util.List;

/**
 * Created by Senh Linsh on 16/8/9.
 */
public class LshAppUtils {

    /**
     * 判断服务是否在运行
     */
    public static boolean isServiceRunning(String serviceClassName) {
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    @RequiresPermission(Manifest.permission.GET_TASKS)
    public static boolean isAppOnForeground() {
        return isAppOnForeground(getPackageName());
    }

    /**
     * 判断App是否在前台运行
     * 注意: 该方法是判断APP是否处于栈顶, 处于栈顶但是是关闭屏幕的情况下依然返回true
     */
    @RequiresPermission(Manifest.permission.GET_TASKS)
    public static boolean isAppOnForeground(String packageName) {
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (tasksInfo.get(0).topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取顶部Activity的名称
     */
    public static String getTopActivityName() {
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

    /**
     * 获取包名
     */
    public static String getPackageName() {
        return MyApplication.getInstance().getPackageName();
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName() {
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            // 获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            // 获取版本名称
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用名称
     */
    public String getAppName() {
        PackageManager pm = MyApplication.getInstance().getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用签名
     */
    public static String getAppSignature() {
        PackageManager pm = MyApplication.getInstance().getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            return packinfo.signatures[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测某个应用是否安装
     */
    public static boolean isAppInstalled(String packageName) {
        try {
            MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 检测某个应用是否安装, 如果一安装则启动跳转该应用
     */
    public static boolean checkAndStartInstalledApp(Activity activity, String packageName) {
        if (isAppInstalled(packageName)) {
            Intent intent = MyApplication.getInstance().getPackageManager().getLaunchIntentForPackage(packageName);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 杀死当前进程, 退出APP
     */
    public static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}