package com.winwang.composewanandroid.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParentBean(
    var children: MutableList<ParentBean>?,
    var courseId: Int = -1,
    var id: Int = -1,
    var name: String? = "分类",
    var order: Int = -1,
    var parentChapterId: Int = -1,
    var userControlSetTop: Boolean = false,
    var visible: Int = -1,
    var icon: String? = null,
    var link: String? = null
) : Parcelable