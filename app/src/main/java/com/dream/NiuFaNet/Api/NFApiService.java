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

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * 接口集中类
 * Created by Administrator on 2017/4/7/007.
 */
public interface NFApiService {

    //首页推荐list
    @FormUrlEncoded
    @POST(Const.app+"apiQuestion.do")
    Observable<RecomendBean> getRecomendData(@Field("id") String user_id);

    //问答
    @FormUrlEncoded
    @POST(Const.app+"nfVoice.do")
    Observable<ChatBean> getChatAnswer(@Field("id") String id, @Field("text") String text, @Field("type") String type, @Field("IMEI") String token);

    //反馈
    @FormUrlEncoded
    @POST(Const.app+"apiFeedback.do")
    Observable<CommonBean> toFeedBack(@FieldMap Map<String, String> map);

    //获取验证码
    @FormUrlEncoded
    @POST(Const.app+"apiCaptcha.do")
    Observable<CommonBean> getYzCode(@Field("mobilePhone") String mobilePhone, @Field("type") String type);

    //注册
    @FormUrlEncoded
    @POST(Const.app+"apiRegister.do")
    Observable<RegisterBean> toRegister(@Field("mobilePhone") String mobilePhone, @Field("captcha") String captcha, @Field("password") String password);

    //登录
    @FormUrlEncoded
    @POST(Const.app+"apiLogin.do")
    Observable<LoginBean> toLogin(@Field("mobilePhone") String mobilePhone, @Field("password") String password);

    //第三方（微信、qq）登录
    @FormUrlEncoded
    @POST(Const.app+"apiThirdLogin.do")
    Observable<LoginBean> toLogin(@Field("openId") String openId, @Field("unionId") String unionId, @Field("nickName") String nickName, @Field("loginType") String loginType,@Field("headUrl") String headUrl);

    //编辑用户信息
    @FormUrlEncoded
    @POST(Const.app+"apiEditUser.do")
    Observable<CommonBean> editUserInfo(@Field("user") String user);

    //找回密码
    @FormUrlEncoded
    @POST(Const.app+"apiFindPassword.do")
    Observable<CommonBean> findbackPWD(@Field("mobilePhone") String mobilePhone, @Field("captcha") String captcha, @Field("password") String password);

    //修改密码
    @FormUrlEncoded
    @POST(Const.app+"apiUpdPassword.do")
    Observable<CommonBean> editPWD(@Field("userId") String userId, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    //绑定手机
    @FormUrlEncoded
    @POST(Const.app+"apiBindingPhone.do")
    Observable<LoginBean> bindPhone(@Field("mobilePhone") String mobilePhone, @Field("captcha") String captcha, @Field("userId") String userId);

    //图片上传
    @Multipart
    @POST(Const.app+"apiImage.do")
    Observable<HeadPortraitBean> updateHeadPortrait(@Part MultipartBody.Part fdImg_file, @Part("userId") RequestBody userId);

    //图片上传（错误示范）
    @FormUrlEncoded
    @POST(Const.app+"apiImage.do")
    Observable<HeadPortraitBean> updateHeadPortrait(@Field("file") File fdImg_file, @Field("userId") String userId);

    //分享
    @FormUrlEncoded
    @POST(Const.app+"apiShare.do")
    Observable<ShareBean> shareAPP(@Field("userId") String userId);

    //功能
    @FormUrlEncoded
    @POST(Const.app+"apiAction.do")
    Observable<FunctionBean> getFunctionData(@Field("userId") String userId);

    //版本更新
    @FormUrlEncoded
    @POST(Const.app+"apiVersions.do")
    Observable<VersionBean> requestVersion(@Field("type") String type, @Field("version") String version);

    //首页banner
    @FormUrlEncoded
    @POST(Const.app+"apiBanner.do")
    Observable<BannerBean> getBannerData(@Field("type") String type);

    //首页功能
    @FormUrlEncoded
    @POST(Const.app+"apiIndexAction.do")
    Observable<MainFunctionBean> getMainFunction(@Field("type") String type);

    //创建日程
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @Multipart
    @POST(Const.app+"createSchedule.do")
    Observable<NewCalResultBean> addCalender(@Part("data") RequestBody data, @Part MultipartBody.Part[] mFile);

    //获取日程列表
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchScheduleByDay.do")
    Observable<CalenderedBean> getCalenders(@Field("userId") String userId, @Field("beginTime") String beginTime, @Field("endTime") String endTime);

    //查看日程
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchSchedule.do")
    Observable<CalendarDetailBean> getCalendarDetail(@Field("userId") String userId,@Field("scheduleId") String scheduleId);

    //删除日程图片
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"deletePic.do")
    Observable<CommonBean> deletePic(@Field("id") String picId);

