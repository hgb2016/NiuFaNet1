package com.dream.NiuFaNet.Utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/20 0020.
 */
public class StringUtil {
    public static boolean notEmpty(String tempStr) {
        if (tempStr.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从字符串中查找数字字符串
     */
    public static String getNumbers(String content) {

        StringBuffer digitList = new StringBuffer();
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
//        Pattern p = Pattern.compile("(\\d+)");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String find = m.group(1).toString();
            digitList.append(find);
        }
        return digitList.toString();
    }

    /**
     * 查询符合的手机号码
     *
     * @param str
     */
    public static List<String> checkCellphone(String str) {
        List<String> phoneList = new ArrayList<>();
        // 将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        //查找字符串中是否有符合的子字符串
        while (matcher.find()) {
            //查找到符合的即输出
            Log.e("tag", "phoneNum=" + matcher.group());
            phoneList.add(matcher.group());
        }
        return phoneList;
    }

    public static String checkNum(String num) {
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[358]\\d{9})|(?:861[358]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(num);
        Set map = new HashSet();
        while (matcher.find()) {
            String group = matcher.group();
            Log.e("tag", "phoneNum =" + group);
            map.add(group);
        }
        Iterator it = map.iterator();
        while (it.hasNext()) {
            String aa = (String) it.next();
            String helf = "<a href=\"tel:" + aa + "\"" + ">"+aa+"</a>";
            num =  num.replaceAll(aa, helf);

        }
        Log.e("tag","num="+num);
        return num;
    }

    /**
     * 返回false表示null或者空
     *
     * @param s
     * @return
     */
    public static boolean NoNullOrEmpty(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        }
        return false;
    }


    public static String checkNull(String tempStr,String defaultStr){
        return tempStr==null?defaultStr:tempStr;
    }
}
