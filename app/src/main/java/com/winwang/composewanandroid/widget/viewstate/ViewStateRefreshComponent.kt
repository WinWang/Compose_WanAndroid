package com.winwang.composewanandroid.widget.viewstate

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.winwang.composewanandroid.widget.refreshlayout.SwipeRefreshLayout
import com.winwang.composewanandroid.widget.refreshlayout.SwipeRefreshStateType
import com.winwang.composewanandroid.widget.refreshlayout.rememberSwipeRefreshState
import com.winwang.composewanandroid.base.lifecycle.ComposeLifeCycleListener
import com.winwang.composewanandroid.base.viewmodel.ViewState
import com.winwang.composewanandroid.base.viewmodel.ViewStateLiveData
import com.winwang.composewanandroid.databinding.LayoutLoadingBinding
import kotlinx.coroutines.launch


/**
 * Created by ssk on 2022/1/21.
 * Description->页面状态切换组件, 根据viewStateLiveData，自动切换各种状态页面, 支持下拉刷新
 * @param viewStateLiveData：页面状态livedata
 * @param modifier：页面布局修饰
 * @param lifeCycleListener：生命周期监听
 * @param loadDataBlock：数据加载块
 * @param specialRetryBlock：特殊的重试请求代码块,没设置时重试逻辑将直接调用loadDataBlock
 * @param refreshBlock: 刷新代码块
 * @param idleBlock: 刷新空闲回调
 * @param autoRefresh： 标记是否自动刷新，有些页面onResume时需要刷新接口，这时候refresh header不应该显示
 * @param customEmptyComponent：自定义空布局,没设置则使用默认空布局
 * @param customFailComponent：自定义失败布局,没设置则使用默认失败布局
 * @param customErrorComponent：自定义错误布局,没设置则使用默认错误布局
 * @param contentView：正常页面内容
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun <T> ViewStateRefreshComponent(
    viewStateLiveData: ViewStateLiveData<T>?,
    modifier: Modifier = Modifier,
    lifeCycleListener: ComposeLifeCycleListener? = null,
    loadDataBlock: (() -> Unit)? = null,
    refreshBlock: (() -> Unit)? = null,
    idleBlock: (() -> Unit)? = null,
    autoRefresh: Boolean = false,
    specialRetryBlock: (() -> Unit)? = null,
    customEmptyComponent: @Composable (() -> Unit)? = null,
    customFailComponent: @Composable (() -> Unit)? = null,
    customErrorComponent: @Composable (() -> Unit)? = null,
    contentView: @Composable BoxScope.(data: T) -> Unit
) {
    lifeCycleListener?.let { listener ->
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            listener.onEnterCompose(lifecycleOwner)

            val lifecycleEventObserver = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        listener.onCreate(lifecycleOwner)
                    }
                    Lifecycle.Event.ON_START -> {
                        listener.onStart(lifecycleOwner)
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        listener.onResume(lifecycleOwner)
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        listener.onPause(lifecycleOwner)
                    }
                    Lifecycle.Event.ON_STOP -> {
                        listener.onStop(lifecycleOwner)
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        listener.onDestroy(lifecycleOwner)
                    }
                    else -> {}
                }
            }

            lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

            onDispose {
                listener.onExitCompose(lifecycleOwner)
                lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
            }
        }
    }

    val showViewState = remember {
        mutableStateOf(true)
    }
    val successData = remember {
        mutableStateOf<T?>(null)
    }

    var refreshStateType by remember {
        mutableStateOf<SwipeRefreshStateType>(SwipeRefreshStateType.IDLE)
    }

    val refreshState = rememberSwipeRefreshState(refreshStateType)

    val coroutineScope = rememberCoroutineScope()

    val autoRefreshSate = rememberUpdatedState(newValue = autoRefresh)
    if (showViewState.value) {
        HandleViewStateComponent(
            viewStateLiveData,
            modifier,
            successData,
            showViewState,
            loadDataBlock,
            specialRetryBlock,
            customEmptyComponent,
            customFailComponent,
            customErrorComponent,
        )
    } else {
        if (viewStateLiveData != null) {
            val viewState by viewStateLiveData.observeAsState()
            SwipeRefreshLayout(
                state = refreshState,
                modifier = modifier,
                onRefresh = {
                    refreshBlock?.invoke()
                },
                onIdle = {
                    idleBlock?.invoke()
                    refreshStateType = SwipeRefreshStateType.IDLE
                }
            ) {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    if (viewState is ViewState.Success) {
                        successData.value = (viewState as ViewState.Success<T>).data!!

                        if (refreshState.isRefreshing() && !autoRefreshSate.value) {
                            refreshStateType = SwipeRefreshStateType.SUCCESS
                        }
                    }

                    successData.value?.let {
                        contentView(it)
                    }

                    when (viewState) {
                        is ViewState.Loading -> {
                            if(!autoRefreshSate.value) {
                                refreshStateType = SwipeRefreshStateType.REFRESHING
                            }
                        }
                        is ViewState.Empty, is ViewState.Failed, is ViewState.Error -> {
                            if (refreshState.isRefreshing() && !autoRefreshSate.value) {
                                coroutineScope.launch {
                                    refreshStateType = SwipeRefreshStateType.IDLE
                                    refreshState.resetOffset()
                                    showViewState.value = true
                                }
                            }else {
                                showViewState.value = true
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> HandleViewStateComponent(
    viewStateLiveData: ViewStateLiveData<T>?,
    modifier: Modifier = Modifier,
    successData: MutableState<T?>,
    showViewState: MutableState<Boolean>,
    loadDataBlock: (() -> Unit)? = null,
    specialRetryBlock: (() -> Unit)? = null,
    customEmptyComponent: @Composable (() -> Unit)? = null,
    customFailComponent: @Composable (() -> Unit)? = null,
    customErrorComponent: @Composable (() -> Unit)? = null,
) {
    if (viewStateLiveData != null) {
        val viewState by viewStateLiveData.observeAsState()
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (viewState) {
                is ViewState.Loading -> {
                    AndroidViewBinding(factory = LayoutLoadingBinding::inflate) {
                        (loadingImageview.drawable as AnimationDrawable).start()
                    }
                }
                is ViewState.Success -> {
                    successData.value = (viewState as ViewState.Success<T>).data!!
                    showViewState.value = false
                }
                is ViewState.Empty -> {
                    customEmptyComponent?.invoke() ?: LoadFailedComponent(
                        loadDataBlock = loadDataBlock,
                        specialRetryBlock = specialRetryBlock
                    )
                }
                is ViewState.Failed -> {
                    customFailComponent?.invoke() ?: LoadFailedComponent(
                        message = "${(viewState as ViewState.Failed).errorMsg} \n点我重试",
                        loadDataBlock = loadDataBlock,
                        specialRetryBlock = specialRetryBlock
                    )
                }
                is ViewState.Error -> {
                    if (customErrorComponent != null) {
                        customErrorComponent.invoke()
                    } else {
                        val errorMessagePair = getErrorMessagePair((viewState as ViewState.Error).exception)
                        LoadFailedComponent(
                            message = errorMessagePair.first,
                            iconResId = errorMessagePair.second,
                            loadDataBlock = loadDataBlock,
                            specialRetryBlock = specialRetryBlock
                        )
                    }
                }
                else -> {
                    loadDataBlock?.invoke()
                }
            }
        }
    } else {
        loadDataBlock?.invoke()
    }
}

