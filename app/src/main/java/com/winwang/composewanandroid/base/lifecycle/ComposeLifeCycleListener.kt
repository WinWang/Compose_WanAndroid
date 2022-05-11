package com.winwang.composewanandroid.base.lifecycle

import androidx.lifecycle.LifecycleOwner

/**
 * Created by ssk on 2022/1/11.
 * Description-> compose组件生命周期监听
 */
interface ComposeLifeCycleListener {

    // 首次进入compose函数
    fun onEnterCompose(owner: LifecycleOwner) {}

    // 退出compose函数
    fun onExitCompose(owner: LifecycleOwner) {}

    // activity onCreate
    fun onCreate(owner: LifecycleOwner) {}

    // activity onStart
    fun onStart(owner: LifecycleOwner) {}

    // activity onResume
    fun onResume(owner: LifecycleOwner) {}

    // activity onPause
    fun onPause(owner: LifecycleOwner) {}

    // activity onStop
    fun onStop(owner: LifecycleOwner) {}

    // activity onDestroy
    fun onDestroy(owner: LifecycleOwner) {}

}


