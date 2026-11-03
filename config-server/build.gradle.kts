import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("pastry.spring-boot-service")
}

dependencies {
    implementation(libs.spring.cloud.config.server)
}

tasks.named<BootRun>("bootRun") {
    workingDir = rootDir
}