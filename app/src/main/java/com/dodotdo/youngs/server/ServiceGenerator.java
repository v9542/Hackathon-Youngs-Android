package com.dodotdo.youngs.server;

import android.util.Base64;
import android.util.Log;


import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class ServiceGenerator extends NetDefine {
    private static String token = "5";
    private static HttpLoggingInterceptor logging;

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(getBasicPath())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("mm/dd/yyyy HH:mm").create()));

    private static boolean isFile = false;

    public static <S> S createService(Class<S> serviceClass) {
        isFile = false;
        if (logging == null) {
            logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        httpClient.interceptors().clear();
        //header log
        httpClient.interceptors().add(logging);
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body());
                if (isFile) {
                    requestBuilder.header("Content-Type", "multipart/form-data");
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return createService(serviceClass, token);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.interceptors().clear();
            httpClient.interceptors().add(logging);
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "JWT " + authToken)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                    if (isFile) {
                        requestBuilder.header("Content-Type", "multipart/form-data");
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

}