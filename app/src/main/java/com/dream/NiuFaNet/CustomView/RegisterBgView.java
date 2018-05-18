package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BitmapUtils;


/**
 * Created by Administrator on 2017/8/4 0004.
 */
public class RegisterBgView extends View {

    private Paint paint;
    private Context context;
    public RegisterBgView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public RegisterBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int layer = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        Drawable mDrawble = context.getResources().getDrawable(R.drawable.shape_ubg);
        Bitmap bitmap1 = BitmapUtils.drawableToBitmap(mDrawble);
        //画图1
        canvas.drawBitmap(bitmap1,canvasHeight,canvasHeight,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        Drawable mDrawble1 = context.getResources().getDrawable(R.drawable.them_bg4);
        Bitmap bitmap2 = BitmapUtils.drawableToBitmap(mDrawble1);
        //画图2
        canvas.drawBitmap(bitmap2,canvasHeight,canvasHeight,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layer);

    }
}
