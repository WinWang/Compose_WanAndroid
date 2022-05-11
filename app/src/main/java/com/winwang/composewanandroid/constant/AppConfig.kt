package com.winwang.composewanandroid.constant

/**
 * Created by WinWang on 2022/5/6
 * Description->
 */
object AppConfig {

    /**
     * 调试模式
     */
    var DEBUG = true

    /**
     * 能否抓包
     */
    var PROXY = false

    fun isDebug(): Boolean {
        return DEBUG
    }

    fun isProxy(): Boolean {
        return PROXY
    }

}