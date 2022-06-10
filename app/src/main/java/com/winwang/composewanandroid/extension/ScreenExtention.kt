package com.winwang.composewanandroid.extension

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by WinWang on 2022/5/7
 * Description->屏幕适配相关--基于屏幕750的基础
 */

val Number.cdp
    get() = Dp(toFloat() / 750 * Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density)


val Number.csp
    get() = (toFloat() / 750 * Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density).sp

