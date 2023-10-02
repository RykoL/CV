import io.ktor.plugin.features.*

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

kotlin {
    jvmToolchain(19)
}

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    kotlin("plugin.serialization") version "1.9.10"
}

group = "de.rlang"
version = "0.0.1"

application {
    mainClass.set("de.rlang.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    docker {
        localImageName.set("contact-service")
        imageTag.set("latest")
        externalRegistry.set(
            DockerImageRegistry.externalRegistry(
                username = providers.environmentVariable("CR_USERNAME"),
                password = providers.environmentVariable("CR_ACCESS_TOKEN"),
                project = providers.environmentVariable("CR_REGISTRY"),
                hostname = providers.environmentVariable("CR_HOST")
            )
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.4")
    implementation("net.mamoe.yamlkt:yamlkt:0.13.0")
    testImplementation("org.amshove.kluent:kluent:1.73")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
}
