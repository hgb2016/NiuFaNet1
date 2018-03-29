package com.dream.NiuFaNet.Bean;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Administrator on 2017/11/20 0020.
 */
public class UpFileBean {
    private Bitmap bitmap;
    private File mFile;

    public File getmFile() {
        return mFile;
    }

    public void setmFile(File mFile) {
        this.mFile = mFile;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
