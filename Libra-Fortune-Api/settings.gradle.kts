pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "libra-fortune-parent"

include(":libra-fortune-api")
project(":libra-fortune-api").projectDir = file("libra-fortune-api")

includeBuild("../Galaxy-Microservices/galaxy-common")
includeBuild("../Galaxy-Microservices/galaxy-web")
includeBuild("../Galaxy-Microservices/galaxy-authentication")
