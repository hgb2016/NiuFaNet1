package com.dream.NiuFaNet.Utils;

import android.os.AsyncTask;

import java.util.HashMap;

public abstract class RequestAsynTask extends AsyncTask<String, Integer, String> {

    private HashMap<String, String> map;
    private String method;
    private String baseUrl;

    public RequestAsynTask(HashMap<String, String> map, String baseUrl, String method) {
        this.map = map;
        this.baseUrl = baseUrl;
        this.method = method;
    }

    @Override
    protected String doInBackground(String... strings) {
        return HttpUtils.requestGet(map, baseUrl, method);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResult(s);
    }

    protected abstract void onResult(String result);
}