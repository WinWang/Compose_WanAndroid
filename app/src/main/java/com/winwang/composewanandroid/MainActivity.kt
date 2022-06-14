package com.winwang.composewanandroid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.winwang.composewanandroid.base.activity.BaseActivity
import com.winwang.composewanandroid.base.viewmodel.BaseViewStateViewModel
import com.winwang.composewanandroid.extension.cdp
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import com.winwang.composewanandroid.route.BottomBarView
import com.winwang.composewanandroid.ui.page.category.CategoryPage
import com.winwang.composewanandroid.ui.page.home.HomePage
import com.winwang.composewanandroid.ui.page.mine.MinePage
import com.winwang.composewanandroid.ui.page.project.ProjectPage
import com.winwang.composewanandroid.ui.theme.ComposeWanAndroidTheme
import com.winwang.composewanandroid.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalPagerApi::class)
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWanAndroidTheme {
                val rememberPagerState = rememberPagerState(pageCount = 4, initialPage = 0, initialOffscreenLimit = 4)
                Scaffold(
                    content = {
                        HorizontalPager(state = rememberPagerState, dragEnabled = false) { page ->
                            when (page) {
                                0 -> HomePage()
                                1 -> CategoryPage()
                                2 -> ProjectPage()
                                3 -> MinePage()
                            }
                        }
                    },
                    bottomBar = {
                        BottomBarView(pagerState = rememberPagerState)
                    },
                    drawerContent = {
                        drawerContent()
                    }
                )
            }
        }
    }

    @Composable
    private fun drawerContent() {
        Column(
            modifier = Modifier
                .background(White)
                .width(300.cdp)
                .fillMaxHeight()
        ) {

        }
    }


}

@HiltViewModel
class MainViewmodel @Inject constructor(private val api: WanAndroidApi) : BaseViewStateViewModel() {

}
