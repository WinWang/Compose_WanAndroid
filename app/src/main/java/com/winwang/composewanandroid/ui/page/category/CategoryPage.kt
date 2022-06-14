package com.winwang.composewanandroid.ui.page.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.winwang.composewanandroid.base.viewmodel.BaseViewStateViewModel
import com.winwang.composewanandroid.base.viewmodel.ViewStateMutableLiveData
import com.winwang.composewanandroid.extension.cdp
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import com.winwang.composewanandroid.model.ParentBean
import com.winwang.composewanandroid.widget.CommonTopAppBar
import com.winwang.composewanandroid.widget.viewstate.ViewStateComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by WinWang on 2022/5/10
 * Description->分类页面
 */
@Composable
fun CategoryPage() {
    val viewmodel: CategoryViewmodel = hiltViewModel()
    Column {
        CommonTopAppBar(title = "分类", showBackButton = false, showBottomDivider = true)
        ViewStateComponent(viewStateLiveData = viewmodel.categoryList, loadDataBlock = { viewmodel.getCategory() }) { data ->
            LazyColumn {
                items(data.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.cdp)
                            .height(100.cdp)
                            .background(Color.Yellow)
                    )
                }
            }
        }

    }


}

@HiltViewModel
class CategoryViewmodel @Inject constructor(private val api: WanAndroidApi) : BaseViewStateViewModel() {
    val categoryList by lazy {
        ViewStateMutableLiveData<MutableList<ParentBean>>()
    }

    /**
     * 获取分类数据
     */
    fun getCategory() {
        launch(categoryList) {
            api.getCategoryList()
        }
    }


}