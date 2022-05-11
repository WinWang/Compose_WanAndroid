package com.winwang.composewanandroid.http

/**
 * Created by WinWang on 2022/5/6
 * Description->
 */
data class BaseResult<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
) : IResultBean<T> {
    override fun httpCode(): String {
        return errorCode.toString()
    }

    override fun httpMsg(): String {
        return errorMsg
    }

    override fun httpData(): T? {
        return data
    }

    override fun requestOk(): Boolean {
        return errorCode == 0
    }

}

data class ListWrapper<T>(
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: ArrayList<T>
)
