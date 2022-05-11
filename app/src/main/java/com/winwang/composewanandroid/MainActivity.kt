package com.winwang.composewanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.winwang.composewanandroid.base.viewmodel.BaseViewStateViewModel
import com.winwang.composewanandroid.http.apiservice.WanAndroidApi
import com.winwang.composewanandroid.route.BottomNavBarView
import com.winwang.composewanandroid.route.RouteName
import com.winwang.composewanandroid.route.routes
import com.winwang.composewanandroid.ui.theme.ComposeWanAndroidTheme
import com.winwang.composewanandroid.utils.AppLogUtil
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWanAndroidTheme {
                val navCtrl = rememberNavController()
                val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
                navBackStackEntry?.destination?.route = RouteName.HOME
                val currentDestination = navBackStackEntry?.destination
                var route = currentDestination?.route
                AppLogUtil.d("$route>>>>>>>>>>>>>>>>>")
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    bottomBar = {
                        when (currentDestination?.route) {
                            RouteName.HOME -> BottomNavBarView(navCtrl = navCtrl)
                            RouteName.CATEGORY -> BottomNavBarView(navCtrl = navCtrl)
                            RouteName.PROJECT -> BottomNavBarView(navCtrl = navCtrl)
                            RouteName.MINE -> BottomNavBarView(navCtrl = navCtrl)
//                            else -> BottomNavBarView(navCtrl = navCtrl)
                        }
                    },
                    content = {
                        routes(navController = navCtrl, startDestination = RouteName.HOME, route = RouteName.HOME)
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val viewModel: MainViewmodel = hiltViewModel()
    LaunchedEffect(key1 = true, block = {
        viewModel.getHomeList()
    })
    SideEffect {
        viewModel.getHomeList()
    }
    Column() {
        Text(text = "Hello $name!")
        Button(
            onClick = { viewModel.getHomeList() }, modifier = Modifier
                .background(color = Color.Cyan)
                .height(50.dp)
        ) {

        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeWanAndroidTheme {
        Greeting("Android")
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
