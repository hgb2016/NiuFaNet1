package com.dream.NiuFaNet.Base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.dream.NiuFaNet.Other.MyApplication;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public abstract class BaseFragment extends Fragment {
    protected View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this,root);
        initView();
        initEvents();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDta();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initEvents();

    public abstract void initDta();

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 封装一个Intent方法
     * @param cls
     * @param bundle
     */
    protected void OpenActivity(Class<?>cls,Bundle bundle ){
        Intent intent=new Intent(MyApplication.getInstance(),cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        MyApplication.getInstance().startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.enter_in,R.anim.exit_out);
    }
}
