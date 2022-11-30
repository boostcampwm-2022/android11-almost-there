import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.woory.presentation"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        val projectProperties = readProperties(file("../local.properties"))
        buildConfigField("String", "MAP_API_KEY", projectProperties["MAP_API_KEY"] as String)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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

        kotlinOptions {
            jvmTarget = "1.8"
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
    implementation(project(":data"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // coroutine
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // T map
    implementation(files("libs/tmap-sdk-1.1.aar"))
    implementation(files("libs/vsm-tmap-sdk-v2-android-1.6.60.aar"))

    // DI
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    // ThreeTenABP
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.3")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // FusedLocationProvider
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Lottie
    implementation("com.airbnb.android:lottie:5.2.0")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}

kapt {
    correctErrorTypes = true
}