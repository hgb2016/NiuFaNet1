package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hou on 2018/4/2.
 */

public class CalenderLogActivity extends CommonActivity {
    @Bind(R.id.log_lv)
    MyListView log_lv;
    @Override
    public int getLayoutId() {
        return R.layout.activity_calenderlog;
    }

    @Override
    public void initView() {
        ArrayList list=new ArrayList();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        log_lv.setAdapter(new LogAdapter(getApplicationContext(),list,R.layout.view_calenderlog));
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }
    public class LogAdapter extends CommonAdapter{


        public LogAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
}
