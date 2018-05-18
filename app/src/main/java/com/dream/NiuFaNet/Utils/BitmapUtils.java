package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;


import com.dream.NiuFaNet.Other.Const;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/9 0009.
 */
public class BitmapUtils {

    public static Map<Bitmap,File> parseData(Intent data, int method, Activity activity) {
        File mFile = null;
        Bitmap bitmap = null;
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        if (method== Const.CAMERA){
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                ToastUtils.Toast_short("请插入SD卡");
                return null;
            }
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            bitmap = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            File file = new File("/sdcard/pintu/");
            file.mkdirs();
            String filename = file.getPath() + name;
            try {
                fout = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mFile = new File(filename);
            Log.e("tag","mFileLength="+mFile.length());
        }else if (method== Const.PICTURE){
            File file = new File("/sdcard/pintu/");
            file.mkdirs();
            String filename = file.getPath() + name;
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Log.e("tag","picturePath="+picturePath);
            mFile = new File(picturePath);
            Log.e("tag","mFileLength="+mFile.length());
            bitmap = BitmapFactory.decodeFile(picturePath);
            BufferedOutputStream buff = null;
            try {
                buff = new BufferedOutputStream(new FileOutputStream(filename));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buff);//强制转为jpg
            }  catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    buff.flush();
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mFile  = new File(filename);
        }
        Map<Bitmap,File> map = new HashMap<>();
        map.put(bitmap,mFile);
        return map;
    }
    public static File parseData2File(Intent data,int method,Activity activity) {
        File mFile = null;
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        if (method== Const.CAMERA){
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                ToastUtils.Toast_short("请插入SD卡");
                return null;
            }
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            File file = new File("/sdcard/pintu/");
            file.mkdirs();
            String filename = file.getPath() + name;
            try {
                fout = new FileOutputStream(filename);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mFile = new File(filename);
            Log.e("tag","mFileLength="+mFile.length());
        }else if (method==Const.PICTURE){
            File file = new File("/sdcard/pintu/");
            file.mkdirs();
            String filename = file.getPath() + name;
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Log.e("tag","picturePath="+picturePath);
            mFile = new File(picturePath);
            Log.e("tag","mFileLength="+mFile.length());
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            BufferedOutputStream buff = null;
            try {
                buff = new BufferedOutputStream(new FileOutputStream(filename));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buff);//强制转为jpg
            }  catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    buff.flush();
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mFile  = new File(filename);
        }
        return mFile;
    }
    public static String parseData2Str(Intent data,int method,Activity activity) {
        String path = null;
        if (method== Const.CAMERA){
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                ToastUtils.Toast_short("请插入SD卡");
                return null;
            }
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            FileOutputStream fout = null;
            File file = new File("/sdcard/pintu/");
            file.mkdirs();
           String tempStr = file.getPath() + name;
            try {
                fout = new FileOutputStream(tempStr);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File mFile = new File(tempStr);
            path = mFile.getPath();
        }else if (method==Const.PICTURE){

            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            path = c.getString(columnIndex);
            c.close();
        }
        Log.e("tag","picturePath="+path);

        return path;
    }

    //把图片加工成圆形图片
    public static Bitmap createCircleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }


    public static Bitmap parseBitmap(Activity activity, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        String picturePath = null;
        if (selectedImage!=null&&filePathColumns!=null){
            Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            picturePath = c.getString(columnIndex);
            c.close();
        }
        return getDiskBitmap(picturePath);
    }

    private static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        if (pathString!=null){
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        }
        return bitmap;
    }

    //1、Drawable → Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
