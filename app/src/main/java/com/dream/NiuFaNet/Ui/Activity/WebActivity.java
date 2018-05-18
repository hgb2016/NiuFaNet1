package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Contract.PermissionListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DownPicUtil;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import java.io.FileNotFoundException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class WebActivity extends CommonActivity {

    @Bind(R.id.web_view)
    WebView web_view;
    WebSettings  webSettings;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
            ToastUtils.Toast_short("保存成功");

        }
    };
    private InputMethodManager imm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        imm=ImmUtils.getImm(mActivity);
    }

    @Override
    public void initDatas() {
        mLoadingDialog.show();
        String weburl = getIntent().getStringExtra(Const.webUrl);
        Log.e("tag", "weburl=" + weburl);
        webSettings = web_view.getSettings();
        web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        web_view.loadUrl("http://api.niufa.cn:9080/app/apiDownloadPic.do?url=21234.jpg");
        //设置不用系统浏览器打开,直接显示在当前Webview
        web_view.loadUrl(weburl);
        web_view.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return  true;
            }
        });
        //web_view.setWebChromeClient(new WebChromeClient());
       // web_view.setWebChromeClient(new WebChromeClient());
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        web_view.addJavascriptInterface(new JsOperation(), "Android");//JS与java的交互
// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mLoadingDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        web_view.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        web_view.onPause();
    }

    @Override
    public void eventListener() {

        // 长按点击事件
        web_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = web_view.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = hitTestResult.getExtra();
                            // 下载图片到本地
                            DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener() {

                                @Override
                                public void getDownPath(String s) {
//                                    Toast.makeText(context,"下载完成",Toast.LENGTH_LONG).show();
                                    Message msg = Message.obtain();
                                    msg.obj = s;
                                    handler.sendMessage(msg);
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });

    }

    @OnClick({R.id.back_relay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                ImmUtils.hideImm(mActivity,imm);
                if (web_view.canGoBack()) {
                    web_view.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {
            web_view.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (web_view != null) {
            web_view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            web_view.clearHistory();
            web_view.clearCache(true);
            ((ViewGroup) web_view.getParent()).removeView(web_view);
            web_view.destroy();
            web_view = null;
        }
        super.onDestroy();
    }

    class JsOperation {


        public JsOperation() {
        }

        @JavascriptInterface
        public void saveImg(final String imgUrl) {
            // 下载图片到本地(最好做一个避免重复下载的问题)
            Log.e("tag","imgUrl="+imgUrl);
           /* for (String url : MyApplication.getImgList()){
                if (url.equals(imgUrl)){
                    ToastUtils.Toast_short("保存成功");
                    return;
                }
            }*/
            DownPicUtil.downPic(imgUrl, new DownPicUtil.DownFinishListener() {
                @Override
                public void getDownPath(String s) {
                    Message msg = Message.obtain();
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    @Override
    public void finish() {
        // 当我们对Activity进行finish的时候，webview持有的页面并不会立即释放，如果页面中有在执行js等其他操作，仅仅进行finish是完全不够的。
        web_view.loadUrl("about:blank");
        super.finish();
    }
}
