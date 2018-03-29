package com.dream.NiuFaNet.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Ui.Activity.MainActivity;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
public class IntentUtils {
    public static void toActivity(Class<?> tClass, Activity activity) {
        activity.startActivity(new Intent(activity, tClass));
    }
    public static void toActivity(Class<?> tClass, Context activity) {
        activity.startActivity(new Intent(activity, tClass));
    }

    public static void toActivity_result(Class<?> tClass, Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, tClass), requestCode);
    }

    public static void toActivityWithTag(Class<?> tClass, Activity activity, String tag) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(Const.intentTag, tag);
        activity.startActivity(intent);
    }

    public static void toActivityWithUrl(Class<?> tClass, Activity activity, String url, String title) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(Const.intentTag, title);
        intent.putExtra(Const.webUrl, url);
        activity.startActivity(intent);
    }

    public static void toActivityWithTag(Class<?> tClass, Activity activity, String tag, int requestCode) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(Const.intentTag, tag);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toPicture(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)//权限未授予
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.PICTURE
            );
        } else//已授予权限
        {
            Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(picture, Const.PICTURE);
        }
    }

    public static void toCamare(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)//权限未授予
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.CAMERA
            );
        } else//已授予权限
        {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(camera, Const.CAMERA);
        }
    }
    public static void applyWakePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.DISABLE_KEYGUARD)
                != PackageManager.PERMISSION_GRANTED)//权限未授予
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.DISABLE_KEYGUARD}, Const.CAMERA
            );
        } else//已授予权限
        {
            Log.e("tag","111");
        }
    }
    public static void applyWakePermission1(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WAKE_LOCK)
                != PackageManager.PERMISSION_GRANTED)//权限未授予
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WAKE_LOCK}, Const.CAMERA
            );
        } else//已授予权限
        {
            Log.e("tag","222");

        }
    }

    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    public static void call(String phone, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
       /* if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE}, Const.CAll
            );
        }else {
        }*/
        activity.startActivity(intent);
    }

    public static void retoMain(Context context){
        //恢复应用到前台显示
        Intent mIntent = new Intent(Intent.ACTION_MAIN);
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mIntent.setComponent(new ComponentName(context.getPackageName(), MainActivity.class.getCanonicalName()));
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mIntent);
    }
}
