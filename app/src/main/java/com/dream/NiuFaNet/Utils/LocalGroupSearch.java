package com.dream.NiuFaNet.Utils;

import android.text.TextUtils;

import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hou on 2018/4/19.
 */

public class LocalGroupSearch {
    //搜索手机联系人
    /**
     * 按群号-群名拼音搜索
     *
     * @param str
     */
    public static ArrayList<Contact> searchGroup(CharSequence str, ArrayList<Contact> allContacts) {
        ArrayList<Contact> groupList = new ArrayList<Contact>();
        // 如果搜索条件以0 1 +开头则按号码搜索
        if (str.toString().startsWith("0") || str.toString().startsWith("1")
                || str.toString().startsWith("+")) {
            for (Contact group : allContacts) {
                if (getGroupName(group) != null
                        && group.getPhoneNumber() + "" != null) {
                    if ((group.getPhoneNumber() + "").contains(str)) {
                        groupList.add(group);
                    }
                }
            }
            return groupList;
        }
        CharacterParser finder = CharacterParser.getInstance();

        String result = "";
        for (Contact group : allContacts) {
            // 先将输入的字符串转换为拼音
            finder.setResource(str.toString());
            result = finder.getSpelling();
            if (contains(group, result)) {
                groupList.add(group);
            } else if ((group.getPhoneNumber() + "").contains(str)) {
                groupList.add(group);
            }
        }
        return groupList;
    }

    /**
     * 根据拼音搜索
     *
     *
     *            正则表达式
     *
     *            拼音
     * e
     *            搜索条件是否大于6个字符
     * @return
     */
    private static boolean contains(Contact group, String search) {
        if (TextUtils.isEmpty(group.getName())) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
            String firstLetters = PinYinUtil.getInstance().getPinYinHeadChar(getGroupName(group).substring(0, 1));
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();
        }

        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            CharacterParser finder = CharacterParser.getInstance();
            finder.setResource(getGroupName(group));
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
            flag = matcher2.find();
        }

        return flag;
    }

    private static String getGroupName(Contact group) {
        String strName = null;
        if (!TextUtils.isEmpty(group.getName())) {
            strName = group.getName();
        }  else {
            strName = "";
        }

        return strName;
    }
    //搜索好友
    /**
     * 按群号-群名拼音搜索
     *
     * @param str
     */
    public static ArrayList<MyFrendBean.DataBean> searchGroup2(CharSequence str,
                                                              ArrayList<MyFrendBean.DataBean> allContacts) {
        ArrayList<MyFrendBean.DataBean> groupList = new ArrayList<MyFrendBean.DataBean>();
        // 如果搜索条件以0 1 +开头则按号码搜索
        if (str.toString().startsWith("0") || str.toString().startsWith("1")
                || str.toString().startsWith("+")) {
            for (MyFrendBean.DataBean group : allContacts) {
                if (getGroupName(group) != null
                        && group.getFriendId() + "" != null) {
                    if (group.getFriendId().contains(str)){
                        groupList.add(group);
                    }
                }
            }
            return groupList;
        }
        CharacterParser finder = CharacterParser.getInstance();

        String result = "";
        for (MyFrendBean.DataBean group : allContacts) {
            // 先将输入的字符串转换为拼音
            finder.setResource(str.toString());
            result = finder.getSpelling();
            if (contains1(group, result)) {
                groupList.add(group);
            } else if ((group.getFriendName() + "").contains(str)) {
                groupList.add(group);
            }
        }
        return groupList;
    }

    /**
     * 根据拼音搜索
     *
     *
     *            正则表达式
     *
     *            拼音
     * e
     *            搜索条件是否大于6个字符
     * @return
     */
    private static boolean contains1(MyFrendBean.DataBean group, String search) {
        if (TextUtils.isEmpty(group.getFriendName())) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
            String firstLetters = PinYinUtil.getInstance().getPinYinHeadChar(getGroupName(group).substring(0, 1));
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();
        }

        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            CharacterParser finder = CharacterParser.getInstance();
            finder.setResource(getGroupName(group));
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
            flag = matcher2.find();
        }

        return flag;
    }

    private static String getGroupName(MyFrendBean.DataBean group) {
        String strName = null;
        if (!TextUtils.isEmpty(group.getFriendName())) {
            strName = group.getFriendName();
        }  else {
            strName = "";
        }

        return strName;
    }
    /**
     * 搜索项目
     */
    public static ArrayList<ProgramListBean.DataBean> searchProject(CharSequence str, ArrayList<ProgramListBean.DataBean> allContacts) {
        ArrayList<ProgramListBean.DataBean> groupList = new ArrayList<ProgramListBean.DataBean>();
        CharacterParser finder = CharacterParser.getInstance();
        String result = "";
        for (ProgramListBean.DataBean group : allContacts) {
            // 先将输入的字符串转换为拼音
            finder.setResource(str.toString());
            result = finder.getSpelling();
            if (contains2(group, result)) {
                groupList.add(group);
            } else if ((group.getName() + "").contains(str)) {
                groupList.add(group);
            }
        }
        return groupList;
    }
    /**
     * 根据拼音搜索
     *
     *
     *            正则表达式
     *
     *            拼音
     * e
     *            搜索条件是否大于6个字符
     * @return
     */
    private static boolean contains2(ProgramListBean.DataBean group, String search) {
        if (TextUtils.isEmpty(group.getName())) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
            String firstLetters = PinYinUtil.getInstance().getPinYinHeadChar(getGroupName(group).substring(0, 1));
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();
        }

        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            CharacterParser finder = CharacterParser.getInstance();
            finder.setResource(getGroupName(group));
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
            flag = matcher2.find();
        }

        return flag;
    }
    private static String getGroupName(ProgramListBean.DataBean group) {
        String strName = null;
        if (!TextUtils.isEmpty(group.getName())) {
            strName = group.getName();
        }  else {
            strName = "";
        }

        return strName;
    }
}
