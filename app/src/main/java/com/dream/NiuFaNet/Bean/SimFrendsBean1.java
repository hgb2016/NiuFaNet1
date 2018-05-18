package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
public class SimFrendsBean1 {
    private String letter;
    private List<MyFrendBean.DataBean> mContacts;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<MyFrendBean.DataBean> getContacts() {
        return mContacts;
    }

    public void setContacts(List<MyFrendBean.DataBean> contacts) {
        mContacts = contacts;
    }
}
