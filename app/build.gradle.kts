import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.hilt.plugin.get().pluginId)
}

android {
    buildToolsVersion = Configuration.BUILD_TOOLS_VERSION
    compileSdk = Configuration.COMPILE_SDK

    defaultConfig {
        applicationId = "com.woory.almostthere"
        minSdk = Configuration.MIN_SDK
        targetSdk = Configuration.TARGET_SDK
        versionCode = Configuration.VERSION_CODE
        versionName = Configuration.VERSION_NAME

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    // Modules
    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":network"))
    implementation(project(":database"))

    // Androidx
    implementation(libs.androidx.startup)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.compiler)

    // To be initialized
    implementation(libs.timber)
    implementation(libs.threeten)
    implementation(libs.kakao.share)

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    kapt(libs.room.persistence)
    testImplementation(libs.androidx.room.testing)

    // DataStore
    implementation(libs.datastore.preferences)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // Firebase Services
    implementation(libs.firebase.bom)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)

    // Unit test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}

fun getApiKey(propertyKey: String): String = gradleLocalProperties(rootDir).getProperty(propertyKey)