package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dream.NiuFaNet.Base.BaseViewLay;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.NewProgramActivity;
import com.dream.NiuFaNet.Utils.IntentUtils;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/3/2 0002.
 */
public class Emptyview_Pro extends BaseViewLay {
    @Bind(R.id.midadd_lay)
    LinearLayout midadd_lay;
    public Emptyview_Pro(final Context context) {
        super(context);
        midadd_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                IntentUtils.toActivity(NewProgramActivity.class, context);
            }
        });
    }

    public Emptyview_Pro(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_empty_pro;
    }
}
