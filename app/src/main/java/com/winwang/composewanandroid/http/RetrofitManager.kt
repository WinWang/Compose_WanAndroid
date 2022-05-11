package com.winwang.composewanandroid.http

import retrofit2.Retrofit

/**
 * Created by WinWang on 2022/5/6
 * Description->动态设置Retrofit BaseUrl
 */
class RetrofitManager constructor(private val builder: Retrofit.Builder) {

    private val retrofitCache = hashMapOf<String, Retrofit>()

    fun setBaseUrl(baseUrl: String): Retrofit {
        val cacheRetrofit = retrofitCache[baseUrl]
        return if (cacheRetrofit != null) {
            cacheRetrofit
        } else {
            val retrofit = builder
                .baseUrl(baseUrl)
                .build()
            retrofitCache[baseUrl] = retrofit
            retrofit
        }
    }

}