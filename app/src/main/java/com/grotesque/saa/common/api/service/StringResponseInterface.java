package com.grotesque.saa.common.api.service;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by 경환 on 2016-05-21.
 */
public interface StringResponseInterface {

    @GET()
    Call<String> getStringHtml(@Url String url);

    @Headers({
            "Connection: keep-alive",
            "Accept-Encoding: identity",
            "Content-Type: text/html; charset=UTF-8",
            "Accept-Language: ko-KR, ko",
    })
    @GET("?_filter=search")
    Call<String> getSearch(@QueryMap HashMap<String, String> query);


    @Headers({
            "Connection: keep-alive",
            "Accept-Encoding: identity",
            "Content-Type: text/html; charset=UTF-8",
            "Accept-Language: ko-KR, ko",
    })
    @GET()
    Call<String> getPointRanking(@Url String url);
}
