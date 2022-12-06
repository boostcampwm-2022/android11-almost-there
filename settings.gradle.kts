pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven{ url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")}
    }
}
rootProject.name = "AlmostThere"
include(":app")
include(":data")
include(":network")
include(":database")
include(":firebase")
include(":presentation")