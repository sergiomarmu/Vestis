pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Vestis"
include(":app")
include(":feature:feature-products")
include(":feature:feature-favorites")
include(":feature:feature-profile")
include(":domain:domain-products")
include(":domain:domain-profile")
include(":domain:domain-favorites")
include(":data:data-favorites")
include(":data:data-profile")
include(":data:data-products")
include(":core:core-ui")
include(":core:core-database")
