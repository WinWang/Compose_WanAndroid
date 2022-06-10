package com.winwang.composewanandroid.extension

import android.content.res.Resources
import androidx.compose.ui.unit.Dp

/**
 * Created by WinWang on 2022/6/9
 * Description->
 */

/**
 * 将数字转换成compose中的DP
 */
val Number.transformDp
    get() = Dp(toFloat() / Resources.getSystem().displayMetrics.density)