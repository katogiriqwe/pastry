import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("pastry.spring-boot-service")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("spring-cloud-eureka-client").get())
    implementation(libs.findLibrary("spring-cloud-starter-config").get())
}