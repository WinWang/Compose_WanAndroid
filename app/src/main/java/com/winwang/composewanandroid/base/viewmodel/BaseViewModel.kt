package com.winwang.composewanandroid.base.viewmodel

import android.app.Application
import android.os.NetworkOnMainThreadException
import androidx.lifecycle.*
import com.google.gson.JsonParseException
import com.winwang.composewanandroid.MyApp
import com.winwang.composewanandroid.R
import com.winwang.composewanandroid.base.lifecycle.MyLifecycleObserver
import com.winwang.composewanandroid.extension.showToast
import com.winwang.composewanandroid.utils.AppLogUtil
import kotlinx.coroutines.*
import withUI
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


typealias Block<T> = suspend () -> T     //可以在别名类指定类型，例如suspend CoroutineScope.() -> Unit -----但是此时的block不需要调用invoke 了，直接block（）
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit
typealias EmitBlock<T> = suspend LiveDataScope<T>.() -> T

/**
 * Created by WinWang on 2020/6/8
 * Description->
 * Subclasses must have a constructor which accepts [Application] as the only parameter , when you create [ViewModel] using reflection
 */
open class BaseViewModel :
    ViewModel(), MyLifecycleObserver {

    val loginStatusInvalid: MutableLiveData<Boolean> = MutableLiveData()

    //封装页面状态的LiveData
    val viewStatus: MutableLiveData<ViewStatusEnum> = MutableLiveData()

    /**
     * 弹窗展示Dialog
     */
    val dialogLiveData = MutableLiveData<Boolean>()

    //当前要请求的页码
    var mRequestPage = 1

    //上一次要请求的页码, 用于请求请求失败后恢复上一次的页码
    private var mPreRequestPage = 1


    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @param cancel 错误时执行
     * @param handleError 是否处理异常
     * @param isLoadMore 是否加载更多，默认为null，null适用于非列表的请求
     *                   如果不为null，将维护mRequestPage，true表示加载下一页，false表示刷新
     * @return Job
     */
    protected fun launch(
        error: Error? = null,
        cancel: Cancel? = null,
        handleError: Boolean = true,
        handleSuccess: Boolean = true,
        dispatcher: CoroutineContext = EmptyCoroutineContext,
        isLoadMore: Boolean? = null,
        notShowOtherErrorToast: Boolean = false,
        block: Block<Unit>
    ): Job {
        isLoadMore?.let {
            mPreRequestPage = mRequestPage
            if (it) ++mRequestPage else mRequestPage = 1
        }

        return viewModelScope.launch(dispatcher) {
            try {
                block.invoke()
                if (handleSuccess) {
                    viewStatus.value = ViewStatusEnum.SUCCESS
                }
            } catch (e: Exception) {
                isLoadMore?.let {
                    // 刷新或者加载更多失败,恢复页码
                    mRequestPage = mPreRequestPage
                }
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        if (handleError) {
                            onError(e, notShowOtherErrorToast)
                        }
                        error?.invoke(e)
                    }
                }
            }
        }
    }


    /**
     * @param dispatcher  设置线程，这里默认主线程是因为默认的方法通过suspend挂起有自身线程封闭机制，所以不需要创建多余的线程，预留字段，是为了给自己Jsoup框架子线程执行-防止崩溃
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        block: Block<T>
    ): Deferred<T> {
        return viewModelScope.async(dispatcher) { block.invoke() }
    }


    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 省去每次创建liveData的烦恼，利用liveData的包装创建，直接传入block发送道对应的页面（此时用livedata的协程作用域，不需要用viewmodeScope，用的是liveDataScope）
     */
    fun <T> emit(
        error: Error? = null,
        cancel: Cancel? = null,
        handleError: Boolean = true,
        handleSuccess: Boolean = true,
        block: EmitBlock<T>
    ): LiveData<T> = liveData {
        try {
            emit(block())
            if (handleSuccess) {
                showSuccess()
            }
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> cancel?.invoke(e)
                else -> {
                    if (handleError) {
                        onError(e)
                    }
                    error?.invoke(e)
                }
            }
        }
    }


    /**
     * 统一处理错误
     * @param e 异常
     */
    private suspend fun onError(e: Exception, notShowOtherErrorToast: Boolean = false) = withUI {
        when (e) {
            is UnknownHostException -> {
                // 连接失败
                MyApp.instance.showToast(R.string.network_connection_failed)
                showNetWorkError()
            }
            is ConnectException -> {
                // 连接失败
                MyApp.instance.showToast(R.string.network_connection_failed)
                showNetWorkError()
            }
            is SocketTimeoutException -> {
                // 请求超时
                MyApp.instance.showToast(R.string.network_request_timeout)
                showNetWorkError()
            }

            is JsonParseException -> {
                // 数据解析错误
                MyApp.instance.showToast(R.string.api_data_parse_error)
                showDataError()
            }
            is NetworkOnMainThreadException -> {
                MyApp.instance.showToast(R.string.network_thread_exception)
                showDataError()
            }
            else -> {
                // 其他错误
                if (!notShowOtherErrorToast) {
                    e.message?.let { MyApp.instance.showToast(it) }
                }
                showDataError()
            }
        }
    }

    fun getErrorMessage(e: Exception): String = when (e) {
        is UnknownHostException -> {
            // 连接失败
            MyApp.instance.getString(R.string.network_connection_failed)
        }
        is ConnectException -> {
            // 连接失败
            MyApp.instance.getString(R.string.network_connection_failed)
        }
        is SocketTimeoutException -> {
            // 请求超时
            MyApp.instance.getString(R.string.network_request_timeout)
        }

        is JsonParseException -> {
            // 数据解析错误
            MyApp.instance.getString(R.string.api_data_parse_error)
        }
        is NetworkOnMainThreadException -> {
            MyApp.instance.getString(R.string.network_thread_exception)
        }
        else -> {
            // 其他错误
            e.message ?: "未知错误"
        }
    }

    fun showNetWorkError() {
        viewStatus.postValue(ViewStatusEnum.NETWORKERROR)
    }

    fun showDataError() {
        viewStatus.postValue(ViewStatusEnum.ERROR)
    }

    fun showEmpty() {
        viewStatus.postValue(ViewStatusEnum.EMPTY)
    }

    fun showSuccess() {
        viewStatus.postValue(ViewStatusEnum.SUCCESS)
    }

    fun showDialogLoading() {
        dialogLiveData.value = true
    }

    fun hideDialogLoading() {
        dialogLiveData.value = false
    }

    override fun onCreate(source: LifecycleOwner) {
        AppLogUtil.d("ViewModel", "onCreate>>>>")
    }

    override fun onPause(source: LifecycleOwner) {
        AppLogUtil.d("ViewModel", "onPause>>>>")
    }

    override fun onResume(source: LifecycleOwner) {
        AppLogUtil.d("ViewModel", "onResume>>>>")
    }

    override fun onDestroy(source: LifecycleOwner) {
        AppLogUtil.d("ViewModel", "onDestroy>>>>")
    }
}

/**
 *Created by WinWang on 2020/6/9
 *Description->提取状态类型
 */
enum class ViewStatusEnum {
    SUCCESS,  //成功
    ERROR,    //解析错误
    NETWORKERROR, //网络错误
    EMPTY,       //空数据
}