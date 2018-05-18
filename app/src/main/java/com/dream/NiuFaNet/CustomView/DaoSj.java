package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ColorUtils;

/**
 * Created by Administrator on 2017/9/13 0013.
 * 倒三角
 */
public class DaoSj extends View {
    private Paint paint;
    public DaoSj(Context context) {
        super(context);
        init(context);
    }

    public DaoSj(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // 绘制多边形
        Path path1 = new Path();
        // 设置多边形的点
        path1.moveTo(0, 0);
        path1.lineTo(width, 0);
        path1.lineTo(width/2, height);
        path1.lineTo(0, 0);
        // 使这些点构成封闭的多边形
        path1.close();
        paint.setColor(ColorUtils.getColor(R.color.tip_bg));
        // 绘制这个多边形
        canvas.drawPath(path1, paint);
    }
}
