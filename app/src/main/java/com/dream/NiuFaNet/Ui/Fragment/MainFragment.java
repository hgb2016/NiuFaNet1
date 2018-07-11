package com.dream.NiuFaNet.Ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.dream.NiuFaNet.Adapter.BannerAdapter;
import com.dream.NiuFaNet.Adapter.ChatMainRvAdapter;
import com.dream.NiuFaNet.Adapter.MainFunctionAdpter;
import com.dream.NiuFaNet.Adapter.MainVpAdapterView;
import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshCalBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshMyToolsBean;
import com.dream.NiuFaNet.Bean.CalendarBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.MainFunctionBean;
import com.dream.NiuFaNet.Bean.MyToolsBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Contract.MainContract;
import com.dream.NiuFaNet.Contract.MainFunctionContract;
import com.dream.NiuFaNet.CustomView.Emptyview_RvSchedule;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.CustomView.MyScrollView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.Presenter.MainFunctionPresenter;
import com.dream.NiuFaNet.Presenter.MainPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.CaseCenterActivity;
import com.dream.NiuFaNet.Ui.Activity.FunctionActivity;
import com.dream.NiuFaNet.Ui.Activity.NewCalenderActivity;

import com.dream.NiuFaNet.Ui.Activity.WebActivity;
import com.dream.NiuFaNet.Ui.Service.SendAlarmBroadcast;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.ChinaDate;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kevin.wraprecyclerview.WrapRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/12 0012.
 */
public class MainFragment extends BaseFragmentV4 implements MainContract.View, MainFunctionContract.View, CalenderMainContract.View , MyScrollView.OnScrollListener{

    @Bind(R.id.chat_rv)
    WrapRecyclerView chat_rv;
    @Bind(R.id.cmd_tv)
    TextView cmd_tv;
    @Bind(R.id.main_bannervp)
    Banner banner;
    @Bind(R.id.main_gv)
    GridView main_gv;
    @Bind(R.id.main_hsv)
    HorizontalScrollView main_hsv;
    @Bind(R.id.calender_lay)
    LinearLayout calender_lay;
    @Bind(R.id.clv_vp)
    ViewPager clv_vp;
    @Bind(R.id.maincalendar_rv)
    MyListView maincalendar_rv;
    @Bind(R.id.no_callay)
    Emptyview_RvSchedule no_callay;
    @Bind(R.id.no_caltv)
    ImageView no_caltv;
    @Bind(R.id.day_tv1)
    TextView day_tv1;
    @Bind(R.id.myscrollview)
    MyScrollView myScrollView;
    @Bind(R.id.add_schedule)
    ImageView add_schedule;
    @Inject
    MainPresenter mainPresenter;
    @Inject
    MainFunctionPresenter mainFunctionPresenter;
    @Inject
    CalenderMainPresenter calenderMainPresenter;

    @Bind(R.id.tv_topView)
    LinearLayout mTopView;
    /**
     * 要悬浮在顶部的View的子View
     */
    @Bind(R.id.ll_tabTopView)
    LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    @Bind(R.id.ll_tabView)
    LinearLayout mTabViewLayout;


    private BannerAdapter bannerAdapter;
    private MainCalenderAdapter mainCalenderAdapter;
    private WorkCalenderAdapter workCalenderAdapter;

    private ChatMainRvAdapter chatAdapter;
    private List<RecomendBean.BodyBean> dataList = new ArrayList<>();
    private List<RecomendBean.BodyBean> tempList = new ArrayList<>();
    private List<ImageView> bannerIvs = new ArrayList<>();
    private List<MainFunctionBean.DataBean> functionList = new ArrayList<>();
    private MainFunctionAdpter mainFunctionAdpter;

