package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.ProjectClientListBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.LocalGroupSearch;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Created by hou on 2018/3/30.
 */

public class AddProjectActivity extends CommonActivity implements ProgramContract.View{
    @Bind(R.id.project_lv)
    MyListView project_lv;
    @Bind(R.id.sure_relay)
    RelativeLayout sure_relay;
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Inject
    ProgramPresenter programPresenter;
    private ProjectAdapter projectAdapter;
    private List<ProgramListBean.DataBean> projectList = new ArrayList<>();
    private List<ProgramListBean.DataBean> searchData = new ArrayList<>();
    private Map<String, String> map = MapUtils.getMap();
    private  int i=-1;
    private InputMethodManager imm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_addproject;
    }
    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        programPresenter.attachView(this);
        imm = ImmUtils.getImm(this);
    }
    //
    @Override
    public void initDatas() {

        projectAdapter = new ProjectAdapter(getApplicationContext(), projectList, R.layout.view_text);
        project_lv.setAdapter(projectAdapter);
        map.put("type","all");
        programPresenter.getProgramList(CommonAction.getUserId(),map);
    }

    @Override
    public void eventListener() {
        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData.clear();
                if (s != null && s.length() > 0) {
                    searchData = LocalGroupSearch.searchProject(s, (ArrayList<ProgramListBean.DataBean>) projectList);
                    if (searchData.size() > 0 && searchData != null) {
                        projectAdapter = new ProjectAdapter(getApplicationContext(), searchData, R.layout.view_text);
                        project_lv.setAdapter(projectAdapter);
                        projectAdapter.notifyDataSetChanged();
                    } else {
                       // ToastUtils.Toast_short("没有搜索到项目");
                    }
                } else {
                    projectAdapter = new ProjectAdapter(getApplicationContext(), projectList, R.layout.view_text);
                    project_lv.setAdapter(projectAdapter);
                    projectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void showError() {

    }
    @OnClick({R.id.sure_relay,R.id.back_relay,R.id.addproject_iv,R.id.search_lay,R.id.clear_iv})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.sure_relay:
                Intent intent = new Intent();
                for (int i=0;i<projectList.size();i++) {
                    if (projectList.get(i).isSelect()) {
                        intent.putExtra("title",projectList.get(i).getName());
                        intent.putExtra("projectId",projectList.get(i).getId());
                    }
                }
                setResult(222,intent);
                finish();
                break;
            case R.id.back_relay:
                finish();
                break;
            case R.id.addproject_iv:
                IntentUtils.toActivityWithTag(NewProgramActivity.class,mActivity,"newCal",55);
                break;
            case R.id.search_lay:

                break;
            case R.id.clear_iv:
                search_edt.setText("");
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        ImmUtils.hideImm(mActivity, imm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==55){
            i=55;
            map.put("type","all");
            programPresenter.getProgramList(CommonAction.getUserId(),map);
        }
    }

    @Override
    public void complete() {

    }

    @Override
    public void showData(ProgramListBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<ProgramListBean.DataBean> data = dataBean.getData();
            if (data != null) {

                if (data.size()>projectList.size()) {
                    projectList.clear();
                    projectList.addAll(data);
                    projectAdapter.notifyDataSetChanged();
                    if (i==55){
                        projectList.get(0).setSelect(true);
                    }else {
                        String tag = getIntent().getStringExtra(Const.intentTag);
                        for (int i = 0; i < projectList.size(); i++) {
                            if (tag.equals(projectList.get(i).getName())) {
                                projectList.get(i).setSelect(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void showProjectClientList(ProjectClientListBean dataBean) {

    }

    public class ProjectAdapter extends CommonAdapter<ProgramListBean.DataBean>{

        public ProjectAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }
        @Override
        public void convert(final BaseViewHolder holder, final ProgramListBean.DataBean dataBean, int position) {
            TextView projectname=holder.getView(R.id.projectname_tv);
            ImageView select_iv=holder.getView(R.id.select_iv);
            projectname.setText(dataBean.getName());
            if (dataBean.isSelect()){
                select_iv.setImageResource(R.mipmap.check_green);
            }else {
                select_iv.setImageResource(R.mipmap.emptycheck_2);
            }
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataBean.isSelect()){
                        dataBean.setSelect(false);
                    }else {
                        int count=0;
                        for (int i=0;i<mDatas.size();i++){
                            if (mDatas.get(i).isSelect()){
                                count++;
                            }
                        }
                        if (count>1){
                            dataBean.setSelect(false);
                        }else {
                            for (int i=0;i<mDatas.size();i++){
                               mDatas.get(i).setSelect(false);
                            }
                            dataBean.setSelect(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

}
