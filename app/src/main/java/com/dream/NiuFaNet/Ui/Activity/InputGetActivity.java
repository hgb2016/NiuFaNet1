package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.NewCalenderBean;
import com.dream.NiuFaNet.Bean.RemindWordBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.RemindWordContract;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Listener.NoShortClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.RemindWordPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.JsonParser;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24 0024.
 */
public class InputGetActivity extends BaseActivity implements RemindWordContract.View {

    @Bind(R.id.calvoice_iv)
    ImageView calvoice_iv;
    @Bind(R.id.calvoice_rv)
    RecyclerView calvoice_rv;

    @Bind(R.id.audio_lay)
    LinearLayout audio_lay;
    @Bind(R.id.audioview_left)
    AudioAnimView audioview_left;
    @Bind(R.id.audioview_right)
    AudioAnimView audioview_right;
    @Bind(R.id.audiores_tv)
    TextView audiores_tv;

    private SpeechRecognizer mIat;// 语音听写
    private String audioResultStr;
    private boolean isUp, isInit, isOver,isMove;
    private String TAG = "tag";
    private Dialog permissionDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    @Inject
    RemindWordPresenter remindWordPresenter;

    private RemindWordAdapter wordAdapter;
    private List<String> dataList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 210:
                    if (isUp && !audioResultStr.isEmpty()&&!isMove) {

                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        isOver = true;
                        String intentTag = getIntent().getStringExtra(Const.intentTag);
                        if (intentTag.equals("main")){
                            Intent intent = new Intent(mContext, NewCalenderActivity.class);
                            intent.putExtra("text",audioResultStr);
                            startActivity(intent);
                        }else {
                            Intent intent = getIntent();
                            intent.putExtra("text",audioResultStr);
                            setResult(333,intent);
                        }
                        finish();
                        audioResultStr = "";

                    }
                    break;
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_voicecalendar;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        remindWordPresenter.attachView(this);

        RvUtils.setOptionnoLine(calvoice_rv, this);
        wordAdapter = new RemindWordAdapter(this, dataList, R.layout.rvitem_onlytext_remindword);
        calvoice_rv.setAdapter(wordAdapter);

        initmTat();
        permissionDialog = DialogUtils.showPermissionTip(mActivity);
    }


    @Override
    public void loadResum() {
        String them = CommonAction.getThem();
        Log.e("tag","them="+them);
        if (them.equals(Const.Candy)){
            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
        }else {
            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
        }
    }

    @Override
    public void initDatas() {
        remindWordPresenter.getRemindWord("");
    }

    @Override
    public void eventListener() {
        calvoice_iv.setOnTouchListener(new NoShortClickListener() {
            float temY = 0;
            @Override
            public void onACTION_DOWN(float startY) {
                //启动录音功能，6.0需要重新申请权限
                isUp = false;
                isInit = false;
                isOver = false;
                isMove = false;
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)//权限未授予
                {

                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, 250
                    );
                } else//已授予权限
                {
                    mIatResults.clear();
                    audiores_tv.setText("");
                    starWrite();
                }
            }

            @Override
            public void onShortClick() {

            }

            @Override
            public void onACTION_UP() {
                audio_lay.setVisibility(View.GONE);
                audiores_tv.setText("");
                isUp = true;
                if (audioResultStr != null && isInit) {
                    Message message = Message.obtain();
                    message.what = 210;
                    mHandler.sendMessage(message);
                }

            }

            @Override
            public void onACTION_MOVE(MotionEvent motionEvent) {
                float move =motionEvent .getY() - temY;
                Log.e("tag","move="+move);
                if (Math.abs(move)>=100){
                    audio_lay.setVisibility(View.GONE);
                    audiores_tv.setText("");
                    isMove = true;
                }
            }
        });
    }

    @OnClick({R.id.close_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
                break;
        }
    }

    @Override
    public void showData(RemindWordBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<String> data = dataBean.getData();
            if (data != null) {
                dataList.clear();
                dataList.addAll(data);
                wordAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    private class RemindWordAdapter extends RVBaseAdapter<String> {

        public RemindWordAdapter(Context context, List<String> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final String remindWordBean, int position) {
            holder.setText(R.id.only_tv, remindWordBean);
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    Intent intent;
                    String intentTag = getIntent().getStringExtra(Const.intentTag);
                    if (intentTag!=null&&intentTag.equals("main")){
                        intent = new Intent(mContext, NewCalenderActivity.class);
                        intent.putExtra("text",remindWordBean);
                        startActivity(intent);
                    }else {
                        intent = getIntent();
                        intent.putExtra("text",remindWordBean);
                        setResult(333,intent);
                    }
                    finish();
                }
            });
        }
    }

    /**
     * 初始化语音合成监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.e(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                // showTip("初始化失败,错误码：" + code);
                ToastUtils.Toast_short(mActivity, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private void initmTat() {
        // 语音听写1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
        // 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        // 语音识别应用领域（：iat，search，video，poi，music）
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 接收语言中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 接受的语言是普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        // 设置听写引擎（云端）
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    }
    /**
     * 初始化参数开始听写
     *
     * @Description:
     */
    private void starWrite() {

//        mIat.setParameter(SpeechConstant.VAD_EOS,"10000");
        /*iatDialog.setListener(mRecognizerDialogListener);
        iatDialog.show();*/
//        audioDialog.show();
//        DialogUtils.setBespreadWindow(audioDialog, mActivity);

//        ToastUtils.Toast_short(mActivity, "请说话…");
//         3.开始听写
        audio_lay.setVisibility(View.VISIBLE);

        mIat.startListening(mRecoListener);
//         听写监听器

    }

    /**
     * 语音听写监听
     */
    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        // 关于解析Json的代码可参见MscDemo中JsonParser类；
        // isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!isLast && !isOver) {
                printResult(results);
            }
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            int errorCode = error.getErrorCode();
            if (errorCode == 10118) {
//                ToastUtils.Toast_short(mActivity, "你好像没有说话哦");
            } else if (errorCode == 20006) {//录音权限被拒绝
                audio_lay.setVisibility(View.GONE);
                Log.e("tag", "ErrorCode=" + errorCode);
                permissionDialog.show();
            } else {
                ToastUtils.Toast_short(mActivity, "操作失败，错误码：" + errorCode);
            }
//            ToastUtils.Toast_short(mActivity, error.getPlainDescription(true));

        }// 获取错误码描述}

        // 开始录音
        public void onBeginOfSpeech() {
            Log.e(TAG, "开始说话");
//            ToastUtils.Toast_short(mActivity, "开始说话");
        }

        // 结束录音
        public void onEndOfSpeech() {
            Log.e(TAG, "说话结束");
//            ToastUtils.Toast_short(mActivity, "说话结束");

        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }

        //音量
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if (volume > 0) {
                if (audioview_left != null && audioview_right != null) {
                    audioview_left.refreshView(volume);
                    audioview_right.refreshView(volume);
                }
            }
        }

    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        audioResultStr = resultBuffer.toString();
        if (audiores_tv!=null){
            audiores_tv.setText(audioResultStr);
            Log.e("tag", "resultStr=" + resultBuffer.toString());
            Message message = Message.obtain();
            message.what = 210;
            mHandler.sendMessage(message);
            isInit = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 250) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                starWrite();

            } else {
                // Permission Denied
                ToastUtils.Toast_short("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIat!=null){
            mIat.destroy();
        }
    }
}
