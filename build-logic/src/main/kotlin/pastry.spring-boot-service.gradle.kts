import org.gradle.api.artifacts.VersionCatalogsExtension
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("pastry.java-conventions")
    id("org.springframework.boot")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("spring-boot-starter-actuator").get())
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set("pastry/${project.name}:latest")
}