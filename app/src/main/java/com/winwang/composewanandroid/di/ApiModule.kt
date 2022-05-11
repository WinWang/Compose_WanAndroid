package com.winwang.composewanandroid.di

import com.winwang.composewanandroid.constant.HttpUrl
import com.winwang.composewanandroid.http.RetrofitManager
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by WinWang on 2022/5/6
 * Description->
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideWanAndroidApiService(retrofit: RetrofitManager): WanAndroidApi {
        return retrofit.setBaseUrl(HttpUrl.BaseUrl).create(WanAndroidApi::class.java)
    }

}