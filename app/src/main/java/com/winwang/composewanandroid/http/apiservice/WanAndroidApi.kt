package com.winwang.composewanandroid.http.apiservice

import com.winwang.composewanandroid.http.BaseResult
import com.winwang.composewanandroid.http.ListWrapper
import com.winwang.composewanandroid.model.ArticleBean
import com.winwang.composewanandroid.model.BannerBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by WinWang on 2022/5/6
 * Description->
 */
interface WanAndroidApi {

    //首页
    @GET("/article/list/{page}/json")
    suspend fun getHomeList(@Path("page") page: Int): BaseResult<ListWrapper<ArticleBean>>

    //banner
    @GET("/banner/json")
    suspend fun getBanners(): BaseResult<MutableList<BannerBean>>

}