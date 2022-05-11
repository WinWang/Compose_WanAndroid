package com.winwang.composewanandroid.widget.viewstate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.gson.JsonParseException
import com.winwang.composewanandroid.R
import com.winwang.composewanandroid.databinding.LayoutEmptyComposeBinding
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by ssk on 2022/1/11.
 * Description-> compose版本加载失败组件
 * @param modifier： 修饰
 * @param message：提示文本消息
 * @param iconResId：提示图标资源id
 * @param contentAlignment： 居中方式
 * @param loadDataBlock：加载数据逻辑代码块
 * @param specialRetryBlock： 特殊重试加载代码块
 */
@Composable
fun LoadFailedComponent(
    modifier: Modifier = Modifier.fillMaxHeight(),
    message: String? = null,
    iconResId: Int = -1,
    contentAlignment: Alignment = Alignment.Center,
    loadDataBlock: (() -> Unit)? = null,
    specialRetryBlock: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        AndroidViewBinding(
            modifier = Modifier.wrapContentHeight(),
            factory = LayoutEmptyComposeBinding::inflate
        ) {
            if (iconResId != -1) {
                leEmptyImage.setImageResource(iconResId)
            }
            message?.let {
                tvEmpty.text = it
            }
            llEmptyRoot.setOnClickListener {
                specialRetryBlock?.invoke() ?: loadDataBlock?.invoke()
            }
        }
    }

}


fun getErrorMessagePair(exception: Throwable): Pair<String, Int> {
    return when (exception) {
        is ConnectException,
        is UnknownHostException -> {
            Pair("网络连接失败", R.drawable.timeout)
        }
        is SocketTimeoutException -> {
            Pair("网络连接超时", R.drawable.timeout)
        }
        is JsonParseException -> {
            Pair("数据解析错误", R.drawable.timeout)
        }
        else -> {
            Pair("未知错误", R.drawable.timeout)
        }
    }
}