package com.winwang.composewanandroid.ui.page.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.winwang.composewanandroid.R
import com.winwang.composewanandroid.extension.cdp
import com.winwang.composewanandroid.extension.csp
import com.winwang.composewanandroid.model.ArticleBean
import com.winwang.composewanandroid.ui.theme.Color333
import com.winwang.composewanandroid.ui.theme.ColorDFDFDF
import com.winwang.composewanandroid.widget.CommonLocalImage

/**
 * Created by WinWang on 2022/6/13
 * Description->首页Item的Widget
 */

@Composable
fun itemHomeComponent(articleBean: ArticleBean?, onClick: () -> Unit = {}, onItemChilcClick: () -> Unit = {}) {

    Column(modifier = Modifier
        .padding(horizontal = 20.cdp)
        .clickable { onClick.invoke() }) {
        Row {
            articleBean?.shareUser?.let { Text(text = it, fontSize = 24.csp, color = Color333) }
            Spacer(modifier = Modifier.weight(1f))
            articleBean?.niceDate?.let { Text(text = it, fontSize = 24.csp, color = Color333) }
        }
        Spacer(modifier = Modifier.height(15.cdp))
        articleBean?.title?.let {
            Text(text = it, fontSize = 30.csp, color = Color333)
        }
        Spacer(modifier = Modifier.height(15.cdp))
        Row {
            articleBean?.superChapterName?.let {
                Text(text = it, fontSize = 24.csp, color = Color333)
            }
            Spacer(modifier = Modifier.weight(1f))
            CommonLocalImage(
                if (articleBean?.collect != false) R.drawable.icon_collect_red else R.drawable.icon_collet_gray,
                modifier = Modifier
                    .height(50.cdp)
                    .width(50.cdp)
                    .clickable {
                        onItemChilcClick.invoke()
                    }
            )
        }

        Spacer(modifier = Modifier.height(15.cdp))
        Divider(
            modifier = Modifier
                .height(1.cdp)
                .background(ColorDFDFDF)
        )

    }


}