package com.dream.NiuFaNet.Other;

/**
 * Created by Administrator on 2017/4/5/005.
 */
public class Const {

    public static final String LogTag = "LogTag";
    public static final int time = 3000;

    public static final String API_BASE_URL = "http://api.niufa.cn:9081/";
//    public static final String API_BASE_URL = "http://192.168.1.143:8080/niufa_chatbot/";
    public static final String aboutmeUrl = "http://api.niufa.cn:9081/about.html";
    public static final String shareUrl = "http://api.niufa.cn:9081"+
            "/sharing.html";
    //public static final String API_BASE_URL="http://10.0.0.59:8080/niufa_chatbot/";
    public static final String app = "app/";
    public static final String token = "Authorization:d3d3Lm5pdWZhLmNu";
    public static final int CAMERA = 23;
    public static final int PICTURE = 25;
    public static final int perSD = 66;
    public static final int GO_GUIDE=100;
    public static final int GO_MAIN=101;
    public static final String pageSize = "pageSize";
    public static final String page = "page";
    public static final String activity = "activity";
    public static final String Them = "Them";
    public static final String Candy = "Candy";
    public static final String Black = "Black";
    public static final String voiceType = "voice_type";
    public static final String vTyep_On = "on";
    public static final String vTyep_Off = "off";
    public static final String success = "false";
    public static final String fail = "true";
    public static final String intentTag = "itentTag";
    public static final String ID = "id";
    public static final String questionType = "app";
    public static final String questionUrl = "questionUrl";
    public static final String refresh = "refresh";
    public static final String is_first = "is_first";
    public static final String picUrl = "picUrl";
    public static final String title = "title";
    public static final int SPLASH_DELAY_TIME=500;//闪屏时间
    public static final int chatSize=10;//闪屏时间
    public static final String content = "content";
    public static final String dataCache = "dataCache";
    public static final String mainCache = "mainCache";
    public static final String mainStatu = "mainStatu";
    public static final String scrollPosition = "scrollPosition";
    public static final String leftScroll = "leftScroll";
    public static final String rightScroll = "rightScroll";
    public static final String dataBean = "dataBean";

    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String YMD_HM = "yyyy-MM-dd  HH:mm";
    public static final String YMD_HM2 = "yyyy.MM.dd HH:mm";
    public static final String MD_HM3 = "MM.dd HH:mm";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String NYR = "yyyy年MM月dd日";
    public static final String MR = "MM月dd日";
    public static final String MD = "MM-dd";
    public static final String HM = "HH:mm";
    public static final String DD = "dd";
    public static final String HMS = "HH:mm:ss";
    public static final String startTime = " 00:00:00";
    public static final String endTime = " 23:59:59";

    public static final String scheduleId = " scheduleId";
    public static final String defaultTip = " 准点提醒";
    public static final int defaultTip_Minute = 0;

    //分享
    public static final String wechatFrend_ShareTitle = "邀你成为好友，一起使用法律机器人";
    public static final String wechatFrend_ShareContent = "法律机器人会18 项技能，还会做案件管理和日程协同，快去查看他的日程吧。";
    public static final String wechatFrend_ShareClickUrl = "http://m.niufa.cn/Schedule/index?userId=";


    public static final int CAll = 28;
    public static final String webUrl = "webUrl";
    public static final String edit = "edit";
    public static final String Contact = "Contact";
    public static final String Letter = "Letter";
    public static final String Banner="banner";
    public static final String MyTools="MyTools";
    //判断是否是本地登录（非第三方登录）
    public static final String isBd = "isBd";
    public static final String thirdType = "thirdType";
    public static final String bdUser = "bduser";


    //第三方登录
//    public static final String tdHeadUrl = "tdHeadUrl";
//    public static final String tdUserName = "tdUserName";


    //存储用户信息
    public static final String userId = "userId";
    public static final String userName = "userName";
    public static final String userPhone = "userPhone";
    public static final String userEmail = "userEmail";
    public static final String userHeadUrl = "userHeadUrl";
    public static final String userAddress = "userAddress";
    public static final String isLogin = "isLogin";
    public static final String userSex = "userSex";
    public static final String company = "company";
    public static final String userDuty = "userDuty";

    //新建日程返回码
    public static final int EVENT = 100;
    public static final int REMIND = 101;
    public static final int REMINDTYPE = 102;
    public static final int ADDRESS = 103;
    public static final int NOTE = 104;

    //修改个人信息
    public static final int NAME = 110;
    public static final int COMPANY = 111;
    public static final int WORK = 112;
    public static final int EMAIL = 113;
    public static final int FATE = 114;
    public static final int USERADDRESS = 115;

    public static String CALANDER_URL = "content://com.android.calendar/calendars";
    public static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    public   static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";



}
