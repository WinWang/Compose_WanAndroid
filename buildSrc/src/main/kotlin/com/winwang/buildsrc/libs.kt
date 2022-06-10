package com.winwang.buildsrc

/**
 * Created by WinWang on 2022/5/5
 * Description->
 */
object Libs {

    object Kotlin {
        private const val version = "1.5.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Compose {
        private const val compose_version = "1.1.0"
        const val composeUI = "androidx.compose.ui:ui:$compose_version"
        const val composeMaterial = "androidx.compose.material:material:$compose_version"
        const val composeUIPreview = "androidx.compose.ui:ui-tooling-preview:$compose_version"
        const val composeActivity = "androidx.activity:activity-compose:1.4.0"
        const val composeLivedata = "androidx.compose.runtime:runtime-livedata:1.0.1"
        const val composeViewBinding = "androidx.compose.ui:ui-viewbinding:1.0.5"
        const val composeConstraintlayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01"
    }

    object Accompanist {
        const val composeAccompanistPage = "com.google.accompanist:accompanist-pager:0.18.0"
        const val composeAccompanistCoil = "com.google.accompanist:accompanist-coil:0.15.0"
        const val composeAccompanistSystemUI = "com.google.accompanist:accompanist-systemuicontroller:0.18.0"
    }


    object Androidx {
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"
        const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
        const val material  = "com.google.android.material:material:1.5.0"
    }

    object KTX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    }

    object Hilt {
        private const val hilt_version = "2.40.1"
        const val HiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
        const val HiltAndroid = "com.google.dagger:hilt-android:$hilt_version"
        const val HiltViewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val HiltCompiler = "com.google.dagger:hilt-android-compiler:$hilt_version"
    }

    object Navigation {
        const val NavigationCompose = "androidx.navigation:navigation-compose:2.5.0-beta01"
        const val HiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
    }

    object CommonLib {
        private const val okhttp_version = "4.9.0"
        private const val retrofit_version = "2.9.0"
        private const val coil_version = "2.0.0-rc02"
        const val okhttp = "com.squareup.okhttp3:okhttp:$okhttp_version"
        const val coil = "io.coil-kt:coil-compose:$coil_version"
        const val coilGif = "io.coil-kt:coil-gif:$coil_version"
        const val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttp_version"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
        const val gsonFactory = "com.squareup.retrofit2:converter-gson:$retrofit_version"
        const val gson = "com.google.code.gson:gson:2.8.7"

    }


}