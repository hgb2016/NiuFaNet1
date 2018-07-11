package com.dream.NiuFaNet.Utils.Dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Bean.BusBean.FinishBus;
import com.dream.NiuFaNet.BuildConfig;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.CustomView.VoiceLineView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.LoginActivity;
import com.dream.NiuFaNet.Ui.Activity.LoginActivity1;
import com.dream.NiuFaNet.Utils.AppManager;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.FileUtil;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.NetUtil;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToSettingPageUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class DialogUtils {
    public static Dialog shareDialog(final Activity activity) {

        final Dialog tipDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        RelativeLayout share_close = (RelativeLayout) headView.findViewById(R.id.share_close);
        LinearLayout wechat_lay = (LinearLayout) headView.findViewById(R.id.wechat_lay);
        LinearLayout frends_lay = (LinearLayout) headView.findViewById(R.id.frends_lay);
        LinearLayout qqfrends_lay = (LinearLayout) headView.findViewById(R.id.qqfrends_lay);
        LinearLayout sina_lay = (LinearLayout) headView.findViewById(R.id.sina_lay);
        LinearLayout copylink_lay = (LinearLayout) headView.findViewById(R.id.copylink_lay);
        tipDialog.setContentView(headView);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = tipDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        tipDialog.setCancelable(true);
        share_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                tipDialog.dismiss();
            }
        });
        wechat_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                showShareWX(activity, Wechat.NAME);
                tipDialog.dismiss();
            }
        });
        frends_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                showShareWX(activity, WechatMoments.NAME);
                tipDialog.dismiss();
            }
        });
        qqfrends_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                showShare(activity, QQ.NAME);
                tipDialog.dismiss();
            }
        });
        sina_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                showShare(activity, SinaWeibo.NAME);
                tipDialog.dismiss();
            }
        });
        copylink_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(Const.shareUrl);
                ToastUtils.Toast_short(activity, "已复制到粘贴板");
                tipDialog.dismiss();
            }
        });
        return tipDialog;
    }
    public static Dialog shareDialogcal(final Activity activity) {

        final Dialog tipDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_sharefrends, null);
        ImageView wechat_iv = (ImageView) headView.findViewById(R.id.wechat_iv);
        ImageView qq_iv = (ImageView) headView.findViewById(R.id.qq_iv);
        tipDialog.setContentView(headView);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = tipDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        tipDialog.setCancelable(true);
        tipDialog.setCanceledOnTouchOutside(true);
        wechat_iv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                tipDialog.dismiss();
            }
        });
        return tipDialog;
    }

    public static void showShare(Activity activity, String platformName) {
        OnekeyShare oks = new OnekeyShare();

        String content = (String) SpUtils.getParam(Const.content, "");
        String title = (String) SpUtils.getParam(Const.title, "");
        String picUrl = (String) SpUtils.getParam(Const.picUrl, "");
        Log.e("tag", "picUrl=" + picUrl);
        oks.setImageUrl(picUrl);
        if (platformName.equals(SinaWeibo.NAME)) {
            String tempContent = title + "\n" + content + "\n" + Const.shareUrl;
            oks.setText(tempContent);
            oks.setSilent(true);
//            oks.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg ");
        } else {
            oks.setTitleUrl(Const.shareUrl);
            oks.setText(content);
            oks.setTitle(title);
        }
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("ssss", "执行了");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i("ssss", "onError" + throwable);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i("ssss", "oncancel");
            }
        });

        oks.setPlatform(platformName);
        oks.show(activity);
    }

    public static void showShareWX(final Activity activity, String methodName) {
        Platform.ShareParams params = new Platform.ShareParams();
        // imagedata、imagepath和imageurl三选一
//        Bitmap logo = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo200);
        params.setShareType(Platform.SHARE_WEBPAGE);//如果分享网页，那么这个一定要写
//                params.setShareType(Platform.SHARE_IMAGE);//如果分享图片，那么一定要写这个
//        params.setImageData(logo);
//                params.setImagePath("/sdcard/1.jpg");
        String content = (String) SpUtils.getParam(Const.content, "");
        String title = (String) SpUtils.getParam(Const.title, "");
        String picUrl = (String) SpUtils.getParam(Const.picUrl, "");

        params.setImageUrl(picUrl);

        params.setText(content);
        params.setTitle(title);
        //如果是分享图片，那么不要设置url
        params.setUrl(Const.shareUrl);
        Platform wechat = ShareSDK.getPlatform(methodName);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.Toast_short(activity, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("ssss", "onError" + throwable);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("ssss", "onCancel");
            }
        });
        wechat.share(params);

    }
    public static void showShareWX(final Activity activity, String methodName,String title,String content,String picUrl,String clickUrl) {
        Platform.ShareParams params = new Platform.ShareParams();
        // imagedata、imagepath和imageurl三选一
        params.setShareType(Platform.SHARE_WEBPAGE);//如果分享网页，那么这个一定要写
        if (picUrl.isEmpty()){
            Bitmap logo = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo);
//                params.setShareType(Platform.SHARE_IMAGE);//如果分享图片，那么一定要写这个
            params.setImageData(logo);
            //                params.setImagePath("/sdcard/1.jpg");

        }else {
            params.setImageUrl(picUrl);
        }
        params.setText(content);
        params.setTitle(title);
        //如果是分享图片，那么不要设置url
        params.setUrl(clickUrl);
        Platform wechat = ShareSDK.getPlatform(methodName);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.Toast_short(activity, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("ssss", "onError" + throwable);
                ToastUtils.Toast_short(activity, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("ssss", "onCancel");
            }
        });
        wechat.share(params);

    }


   /* public void shareSdk() {
        sp.setText("hello mob http://www.mob.com");
                sp.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg ");

    }*/

    public static Dialog actionResultDialog(Activity activity, final String tipStr) {

        final Dialog tipDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_actionresult, null);
        TextView tip_tv = (TextView) headView.findViewById(R.id.dialogtip_tv);
        TextView dialog_suretv = (TextView) headView.findViewById(R.id.dialog_suretv);

        tip_tv.setText(tipStr);
        tipDialog.setContentView(headView);
        //获取当前Activity所在的窗体
       /* Window dialogWindow1 = tipDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity( Gravity.BOTTOM);*/
        tipDialog.setCancelable(true);
        dialog_suretv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                if (tipStr.equals("重置密码成功!")) {
                    EventBus.getDefault().post("finish");
                }
                if (tipStr.equals("修改密码成功!")) {
                    FinishBus bus = new FinishBus();
                    bus.setEventStr("finish");
                    EventBus.getDefault().post(bus);
                }
                if (tipStr.equals("绑定成功!")) {
                    EventBus.getDefault().post("finish");
                    CommonAction.refreshLogined();
                }
            }
        });
        return tipDialog;
    }

    public static Dialog audioDialog(Activity activity) {

        final Dialog tipDialog = new Dialog(activity, R.style.progress_dialog);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_audio, null);
        AudioAnimView audioview_right = (AudioAnimView) headView.findViewById(R.id.audioview_right);
        AudioAnimView audioview_left = (AudioAnimView) headView.findViewById(R.id.audioview_left);
        /*audioview_left.setVolume(70);
        audioview_right.setVolume(70);*/
        tipDialog.setContentView(headView);
        //获取当前Activity所在的窗体
       /* Window dialogWindow1 = tipDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity( Gravity.BOTTOM);*/
        tipDialog.setCancelable(true);
        return tipDialog;
    }

    public static void setBespreadWindow(Dialog mDialog, Activity context) {
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        mDialog.getWindow().setAttributes(lp);
    }

    public static void show(Activity activity, final Dialog dialog) {

        if ("main".equals(Thread.currentThread().getName())) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }
            });
        }
    }

    public static Dialog initLoadingDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, R.style.progress_dialog);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("Loading...");
        return dialog;
    }

    public static void dismiss(Activity activity, final Dialog dialog) {
        if ("main".equals(Thread.currentThread().getName())) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    public static Dialog initPictureMethodDialog(final Activity activity) {
        final Dialog headDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_changeheadicon, null);
        TextView bitmap_tv = (TextView) headView.findViewById(R.id.bitmap_tv);
        TextView takephoto_tv = (TextView) headView.findViewById(R.id.takephoto_tv);
        RelativeLayout cancel_relay = (RelativeLayout) headView.findViewById(R.id.cancel_relay);
        headDialog.setContentView(headView);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = headDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        headDialog.setCancelable(true);

        //拍照上传
        takephoto_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                //相机
                headDialog.dismiss();

                IntentUtils.toCamare(activity);
            }
        });

        //从相册上传图片
        bitmap_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                headDialog.dismiss();
               IntentUtils.toPicture(activity);
            }
        });

        cancel_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (headDialog.isShowing()) {
                    headDialog.dismiss();
                }
            }
        });
        return headDialog;
    }

    public static AlertDialog getLoginTip(final Context activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("温馨提示");
        View login = LayoutInflater.from(activity).inflate(R.layout.view_logintip, null);
        TextView tv = (TextView) login.findViewById(R.id.ttttt);
        tv.setText("您还未登录");
        builder.setView(login);
        AlertDialog alertDialog = builder.setPositiveButton("去登录", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, LoginActivity1.class);
                activity.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        return alertDialog;
    }

    //版本信息弹框
    public static void getVersionDialog(String webVersionName,String versionRemark, final String apkUrl, final Activity activity) {
        final Dialog dialog_tip = new Dialog(activity);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_versionupdate, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        TextView versionremark_tv = (TextView) login.findViewById(R.id.versionremark_tv);
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        tip_title.setText("发现新版本");
        versionremark_tv.setText(versionRemark.replace("\\n","\n"));
        version_tv.setText(webVersionName);
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog_tip.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.85 );     //宽度设置为屏幕的0.75
       // p.height = (int) (displayHeight * 0.45);  //高度设置为屏幕的0.45
        dialog_tip.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog_tip.getWindow().setAttributes(p);    //设置生效

      /*  // TODO: 2016/12/23  //去掉蓝线
        int dividerID=activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider=dialog_tip.findViewById(dividerID);
        divider.setBackgroundColor(Color.TRANSPARENT);(这句在5.1上会报空指针)*/

        dialog_tip.show();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
                String netTypeName = NetUtil.getNetTypeName(activity);
                Log.e("tag", "netTypeName=" + netTypeName);

                if (netTypeName.equals("无网络")) {
                    ToastUtils.Toast_short("您的网络异常");
                    return;
                }
                if (!netTypeName.equals("WIFI")) {
                    downloadTip(activity, apkUrl);
                } else {
                    //下载安装包、
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            //考虑网络断开连接，实现断点下载
                            //注册广播，获取连接状态；
//                        IntentFilter filter = new IntentFilter();
//                        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//                        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//                        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//                        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//                        registerReceiver(networkConnectChangedReceiver, filter);
//                        SharePreferenceUtils.setParam(MainActivity.this,Const.apkUrl_fileName,apkUrl);
                            HttpUtils.downloadFile(apkUrl, FileUtil.DIR_APK, "法律机器人.apk",activity);

                        }
                    };
                    new Thread(runnable).start();
                }
            }
        });
    }


    //广告弹框
    public static void advertiseTip(final Activity activity){
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View adv=LayoutInflater.from(activity).inflate(R.layout.dialog_adv,null);

    }


    //下载提示弹框
    public static void downloadTip(final Activity activity, final String apkUrl) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_networktip, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = dialog_tip.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog_tip.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.75); //宽度设置为屏幕的0.75
