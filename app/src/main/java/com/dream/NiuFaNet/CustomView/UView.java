package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BitmapUtils;

/**
 * Created by Administrator on 2017/8/4 0004.
 * U型View
 */
public class UView extends View {

    private Paint paint;
    private Context context;

    public UView(Context context) {
        super(context);
        init(context);
    }

    public UView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置背景色
        int width = getWidth();
        int height = getHeight();
        Log.e("tag", "width=" + width);
        Log.e("tag", "height=" + height);

        canvas.save();
        int layer = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        //画图1
        Drawable mDrawble = context.getResources().getDrawable(R.drawable.shape_ubg);
        Bitmap bitmap1 = BitmapUtils.drawableToBitmap(mDrawble);
        paint.setXfermode(null);
        canvas.drawBitmap(bitmap1, 0, 0, paint);

        //画图2
        String them = CommonAction.getThem();
        Drawable mDrawble2 = null;
        if (them.equals(Const.Black)) {
            mDrawble2 = context.getResources().getDrawable(R.drawable.them_bg3);
        } else{
            mDrawble2 = context.getResources().getDrawable(R.drawable.them_bg4);
        }
        Bitmap bitmap2 = BitmapUtils.drawableToBitmap(mDrawble2);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        paint.setXfermode(null);
        canvas.restoreToCount(layer);

    }

    public void refreshView(Context context, String them) {

        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        //画图1
        Drawable mDrawble = context.getResources().getDrawable(R.drawable.shape_ubg);
        Bitmap bitmap1 = BitmapUtils.drawableToBitmap(mDrawble);
        paint.setXfermode(null);
        canvas.drawBitmap(bitmap1, 0, 0, paint);

        //画图2
        Drawable mDrawble2 = null;
        if (them.equals(Const.Candy)) {
            mDrawble2 = context.getResources().getDrawable(R.drawable.them_bg4);
        } else if (them.equals(Const.Black)) {
            mDrawble2 = context.getResources().getDrawable(R.drawable.them_bg3);
        }
        Bitmap bitmap2 = BitmapUtils.drawableToBitmap(mDrawble2);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, 0, 0, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        paint.setXfermode(null);
        canvas.restoreToCount(layer);
    }

}
