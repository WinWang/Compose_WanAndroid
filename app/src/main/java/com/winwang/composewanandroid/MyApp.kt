package com.winwang.composewanandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by WinWang on 2022/5/5
 * Description->
 */
@HiltAndroidApp
class MyApp : Application() {

    companion object {
        @JvmStatic
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}