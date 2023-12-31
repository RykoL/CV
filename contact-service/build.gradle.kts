val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

kotlin {
    jvmToolchain(17)
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
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-mustache:$ktor_version")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.4")
    implementation("net.mamoe.yamlkt:yamlkt:0.13.0")
    implementation("io.ktor:ktor-server-mustache-jvm:2.3.4")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.4")
    implementation("org.bouncycastle:bcprov-jdk15to18:1.76")
    testImplementation("org.amshove.kluent:kluent:1.73")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
}

tasks.test {
    useJUnitPlatform()
}