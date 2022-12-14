pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}
rootProject.name = "AlmostThere"
include(":app")
include(":presentation")
include(":data")
include(":network")
include(":database")