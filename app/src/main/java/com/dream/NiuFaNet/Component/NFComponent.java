package com.dream.NiuFaNet.Component;

import com.dream.NiuFaNet.CustomView.ApplyFrendView;
import com.dream.NiuFaNet.CustomView.CalenderRvUtils.CalendarRecyclerView;
import com.dream.NiuFaNet.CustomView.CalenderRvUtils.ChildRvReLay;
import com.dream.NiuFaNet.CustomView.MessageChildView;
import com.dream.NiuFaNet.CustomView.PopChildView;
import com.dream.NiuFaNet.Ui.Activity.AddFrendsActivity;
import com.dream.NiuFaNet.Ui.Activity.AddProjectActivity;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity1;
import com.dream.NiuFaNet.Ui.Activity.ChangePwdActivity;
import com.dream.NiuFaNet.Ui.Activity.ChatActivity;
import com.dream.NiuFaNet.Ui.Activity.ClientsActivity;
import com.dream.NiuFaNet.Ui.Activity.DownScheduleActivity;
import com.dream.NiuFaNet.Ui.Activity.FeedBackActivity;
import com.dream.NiuFaNet.Ui.Activity.FindPwdOrBindUserActivity;
import com.dream.NiuFaNet.Ui.Activity.FrendScheduleActivity;
import com.dream.NiuFaNet.Ui.Activity.FriendCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.FriendDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.FunctionActivity;
import com.dream.NiuFaNet.Ui.Activity.InputActivity;
import com.dream.NiuFaNet.Ui.Activity.InputGetActivity;
import com.dream.NiuFaNet.Ui.Activity.LoginActivity;
import com.dream.NiuFaNet.Ui.Activity.LoginActivity1;
import com.dream.NiuFaNet.Ui.Activity.MainActivity;
import com.dream.NiuFaNet.Ui.Activity.MineActivity;
import com.dream.NiuFaNet.Ui.Activity.MineActivity1;
import com.dream.NiuFaNet.Ui.Activity.MyFrendsActivity;
import com.dream.NiuFaNet.Ui.Activity.MySimFrendsActivity;
import com.dream.NiuFaNet.Ui.Activity.NewCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.NewClientActivity;
import com.dream.NiuFaNet.Ui.Activity.NewFriendsActivity;
import com.dream.NiuFaNet.Ui.Activity.NewMessageActivity;
import com.dream.NiuFaNet.Ui.Activity.NewProgramActivity;
import com.dream.NiuFaNet.Ui.Activity.ParticipantsActivity;
import com.dream.NiuFaNet.Ui.Activity.PopVoiceActivity;
import com.dream.NiuFaNet.Ui.Activity.ProgramDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.ProjectDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.RegisterActivity;
import com.dream.NiuFaNet.Ui.Activity.ResetPwdActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchFriendsActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchNewFriendsActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchProjectActivity;
import com.dream.NiuFaNet.Ui.Activity.SetRemindActivity1;
import com.dream.NiuFaNet.Ui.Activity.UploadApkActivity;
import com.dream.NiuFaNet.Ui.Activity.UserInfoActivity;
import com.dream.NiuFaNet.Ui.Activity.UserInfoActivity1;
import com.dream.NiuFaNet.Ui.Activity.VoiceActivity;
import com.dream.NiuFaNet.Ui.Activity.WelcomeActivity;
import com.dream.NiuFaNet.Ui.Activity.WorkVisibleFrendsActivity;
import com.dream.NiuFaNet.Ui.Fragment.CalenderFragment;
import com.dream.NiuFaNet.Ui.Fragment.ContactFragment;
import com.dream.NiuFaNet.Ui.Fragment.FunctionFragment;
import com.dream.NiuFaNet.Ui.Fragment.MainFragment;
import com.dream.NiuFaNet.Ui.Fragment.ProgramFragment;
import com.dream.NiuFaNet.Ui.Fragment.ProjectFragment;
import com.dream.NiuFaNet.Ui.Fragment.RecommendFragment;
import com.dream.NiuFaNet.CustomView.CalenderUtils.CalendarGridViewAdapter;
import com.dream.NiuFaNet.Ui.Service.AlarmService;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface NFComponent {
    LoginActivity inject(LoginActivity activity);
    RecommendFragment inject(RecommendFragment fragment);
    MainFragment inject(MainFragment fragment);
    FunctionFragment inject(FunctionFragment fragment);
    FunctionActivity inject(FunctionActivity activity);
    ProgramFragment inject(ProgramFragment fragment);
    CalenderFragment inject(CalenderFragment fragment);
    ChatActivity inject(ChatActivity activity);
    FeedBackActivity inject(FeedBackActivity activity);
    RegisterActivity inject(RegisterActivity activity);
    UserInfoActivity inject(UserInfoActivity activity);
    ResetPwdActivity inject(ResetPwdActivity activity);
    FindPwdOrBindUserActivity inject(FindPwdOrBindUserActivity activity);
    ChangePwdActivity inject(ChangePwdActivity activity);
    WelcomeActivity inject(WelcomeActivity activity);
    MainActivity inject(MainActivity activity);
    NewCalenderActivity inject(NewCalenderActivity activity);
    ChildRvReLay inject(ChildRvReLay view);
    CalendarRecyclerView inject(CalendarRecyclerView view);
    CalendarGridViewAdapter inject(CalendarGridViewAdapter adapter);
    CalenderDetailActivity inject(CalenderDetailActivity activity);
    CalenderDetailActivity1 inject(CalenderDetailActivity1 activity);
    InputGetActivity inject(InputGetActivity activity);
    NewProgramActivity inject(NewProgramActivity activity);
    ProgramDetailActivity inject(ProgramDetailActivity activity);
    AddFrendsActivity inject(AddFrendsActivity activity);
    MyFrendsActivity inject(MyFrendsActivity activity);
    FrendScheduleActivity inject(FrendScheduleActivity activity);
    ApplyFrendView inject(ApplyFrendView view);
    PopChildView inject(PopChildView view);
    AlarmService inject(AlarmService service);
    MessageChildView inject(MessageChildView view);
    MySimFrendsActivity inject(MySimFrendsActivity activity);
    MineActivity inject(MineActivity activity);
    ParticipantsActivity inject(ParticipantsActivity activity);
    WorkVisibleFrendsActivity inject(WorkVisibleFrendsActivity activity);
    DownScheduleActivity inject(DownScheduleActivity activity);

    LoginActivity1 inject(LoginActivity1 activity);

    MineActivity1 inject(MineActivity1 activity);

    UserInfoActivity1 inject(UserInfoActivity1 activity);

    FriendCalenderActivity inject(FriendCalenderActivity activity);

    AddProjectActivity inject(AddProjectActivity activity);

    ProjectFragment inject(ProjectFragment fragment);

    ProjectDetailActivity inject(ProjectDetailActivity activity);

    NewClientActivity inject(NewClientActivity activity);

    ContactFragment inject(ContactFragment fragment);

    SearchNewFriendsActivity inject(SearchNewFriendsActivity activity);

    InputActivity inject(InputActivity activity);

    SearchFriendsActivity inject(SearchFriendsActivity activity);

    FriendDetailActivity inject(FriendDetailActivity activity);

    SetRemindActivity1 inject(SetRemindActivity1 activity1);

    NewFriendsActivity inject(NewFriendsActivity activity);

    NewMessageActivity inject(NewMessageActivity activity);

    VoiceActivity inject(VoiceActivity activity);

    SearchProjectActivity inject(SearchProjectActivity activity);

    PopVoiceActivity inject(PopVoiceActivity activity);

    UploadApkActivity inject(UploadApkActivity activity);

    ClientsActivity inject(ClientsActivity activity);
}