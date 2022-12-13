package com.woory.almostthere.initializer

import android.content.Context
import androidx.startup.Initializer
import com.kakao.sdk.common.KakaoSdk
import com.woory.almostthere.BuildConfig

class KakaoSdkInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}