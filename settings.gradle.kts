rootProject.name = "pastry"

pluginManagement {
    includeBuild("build-logic")
    repositories { gradlePluginPortal()}
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { mavenCentral()}
}

include(
    "analysis-service",
    "paste-service",
    "auth-service",
    "common",
    "gateway",
    "eureka-server",
    "config-server"
)