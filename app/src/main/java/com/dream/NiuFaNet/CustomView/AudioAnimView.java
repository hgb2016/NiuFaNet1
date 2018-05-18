package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dream.NiuFaNet.R;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public class AudioAnimView extends View {

    //矩形宽度
    private int rectfWidth = 5;
    //随机高度的最大值
    private int rmaxHeight = 100;
    //音量值
    private int volume = 10;
    //矩形间隔
    private int interval = 11;
    //矩形数量
    private int count = 20;

    private Paint paint;
    public AudioAnimView(Context context) {
        super(context);
        init();
    }

    public AudioAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.red_delete));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int y0 = height /2;
        int rl0 = (width - ((count - 1) * interval + (count * rectfWidth))) / 2;
//        Random random = new Random();
        for (int i = 0; i <count ; i++) {
            int randHeight = (int) (Math.random()*30+10);
//            int randHeight = random.nextInt(rmaxHeight);
            int rl1 = rl0+(i*rectfWidth)+(i*interval);
            int rt1 = y0 - randHeight+volume;
            int rr1 = rl1+rectfWidth;
            int rb1 = y0 + randHeight+volume;
            Rect rect = new Rect(rl1,rt1,rr1,rb1);
            canvas.drawRect(rect,paint);
        }

//        postInvalidateDelayed(100);
    }

    public void refreshView(int volume){
        this.volume = volume;
        postInvalidateDelayed(100);
    }
}
