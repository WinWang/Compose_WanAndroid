package com.winwang.composewanandroid.widget.viewstate

import android.graphics.drawable.AnimationDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.winwang.composewanandroid.databinding.LayoutLoadingComposeBinding

/**
 * Created by ssk on 2022/1/11.
 * Description-> compose版本加载失败组件
 * @param modifier： 修饰
 * @param scrollable：是否可滚动
 * @param contentAlignment：居中模式
 */
@Composable
fun LoadingComponent(
    modifier: Modifier = Modifier.fillMaxHeight(),
    scrollable: Boolean = false,
    contentAlignment: Alignment = Alignment.Center
) {
    if (scrollable) {
        Modifier.verticalScroll(rememberScrollState())
    }
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        AndroidViewBinding(
            modifier = Modifier.wrapContentHeight(),
            factory = LayoutLoadingComposeBinding::inflate
        ) {
            (loadingImageview.drawable as AnimationDrawable).start()
        }
    }
}
