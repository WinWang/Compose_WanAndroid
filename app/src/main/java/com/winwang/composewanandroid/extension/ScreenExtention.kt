package com.winwang.composewanandroid.extension

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by WinWang on 2022/5/7
 * Description->屏幕适配相关
 */
fun Int.sdp(): Dp {
    val screenDp =
        Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
    return (this.toFloat() / 750 * screenDp).dp
}

fun Double.sdp(): Dp {
    val screenDp =
        Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
    return (this / 750 * screenDp).toInt().dp
}

fun Int.ssp(): TextUnit {
    val screenDp =
        Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
    return (this.toFloat() / 750 * screenDp).sp
}

fun Double.ssp(): TextUnit {
    val screenDp =
        Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
    return (this.toFloat() / 750 * screenDp).sp
}
