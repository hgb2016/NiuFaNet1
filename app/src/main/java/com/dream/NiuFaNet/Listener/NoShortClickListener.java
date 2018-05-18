package com.dream.NiuFaNet.Listener;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

//避免短时间内重复点击造成的界面混乱
public abstract class NoShortClickListener implements View.OnTouchListener {

    private boolean isDown,isAnim;
    private int longTime = 1;
    private float tempY = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                longTime = 1;
                tempY = motionEvent.getY();
                mHandler.postDelayed(runnable,500);
                break;
            case MotionEvent.ACTION_UP:
                if (isDown&&isAnim){
                    onACTION_UP();
                }
                isDown = false;
                isAnim = false;
                break;
            case MotionEvent.ACTION_MOVE:
                onACTION_MOVE(motionEvent);
                break;
        }
        return true;
    }

    public abstract void onACTION_DOWN(float startY);
    public abstract void onShortClick();
    public abstract void onACTION_UP();
    public abstract void onACTION_MOVE(MotionEvent motionEvent);

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            longTime--;
            if (longTime==0&&isDown){
                onACTION_DOWN(tempY);
                isAnim = true;
            }else {
                onShortClick();
            }

        }
    };

}