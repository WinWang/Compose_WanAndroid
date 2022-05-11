package com.winwang.composewanandroid.model

/**
 * Created by WinWang on 2022/5/6
 * Description->
 */
data class BannerBean(
    var desc: String?,
    var id: Int,
    var imagePath: String?,
    var isVisible: Int,
    var order: Int,
    var title: String?,
    var type: Int,
    var url: String?
)