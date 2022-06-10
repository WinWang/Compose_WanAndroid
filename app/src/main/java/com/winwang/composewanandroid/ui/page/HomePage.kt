package com.winwang.composewanandroid.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.winwang.composewanandroid.base.viewmodel.BaseViewStateViewModel
import com.winwang.composewanandroid.base.viewmodel.ViewState
import com.winwang.composewanandroid.base.viewmodel.ViewStateMutableLiveData
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import com.winwang.composewanandroid.utils.AppLogUtil
import com.winwang.composewanandroid.widget.Banner
import com.winwang.composewanandroid.widget.BannerData
import com.winwang.composewanandroid.widget.CommonTopAppBar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by WinWang on 2022/5/10
 * Description->首页
 */
@Composable
@OptIn(ExperimentalPagerApi::class)
fun HomePage() {
    val viewmodel: HomeViewmodel = hiltViewModel()
    val bannerList = viewmodel.bannerLiveData.observeAsState()
    LaunchedEffect(true) {
        viewmodel.getBanner()
    }
    Column {
        CommonTopAppBar(title = "首页", showBackButton = false)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (bannerList.value is ViewState.Success) {
                Banner(list = (bannerList.value as ViewState.Success).data, onClick = { link, title ->

                })
            }
        }
    }
}


@HiltViewModel
class HomeViewmodel @Inject constructor(val api: WanAndroidApi) : BaseViewStateViewModel() {

    val bannerLiveData by lazy {
        ViewStateMutableLiveData<List<BannerData>>()
    }

    fun getBanner() {
        launchConvert(
            bannerLiveData, convert = { it ->
                it.map {
                    BannerData(title = it.title ?: "", imageUrl = it.imagePath ?: "", linkUrl = it.url ?: "")
                }
            }
        ) {
            api.getBanners()
        }
    }

}




