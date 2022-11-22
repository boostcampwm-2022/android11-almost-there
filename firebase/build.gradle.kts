plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.woory.firebase"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    // Modules
    implementation(project(":data"))

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.0.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // di
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    implementation("com.jakewharton.threetenabp:threetenabp:1.4.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}