    //删除日程
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"deleteSchedule.do")
    Observable<CommonBean> deleteCalendar(@Field("scheduleId") String scheduleId);

    //修改日程
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @Multipart
    @POST(Const.app+"editSchedule.do")
    Observable<NewCalResultBean> edtCalender(@Part("data") RequestBody data, @Part MultipartBody.Part[] mFile);

    //提示语
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"prompt.do")
    Observable<RemindWordBean> getRemindWord(@Field("id") String id);

    //语音提取
    @FormUrlEncoded
    @POST("niufa_chatbot/schedule.do")
    Observable<List<InputGetBean>> getInputWord(@Field("text") String text);

    //创建项目
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"createProject.do")
    Observable<NewProResultBean> newProgram(@Field("data") String data);

    //项目列表
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchProjectList.do")
    Observable<ProgramListBean> getProgramlist(@Field("userId") String userId,@FieldMap Map<String, String> map);

    //根据项目获取日程
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchProjectSchedule.do")
    Observable<CommonBean> searchProjectSchedule(@Field("projectId") String projectId);

    //项目详情
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchProject.do")
    Observable<ProgramDetailBean> getProjectDetail(@Field("projectId") String projectId);

    //编辑项目
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"editProject.do")
    Observable<CommonBean> edtProject(@Field("data") String data);

    //删除项目
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"deleteProject.do")
    Observable<CommonBean> deleteProject(@Field("projectId") String projectId);

    //搜索用户
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchUsers.do")
    Observable<SearchUserBean> searchUser(@Field("keyWord") String keyWord);

    //我的好友列表
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"searchFriends.do")
    Observable<MyFrendBean> getMyFrends(@Field("userId") String userId);

    //添加好友
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"addFriend.do")
    Observable<CommonBean> addFrend(@Field("userId") String userId,@Field("friendId") String friendId);

    //陌生人的添加好友请求
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"addFriendNotice.do")
    Observable<ApplyBeFrendBean> applyBeFrend(@Field("userId") String userId);

    //答复好友申请
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"replyAddFriend.do")
    Observable<CommonBean> answerApply(@Field("id") String id,@Field("status") String status);

    //日程邀请的通知
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"scheduleNotice.do")
    Observable<CalInviteBean> getCalInviteList(@Field("userId") String userId);

    //答复日程邀请
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"replySchedule.do")
    Observable<CommonBean> replySchedule(@Field("scheduleId") String scheduleId,@Field("status") String status,@Field("userId") String userId);

    //删除日程参与人
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"deleteParticipant.do")
    Observable<CommonBean> deleteParticipant(@Field("scheduleId") String scheduleId,@Field("userId") String userId);

    //删除日程参与人
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"sendMsg.do")
    Observable<CommonBean> sendMsg(@Field("mobile") String phoneNum,@Field("userId") String userId,@Field("userName") String userName);

    //添加工作可见的好友
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"addWorkVisible.do")
    Observable<CommonBean> addWorkVisible(@Field("data") String data);

    //删除工作可见用户
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"deleteWorkVisible.do")
    Observable<CommonBean> deleteWorkVisible(@Field("userId") String userId,@Field("user_id") String user_id);

    //查找工作可见用户
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"workVisible.do")
    Observable<WorkVisibleBean> getWorkVisible(@Field("userId") String userId);

    //查找工作可见用户
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @POST(Const.app+"voicePrompt.do")
    Observable<VoiceRvBean> voicePrompt();

    //导出日程
    @Headers({"Authorization:d3d3Lm5pdWZhLmNu"})
    @FormUrlEncoded
    @POST(Const.app+"exportProjectSchedule.do")
    Observable<CommonBean> exportProjectSchedule(@FieldMap Map<String,String> map);

}
