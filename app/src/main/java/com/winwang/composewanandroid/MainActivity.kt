package com.winwang.composewanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.winwang.composewanandroid.base.activity.BaseActivity
import com.winwang.composewanandroid.base.viewmodel.BaseViewStateViewModel
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import com.winwang.composewanandroid.route.BottomBarView
import com.winwang.composewanandroid.ui.page.CategoryPage
import com.winwang.composewanandroid.ui.page.HomePage
import com.winwang.composewanandroid.ui.page.MinePage
import com.winwang.composewanandroid.ui.page.ProjectPage
import com.winwang.composewanandroid.ui.theme.ComposeWanAndroidTheme
import com.winwang.composewanandroid.utils.AppLogUtil
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalPagerApi::class)
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWanAndroidTheme {
                val rememberPagerState = rememberPagerState(pageCount = 4, initialPage = 0, initialOffscreenLimit = 3)
                Scaffold(
                    bottomBar = {
                        BottomBarView(pagerState = rememberPagerState)
                    },
                    content = {
                        HorizontalPager(state = rememberPagerState, dragEnabled = false) { page ->
                            when (page) {
                                0 -> HomePage()
                                1 -> CategoryPage()
                                2 -> ProjectPage()
                                3 -> MinePage()
                            }
                        }
                    }
                )
            }
        }
    }
}

@HiltViewModel
class MainViewmodel @Inject constructor(private val api: WanAndroidApi) : BaseViewStateViewModel() {

    fun getHomeList() {
        viewModelScope.launch {
            val homeList = api.getHomeList(1)
            val banners = api.getBanners()
            AppLogUtil.e(banners.toString())

        }

    }
}
