package com.dream.NiuFaNet.Utils;

import android.os.CountDownTimer;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public abstract class CodeUtils extends CountDownTimer {
    public CodeUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
        onTicked(l);
    }

    @Override
    public void onFinish() {
        onFinished();
    }
    protected abstract void onTicked(long l);
    protected abstract void onFinished();
}
