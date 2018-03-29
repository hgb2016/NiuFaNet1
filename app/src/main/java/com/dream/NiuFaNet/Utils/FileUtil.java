package com.dream.NiuFaNet.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 文件操作工具类
 * Created by Administrator on 2016/8/9 0009.
 */
public class FileUtil {
    public static  final File DIR_IMAGE =getDir("AppSearch");
    public static  final File DIR_CACHE =getDir("cache");
    public static  final File DIR_APK =getDir("apk");
    public static  final File DIR_MP3 =getDir("mp3");
    public static  final File DIR_VIDOE =getDir("vidoe");
    /*
    * SD卡根目录
    * */
    public static File getSDcardDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File storageDictory = Environment.getExternalStorageDirectory();
            return storageDictory;
        }
        throw new RuntimeException("没有找到内存卡！");
    }
    public static File getAppDir(){
        File dir = new File(getSDcardDir(),"NiuFa");//项目的根目录
        if (!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }
    /*
    * 获取应用目录下面的指定目录
    * */
    public static File getDir(String dir){
        File file = new File(getAppDir(),dir);
        if (!file.exists()){
            file.mkdir();
        }
        return file;

    }
    public static  void installApk(Context context, File apk){
        Uri uri = Uri.fromFile(apk);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
