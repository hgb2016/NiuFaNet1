package com.dream.NiuFaNet.Other;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.awen.photo.FrescoImageLoader;
import com.baidu.mobstat.StatService;
import com.dream.NiuFaNet.Component.AppComponent;
import com.dream.NiuFaNet.Component.DaggerAppComponent;
import com.dream.NiuFaNet.Module.AppModule;
import com.dream.NiuFaNet.Module.NFApiModule;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Service.AlarmService;
import com.dream.NiuFaNet.Utils.AppUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;


/**
 * Created by Administrator on 2017/4/5/005.
 */
public class MyApplication extends Application {

    private String time_data;

    public String getTime_data() {
        return time_data;
    }

    public void setTime_data(String time_data) {
        this.time_data = time_data;
    }

    private static MyApplication context;
    private static Typeface typeface_h, typeface_xh;
    private AppComponent appComponent;
    private static String szImei;

    private int activityCount;//activity的count数
    private boolean isForeground;//是否在前台

    public static MyApplication getInstance() {
        if (null == context) {
            context = new MyApplication();
        }
        return context;
    }

    public static Typeface getTypeface_fzlth() {
        if (null == typeface_h) {
            typeface_h = Typeface.createFromAsset(context.getAssets(), "fonts/fzlth.ttf");
        }
        return typeface_h;
    }

    public static Typeface getTypeface_fzltxh() {
        if (null == typeface_xh) {
            typeface_xh = Typeface.createFromAsset(context.getAssets(), "fonts/fzltxh.ttf");
        }
        return typeface_xh;
    }

    public static String getDeviceId() {
        if (null == szImei) {
            //设备id
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            szImei = TelephonyMgr.getDeviceId();
        }
        return szImei;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initCompoent();
        AppUtils.init(this);
        FrescoImageLoader.init(this);
        MobSDK.init(this, "20f4091be79c7", "414be537e9515d5837e78590bd73bac8");
        //ShareSDK.initSDK(this);
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=598a8250");
        //百度统计服务开启
        //StatService.autoTrace(this,true,true);
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);

        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                activityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityCount--;
                if (0 == activityCount) {
                    isForeground = false;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    private void initCompoent() {
        appComponent = DaggerAppComponent.builder()
                .nFApiModule(new NFApiModule())
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }

}
