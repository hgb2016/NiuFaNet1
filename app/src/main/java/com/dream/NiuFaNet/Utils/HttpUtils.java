package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.dream.NiuFaNet.Bean.BusBean.DownloadBean;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/6/15 0015.
 */
public  class HttpUtils {

    private static String TAG = "HttpUtils";
    public static MultipartBody.Part getRequestBodyPart(String element,File mFile){
        if (mFile!=null){
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
            builder.addFormDataPart(element, mFile.getName(), imageBody);//imgfile 后台接收图片流的参数名
            MultipartBody.Part part = builder.build().part(0);
            return part;
        }else {
            return null;
        }
    }
    public static MultipartBody.Part[] getRequestBodyParts(String element, List<File> files){
        if (files!=null){
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//ParamKey.TOKEN 自定义参数key常量类，即参数名
            MultipartBody.Part[] parts = new MultipartBody.Part[files.size()];
            for (int i = 0; i <files.size() ; i++) {
                File mFile = files.get(i);
                Log.e("tag","mFile="+mFile);
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
                builder.addFormDataPart(element, mFile.getName(), imageBody);//imgfile 后台接收图片流的参数名
                parts[i]  = builder.build().part(i);
            }
            return parts;
        }else {
            return null;
        }
    }

    public static RequestBody getBody(String str){
        return RequestBody.create(null,str);
    }

    /**
     * @将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @author QQ986945193
     * @Date 2015-01-26
     * @param path 图片路径
     * @return
     */
    public static String imageToBase64(String path) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {

            InputStream in = new FileInputStream(path);

            data = new byte[in.available()];

            in.read(data);

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        String encodedString = Base64.encodeToString(data, Base64.DEFAULT);
        return encodedString;

    }

    public static File downloadFile(String httpurl, File dir, String rename, Activity activity) {
        File target = new File(dir, rename);
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(httpurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setReadTimeout(5000);
//            conn.setConnectTimeout(5000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                long contentLength = conn.getContentLength();
                Log.e("tag", "target.length=" + target.length());
                Log.e("tag", "contentLength=" + contentLength);
                byte[] buff = new byte[1024 * 1024 * 100];
                inputStream = conn.getInputStream();
                fos = new FileOutputStream(target);
                int read = -1;
                long download = 0;
                NotificationManager manager = NotifyUtil.getNotificationManager(MyApplication.getInstance());
                DownloadBean bean = new DownloadBean();
                bean.setEventStr("doing");
                EventBus.getDefault().post(bean);
                while ((read = inputStream.read(buff)) != -1) {
                    fos.write(buff, 0, read);
                    fos.flush();
                    download += read;
                    //计算下载百分比
                    final int progress = (int) (100 * download / contentLength);
                    Log.e("tag", "progress=" + progress);
                    Log.e("tag", "download=" + download);
                       /* runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });*/

                    NotifyUtil.sendAppVersionNotify(MyApplication.getInstance(), manager, progress);
                }
                Log.e("tag", "文件下载成功！");
                FileUtil.installApk(activity, target);
                return target;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    // fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("TAG", "文件下载失败！");
        DownloadBean bean = new DownloadBean();
        bean.setEventStr("fail");
        EventBus.getDefault().post(bean);
        return null;

    }

    public static String requestGet(HashMap<String, String> paramsMap,String baseUrl,String reguestMethod) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,paramsMap.get(key)));
                pos++;
            }
            String requestUrl = baseUrl+"?" + tempParams.toString();
            Log.e(TAG,"requestUrl="+requestUrl);
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod(reguestMethod);
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "请求成功，result--->" + result);
                return result;
            } else {
                Log.e(TAG, "请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray,"GBK");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

}
