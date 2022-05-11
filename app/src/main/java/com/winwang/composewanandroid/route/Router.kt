package com.winwang.composewanandroid.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.winwang.composewanandroid.ui.page.CategoryPage
import com.winwang.composewanandroid.ui.page.HomePage
import com.winwang.composewanandroid.ui.page.MinePage
import com.winwang.composewanandroid.ui.page.ProjectPage
import com.winwang.composewanandroid.utils.AppLogUtil

/**
 * Created by WinWang on 2022/5/10
 * Description->注册路由表信息
 */

object RouteName {
    const val HOME = "home"
    const val CATEGORY = "category"
    const val COLLECTION = "collection"
    const val MINE = "mine"
    const val PROJECT = "project"
    const val PROFILE = "profile"
    const val WEB_VIEW = "web_view"
    const val LOGIN = "login"
    const val ARTICLE_SEARCH = "article_search"
}

@Composable
fun routes(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    route: String? = null
) {
    NavHost(navController = navController, startDestination = startDestination) {
        /**
         * 首页
         */
        composable(route = RouteName.HOME) {
            AppLogUtil.d(">>>>>>>>>>>>>>>>")
            HomePage()
        }
        /**
         * 分类
         */
        composable(route = RouteName.CATEGORY) {
            CategoryPage()
        }

        /**
         * 项目
         */
        composable(route = RouteName.PROJECT) {
            ProjectPage()
        }

        /**
         * 我的
         */
        composable(route = RouteName.MINE) {
            MinePage()
        }
    }
}