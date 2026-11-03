import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    java
}

val libs = the<VersionCatalogsExtension>().named("libs")

group = "net.pastry"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.findVersion("java").get().requiredVersion))
    }
}

dependencies {
    val springBootBom = platform(libs.findLibrary("spring-boot-bom").get())
    val springCloudBom = platform(libs.findLibrary("spring-cloud-bom").get())
    val lombok = libs.findLibrary("lombok").get()

    implementation(springBootBom)
    implementation(springCloudBom)
    annotationProcessor(springBootBom)

    compileOnly(lombok)
    annotationProcessor(lombok)

    testCompileOnly(lombok)
    testAnnotationProcessor(lombok)

    testImplementation(libs.findBundle("test").get())
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}