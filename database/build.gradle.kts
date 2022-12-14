@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

android {
    namespace = "com.woory.almostthere.database"
    buildToolsVersion = Configuration.BUILD_TOOLS_VERSION
    compileSdk = Configuration.COMPILE_SDK

    defaultConfig {
        minSdk = Configuration.MIN_SDK
        targetSdk = Configuration.TARGET_SDK
    }
}

dependencies {
    // Modules
    implementation(project(":data"))

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    kapt(libs.room.persistence)
    testImplementation(libs.androidx.room.testing)

    // DataStore
    implementation(libs.datastore.preferences)

    // For Time stuff
    implementation(libs.threeten)

    // Unit test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}