    private List<CalendarBean> calendarList = new ArrayList<>();
    private List<CalenderedBean.DataBean> worksList = new ArrayList<>();
    private List<CalenderedBean.DataBean> data = new ArrayList<>();
    private List<CalendarBean> halfyear1 = new ArrayList<>();
    private List<CalendarBean> halfyear2 = new ArrayList<>();
    private Calendar tempCalendar = Calendar.getInstance();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private  MyToolsAdapter myToolsAdapter;
    private List<MyToolsBean.DataBean> myTools=new ArrayList<>();
    private Bitmap localbanner;
    private int mHeight;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_main, null);
    }
    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        mainPresenter.attachView(this);
        mainFunctionPresenter.attachView(this);
        calenderMainPresenter.attachView(this);
        EventBus.getDefault().register(this);
        RvUtils.setOptionnoLine(chat_rv, getActivity());
        chatAdapter = new ChatMainRvAdapter(getContext(), dataList, R.layout.rvitem_onlytext);
        chat_rv.setAdapter(chatAdapter);
        myToolsAdapter = new MyToolsAdapter(getContext(), myTools, R.layout.view_imgtext);
        main_gv.setAdapter(myToolsAdapter);
//        RvUtils.setOption(clv_rv, getActivity());
//        mainCalenderAdapter = new MainCalenderAdapter(getContext(), calendarList, R.layout.rvitem_maincalender);
//        clv_rv.setAdapter(mainCalenderAdapter);

        workCalenderAdapter = new WorkCalenderAdapter(getContext(), worksList, R.layout.rvitem_maincalwork);
        maincalendar_rv.setAdapter(workCalenderAdapter);
        //滑动监听
        myScrollView.setOnScrollListener(this);
        if (!CommonAction.getIsLogin()){
            Calendar calendar = Calendar.getInstance();
            refreshIsfirst(calendar.getTime());
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void initResume() {

    }

    @Override
    public void initEvents() {

    }


    //初始化数据
    @Override
    public void initDta() {

        mainPresenter.getBannerDat("2");
        //mainFunctionPresenter.getMianFunction("");
        String cacheData = (String) SpUtils.getParam(Const.mainCache, "");
        Log.e("tag", "cacheData=" + cacheData);
        if (!cacheData.isEmpty()) {
            List<RecomendBean.BodyBean> beanList = new Gson().fromJson(cacheData, new TypeToken<List<RecomendBean.BodyBean>>() {
            }.getType());
            if (beanList != null) {
                dataList.addAll(beanList);
                chatAdapter.notifyDataSetChanged();
            }
        } else {
            getRecommendData();
        }

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fzlth.ttf");
        cmd_tv.setTypeface(tf);
        //获取我的工具
        getMyToolsData();
        //设置我的日程数据
        setCalendarData();
        //获取缓存日程数据
        getCacheCalendarData();
        //获取我的日程数据
        getCalendarData();
        //获取缓存图片
        getCacheBanner();
    }

    //获取缓存日程数据
    private void getCacheCalendarData() {
        String calInfo= (String) SpUtils.getParam("calendarInfo","");
        if (calInfo!=null&&!calInfo.isEmpty()) {
            Type type = new TypeToken<List<CalenderedBean.DataBean>>() {
            }.getType();
            Gson gson = new Gson();
            List<CalenderedBean.DataBean> list = gson.fromJson(calInfo, type);
            if (list.size()>0) {
                this.data.clear();
                this.data.addAll(list);
                SpUtils.setParam("calendarInfo", new Gson().toJson(data));
                for (int i = 0; i < calendarList.size(); i++) {
                    calendarList.get(i).setWork(false);
                }
                for (int i = 0; i < data.size(); i++) {
                    String beginTime = data.get(i).getBeginTime();
                    Date timeb = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                    String endTime = data.get(i).getEndTime();
                    Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                    long startL = timeb.getTime();
                    long endL = endD.getTime();
                    for (int j = 0; j < calendarList.size(); j++) {
                        Date currentD = calendarList.get(j).getDate();
                        String ymd = DateFormatUtil.getTime(currentD, Const.Y_M_D);
                        Date startDL = DateFormatUtil.getTime(ymd + Const.endTime, Const.YMD_HMS);
                        long startTL = startDL.getTime();
                        Date endDd = DateFormatUtil.getTime(ymd + Const.startTime, Const.YMD_HMS);
                        long endDL = endDd.getTime();
                        if (startTL >= startL && endDL <= endL) {
                            calendarList.get(j).setWork(true);
                        }
                    }
                }

//                mainCalenderAdapter.notifyDataSetChanged();
                refreshMainVP();
                refreshWorkRv(mPosition);
            }
        }

    }
    //获取缓存的banner图，
    private void getCacheBanner() {
        String bannerData = (String) SpUtils.getParam(Const.Banner, "");
        if (bannerData!=null&&!bannerData.isEmpty()){
            Type listType = new TypeToken<List<BannerBean.DataBean>>() {
            }.getType();
            Gson gson = new Gson();
            List<BannerBean.DataBean> data=new ArrayList<>();
             data=gson.fromJson(bannerData,listType);
            List<String> imgUrls = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                imgUrls.add(data.get(i).getImgUrl());
                final String link = data.get(i).getLink();
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Log.e("tag","link="+link);
                        if (!link.isEmpty()) {
                            IntentUtils.toActivityWithUrl(CaseCenterActivity.class, getActivity(), link, ResourcesUtils.getString(R.string.app_name));
                        }
                    }
                });

            }
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImages(imgUrls);
            banner.setDelayTime(Const.time);
            banner.setImageLoader(new GlideImageLoader());
            banner.start();

            /*    ImageView bannerIv1 = new ImageView(getActivity());
                bannerIvs.add(bannerIv1);
                for (int i = 0; i < data.size(); i++) {
                    ImageView bannerIv = new ImageView(getActivity());
                    bannerIvs.add(bannerIv);
                }
                if (data.size()>0){
                    bannerAdapter = new BannerAdapter(bannerIvs,dataList);
                    main_bannervp.setAdapter(bannerAdapter);
                }*/
            mHandler.post(runnable);
        }
    }

    private void getMyToolsData() {
        //获取缓存工具数据
        String cacheData = (String) SpUtils.getParam(Const.MyTools, "");
        Type listType = new TypeToken<List<MyToolsBean.DataBean>>() {
        }.getType();
        Gson gson = new Gson();
        List<MyToolsBean.DataBean> mytools = gson.fromJson(cacheData,listType);
        if (cacheData!=null&&!cacheData.isEmpty()){
            if (mytools!= null){
                myTools.clear();
                myTools.addAll(mytools);
                MyToolsBean.DataBean tool=new MyToolsBean.DataBean();
                myTools.add(tool);
                myToolsAdapter.notifyDataSetChanged();
            }

        }
        if (CommonAction.getIsLogin()){
            mainPresenter.getMyTools(CommonAction.getUserId());
        }else {
            mainPresenter.getMyTools("");
        }
    }

    private void getCalendarData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -leftSum);
        String startTime = DateFormatUtil.getTime(calendar.getTime(), Const.Y_M_D);
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, rightSum);
        String endTime = DateFormatUtil.getTime(calendar.getTime(), Const.Y_M_D);
        Log.e("tag","start="+startTime + Const.startTime);
        Log.e("tag","end="+endTime + Const.endTime);
        mLoadingDialog.show();
        calenderMainPresenter.getCalenders(CommonAction.getUserId(), startTime + Const.startTime, endTime + Const.endTime);
    }

    private void getRecommendData() {
        String userId = CommonAction.getUserId();
        Log.e("tag", "userId=" + userId);
        Log.e("tag", "szImei=" + MyApplication.getDeviceId());
        if (userId.isEmpty()) {
            mainPresenter.getRecomendData(MyApplication.getDeviceId());
        } else {
            mainPresenter.getRecomendData(userId);
        }
    }



    @Override
    public void showData(RecomendBean dataBean) {

        if (dataBean.getError().equals(Const.success)) {
            List<RecomendBean.BodyBean> bodyBeanList = dataBean.getBody();
            if (bodyBeanList != null) {
                dataList.clear();
                dataList.addAll(bodyBeanList);
                tempList.addAll(bodyBeanList);
                chatAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.Toast_short("服务器异常");
        }
    }

    @Override
    public void showBannerData(BannerBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<BannerBean.DataBean> data = dataBean.getData();
            SpUtils.setParam(Const.Banner,new Gson().toJson(data)+"");
            if (data != null) {
                List<String> imgUrls = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    imgUrls.add(data.get(i).getImgUrl());
                    final String link = data.get(i).getLink();
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Log.e("tag","link="+link);
                            if (!link.isEmpty()) {
                                IntentUtils.toActivityWithUrl(CaseCenterActivity.class, getActivity(), link, ResourcesUtils.getString(R.string.app_name));
                            }
                        }
                    });
                }
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setImages(imgUrls);
                banner.setDelayTime(Const.time);
                banner.setImageLoader(new GlideImageLoader());
                banner.start();

            /*    ImageView bannerIv1 = new ImageView(getActivity());
                bannerIvs.add(bannerIv1);
                for (int i = 0; i < data.size(); i++) {
                    ImageView bannerIv = new ImageView(getActivity());
                    bannerIvs.add(bannerIv);
                }
                if (data.size()>0){
                    bannerAdapter = new BannerAdapter(bannerIvs,dataList);
                    main_bannervp.setAdapter(bannerAdapter);
                }*/
                mHandler.post(runnable);
            }
        }
    }
    //获取我的常用工具
    @Override
    public void showMyTools(MyToolsBean dataBean) {
        if(dataBean.getError().equals(Const.success)){
            List<MyToolsBean.DataBean> mytools = dataBean.getDataBean();
            Log.i("myTools",new Gson().toJson(mytools));
            SpUtils.setParam(Const.MyTools,new Gson().toJson(mytools)+"");
            if (mytools!= null){
                myTools.clear();
                myTools.addAll(mytools);
                MyToolsBean.DataBean tool=new MyToolsBean.DataBean();
                myTools.add(tool);
                myToolsAdapter.notifyDataSetChanged();
            }
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showData(CalenderedBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<CalenderedBean.DataBean> data = dataBean.getData();
            if (data != null) {
                this.data.clear();
                this.data.addAll(data);
                SpUtils.setParam("calendarInfo",new Gson().toJson(data));
                for (int i = 0; i < calendarList.size(); i++) {
                    calendarList.get(i).setWork(false);
                }
                for (int i = 0; i < data.size(); i++) {
                    String beginTime = data.get(i).getBeginTime();
                    Date timeb = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                    String endTime = data.get(i).getEndTime();
                    Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                    long startL = timeb.getTime();
                    long endL = endD.getTime();
                    for (int j = 0; j < calendarList.size(); j++) {
                        Date currentD = calendarList.get(j).getDate();
                        String ymd = DateFormatUtil.getTime(currentD, Const.Y_M_D);
                        Date startDL = DateFormatUtil.getTime(ymd + Const.endTime, Const.YMD_HMS);
                        long startTL = startDL.getTime();
                        Date endDd = DateFormatUtil.getTime(ymd + Const.startTime, Const.YMD_HMS);
                        long endDL = endDd.getTime();
                        if (startTL >= startL && endDL <= endL) {
                            calendarList.get(j).setWork(true);
                        }
                    }
                }

//                mainCalenderAdapter.notifyDataSetChanged();
                refreshMainVP();
                refreshWorkRv(mPosition);
            }
        }
    }

    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            Log.e("path", path);
            Bitmap bmp= BitmapFactory.decodeFile(path);
            localbanner=bmp;
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            Log.i("tag","path"+path.toString());
            //Glide 加载图片简单用法
            Glide.with(getContext()).load(path.toString()).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().centerCrop().into(imageView);
           // Glide.with(getContext()).load(R.drawable.banner).transform(new GlideRoundTransform(context,10)).into(imageView);

        }

    }

    @Override
    public void showError() {

        ToastUtils.Toast_short(getActivity(),ResourcesUtils.getString(R.string.failconnect));

    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @OnClick({R.id.add_schedule, R.id.no_caltv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_schedule:
                if (CommonAction.getIsLogin()){
                    long timeInMillis = tempCalendar.getTimeInMillis();
                    Intent intent = new Intent(getContext(), NewCalenderActivity.class);
                    intent.putExtra("date", String.valueOf(timeInMillis));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                }else {
                    DialogUtils.getLoginTip(getActivity()).show();
                }
                break;
            case R.id.no_caltv:
//                IntentUtils.toActivity(MyTestActivity.class,getActivity());
                if (CommonAction.getIsLogin()){
                    long timeInMillis = tempCalendar.getTimeInMillis();
                    Intent intent = new Intent(getContext(), NewCalenderActivity.class);
                    intent.putExtra("date", String.valueOf(timeInMillis));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                }else {
                    DialogUtils.getLoginTip(getActivity()).show();
                }
                break;
        }
    }

    @Override
    public void showData(MainFunctionBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
           /* List<MainFunctionBean.DataBean> data = dataBean.getData();
            if (data != null) {
                functionList.clear();
                functionList.addAll(data);
                mainFunctionAdpter.notifyDataSetChanged();
            }*/
        }
    }

    public final static String[] nStr1 = new String[]{"", "正", "二", "三", "四",
            "五", "六", "七", "八", "九", "十", "冬", "腊"};

    private int leftSum,rightSum;
    //日历的绘制
    public void setCalendarData() {
        Calendar c;
        int year;
        int month;
        int date;
        int weekDay;
        long[] l;

        SimpleDateFormat df = new SimpleDateFormat("dd");

        //六个月前的数据
        c = Calendar.getInstance();
        Date date1 = c.getTime();
        int leftNum = 2*7;
        int tempNum = c.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        int leftSize= 0;
        if (tempNum>0){
            c.add(Calendar.DAY_OF_MONTH, -tempNum);
            leftSize = tempNum+leftNum;
        }else if (tempNum<0){
            c.add(Calendar.DAY_OF_MONTH, -6);
            leftSize = 6+leftNum;
        }else {
            leftSize = leftNum;
        }

        if (leftSize!=0){
            mPosition = leftSize;
        }
        Date date2 = c.getTime();
        int intervalDays = DateUtil.getIntervalDays(date1, date2);
        leftSum = leftSize;
        for (int i = 0; i < leftSize; i++) {
            c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, -(i + 1));
            CalendarBean calendarBean = new CalendarBean();
            calendarBean.setWeekDay(Week.getWeekDay(c.get(Calendar.DAY_OF_WEEK)));
            calendarBean.setgDay(df.format(c.getTime()));
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            date = c.get(Calendar.DATE);
            l = ChinaDate.calElement(year, month, date);
            String chinadate = ChinaDate.getChinaDate((int) l[2]);
            if (chinadate.equals("初一")) {
                chinadate = nStr1[(int) l[1]] + "月";
            }
            calendarBean.setnDay(chinadate);
            calendarBean.setDate(c.getTime());
            calendarBean.setTag("before");
            halfyear1.add(calendarBean);
        }
        Collections.reverse(halfyear1);
        calendarList.addAll(halfyear1);

        //今天的数据
        c = Calendar.getInstance();
        Log.e("tag", "today=" + df.format(c.getTime()));
        CalendarBean calendarBean = new CalendarBean();
        calendarBean.setgDay(df.format(c.getTime()));
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        date = c.get(Calendar.DATE);
        l = ChinaDate.calElement(year, month, date);
        String chinadate = ChinaDate.getChinaDate((int) l[2]);
        if (chinadate.equals("初一")) {
            chinadate = nStr1[(int) l[1]] + "月";
        }
        Log.e("tag", "chinadate=" + chinadate);
        calendarBean.setnDay(chinadate);
        weekDay = c.get(Calendar.DAY_OF_WEEK);
        calendarBean.setWeekDay(Week.getWeekDay(weekDay));
        Log.e("tag", "weekDay=" + weekDay);
        calendarBean.setTag("today");
        calendarBean.setDate(c.getTime());
        calendarList.add(calendarBean);

        //6个月后的数据
        c = Calendar.getInstance();
        Date date3 = c.getTime();
        int rightNum = 6-tempNum+leftNum;
        c.add(Calendar.DAY_OF_MONTH, 6-tempNum+leftNum);
        Date date4 = c.getTime();
        int sixMonthNum = DateUtil.getIntervalDays(date3, date4);
        rightSum = rightNum;
        for (int i = 0; i < rightNum; i++) {
            c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, (i + 1));
            CalendarBean bean = new CalendarBean();
            bean.setWeekDay(Week.getWeekDay(c.get(Calendar.DAY_OF_WEEK)));
            bean.setgDay(df.format(c.getTime()));
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            date = c.get(Calendar.DATE);
            l = ChinaDate.calElement(year, month, date);
            String gStr = ChinaDate.getChinaDate((int) l[2]);
            if (gStr.equals("初一")) {
                gStr = nStr1[(int) l[1]] + "月";
            }
            bean.setnDay(gStr);
            bean.setTag("future");
            bean.setDate(c.getTime());
            halfyear2.add(bean);
        }
        calendarList.addAll(halfyear2);

        refreshMainVP();
