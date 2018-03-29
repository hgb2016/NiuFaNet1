package com.dream.NiuFaNet.CustomView;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BasePagerAdapter;
import com.dream.NiuFaNet.Base.BasePagerAdapterView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
public class MessageRightPopView extends LinearLayout {
    @Bind(R.id.close_iv)
    ImageView close_iv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.deleteall_tv)
    TextView deleteall_tv;
    @Bind(R.id.new_tv)
    TextView new_tv;
    @Bind(R.id.jiu_tv)
    TextView jiu_tv;
    @Bind(R.id.message_vp)
    ViewPager message_vp;
    @Bind(R.id.pop_ttrelay)
    RelativeLayout pop_ttrelay;
    @Bind(R.id.newmessage_lay)
    LinearLayout newmessage_lay;
    @Bind(R.id.jiumessage_lay)
    LinearLayout jiumessage_lay;
    @Bind(R.id.line_jiu)
    View line_jiu;
    @Bind(R.id.line_new)
    View line_new;
    private FragmentManager fm;

    private String[] titleStr = new String[]{"新的提醒","过期提醒"};
    private Activity context;
    public MessageRightPopView(Activity context,FragmentManager fm) {
        super(context);
        this.context = context;
        this.fm = fm;
        init();
        initData();
        initEvent();
    }

    private void initEvent() {
        message_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        refreshnewLay();

                        break;
                    case 1:
                        refreshOldLay();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        newmessage_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                message_vp.setCurrentItem(0);
                refreshnewLay();
            }
        });

        jiumessage_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                message_vp.setCurrentItem(1);
                refreshOldLay();
            }
        });
    }

    private void refreshOldLay() {
        new_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
        jiu_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
        line_new.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
        line_jiu.setBackgroundColor(ResourcesUtils.getColor(R.color.exit_candy));
    }

    private void refreshnewLay() {
        new_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
        jiu_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
        line_new.setBackgroundColor(ResourcesUtils.getColor(R.color.exit_candy));
        line_jiu.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
    }

    public MessageRightPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){

        View view = inflate(context, R.layout.pop_messageright, this);
        ButterKnife.bind(view,this);
       /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = XuniKeyWord.getStatusBarHeight(context);
        params.height = DensityUtil.dip2px(40);
        pop_ttrelay.setLayoutParams(params);*/

    }

    public void initData(){
        List<PopChildView> fragmentList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PopChildView fragment = new PopChildView(context,i);
            fragmentList.add(fragment);
        }
        message_vp.setAdapter(new BasePagerAdapterView(fragmentList));

    }
}
