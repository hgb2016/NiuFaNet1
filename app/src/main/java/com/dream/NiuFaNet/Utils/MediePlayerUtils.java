package com.dream.NiuFaNet.Utils;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2018/1/19 0019.
 */
public class MediePlayerUtils {
    private MediaPlayer mMediaPlayer;
    private static MediePlayerUtils mMediePlayerUtils;
    public static MediePlayerUtils getInstance(){
        if (mMediePlayerUtils==null){
            mMediePlayerUtils = new MediePlayerUtils();
        }
        return mMediePlayerUtils;
    }
    public MediePlayerUtils(){
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mMediaPlayer;
    }
}
