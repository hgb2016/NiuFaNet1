package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;


/**
 * Created by sunnetdesign3 on 2017/2/10.
 */
public class ToastUtils {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };
    public static void Toast_short(final Activity context, final String content){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public static void Toast_long(final Activity context, final String content){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        }else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, content, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public static void Toast_long(Context context,String content){
        Toast.makeText(MyApplication.getInstance(),content,Toast.LENGTH_LONG).show();
    }

    public static void Toast_short(String content){
        mToast = Toast.makeText(MyApplication.getInstance(), content, Toast.LENGTH_SHORT);
        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(content);
        else
            mToast = Toast.makeText(MyApplication.getInstance(), content, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, 1000);

        Log.e("tag","content="+content);
        mToast.show();
//        Toast.makeText(MyApplication.getInstance(),content,Toast.LENGTH_SHORT).show();
    }


    /**
     * 传字段ID
     * @param context
     * @param text 文字ID
     * @param resId 图片ID
     */
    public static void showToast(Context context, String text,int resId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.view_toast, null);
        ImageView toast_image = (ImageView) layout.findViewById(R.id.toast_iv);
        //图片可以自己从外面传递进来换;
        toast_image.setImageResource(resId);
        TextView textV = (TextView) layout.findViewById(R.id.toast_tv);
        textV.setText(text);
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setView(layout);
        } else {
            mToast.cancel();//关闭吐司显示
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            //设置Toast位置的
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setView(layout);
        }
        mToast.show();
    }
    public static void showToast1(Context context, String text) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.view_toast, null);
        ImageView toast_image = (ImageView) layout.findViewById(R.id.toast_iv);
        //图片可以自己从外面传递进来换;
        toast_image.setVisibility(View.GONE);
        TextView textV = (TextView) layout.findViewById(R.id.toast_tv);
        textV.setText(text);
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setView(layout);
        } else {
            mToast.cancel();//关闭吐司显示
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            //设置Toast位置的
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setView(layout);
        }
        mToast.show();
    }
}
