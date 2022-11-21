import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.woory.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        val projectProperties = readProperties(file("../local.properties"))
        buildConfigField("String", "MAP_API_KEY", projectProperties["MAP_API_KEY"] as String)
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {
    // di
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    // retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")

    // moshi converter
    implementation("com.squareup.moshi:moshi:1.9.3")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.3")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}