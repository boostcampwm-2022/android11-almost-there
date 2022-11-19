// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}