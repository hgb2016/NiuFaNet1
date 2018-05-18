package com.dream.NiuFaNet.Utils;

import android.widget.EditText;

/**
 * Created by Administrator on 2017/6/24 0024.
 */
public class EdittextUtils {
    public static void clearInfo(EditText editText){
        String peopleName = editText.getText().toString();
        if (!peopleName.isEmpty()) {
            editText.setText("");
            editText.setFocusable(true);
        }
    }
    public static String addNum(EditText editText,int baseNum){
        String honbao_num = editText.getText().toString();
        if (!honbao_num.isEmpty()){
            int num = Integer.parseInt(honbao_num);
            return String.valueOf(num + baseNum);
        }else {
            return null;
        }
    }
    public static String subNum(EditText editText,int baseNum){
        String honbao_num = editText.getText().toString();
        if (!honbao_num.isEmpty()){
            int num = Integer.parseInt(honbao_num);
            return String.valueOf(num - baseNum);
        }else {
            return null;
        }
    }
}
