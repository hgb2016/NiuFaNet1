package com.dream.NiuFaNet.Utils;

/**
 * Created by Administrator on 2017/4/17.
 * takephoto的帮助类用来初始化
 * 作者：sst
 */

import android.net.Uri;
import android.os.Environment;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * 图片拍照的参数
 */
public class CustomHelper {

    private TakePhoto mTakePhoto;
    private boolean mCropYes;
    //选择图片的数量(默认一张)
    private int limit = 1;
    private String mCropWidth, mCropHeight;


    public static CustomHelper INSTANCE(TakePhoto takePhoto, String width, String height, boolean CropYes, String CropWidth, String CropHeight) {
        return new CustomHelper(takePhoto, width, height, CropYes, CropWidth, CropHeight);
    }

    private CustomHelper(TakePhoto takePhoto, String width, String height, boolean CropYes, String CropWidth, String CropHeight) {
        this.mTakePhoto = takePhoto;
        this.mCropYes = CropYes;
        this.mCropWidth = CropWidth;
        this.mCropHeight = CropHeight;
        init(width, height);
    }


    private void init(String width, String height) {
        configCompress(width, height);
        configTakePhotoOption(true);

    }

    public void onClick(boolean Camera) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        //相机
        if (Camera) {
            if (mCropYes) {//裁剪
                mTakePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
            } else {//不裁剪
                mTakePhoto.onPickFromCapture(imageUri);
            }
            //相册
        } else {
            if (limit > 1) {
                if (mCropYes) {
                    mTakePhoto.onPickMultipleWithCrop(limit, getCropOptions());
                } else {
                    mTakePhoto.onPickMultiple(limit);
                }
                return;
            }
            //从那个路径选择相片
//            String rgFrom="file";//从文件
            String rgFrom = "pic";//从相册（默认）
            if (rgFrom.equals("file")) {
                if (mCropYes) {
                    mTakePhoto.onPickFromDocumentsWithCrop(imageUri, getCropOptions());
                } else {
                    mTakePhoto.onPickFromDocuments();
                }
                return;
            } else {
                if (mCropYes) {
                    mTakePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                } else {
                    mTakePhoto.onPickFromGallery();
                }
            }
        }
    }

    /**
     * 使用takephoto自带的相册？
     */
    private void configTakePhotoOption(boolean own) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        if (own) {//使用自带
            builder.setWithOwnGallery(true);
        }
        //是否使纠正照片角度（默认不纠正）
//            builder.setCorrectImage(true);
        mTakePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 设置压缩参数
     */
    private void configCompress(String mWidth, String mHeight) {
        CompressConfig config;
////        不压缩的配置
//        mTakePhoto.onEnableCompress(null,false);
        //压缩的配置
        int maxSize = 1000*1024;//压缩后的最大尺寸，一般设置3M
        int width = Integer.parseInt(mWidth);//宽
        int height = Integer.parseInt(mHeight);//高
        boolean showProgressBar = true;//是否展示压缩进度条
        boolean enableRawFile = true;//压缩图片后是否保存原图

        //使用自带的压缩工具(默认使用)
        config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();

//        //使用luban压缩工具
//        LubanOptions option=new LubanOptions.Builder()
//                .setMaxHeight(height)
//                .setMaxWidth(width)
//                .setMaxSize(maxSize)
//                .create();
//        config=CompressConfig.ofLuban(option);
//        config.enableReserveRaw(enableRawFile);

        //将设置配置进takephoto中
        mTakePhoto.onEnableCompress(config, showProgressBar);


    }

    /**
     * 设置裁剪参数
     *
     * @return
     */
    private CropOptions getCropOptions() {
        if (!mCropYes) return null;
        int height = Integer.parseInt(mCropHeight);
        int width = Integer.parseInt(mCropWidth);
        boolean withWonCrop = false;//是否使用takephoto自带的裁剪工具
        CropOptions.Builder builder = new CropOptions.Builder();
        boolean bili = false;//使用固定比例或者自由线条的（true使用固定比例，false线条）
        if (bili) {
            builder.setAspectX(width).setAspectY(height);
        } else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }
}
