package com.dream.NiuFaNet.Ui.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;

import butterknife.Bind;
import butterknife.OnClick;

public class CalendarMessageActivity extends CommonActivity {
    @Bind(R.id.created_lay)
    LinearLayout created_lay;
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.calendarstatus_tv)
    TextView calendarstatus_tv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.starttime_tv1)
    TextView starttime_tv1;
    @Bind(R.id.starttime_tv2)
    TextView starttime_tv2;
    @Bind(R.id.duringtime_tv)
    TextView duringtime_tv;
    @Bind(R.id.endtime_tv1)
    TextView endtime_tv1;
    @Bind(R.id.endtime_tv2)
    TextView endtime_tv2;
    @Bind(R.id.type_relay)
    LinearLayout type_relay;
    @Bind(R.id.personal_lay)
    LinearLayout personal_lay;
    @Bind(R.id.work_lay)
    LinearLayout work_lay;
    @Bind(R.id.personal_iv)
    ImageView personal_iv;
    @Bind(R.id.work_iv)
    ImageView work_iv;
    @Bind(R.id.remind_relay)
    LinearLayout remind_lay;
    @Bind(R.id.remind_tv)
    TextView remind_tv;
    @Bind(R.id.particepant_lay)
    LinearLayout particepant_lay;
    @Bind(R.id.particepant_gv)
    MyGridView particepant_gv;
    @Bind(R.id.project_lay)
    LinearLayout project_lay;
    @Bind({R.id.project_tv})
    TextView project_tv;
    @Bind(R.id.address_lay)
    LinearLayout address_lay;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Bind(R.id.file_lay)
    LinearLayout file_lay;
    @Bind(R.id.pic_gv)
    MyGridView pic_gv;
    @Bind(R.id.beizu_lay)
    LinearLayout beizu_lay;
    @Bind(R.id.beizu_tv)
    TextView beizu_tv;
    @Bind(R.id.bot_relay1)
    RelativeLayout bot_relay1;
    @Bind(R.id.bot_relay2)
    RelativeLayout bot_relay2;


    @Override
    public int getLayoutId() {
        return R.layout.activity_calendermessage;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);


    }
    @OnClick({R.id.back_relay})
    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
        }
    }

    //初始化数据
    @Override
    public void initDatas() {

    }

    //监听事件
    @Override
    public void eventListener() {

    }
}
