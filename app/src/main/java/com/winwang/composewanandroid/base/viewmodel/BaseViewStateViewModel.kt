package com.winwang.composewanandroid.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.winwang.composewanandroid.http.IResultBean
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by WinWang on 2021/11/21
 * Description -> ViewState的ViewModel
 */
typealias ViewStateMutableLiveData<T> = MutableLiveData<ViewState<T>>
typealias ViewStateLiveData<T> = LiveData<ViewState<T>>

open class BaseViewStateViewModel : BaseViewModel() {

    var autoPages = 1
        private set

    /**
     * livedata通用处理
     */
    protected fun <T> launch(
        liveData: ViewStateMutableLiveData<T>,
        isLoadMore: Boolean = false,
        partRequest: Boolean = false,
        handleResult: ((T) -> Unit)? = null,
        judgeEmpty: ((T) -> Boolean)? = null,
        call: suspend () -> IResultBean<T>
    ) {
        viewModelScope.launch {
            val needShow = !isLoadMore && !partRequest
            runCatching {
                if (needShow) {
                    autoPages = 1
                    liveData.value = ViewState.Loading(true)
                }
                call()
            }.onSuccess { result ->
                if (result.requestOk()) {
                    val httpData = result.httpData()
                    if ((httpData == null || (judgeEmpty?.invoke(httpData)
                            ?: httpData is List<*> && (httpData as List<*>).isEmpty()) || result.requestSpecialEmpty())
                    ) {
                        liveData.value = ViewState.Empty(needShow)
                    } else {
                        autoPages++
                        handleResult?.invoke(httpData)
                        if (needShow) {
                            liveData.value = ViewState.Loading(false)
                        }
                        liveData.value = ViewState.Success(httpData, isLoadMore)
                    }
                } else {
                    liveData.value = ViewState.Failed(needShow, result.httpCode(), result.httpMsg())
                }
            }.onFailure { e ->
                liveData.value = ViewState.Error(needShow, e)
            }
        }
    }

    protected fun <T, V> launchConvert(
        liveData: ViewStateMutableLiveData<T>? = null,
        isLoadMore: Boolean = false,
        partRequest: Boolean = false,
        judgeEmpty: ((V) -> Boolean)? = null,
        convert: ((V) -> T),
        call: suspend () -> IResultBean<V>
    ): Job {
        return viewModelScope.launch {
            val needShow = !isLoadMore && !partRequest
            runCatching {
                if (needShow) {
                    autoPages = 1
                    liveData?.value = ViewState.Loading(true)
                }
                call()
            }.onSuccess { result ->
                if (result.requestOk()) {
                    val httpData = result.httpData()
                    if ((httpData == null || (judgeEmpty?.invoke(httpData)
                            ?: httpData is List<*> && (httpData as List<*>).isEmpty()) || result.requestSpecialEmpty())
                    ) {
                        liveData?.value = ViewState.Empty(needShow)
                    } else {
                        autoPages++
                        val v = convert.invoke(httpData)
                        if (needShow) {
                            liveData?.value = ViewState.Loading(false)
                        }
                        liveData?.value = ViewState.Success(v, isLoadMore)
                    }
                } else {
                    liveData?.value = ViewState.Failed(needShow, result.httpCode(), result.httpMsg())
                }
            }.onFailure { e ->
                liveData?.value = ViewState.Error(needShow, e)
            }
        }
    }

    /**
     * 无需创建livedata的通用处理
     */
    protected fun <T> launchWithEmit(
        isLoadMore: Boolean = false,
        partRequest: Boolean = false,
        handleResult: ((T) -> Unit)? = null,
        call: suspend () -> IResultBean<T>
    ): ViewStateLiveData<T> = liveData {
        val needShow = !isLoadMore && !partRequest
        runCatching {
            if (needShow) {
                autoPages = 1
                emit(ViewState.Loading(true))
            }
            call()
        }.onSuccess { result ->
            if (result.requestOk()) {
                val httpData = result.httpData()
                if (httpData == null || (httpData is List<*> && (httpData as List<*>).isEmpty()) || result.requestSpecialEmpty()) {
                    emit(ViewState.Empty(needShow))
                } else {
                    autoPages++
                    handleResult?.invoke(httpData)
                    if (needShow) {
                        emit(ViewState.Loading(false))
                    }
                    emit(ViewState.Success(httpData, isLoadMore))
                }
            } else {
                emit(ViewState.Failed(needShow, result.httpCode(), result.httpMsg()))
            }
        }.onFailure { e ->
            //e.printStackTrace()
            emit(ViewState.Error(needShow, e))
        }
    }

    protected fun <T, V> launchConvertEmit(
        isLoadMore: Boolean = false,
        partRequest: Boolean = false,
        convert: ((V) -> T),
        call: suspend () -> IResultBean<V>
    ): ViewStateLiveData<T> = liveData {
        val needShow = !isLoadMore && !partRequest
        runCatching {
            if (needShow) {
                autoPages = 1
                emit(ViewState.Loading(true))
            }
            call()
        }.onSuccess { result ->
            if (result.requestOk()) {
                val httpData = result.httpData()
                if (httpData == null || (httpData is List<*> && (httpData as List<*>).isEmpty()) || result.requestSpecialEmpty()) {
                    emit(ViewState.Empty(needShow))
                } else {
                    autoPages++
                    val v = convert.invoke(httpData)
                    emit(ViewState.Success(v, isLoadMore))
                    if (needShow) {
                        emit(ViewState.Loading(false))
                    }
                }
            } else {
                emit(ViewState.Failed(needShow, result.httpCode(), result.httpMsg()))
            }
        }.onFailure { e ->
            emit(ViewState.Error(needShow, e))
        }
    }

}

sealed class ViewState<out T> {
    data class Loading(val isLoading: Boolean) : ViewState<Nothing>()
    data class Success<T>(val data: T, val isLoadMore: Boolean) : ViewState<T>()
    data class Empty(val needShow: Boolean) : ViewState<Nothing>()
    data class Failed(val needShow: Boolean, val errorCode: String?, val errorMsg: String?) : ViewState<Nothing>() //请求200但是code不ok
    data class Error(val needShow: Boolean, val exception: Throwable) : ViewState<Nothing>()
}

