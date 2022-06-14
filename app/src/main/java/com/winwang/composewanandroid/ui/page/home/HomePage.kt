package com.winwang.composewanandroid.ui.page.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.winwang.composewanandroid.base.viewmodel.BaseViewStateViewModel
import com.winwang.composewanandroid.extension.buildPager
import com.winwang.composewanandroid.extension.cdp
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import com.winwang.composewanandroid.ui.page.home.component.itemHomeComponent
import com.winwang.composewanandroid.ui.theme.White
import com.winwang.composewanandroid.utils.AppLogUtil
import com.winwang.composewanandroid.widget.Banner
import com.winwang.composewanandroid.widget.BannerData
import com.winwang.composewanandroid.widget.CommonTopAppBar
import com.winwang.composewanandroid.widget.viewstate.ViewStateListPagingComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by WinWang on 2022/5/10
 * Description->首页
 */
@Composable
@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
fun HomePage() {
    val viewmodel: HomeViewmodel = hiltViewModel()
    val bannerList = viewmodel.bannerLiveData
    val homeList = viewmodel.getHomeList().collectAsLazyPagingItems()
    LaunchedEffect(true) {
        viewmodel.getBanner()
        viewmodel.getHomeList()
    }
    Column {
        CommonTopAppBar(title = "首页", showBackButton = false, showBottomDivider = true)
        ViewStateListPagingComponent(modifier = Modifier.background(White), collectAsLazyPagingItems = homeList) {

            item {
                Banner(list = bannerList, onClick = { link, title ->

                })
            }
            items(homeList.itemCount) { it ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.cdp)
                ) {
                    itemHomeComponent(articleBean = homeList[it], onClick = {
                        AppLogUtil.d("整体条目点击")
                    }, onItemChilcClick = {
                        AppLogUtil.d("收藏按钮点击")
                    })
                }
            }
        }
    }
}


@HiltViewModel
class HomeViewmodel @Inject constructor(private val api: WanAndroidApi) : BaseViewStateViewModel() {
    val bannerLiveData by lazy {
        mutableStateListOf<BannerData>()
    }

    /**
     * 获取首页Banner数据
     */
    fun getBanner() {
        launch {
            val banners = api.getBanners()
            val map = banners.data?.map {
                BannerData(title = it.title ?: "", imageUrl = it.imagePath ?: "", linkUrl = it.url ?: "")
            }
            bannerLiveData.addAll(map ?: emptyList())
        }
    }

    /**
     * 获取首页数据
     */
    fun getHomeList() = buildPager(
        transformListBlock = { it?.datas }
    ) { currentPage, _ ->
        api.getHomeList(currentPage)
    }


}




