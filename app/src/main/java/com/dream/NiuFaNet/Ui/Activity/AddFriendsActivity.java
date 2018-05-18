package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/4/3.
 */

public class AddFriendsActivity extends CommonActivity {
    @Bind(R.id.addfriend_lv)
    MyListView addfriends_lv;
    @Bind(R.id.addfriend_gv)
    MyGridView addfriend_gv;
    private ArrayList<Person> lists=new ArrayList();
    private ArrayList<Person> lists1=new ArrayList();
    private FriendsAdapterLv lvAdaper;
    private FriendsAdapterGv gvAdaper;
    @Override
    public int getLayoutId() {
        return R.layout.activity_addfriends;
    }

    @Override
    public void initView() {
        lists.add(new Person("1",false));
        lists.add(new Person("2",false));
        lists.add(new Person("3",false));
        lists.add(new Person("4",false));
        lvAdaper=new FriendsAdapterLv(getApplicationContext(),lists,R.layout.view_addparticipants);
        addfriends_lv.setAdapter(lvAdaper);
        gvAdaper=new FriendsAdapterGv(getApplicationContext(),lists1,R.layout.view_friends);
        addfriend_gv.setAdapter(gvAdaper);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }

    public class FriendsAdapterLv extends CommonAdapter<Person>{

        public FriendsAdapterLv(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final Person person, int position) {
            final ImageView check_iv=helper.getView(R.id.check_iv);
            if (person.isflag){
                check_iv.setImageResource(R.mipmap.check_green);
            }else {
                check_iv.setImageResource(R.mipmap.emptycheck_2);
            }
            check_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (person.isflag){
                        person.setIsflag(false);
                        lists1.remove(person);
                    }else {
                        person.setIsflag(true);
                        lists1.add(person);
                    }
                    lvAdaper.notifyDataSetChanged();
                    gvAdaper.notifyDataSetChanged();
                }
            });
        }
    }
    public class FriendsAdapterGv extends CommonAdapter<Person>{

        public FriendsAdapterGv(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final Person person, int position) {
            ImageView delete_iv=helper.getView(R.id.delete_iv);
            delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<lists.size();i++){
                        if (lists.get(i).equals(person)){
                            lists.get(i).setIsflag(false);
                            lists1.remove(person);
                            lvAdaper.notifyDataSetChanged();
                            gvAdaper.notifyDataSetChanged();
                            break;
                        }
                    }

                }
            });
        }
    }
    public class Person{
        private String username;
        private boolean isflag;

        public Person(String username, boolean isflag) {
            this.username = username;
            this.isflag = isflag;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isIsflag() {
            return isflag;
        }

        public void setIsflag(boolean isflag) {
            this.isflag = isflag;
        }
    }
}
