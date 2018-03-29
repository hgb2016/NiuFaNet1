package com.dream.NiuFaNet.Utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * 用于检测权限管理状态的类
 *
 * @author Administrator
 */
public class CheckAudioPermission {
    public static final int STATE_RECORDING = -1;
    public static final int STATE_NO_PERMISSION = -2;
    public static final int STATE_SUCCESS = 1;

    /**
     * 用于检测录音权限是禁用还是允许状态
     *
     * @return 返回1表示权限是允许状态，返回-2表示权限是禁用状态
     * @author ZhuJian
     */
    public static int getRecordState() {
        int minBuffer = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, (minBuffer * 100));
        short[] point = new short[minBuffer];
        int readSize = 0;
        try {
            audioRecord.startRecording();//检测是否可以进入初始化状态
        } catch (Exception e) {
            if (audioRecord != null) {
                audioRecord.release();
                audioRecord = null;
                Log.i("TAGS", "无法进入录音初始状态");
            }
            return STATE_NO_PERMISSION;
        }
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
//6.0以下机型都会返回状态，故使用时需要判断bulid版本
//检测是否在录音中
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                Log.i("TAGS", "录音机被占用");
            }
            return STATE_SUCCESS;
        } else {//检测是否可以获取录音结果
            readSize = audioRecord.read(point, 0, point.length);
            if (readSize <= 0) {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }
                Log.i("TAGS", "录音的结果为空");
                return STATE_NO_PERMISSION;
            } else {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }
                return STATE_SUCCESS;
            }
        }
    }
}