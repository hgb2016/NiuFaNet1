package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.core.graphics.ColorUtils;
import android.view.View;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.ChatActivity;
import com.dream.NiuFaNet.Ui.Activity.WebActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public class ChatMainRvAdapter extends RVBaseAdapter<RecomendBean.BodyBean> {
    private Context context;
    private Typeface tf;
    public ChatMainRvAdapter(Context context, List<RecomendBean.BodyBean> data, int layoutId) {
        super(context, data, layoutId);
        this.context = context;
        tf=Typeface.createFromAsset(context.getAssets(), "fonts/fzltxh.ttf");
    }

    @Override
    public void onBind(RVBaseHolder holder, final RecomendBean.BodyBean bodyBean, int position) {
        //根据路径得到Typeface
        TextView content_tv = holder.getView(R.id.only_tv);
        content_tv.setTypeface(tf);
//        content_tv.setTextColor(context.getResources().getColor(R.color.white_alpha80));
        content_tv.setText("“"+bodyBean.getQuestion()+"”");
        holder.itemView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                String url = bodyBean.getUrl();
                Intent intent = new Intent(context, ChatActivity.class);
                if (url !=null&& !url.isEmpty()){
                    if (url.equals(Const.questionUrl)){
                        intent.putExtra("question",Const.questionUrl);
                        context.startActivity(intent);
                    }else {
                        //直接跳转到相关链接
                        Intent webIntent = new Intent(context, WebActivity.class);
                        webIntent.putExtra(Const.webUrl,url);
                        context.startActivity(webIntent);
                    }
                }else {

                    if (url.equals(Const.questionUrl)){
                        intent.putExtra("question",Const.questionUrl);
                    }else {
                        intent.putExtra("question",bodyBean.getQuestion());
                    }
                    context.startActivity(intent);
                }
            }
        });
    }
}
