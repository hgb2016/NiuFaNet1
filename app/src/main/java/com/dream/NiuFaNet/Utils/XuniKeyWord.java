package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;


/**
 * Created by sunnetdesign3 on 2016/11/4.
 */
public class XuniKeyWord {
    public static void assistActivity(Activity activity) {
        new XuniKeyWord(activity.findViewById(android.R.id.content));
    }

    public static void setMargintop(View content, View currentview, Activity activity) {
        new XuniKeyWord(content, currentview, activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private static int activityHeight;
    private ViewGroup.LayoutParams frameLayoutParams;

    private XuniKeyWord(View content) {
        mChildOfContent = content;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                activityHeight = possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private XuniKeyWord(View content, View view, Activity activity) {
        mChildOfContent = content;
        //计算视图可视高度
        Rect r = new Rect();
//        int a = 90;
        mChildOfContent.getWindowVisibleDisplayFrame(r);

        if (Build.VERSION.SDK_INT >= 19) {
           /* if (Build.VERSION.SDK_INT >= 21){
                view.setPadding(0, getStatusBarHeight(activity), 0, 0);//65
            }else {
            }*/
            view.setPadding(0, getStatusBarHeight(activity), 0, 0);//75
        }
    }

    private int possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致

//            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                // keyboard probably just became visible
//                isKeyBordVisiable=true;
//            } else {
//                // keyboard probably just became hidden
//                isKeyBordVisiable=false;
//            }
            //将计算的可视高度设置成视图的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
        return usableHeightNow;
    }

    private int computeUsableHeight() {
        //计算视图可视高度
        Rect r = new Rect();
//        int a = 90;
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        //这里要减去状态栏的高度
        if (Build.VERSION.SDK_INT >= 19)  //沉浸功能只能在SDK>=19中使用
        {
            /*if (Build.VERSION.SDK_INT >= 21) {
                return (r.bottom - r.top);
            }else {
                // 包含新API的代码块
            }*/
            return (r.bottom);
        } else {
            // 包含旧的API的代码块
            return (r.bottom - r.top);
        }
    }

    public static View initStateView(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        decorViewGroup.addView(statusBarView);
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static int getActivityHeight(Activity activity) {
        XuniKeyWord.assistActivity(activity);
        return activityHeight;
    }

    public static void setShiPei(Activity activity, View root_layout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        XuniKeyWord.assistActivity(activity);
        XuniKeyWord.setMargintop(activity.findViewById(android.R.id.content), root_layout, activity);
        //沉浸式状态栏背景
        XuniKeyWord.initStateView(activity).setBackgroundColor(activity.getResources().getColor(R.color.statu_diwen));
    }

    public static void setShiPeiNoMargin(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        XuniKeyWord.assistActivity(activity);
//        XuniKeyWord.setMargintop(activity.findViewById(android.R.id.content),root_layout,activity);
        //沉浸式状态栏背景
//        XuniKeyWord.initStateView(activity).setBackgroundColor(activity.getResources().getColor(R.color.statubar_bg));
    }

    public static void setShiPei_Home(Activity activity, View root_layout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        XuniKeyWord.assistActivity(activity);
        XuniKeyWord.setMargintop(activity.findViewById(android.R.id.content), root_layout, activity);
    }

    public static void setShiPei_Classify(Activity activity, View root_layout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        XuniKeyWord.assistActivity(activity);
//        XuniKeyWord.setMargintop(activity.findViewById(android.R.id.content),root_layout,activity);
    }

    public static void setShiPei_NoTRANSPARENT(Activity activity, View root_layout) {
        XuniKeyWord.assistActivity(activity);
        XuniKeyWord.setMargintop(activity.findViewById(android.R.id.content), root_layout, activity);
        XuniKeyWord.initStateView(activity).setBackgroundColor(activity.getResources().getColor(R.color.statubar_bg));
    }

    public static void setStatusBarBgColor(Activity activity, int color) {
        XuniKeyWord.initStateView(activity).setBackgroundColor(activity.getResources().getColor(color));
    }

    public static void setStatusBarBgReId(Activity activity, int resourseId) {
        XuniKeyWord.initStateView(activity).setBackgroundResource(resourseId);
    }
}
