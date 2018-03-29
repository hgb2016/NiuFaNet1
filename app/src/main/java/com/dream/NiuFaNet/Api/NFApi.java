package com.dream.NiuFaNet.Api;


import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.FunctionBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.Bean.MainFunctionBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.NewProResultBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Bean.RegisterBean;
import com.dream.NiuFaNet.Bean.RemindWordBean;
import com.dream.NiuFaNet.Bean.SearchUserBean;
import com.dream.NiuFaNet.Bean.ShareBean;
import com.dream.NiuFaNet.Bean.VersionBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.Bean.WorkVisibleBean;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Utils.SpUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 接口封装类
 * Created by Administrator on 2017/4/7/007.
 */
public class NFApi {

    public static NFApi instance;

    private NFApiService service;

    public NFApi(OkHttpClient okHttpClient) {
//        Gson gson = new GsonBuilder() .setLenient() .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
               /* .addConverterFactory(new BaseConverterFactory() {
                    @Override
                    public BaseResponseConverter responseConverter() {
                        return new HotResponseConverter();
                    }
                })*/ // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(NFApiService.class);
    }

    public static NFApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new NFApi(okHttpClient);
        return instance;
    }

    public Observable<RecomendBean> getRecomendData(String topN) {
        return service.getRecomendData(topN);
    }

    public Observable<ChatBean> getChatAnswer(String id, String text, String type, String token) {
        return service.getChatAnswer(id, text, type, token);
    }

    public Observable<CommonBean> toFeedBack(Map<String, String> map) {
        return service.toFeedBack(map);
    }

    public Observable<CommonBean> getYzCode(String mobilePhone, String type) {
        return service.getYzCode(mobilePhone, type);
    }

    public Observable<RegisterBean> toRegister(String mobilePhone, String captcha, String password) {
        return service.toRegister(mobilePhone, captcha, password);
    }

    public Observable<LoginBean> toLogin(String mobilePhone, String password) {
        return service.toLogin(mobilePhone, password);
    }

    public Observable<LoginBean> toLogin(String openId, String unionId, String nickName, String loginType,String headUrl) {
        return service.toLogin(openId, unionId, nickName, loginType,headUrl);
    }

    public Observable<CommonBean> editUserInfo(String user) {
        return service.editUserInfo(user);
    }

    public Observable<CommonBean> findbackPWD(String mobilePhone, String captcha, String password) {
        return service.findbackPWD(mobilePhone, captcha, password);
    }

    public Observable<CommonBean> editPWD(String userId, String oldPassword, String newPassword) {
        return service.editPWD(userId, oldPassword, newPassword);
    }

    public Observable<LoginBean> bindPhone(String mobilePhone, String captcha, String userId) {
        return service.bindPhone(mobilePhone, captcha, userId);
    }

    public Observable<HeadPortraitBean> updateHeadPortrait(MultipartBody.Part fdImg_file, RequestBody userId) {
        return service.updateHeadPortrait(fdImg_file, userId);
    }

    public Observable<HeadPortraitBean> updateHeadPortrait(File fdImg_file, String userId) {
        return service.updateHeadPortrait(fdImg_file, userId);
    }

    public Observable<ShareBean> shareAPP(String id) {
        return service.shareAPP(id);
    }

    public Observable<FunctionBean> getFunctionBean(String id) {
        return service.getFunctionData(id);
    }
    public Observable<VersionBean> requestVersion(String type,String versionName) {
        return service.requestVersion(type,versionName);
    }
    public Observable<BannerBean> getBannerData(String type) {
        return service.getBannerData(type);
    }
    public Observable<MainFunctionBean> getMainFunction(String type) {
        return service.getMainFunction(type);
    }
    public Observable<NewCalResultBean> addCalender(RequestBody data, MultipartBody.Part[] mFile) {
        return service.addCalender(data,mFile);
    }
    public Observable<NewCalResultBean> edtCalender(RequestBody data, MultipartBody.Part[] mFile) {
        return service.edtCalender(data,mFile);
    }

    public Observable<CalenderedBean> getCalenders(String userId, String beginTime,String endTime) {
        return service.getCalenders(userId,beginTime,endTime);
    }
    public Observable<CalendarDetailBean> getCalendarDetail(String userId,String scheduleId) {
        return service.getCalendarDetail(userId,scheduleId);
    }
    public Observable<CommonBean> deleteCalendar(String scheduleId) {
        return service.deleteCalendar(scheduleId);
    }

    public Observable<CommonBean> deletePic(String picId) {
        return service.deletePic(picId);
    }

    public Observable<RemindWordBean> getRemindWord(String id) {
        return service.getRemindWord(id);
    }

    public Observable<List<InputGetBean>> getInputWord(String text) {
        return service.getInputWord(text);
    }
    public Observable<NewProResultBean> newProgram(String data) {
        return service.newProgram(data);
    }
    public Observable<ProgramListBean> getProgramlist(String userId,Map<String, String> map) {
        return service.getProgramlist(userId,map);
    }
    public Observable<CommonBean> searchProjectSchedule(String projectId) {
        return service.searchProjectSchedule(projectId);
    }
    public Observable<ProgramDetailBean> getProjectDetail(String projectId) {
        return service.getProjectDetail(projectId);
    }
    public Observable<CommonBean> edtProject(String data) {
        return service.edtProject(data);
    }
    public Observable<CommonBean> deleteProject(String projectId) {
        return service.deleteProject(projectId);
    }
    public Observable<SearchUserBean> searchUser(String keyWord) {
        return service.searchUser(keyWord);
    }
    public Observable<CommonBean> addFrend(String userId,String friendId) {
        return service.addFrend(userId,friendId);
    }
    public Observable<MyFrendBean> getMyFrends(String userId) {
        return service.getMyFrends(userId);
    }
    public Observable<ApplyBeFrendBean> applyBeFrend(String userId) {
        return service.applyBeFrend(userId);
    }
    public Observable<CommonBean> replyFrend(String id,String status) {
        return service.answerApply(id,status);
    }
    public Observable<CalInviteBean> getCalInviteList(String userId) {
        return service.getCalInviteList(userId);
    }
    public Observable<CommonBean> replySchedule(String id,String status,String userId) {
        return service.replySchedule(id,status,userId);
    }
    public Observable<CommonBean> deleteParticipant(String scheduleId,String userId) {
        return service.deleteParticipant(scheduleId,userId);
    }
    public Observable<CommonBean> sendMsg( String phoneNum,String userId, String userName) {
        return service.sendMsg(phoneNum,userId,userName);
    }
    public Observable<CommonBean> addWorkVisible(String data) {
        return service.addWorkVisible(data);
    }
    public Observable<CommonBean> deleteWorkVisible(String userId,String user_id) {
        return service.deleteWorkVisible(userId,user_id);
    }
    public Observable<WorkVisibleBean> getWorkVisible( String userId) {
        return service.getWorkVisible(userId);
    }
    public Observable<VoiceRvBean> voicePrompt() {
        return service.voicePrompt();
    }
    public Observable<CommonBean> exportProjectSchedule(Map<String,String> map) {
        return service.exportProjectSchedule(map);
    }



}
