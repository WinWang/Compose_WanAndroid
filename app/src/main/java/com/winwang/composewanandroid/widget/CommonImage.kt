package com.winwang.composewanandroid.widget

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.winwang.composewanandroid.R

/**
 * Created by WinWang on 2021/12/4.
 * Description-> 通用网络图片加载组件
 * [url]：图片加载路径
 * [placeholder]：占位图
 * [error]：加载失败图
 * [allowHardware]：是否支持硬件加速，默认不开启（作为某些关闭硬件加速的组件内，如ShadowLayout中，需要把allowHardware设置为false，否则会崩溃）
 * [modifier]：修饰
 */
@Composable
fun CommonNetworkImage(
    url: Any?,
    placeholder: Int = R.drawable.placeholder,
    error: Int = R.drawable.placeholder,
    allowHardware: Boolean = false,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null
) {

    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url ?: "")
                .placeholder(placeholder)
                .error(error)
                .allowHardware(allowHardware)
                .build()
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        colorFilter = colorFilter
    )
}


/**
 * Description-> 通用本地图片加载组件
 * [resId]：图片资源路径
 * [modifier]：修饰
 */
@Composable
fun CommonLocalImage(
    resId: Int,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null
) {

//    Image(
//        painter = painterResource(id = resId),
//        contentDescription = null,
//        modifier = modifier,
//        contentScale = ContentScale.Crop,
//        colorFilter = colorFilter
//    )
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(resId)
                .allowHardware(false)
                .crossfade(false)
                .build()
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        colorFilter = colorFilter
    )
}

@Composable
fun CoilImage(
    modifier: Modifier,
    url: Any?,
    needPlaceholder: Boolean = true,
    contentScale: ContentScale = ContentScale.FillBounds,
    colorFilter: ColorFilter? = null
) {
    val builder = ImageRequest.Builder(LocalContext.current)
    if (needPlaceholder) {
        builder.placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
    }
    AsyncImage(
        modifier = modifier,
        contentDescription = null,
        imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build(),
        model = builder
            .data(url)
            .crossfade(true)
            .build(),
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}