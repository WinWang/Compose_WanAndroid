package com.winwang.composewanandroid.http

/**
 * Created by Loren on 2021/11/16
 * Description -> 返回结果接口
 */
interface IResultBean<T> {
    fun httpCode(): String
    fun httpMsg(): String
    fun httpData(): T?
    fun requestOk(): Boolean
    fun requestSpecialEmpty(): Boolean = false
}