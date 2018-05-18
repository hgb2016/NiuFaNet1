package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public abstract class TakePhotoUtils {

    public TakePhotoUtils(Activity activity){
        initPictureMethodDialog(activity);
    }
    public  Dialog initPictureMethodDialog(final Activity activity) {
        final Dialog headDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_changeheadicon, null);
        TextView bitmap_tv = (TextView) headView.findViewById(R.id.bitmap_tv);
        TextView takephoto_tv = (TextView) headView.findViewById(R.id.takephoto_tv);
        RelativeLayout cancel_relay = (RelativeLayout) headView.findViewById(R.id.cancel_relay);
        headDialog.setContentView(headView);
        //获取当前Activity所在的窗体
        Window dialogWindow1 = headDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow1.setGravity(Gravity.BOTTOM);
        headDialog.setCancelable(true);
        headDialog.show();
        DialogUtils.setBespreadWindow(headDialog,activity);

        //拍照上传
        takephoto_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                //相机
                headDialog.dismiss();
                onCamara();
//                IntentUtils.toCamare(activity);
            }
        });

        //从相册上传图片
        bitmap_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                headDialog.dismiss();
//                IntentUtils.toPicture(activity);
                onPhoto();
            }
        });

        cancel_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (headDialog.isShowing()) {
                    headDialog.dismiss();
                }
            }
        });
        return headDialog;
    }

    protected abstract void onPhoto();
    protected abstract void onCamara();
}
