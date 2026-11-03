plugins {
    id("pastry.spring-boot-service")
}

dependencies {
    implementation(libs.spring.cloud.eureka.server)
}