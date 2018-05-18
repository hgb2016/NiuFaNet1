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
 * Created by hou on 2018/3/30.
 */

public class ParticipantsActivity1 extends CommonActivity {
    @Bind(R.id.participant_lv)
    MyListView participant_lv;
    private List<String> lists=new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_participants1;
    }

    @Override
    public void initView() {
        lists.add("a");
        lists.add("b");
        lists.add("c");
        lists.add("a");
        participant_lv.setAdapter(new ParticipantsAdapter(getApplicationContext(),lists,R.layout.view_participant));
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }
    public class ParticipantsAdapter  extends CommonAdapter {
        public ParticipantsAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
}
