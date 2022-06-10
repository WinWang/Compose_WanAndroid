package com.winwang.composewanandroid.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.animation.ArgbEvaluatorCompat
import com.winwang.composewanandroid.R
import com.winwang.composewanandroid.extension.cdp
import com.winwang.composewanandroid.extension.csp
import com.winwang.composewanandroid.extension.transformDp
import com.winwang.composewanandroid.utils.ActivityTracker

/**
 * Created by WinWang on 2021/12/4
 *
 * compose版本通用TopAppBar
 * @param title: 标题
 * @param leftIconResId: 左边按钮资源id，默认为返回按钮
 * @param rightIconResId: 右边按钮资源id  和rightText只存在一个，同时存在只显示rightIconResId
 * @param rightText: 右边按钮文本  和rightIconResId只存在一个，同时存在只显示rightIconResId
 * @param customRightLayout: 自定义右边布局  如果设置了customRightLayout，rightIconResId和rightText不生效
 * @param leftClick: 左边按钮点击回调
 * @param rightClick: 右边按钮点击回调
 * @param backgroundColor: 背景颜色，默认白色
 * @param statusBarColor: 状态栏颜色
 * @param contentColor: 文本等内容颜色
 * @param modifier: 装饰
 */
@Composable
fun CommonTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    statusBarColor: TopBarStyle = TopBarStyle.SolidColor(Color.White),
    statusBarDarkIcons: Boolean? = null,
    backgroundColor: TopBarStyle = TopBarStyle.SolidColor(Color.White),
    contentColor: TopBarStyle = TopBarStyle.SolidColor(Color.Black),
    leftIconColor: TopBarStyle = TopBarStyle.SolidColor(Color.Black),
    leftIconResId: Int = R.drawable.back_for_radio,
    rightIconResId: Int = -1,
    rightText: String = "",
    customRightLayout: (@Composable () -> Unit)? = null,
    leftClick: (() -> Unit)? = null,
    rightClick: (() -> Unit)? = null,
    showBottomDivider: Boolean = false,
    showBackButton: Boolean = true
) {

    // 左边按钮宽度
    var leftWidth by remember {
        mutableStateOf(1)
    }

    // 右边按钮宽度
    var rightWidth by remember {
        mutableStateOf(1)
    }
    val systemUiController = rememberSystemUiController()
//    systemUiController.isSystemBarsVisible = false
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = statusBarColor.color(),
            darkIcons = statusBarDarkIcons ?: true
        )
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = false
        )
    }

    Column(modifier = modifier) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.cdp)
                .zIndex(1f),
            contentPadding = PaddingValues(0.cdp, 0.cdp),
            backgroundColor = backgroundColor.color(),
            contentColor = backgroundColor.color(),
            elevation = 0.cdp
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (leftIcon, rightLayout, titleLayout, bottomDivider) = createRefs()

                Box(modifier = Modifier
                    .constrainAs(leftIcon) {
                        start.linkTo(parent.start)
                        end.linkTo(titleLayout.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .onGloballyPositioned {
                        leftWidth = it.size.width
                    }) {

                    if (showBackButton) {
                        CommonLocalImage(
                            leftIconResId,
                            modifier = Modifier
                                .clickable {
                                    leftClick?.invoke()
                                        ?: ActivityTracker.getInstance().trackTop?.finish()
                                }
                                .padding(20.cdp)
                                .width(48.cdp)
                                .height(48.cdp),
                            colorFilter = ColorFilter.tint(leftIconColor.color())
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .constrainAs(rightLayout) {
                            start.linkTo(titleLayout.end)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickable {
                            rightClick?.invoke()
                        }
                        .onGloballyPositioned {
                            rightWidth = it.size.width
                        },
                    contentAlignment = Alignment.CenterEnd,
                ) {

                    if (customRightLayout != null) {
                        customRightLayout.invoke()
                    } else {
                        if (rightIconResId != -1) {
                            Image(
                                painter = painterResource(id = leftIconResId),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(20.cdp)
                                    .width(48.cdp)
                                    .height(48.cdp)
                            )
                        } else if (rightText.isNotBlank()) {
                            Text(
                                text = rightText,
                                fontSize = 30.csp,
                                textAlign = TextAlign.Center,
                                color = contentColor.color(),
                                maxLines = 1,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }


                val titleLeftPadding = if (leftWidth >= rightWidth) {
                    0
                } else {
                    rightWidth - leftWidth
                }

                val titleRightPadding = if (leftWidth < rightWidth) {
                    0
                } else {
                    leftWidth - rightWidth
                }

                Box(modifier = Modifier
                    .padding(start = titleLeftPadding.transformDp, end = titleRightPadding.transformDp)
                    .constrainAs(titleLayout) {
                        start.linkTo(leftIcon.end)
                        end.linkTo(rightLayout.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }) {
                    Text(
                        text = title,
                        fontSize = 36.csp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = contentColor.color(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (showBottomDivider) {
                    Divider(modifier = Modifier.constrainAs(bottomDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }, color = Color(0xFFE5E5E5), thickness = 1.cdp)
                }
            }
        }
    }
}

sealed class TopBarStyle {
    data class SolidColor(val color: Color) : TopBarStyle()
    data class DynamicChangeColor(val fraction: Float, val startColor: Color, val endColor: Color) : TopBarStyle()
}

fun TopBarStyle.color(): Color {
    return when (this) {
        is TopBarStyle.SolidColor -> color
        is TopBarStyle.DynamicChangeColor -> Color(
            ArgbEvaluatorCompat.getInstance().evaluate(if (fraction > 1) 1f else fraction, startColor.toArgb(), endColor.toArgb())
        )
    }
}
