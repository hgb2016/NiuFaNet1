package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
public class WorkVisibleFrendsBean {
    private String letter;
    private List<WorkVisibleBean.DataBean> mContacts;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<WorkVisibleBean.DataBean> getContacts() {
        return mContacts;
    }

    public void setContacts(List<WorkVisibleBean.DataBean> contacts) {
        mContacts = contacts;
    }
}
