plugins {
    application
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.3.4"
}

repositories {
    mavenCentral()
}

val ktorVersion: String by project

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-cio-jvm:$ktorVersion")
}

application {
    mainClass.set("demo.ktor.ApplicationKt")
}