package com.chen.baselibrary.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author chen
 * @date 2018/9/27 下午2:54
 * email xiuyi.chen@erinspur.com
 * desc Http service的工厂类
 */
public class HttpServiceFactory {
    private static String BASE_URL = "";
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .build();

    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }

    /**
     * 构造service
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> service) {
        return new Retrofit.Builder().baseUrl(BASE_URL).client(client).build().create(service);
    }
}

