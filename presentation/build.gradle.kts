import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.hilt.plugin.get().pluginId)
    id(libs.plugins.navigation.safeargs.get().pluginId)
}

android {
    namespace = "com.woory.almostthere.presentation"
    buildToolsVersion = Configuration.BUILD_TOOLS_VERSION
    compileSdk = Configuration.COMPILE_SDK

    defaultConfig {
        minSdk = Configuration.MIN_SDK
        targetSdk = Configuration.TARGET_SDK

        buildConfigField("String", "MAP_API_KEY", getApiKey("MAP_API_KEY"))
        manifestPlaceholders["KAKAO_APP_KEY"] = getApiKey("KAKAO_NATIVE_APP_KEY")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles(
                "consumer-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // Modules
    implementation(project(":data"))

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Androidx
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // T map
    implementation(files("libs/tmap-sdk-1.1.aar"))
    implementation(files("libs/vsm-tmap-sdk-v2-android-1.6.60.aar"))

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.play.services.location)
    kapt(libs.hilt.compiler)

    implementation(libs.timber)

    implementation(libs.threeten)

    implementation(libs.kakao.share)

    implementation(libs.lottie)

    implementation(libs.konfetti)

    // Unit test
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}

fun getApiKey(propertyKey: String): String = gradleLocalProperties(rootDir).getProperty(propertyKey)