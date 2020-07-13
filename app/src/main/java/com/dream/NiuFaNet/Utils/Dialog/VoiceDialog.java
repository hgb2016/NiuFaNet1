package com.dream.NiuFaNet.Utils.Dialog;

import android.app.Activity;
import android.app.Dialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.R;

import java.util.List;

/**
 * Created by Administrator on 2018/2/10 0010.
 */
public  class VoiceDialog {
    private  AudioAnimView audioview_left;
    private  AudioAnimView audioview_right;
    private TextView audiores_tv;
    public VoiceDialog(Activity activity, List<VoiceRvBean.BodyBean> voiceContents){
        showVoiceDialog(activity,voiceContents);
    }
    public  void showVoiceDialog(Activity mActivity,List<VoiceRvBean.BodyBean> voiceContents){
        final Dialog voiceDialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        View voiceView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_voice_layout, null);
        audioview_left = (AudioAnimView) voiceView.findViewById(R.id.audioview_left);
        audioview_right = (AudioAnimView) voiceView.findViewById(R.id.audioview_right);
        audiores_tv = (TextView) voiceView.findViewById(R.id.audiores_tv);
        RecyclerView voiceContent_rv = (RecyclerView) voiceView.findViewById(R.id.voiceContent_rv);
        voiceDialog.setContentView(voiceView);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = voiceDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        voiceDialog.setCancelable(false);
    }
}