//        mainCalenderAdapter.notifyDataSetChanged();


    }

    private void refreshMainVP() {
        int a = 6;
        List<View> calViews = new ArrayList<>();
        Log.e("tag","calendarList.size()="+calendarList.size());
        if (calendarList.size()>=35){
            for (int i = 0; i <5 ; i++) {
                List<CalendarBean> list = calendarList.subList((a * i + i), a * (i + 1) + (i+1));
                RecyclerView recyclerView = new RecyclerView(getContext());
                RvUtils.setOption(recyclerView,getActivity());
                recyclerView.setAdapter(new MainCalenderAdapter(getContext(),list, R.layout.rvitem_maincalender));
                calViews.add(recyclerView);
            }
        }
        Log.e("tag","calViews.size()="+calViews.size());
//        clv_vp.removeAllViews();

        clv_vp.setAdapter(new MainVpAdapterView(calViews));
        if (mPosition!=0){
            int b=mPosition/7;
            clv_vp.setCurrentItem(b);
        }else {
            clv_vp.setCurrentItem(2);
        }

    }

    public class MainCalenderAdapter extends RVBaseAdapter<CalendarBean> {
        int screenWidth = DensityUtil.getScreenWidth(getActivity());

        public MainCalenderAdapter(Context context, List<CalendarBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(final RVBaseHolder holder, final CalendarBean calendarBean, final int position) {
            int itemWidth = (screenWidth - DensityUtil.dip2px(28)) / 7;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            params.width = itemWidth;
            holder.itemView.setLayoutParams(params);
            final Date date = calendarBean.getDate();
            final Calendar calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentYear = calendar.get(Calendar.YEAR);
            String mm = DateFormatUtil.getTime(date, "MM");
            String year = DateFormatUtil.getTime(date, "yyyy");

            TextView gDay_tv = holder.getView(R.id.gday_tv);
            TextView nday_tv = holder.getView(R.id.nday_tv);
            String gDay = calendarBean.getgDay();
            if (Integer.parseInt(gDay) < 10) {
                String tempDay = gDay.replaceAll("0", "");
                gDay_tv.setText(tempDay);
            } else {
                gDay_tv.setText(gDay);
            }
            nday_tv.setText(calendarBean.getnDay());
            holder.setText(R.id.week_tv, calendarBean.getWeekDay());
            String s = String.valueOf(currentYear);
            String s1 = String.valueOf(currentMonth);

            if (calendarBean.getTag().equals("today")) {
                gDay_tv.setTextColor(Color.RED);
                gDay_tv.setBackgroundResource(R.drawable.shape_selectcal);
                nday_tv.setTextColor(Color.RED);
            } else {
                if (year.equals(s) && mm.equals(s1)) {
                    gDay_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                } else {
                    gDay_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                }
                nday_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
            }
            ImageView work_iv = holder.getView(R.id.work_iv);
            if (calendarBean.isWork()) {
                work_iv.setVisibility(View.VISIBLE);
            }else {
                work_iv.setVisibility(View.GONE);
            }
            LinearLayout select_lay = holder.getView(R.id.select_lay);
            if (calendarBean.isSelect()){
                gDay_tv.setBackgroundResource(R.drawable.shape_selectcal2);
                gDay_tv.setTextColor(ResourcesUtils.getColor(R.color.white));
            }else {
                gDay_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
                if (calendarBean.getTag().equals("today")) {
                    gDay_tv.setBackgroundResource(R.drawable.shape_selectcal);
                }
            }

            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    for (int i = 0; i < calendarList.size() ; i++) {
                        if (calendarBean.getDate().getTime()==calendarList.get(i).getDate().getTime()){
                            mPosition = i;
                            calendarList.get(i).setSelect(true);
                            refreshWorkRv(mPosition);
                            Log.i("tag","time:"+DateFormatUtil.getTime(calendarBean.getDate(),Const.YMD_HMS));
                            tempCalendar.setTime(calendarBean.getDate());
                        }else {
                            calendarList.get(i).setSelect(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    private int mPosition = 0;
    private void refreshWorkRv(int position) {
        worksList.clear();
        Date currentD = calendarList.get(position).getDate();
        String ymd = DateFormatUtil.getTime(currentD, Const.Y_M_D);
        String nyr = DateFormatUtil.getTime(currentD, Const.NYR);
        Calendar calendar = Calendar.getInstance();
         String cud=DateFormatUtil.getTime(calendar.getTime(),Const.NYR);
         if (nyr.equals(cud)) {
             day_tv1.setText("今天");
         }else {
             day_tv1.setText(nyr);
         }

        Date startDL = DateFormatUtil.getTime(ymd + Const.endTime, Const.YMD_HMS);
        long startTL = startDL.getTime();
        Date endDd = DateFormatUtil.getTime(ymd + Const.startTime, Const.YMD_HMS);
        long endDL = endDd.getTime();
        for (int i = 0; i < data.size(); i++) {
            String beginTime = data.get(i).getBeginTime();
            Date timeb = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
            String endTime = data.get(i).getEndTime();
            Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
            long startL = timeb.getTime();
            long endL = endD.getTime();

            if (startTL >= startL && endDL <= endL) {
                worksList.add(data.get(i));
            }
        }

        if (worksList.size()>0){
            if (position==3){
                SendAlarmBroadcast.startAlarmService(getActivity());//设置闹钟提醒
            }
            no_caltv.setVisibility(View.GONE);
            no_callay.setVisibility(View.GONE);
        }else {
            refreshIsfirst(currentD);
        }
        workCalenderAdapter.notifyDataSetChanged();
    }

    private void refreshIsfirst(Date currentD) {
        if (!CommonAction.getIsFirst()){
            no_callay.setVisibility(View.VISIBLE);
            no_callay.setData(currentD);
        }else {
            no_caltv.setVisibility(View.VISIBLE);
        }
    }

    public class WorkCalenderAdapter extends CommonAdapter<CalenderedBean.DataBean> {

        public WorkCalenderAdapter(Context context, List<CalenderedBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, final CalenderedBean.DataBean calendarBean, int position) {
            TextView work_title = holder.getView(R.id.title_tv);
            TextView endtime_tv = holder.getView(R.id.endtime_tv);
            TextView begint_tv = holder.getView(R.id.begint_tv);

            ImageView dot_iv = holder.getView(R.id.dot_iv);
            work_title.setText(calendarBean.getTitle());
            String beginTime = calendarBean.getBeginTime();
            String endTime = calendarBean.getEndTime();
            Date endDate = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
            Date beginDate = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
            Log.i("myTag",beginTime+"");
            int dayExpend = CalculateTimeUtil.getDayExpend(beginDate.getTime(), endDate.getTime());
            if (dayExpend>=1){
                /*dayexpend_tv.setVisibility(View.VISIBLE);
                dayexpend_tv.setText("+"+dayExpend);*/
                holder.setText(R.id.begint_tv, beginTime.substring(5,beginTime.length()-3)+"  —  ");
                holder.setText(R.id.endtime_tv, endTime.substring(5,endTime.length()-3));
            }else {
              /*  dayexpend_tv.setVisibility(View.GONE);*/
                holder.setText(R.id.begint_tv, beginTime.substring(11,beginTime.length()-3)+"  —  ");
                holder.setText(R.id.endtime_tv, endTime.substring(11,endTime.length()-3));
            }
            Calendar cal = Calendar.getInstance();
            if (cal.getTimeInMillis()>endDate.getTime()){
                work_title.setTextColor(ResourcesUtils.getColor(R.color.outdatecolor));
                dot_iv.setImageResource(R.drawable.shape_circle_dot);
            }else {
                work_title.setTextColor(ResourcesUtils.getColor(R.color.black));
                dot_iv.setImageResource(R.mipmap.dot1 );
            }
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = calendarBean.getScheduleId();
                    if (scheduleId!=null&&!scheduleId.isEmpty()){
                        Intent intent = new Intent(getContext(), CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
    }
    public class MyToolsAdapter extends CommonAdapter<MyToolsBean.DataBean>{

        public MyToolsAdapter(Context context, List<MyToolsBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, MyToolsBean.DataBean dataBean, int position) {
            ImageView img=helper.getView(R.id.img_iv);
            TextView  content_tv=helper.getView(R.id.content_tv);
            if (position==3){
                img.setImageResource(R.mipmap.icon_more);
                content_tv.setText("更多");
                helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        startActivity(new Intent(getContext(),FunctionActivity.class));
                    }
                });
            }else {
                helper.setImageByUrl(R.id.img_iv,dataBean.getImgUrl(),false);
                content_tv.setText(dataBean.getActionName());
                final String url=dataBean.getLink();
                helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        if (url != null && !url.isEmpty()) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra(Const.webUrl, url);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mHandler != null) {
                mHandler.postDelayed(this, Const.time);

               /* if (main_bannervp.getCurrentItem() < bannerIvs.size() - 1) {
                    main_bannervp.setCurrentItem(toRealPosition(main_bannervp.getCurrentItem() + 1));
                } else if (main_bannervp.getCurrentItem() == bannerIvs.size() - 1) {
                    main_bannervp.setCurrentItem(toRealPosition(0));
                }*/
//                main_bannervp.setCurrentItem(toRealPosition(0));

            }
        }
    };

    int toRealPosition(int position) {
        int realCount = bannerIvs.size() - 2;
        if (realCount == 0)
            return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;

        return realPosition;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
            mHandler.removeMessages(100);
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMain(RefreshCalBean refreshCalBean) {
       getCalendarData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMain2(RefreshMyToolsBean refreshMyToolsBean) {
       if (refreshMyToolsBean.getEventStr().equals(Const.refresh)){
           getMyToolsData();
       }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMain1(LoginBus refreshCalBean) {
        if (refreshCalBean.getEventStr().equals(Const.refresh)){
            if (CommonAction.getIsLogin()){
                getCalendarData();
            }else {
                for (int i = 0; i < calendarList.size(); i++) {
                    calendarList.get(i).setWork(false);
                }
                mainCalenderAdapter.notifyDataSetChanged();
                data.clear();
                refreshWorkRv(mPosition);
            }
        }
    }
    //滑动监听
    @Override
    public void onScroll(int scrollY) {
        mHeight =mTabViewLayout.getTop();
        //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
        Log.e("tag",mHeight+"mHeight"+","+scrollY);
        if (scrollY > 0 && scrollY>= mHeight) {
            if (mTopView.getParent() != mTopTabViewLayout) {
                mTabViewLayout.removeView(mTopView);
                mTopTabViewLayout.addView(mTopView);
            }
        } else {
            if (mTopView.getParent() != mTabViewLayout) {
                mTopTabViewLayout.removeView(mTopView);
                mTabViewLayout.addView(mTopView);
            }

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
