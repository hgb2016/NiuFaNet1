package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MessageActivity extends CommonActivity {

    @Bind(R.id.system_lv)
    MyListView system_lv;
    @Bind(R.id.tudo_lv)
    MyListView tudo_lv;
    @Bind(R.id.project_lv)
    MyListView project_lv;
    @Bind(R.id.calendar_lv)
    MyListView calendar_lv;
    private SystemAdapter systemAdapter;
    private TudoAdapter tudoAdapter;
    private ProjectAdapter  projectAdapter;
    private CalendarAdapter calendarAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);

        ArrayList list=new ArrayList();
        list.add("1");
        list.add("1");
        systemAdapter=new SystemAdapter(mContext,list,R.layout.view_system_message);
        tudoAdapter=new TudoAdapter(mContext,list,R.layout.view_tudo_message);
        projectAdapter=new ProjectAdapter(mContext,list,R.layout.view_project_message);
        calendarAdapter=new CalendarAdapter(mContext,list,R.layout.view_calendar_message);

        system_lv.setAdapter(systemAdapter);
        tudo_lv.setAdapter(tudoAdapter);
        project_lv.setAdapter(projectAdapter);
        calendar_lv.setAdapter(calendarAdapter);
    }

    //初始化数据
    @Override
    public void initDatas() {


    }

    //事件监听
    @Override
    public void eventListener() {
        calendar_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext,CalendarMessageActivity.class));
            }
        });
    }
    //点击事件
    @OnClick({R.id.back_relay})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;

        }
    }

    //系统消息列表
    public class SystemAdapter extends CommonAdapter{

        public SystemAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
    //项目消息
    public class ProjectAdapter extends CommonAdapter{


        public ProjectAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
    //待办消息
    public class TudoAdapter extends CommonAdapter{


        public TudoAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
    //日程消息
    public class CalendarAdapter extends CommonAdapter{


        public CalendarAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
}
