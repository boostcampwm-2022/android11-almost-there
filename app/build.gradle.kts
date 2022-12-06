import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.woory.almostthere"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.woory.almostthere"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val projectProperties = readProperties(file("../local.properties"))
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", projectProperties["KAKAO_NATIVE_APP_KEY"] as String)

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        hilt {
            enableAggregatingTask = true
        }
        buildFeatures {
            dataBinding = true
        }
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {
    // Modules
    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":network"))
    implementation(project(":database"))
    implementation(project(":firebase"))

    // Multidex
    implementation("androidx.multidex:multidex:2.0.1")

    // DI
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // ThreeThen
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.3")

    // Kakao Message
    implementation("com.kakao.sdk:v2-share:2.11.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}

kapt {
    correctErrorTypes = true
}