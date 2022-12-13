import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.hilt.plugin.get().pluginId)
}

android {
    namespace = "com.woory.network"
    buildToolsVersion = Configuration.BUILD_TOOLS_VERSION
    compileSdk = Configuration.COMPILE_SDK

    defaultConfig {
        minSdk = Configuration.MIN_SDK
        targetSdk = Configuration.TARGET_SDK

        val projectProperties = readProperties(file("../local.properties"))
        buildConfigField("String", "MAP_API_KEY", projectProperties["MAP_API_KEY"] as String)
        buildConfigField("String", "ODSAY_API_KEY", projectProperties["ODSAY_API_KEY"] as String)
    }
}

dependencies {
    implementation(project(":data"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    implementation(libs.threeten)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}