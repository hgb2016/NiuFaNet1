package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;  
import android.text.method.LinkMovementMethod;  
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dream.NiuFaNet.R;

import java.util.ArrayList;
import java.util.List;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
  
/** 
 * 将字符串中的电话号码找出并设置点击事件的工具类 
 * Created by wlk on 2016/9/8. 
 */  
public class SpannableStringUtil {  
  
    public static void setTelUrl(final Activity context, final String strTel, TextView tv){
//        SpannableString ss = new SpannableString(strTel);
        String phonesHtml = StringUtil.checkNum(strTel);
        Log.e("tag","phonesHtml="+phonesHtml);
       /* for (int i = 0; i <phones.size() ; i++) {
            final String phoneNum = phones.get(i);
            Log.e("tag","phoneNum="+phoneNum);

            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    IntentUtils.call(phoneNum,context);
                }
            },strTel.indexOf(phoneNum.charAt(0)), strTel.indexOf(phoneNum.charAt(phoneNum.length()-1))+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/

        tv.setText(Html.fromHtml(phonesHtml));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

}