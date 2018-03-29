package com.dream.NiuFaNet.Module;



import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Api.support.HeaderInterceptor;
import com.dream.NiuFaNet.Api.support.LoggingInterceptor;
import com.dream.NiuFaNet.Utils.LogUtils;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class NFApiModule {

    @Provides
    public OkHttpClient provideOkHttpClient() {

        LoggingInterceptor logging = new LoggingInterceptor(new MyLog());
        logging.setLevel(LoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(logging);
        return builder.build();
    }

    @Provides
    protected NFApi provideITService(OkHttpClient okHttpClient) {
        return NFApi.getInstance(okHttpClient);
    }

    /**
     * 自定义日志输出
     */
    public static class MyLog implements LoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogUtils.i("oklog: " + message);
        }
    }
}