//        p.height = (int) (displayHeight * 0.45); //高度设置为屏幕的0.45
        dialog_tip.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog_tip.getWindow().setAttributes(p);  //设置生效

      /*  // TODO: 2016/12/23  //去掉蓝线
        int dividerID=activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider=dialog_tip.findViewById(dividerID);
        divider.setBackgroundColor(Color.TRANSPARENT);(这句在5.1上会报空指针)*/

        dialog_tip.show();
        //使用流量下载
        cancle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
                //下载安装包、
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        //考虑网络断开连接，实现断点下载
                        //注册广播，获取连接状态；
//                        IntentFilter filter = new IntentFilter();
//                        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//                        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//                        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//                        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//                        registerReceiver(networkConnectChangedReceiver, filter);
//                        SharePreferenceUtils.setParam(MainActivity.this,Const.apkUrl_fileName,apkUrl);
                        HttpUtils.downloadFile(apkUrl, FileUtil.DIR_APK, "法律机器人.apk",activity);
                    }
                };
                new Thread(runnable).start();
            }
        });

        sure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });

    }

    public static Dialog showPermissionTip(final Context activity) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_permission, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
      /*  //获取当前Activity所在的窗体
        Window dialogWindow1 = dialog_tip.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);*/
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog_tip.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.75); //宽度设置为屏幕的0.75
//        p.height = (int) (displayHeight * 0.45); //高度设置为屏幕的0.45
        dialog_tip.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog_tip.getWindow().setAttributes(p);  //设置生效

      /*  // TODO: 2016/12/23  //去掉蓝线
        int dividerID=activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider=dialog_tip.findViewById(dividerID);
        divider.setBackgroundColor(Color.TRANSPARENT);(这句在5.1上会报空指针)*/

        //取消
        cancle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });

        sure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                ToSettingPageUtil.gotoPermissionSetting();
            }
        });

        return dialog_tip;
    }
    public static void showShareDialog(Activity activity) {
        Dialog dialog = DialogUtils.shareDialog(activity);
        dialog.show();
        DialogUtils.setBespreadWindow(dialog, activity);
    }

    public static Dialog showDeleteDialog(final Context activity, final NoDoubleClickListener mSureClicklistener) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_permission, null);
        TextView tip_title = (TextView) login.findViewById(R.id.tip_title);
        TextView version_tv = (TextView) login.findViewById(R.id.version_tv);
        version_tv.setText("确认删除？");
        TextPaint tp = tip_title.getPaint();
        tp.setFakeBoldText(true);//加粗字体
        TextView cancle = (TextView) login.findViewById(R.id.tip_nav);
        cancle.setText("取消");
        TextView sure = (TextView) login.findViewById(R.id.tip_positive);
        sure.setText("确定");
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
      /*  //获取当前Activity所在的窗体
        Window dialogWindow1 = dialog_tip.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);*/
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        WindowManager.LayoutParams p = dialog_tip.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (displayWidth * 0.75); //宽度设置为屏幕的0.75
//        p.height = (int) (displayHeight * 0.45); //高度设置为屏幕的0.45
        dialog_tip.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog_tip.getWindow().setAttributes(p);  //设置生效

      /*  // TODO: 2016/12/23  //去掉蓝线
        int dividerID=activity.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider=dialog_tip.findViewById(dividerID);
        divider.setBackgroundColor(Color.TRANSPARENT);(这句在5.1上会报空指针)*/

        //取消
        cancle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSureClicklistener.onNoDoubleClick(view);
                dialog_tip.dismiss();
            }
        });
        dialog_tip.show();
        return dialog_tip;
    }
}
