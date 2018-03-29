package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 自定义上下不滚动的GridView 
 */  
public class CustomeGridView extends GridView {
  
    public CustomeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
    /** 
     * 设置上下不滚动 
     */  
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){  
            return true;//true:禁止滚动  
        }  
  
        return super.dispatchTouchEvent(ev);  
    }  
}