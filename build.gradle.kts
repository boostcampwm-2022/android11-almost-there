// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spotless)
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }

    dependencies {
        classpath(libs.agp)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.plugin)
        classpath(libs.google.services)
        classpath(libs.navigation.safeArgs)
    }
}

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlin.time.ExperimentalTime",
        )
    }

    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            targetExclude("$buildDir/**/*.kt")
            ktlint()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}