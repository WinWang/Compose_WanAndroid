package com.winwang.composewanandroid.route

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.winwang.composewanandroid.R

sealed class BottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    var icon: ImageVector
) {
    object Home : BottomNavRoute(RouteName.HOME, R.string.home, Icons.Default.Home)
    object Category : BottomNavRoute(RouteName.CATEGORY, R.string.category, Icons.Default.Menu)
    object Project : BottomNavRoute(RouteName.PROJECT, R.string.project, Icons.Default.Favorite)
    object Mine : BottomNavRoute(RouteName.MINE, R.string.mine, Icons.Default.Person)
}