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

public class CalenderApendixActivity extends CommonActivity {
    @Bind(R.id.apendix_lv)
    MyListView apendix_lv;
    private ArrayList<String> lists=new ArrayList();
    @Override
    public int getLayoutId() {
        return R.layout.activity_apendix;
    }
    @Override
    public void initView() {
        lists.add("a");
        lists.add("b");
        lists.add("c");
        lists.add("a");
        apendix_lv.setAdapter(new ApendixAdapter(getApplicationContext(),lists,R.layout.view_appendix));
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }

    public class ApendixAdapter  extends CommonAdapter{
        public ApendixAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
}
